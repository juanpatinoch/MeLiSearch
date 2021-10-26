package com.mercadolibre.search.model.repository.search

import androidx.paging.PagingSource
import androidx.paging.map
import com.mercadolibre.search.MockData
import com.mercadolibre.search.model.dto.response.CustomResponse
import com.mercadolibre.search.model.remote.ApiServices
import com.mercadolibre.search.model.remote.search.SearchDataSource
import com.mercadolibre.search.model.repository.search.paging_source.SearchPagingSource
import com.mercadolibre.search.utils.Constants
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import retrofit2.Response

@RunWith(MockitoJUnitRunner.Silent::class)
class SearchRepositoryTest {

    @Mock
    lateinit var apiServices: ApiServices

    private lateinit var searchDataSource: SearchDataSource
    private lateinit var searchRepository: SearchRepository

    @Before
    fun setup() {
        searchDataSource = SearchDataSource(apiServices)
        searchRepository = SearchRepositoryImpl(searchDataSource)
    }

    @Test
    fun searchRepositoryPagingSuccess() {
        runBlocking {

            val expected = Response.success(MockData.mockSearchResultsDto)

            //given
            given(
                apiServices.search(
                    MockData.mockDataQuery,
                    Constants.pagingPageSize,
                    0
                )
            ).willReturn(expected)

            val pagingSource = SearchPagingSource(
                MockData.mockDataQuery,
                searchDataSource
            )

            assertEquals(
                PagingSource.LoadResult.Page(
                    data = MockData.mockSearchResultsDto.results,
                    prevKey = null,
                    nextKey = 1
                ),
                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 1,
                        placeholdersEnabled = false
                    )
                )
            )
        }
    }

}