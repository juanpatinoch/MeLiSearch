package com.mercadolibre.search.model.remote.search

import android.util.Log
import com.mercadolibre.search.model.dto.error.ErrorDto
import com.mercadolibre.search.model.dto.response.CustomResponse
import com.mercadolibre.search.model.dto.search.SearchResponseDto
import com.mercadolibre.search.model.remote.ApiServices

class SearchDataSource(private val apiServices: ApiServices) {

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
                        CustomResponse.Failure(errorBody() as ErrorDto)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("searchByQuery", e.toString())
            return CustomResponse.Failure(e)
        }
    }

}