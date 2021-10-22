package com.mercadolibre.search.model.repository.search

interface SearchRepository {

    suspend fun searchByQuery(siteId: String, query: String)

}