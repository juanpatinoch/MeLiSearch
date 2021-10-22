package com.mercadolibre.search.use_cases

import com.mercadolibre.search.model.repository.search.SearchRepository

class SearchAPI(private val searchRepository: SearchRepository) {

    suspend fun execute(siteId: String, query: String) =
        searchRepository.searchByQuery(siteId, query)

}