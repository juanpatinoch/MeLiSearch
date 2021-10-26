package com.mercadolibre.search.model.repository.search

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.model.remote.search.SearchDataSource
import com.mercadolibre.search.model.repository.search.paging_source.SearchPagingSource
import com.mercadolibre.search.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onErrorCollect

class SearchRepositoryImpl(
    private val searchDataSource: SearchDataSource
) : SearchRepository {

    @ExperimentalPagingApi
    override suspend fun searchByQuery(
        query: String
    ): Flow<PagingData<ResultsDto>> {
        return Pager(
            config = Constants.PAGING_CONFIG,
            pagingSourceFactory = {
                SearchPagingSource(
                    query = query,
                    searchDataSource = searchDataSource,
                )
            },
        ).flow
    }
}