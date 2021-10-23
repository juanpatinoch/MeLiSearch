package com.mercadolibre.search.model.repository.search

import androidx.paging.PagingData
import com.mercadolibre.search.model.dto.search.SearchDto
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun searchByQuery(siteId: String, query: String): Flow<PagingData<SearchDto>>

}