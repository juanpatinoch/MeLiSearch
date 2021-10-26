package com.mercadolibre.search.view_model.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.mercadolibre.search.MainCoroutineRule
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.use_cases.SearchAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
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

    @Mock
    lateinit var pagingDataObserver: Observer<PagingData<ResultsDto>>

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setup() {
        searchViewModel = SearchViewModel(
            searchAPI
        )
        searchViewModel.pagingData.observeForever(pagingDataObserver)
    }

    @After
    fun tearDown() {
        searchViewModel.pagingData.removeObserver(pagingDataObserver)
    }

    @Test
    fun nada(){
        //given

        //when

        //then
    }

}