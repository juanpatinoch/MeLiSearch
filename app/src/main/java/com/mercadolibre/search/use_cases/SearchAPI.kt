package com.mercadolibre.search.use_cases

import com.mercadolibre.search.model.repository.search.SearchRepository

class SearchAPI(private val searchRepository: SearchRepository) {

    suspend fun execute(query: String) =
        searchRepository.searchByQuery(query)

}