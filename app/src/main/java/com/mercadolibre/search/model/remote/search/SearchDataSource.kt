package com.mercadolibre.search.model.remote.search

import android.util.Log
import com.mercadolibre.search.model.dto.response.CustomResponse
import com.mercadolibre.search.model.dto.search.SearchResponseDto
import com.mercadolibre.search.model.remote.ApiServices
import com.mercadolibre.search.utils.Utils

/**
 * DataSource del modulo Search
 * @param apiServices -> Interfaz para las peticiones API
 */
class SearchDataSource(private val apiServices: ApiServices) {

    /**
     * Funcion utilizada para traer un listado de productos
     * @param total -> total de registros que devuelve
     * @param offset -> a partir de que posicion estan los resultados
     * @param limit -> cantidad de resultados que trae la lista
     * @return CustomResponse<SearchResponseDto>
     */
    suspend fun searchByQuery(
        query: String,
        limit: Int,
        offset: Int
    ): CustomResponse<SearchResponseDto> {
        try {
            apiServices.search(query, limit, offset).run {
                return when {
                    isSuccessful && body() != null -> {
                        CustomResponse.Success(body() as SearchResponseDto)
                    }
                    else -> {
                        Log.e("searchByQuery", errorBody().toString())
                        CustomResponse.Failure(Utils.convertToError(errorBody()))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("searchByQuery", e.toString())
            return CustomResponse.Failure(e)
        }
    }

}