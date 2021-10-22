package com.mercadolibre.search.model.remote.search

import com.mercadolibre.search.model.remote.ApiServices

class SearchDataSource(private val apiServices: ApiServices) {

    suspend fun searchByQuery(siteId: String, query: String) {}

}