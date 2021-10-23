package com.mercadolibre.search.model.repository.search

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import com.mercadolibre.search.model.dto.search.SearchDto
import com.mercadolibre.search.model.remote.search.SearchDataSource
import com.mercadolibre.search.model.repository.search.paging_source.SearchPagingSource
import com.mercadolibre.search.utils.Constants
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val searchDataSource: SearchDataSource
) : SearchRepository {

    @ExperimentalPagingApi
    override suspend fun searchByQuery(siteId: String, query: String): Flow<PagingData<SearchDto>> {
        return Pager(
            config = Constants.PAGING_CONFIG,
            pagingSourceFactory = {
                SearchPagingSource(
                    siteId = siteId,
                    query = query,
                    searchDataSource = searchDataSource
                )
            },
        ).flow
    }
}