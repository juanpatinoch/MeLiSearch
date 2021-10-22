package com.mercadolibre.search.model.remote.search

import android.util.Log
import com.mercadolibre.search.model.dto.error.ErrorDto
import com.mercadolibre.search.model.dto.response.CustomResponse
import com.mercadolibre.search.model.dto.search.SearchResponseDto
import com.mercadolibre.search.model.remote.ApiServices

class SearchDataSource(private val apiServices: ApiServices) {

    suspend fun searchByQuery(siteId: String, query: String): CustomResponse<SearchResponseDto> {
        try {
            apiServices.search(siteId, query).run {
                return when {
                    isSuccessful && body() != null -> {
                        CustomResponse.Success(body() as SearchResponseDto)
                    }
                    else -> {
                        CustomResponse.Failure(errorBody() as ErrorDto)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message ?: "ERROR")
            return CustomResponse.Failure(e)
        }
    }

}