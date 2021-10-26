package com.mercadolibre.search.model.repository.search.paging_source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mercadolibre.search.model.dto.response.CustomResponse
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.model.remote.search.SearchDataSource
import com.mercadolibre.search.utils.Constants
import retrofit2.HttpException
import java.io.IOException

/**
 * Clase utilizada para la paginación
 * @param query producto que se va buscar,
 * @param searchDataSource DataSource de search donde se va consumir el servicio
 */
class SearchPagingSource(
    private val query: String,
    private val searchDataSource: SearchDataSource,
) : PagingSource<Int, ResultsDto>() {

    /**
     * Funcion que retorna el numero de la pagina que se debe buscar
     * @param state Estado de la paginacion
     * @return Int -> numero de la pagina a consultar
     */
    override fun getRefreshKey(state: PagingState<Int, ResultsDto>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    /**
     * Funcion que hace un llamado a la API, enviando el numero de la paginacion
     * @param params parametros para la paginacion
     * @return LoadResult<Int, ResultsDto> Lista de resultados o error
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultsDto> {
        return try {
            val pageNumber = params.key ?: 0
            val response = searchDataSource.searchByQuery(
                query,
                Constants.pagingPageSize,
                pageNumber * Constants.pagingPageSize
            )
            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            when (response) {
                is CustomResponse.Failure -> {
                    Log.e("CustomResponse.Failure", response.exception.toString())
                    throw response.exception
                }
                is CustomResponse.Success -> {
                    val nextKey = if (response.data.results.isNotEmpty()) pageNumber + 1 else null
                    LoadResult.Page(
                        data = response.data.results,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
            }

        } catch (e: IOException) {
            Log.e("IOException", e.toString())
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("HttpException", e.toString())
            LoadResult.Error(e)
        }
    }
}