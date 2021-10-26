package com.mercadolibre.search.model.remote.search

import com.mercadolibre.search.MockData
import com.mercadolibre.search.model.dto.response.CustomResponse
import com.mercadolibre.search.model.remote.ApiServices
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given
import retrofit2.Response

@RunWith(MockitoJUnitRunner.Silent::class)
class SearchDataSourceTest {

    @Mock
    lateinit var apiServices: ApiServices

    private lateinit var searchDataSource: SearchDataSource

    @Before
    fun setup() {
        searchDataSource = SearchDataSource(apiServices)
    }

    @Test
    fun searchSuccess() {
        runBlocking {
            val expected = Response.success(MockData.mockSearchResultsDto)

            //given
            given(
                apiServices.search(
                    MockData.mockDataQuery,
                    MockData.mockDataLimit,
                    MockData.mockDataOffset
                )
            ).willReturn(expected)

            //when
            val result = searchDataSource.searchByQuery(
                MockData.mockDataQuery,
                MockData.mockDataLimit,
                MockData.mockDataOffset
            ) as CustomResponse.Success

            //then
            assertEquals(expected.body(), result.data)
        }
    }

}