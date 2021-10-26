package com.mercadolibre.search.view_model.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.map
import com.mercadolibre.search.MainCoroutineRule
import com.mercadolibre.search.MockData
import com.mercadolibre.search.getOrAwaitValue
import com.mercadolibre.search.use_cases.SearchAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class SearchViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var searchAPI: SearchAPI

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setup() {
        searchViewModel = SearchViewModel(
            searchAPI
        )
    }

    @Test
    fun searchViewModelSuccess() {
        runBlocking {
            val expected = MockData.mockPagingData
            //given
            given(searchAPI.execute(MockData.mockDataQuery)).willReturn(expected)

            //when
            searchViewModel.searchByQuery(MockData.mockDataQuery)

            //then
            searchViewModel.pagingData.getOrAwaitValue(afterObserve = { pagingDataResult ->
                if (pagingDataResult != null)
                    runBlocking {
                        expected.collect { pagingDataExpected ->
                            assertEquals(
                                pagingDataExpected.map { it.id },
                                pagingDataResult.map { it.id }
                            )
                        }
                    }
            })
        }
    }

}