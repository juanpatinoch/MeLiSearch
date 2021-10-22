package com.mercadolibre.search.model.repository.search

import com.mercadolibre.search.model.dto.response.CustomResponse
import com.mercadolibre.search.model.dto.search.SearchResponseDto

interface SearchRepository {

    suspend fun searchByQuery(siteId: String, query: String): CustomResponse<SearchResponseDto>

}