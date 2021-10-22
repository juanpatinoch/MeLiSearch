package com.mercadolibre.search.model.repository.search

import com.mercadolibre.search.model.remote.search.SearchDataSource

class SearchRepositoryImpl(private val searchDataSource: SearchDataSource) : SearchRepository {

    override suspend fun searchByQuery(siteId: String, query: String) =
        searchDataSource.searchByQuery(siteId, query)

}