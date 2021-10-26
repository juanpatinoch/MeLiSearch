package com.mercadolibre.search.model.remote.search

import android.util.Log
import com.mercadolibre.search.MockData
import com.mercadolibre.search.model.dto.error.ErrorDto
import com.mercadolibre.search.model.dto.response.CustomResponse
import com.mercadolibre.search.model.dto.search.SearchResponseDto
import com.mercadolibre.search.model.remote.ApiServices
import com.mercadolibre.search.utils.Utils
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
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

    @Test
    fun searchFailure() {
        runBlocking {

            val jsonObject = JSONObject()
            jsonObject.put("message", MockData.mockErrorDto.errorMessage)
            jsonObject.put("error", MockData.mockErrorDto.errorText)
            jsonObject.put("status", MockData.mockErrorDto.errorStatus)

            val expected: Response<SearchResponseDto> = Response.error(
                422,
                jsonObject.toString()
                    .toResponseBody(
                        "application/json; charset=utf-8".toMediaTypeOrNull()
                    )
            )

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
            ) as CustomResponse.Failure

            val expectedBody = CustomResponse.Failure<ErrorDto>(Utils.convertToError(expected.errorBody()))

            //then
            assertEquals(expectedBody.exception.message, result.exception.message)
        }
    }

}