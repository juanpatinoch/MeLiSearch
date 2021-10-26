package com.mercadolibre.search.model.repository.search

import androidx.paging.PagingData
import com.mercadolibre.search.model.dto.search.ResultsDto
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun searchByQuery(query: String): Flow<PagingData<ResultsDto>>

}