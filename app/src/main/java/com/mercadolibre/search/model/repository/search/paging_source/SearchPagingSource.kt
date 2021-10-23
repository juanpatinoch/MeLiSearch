package com.mercadolibre.search.model.repository.search.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mercadolibre.search.model.dto.response.CustomResponse
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.model.remote.search.SearchDataSource
import com.mercadolibre.search.utils.Constants
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(
    private val siteId: String,
    private val query: String,
    private val searchDataSource: SearchDataSource,
) : PagingSource<Int, ResultsDto>() {

    override fun getRefreshKey(state: PagingState<Int, ResultsDto>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultsDto> {
        return try {
            val pageNumber = params.key ?: 1
            val response = searchDataSource.searchByQuery(
                siteId,
                query,
                Constants.pagingPageSize,
                pageNumber * Constants.pagingPageSize
            )
            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            when (response) {
                is CustomResponse.Failure -> {
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
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}