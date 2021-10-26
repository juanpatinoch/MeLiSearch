package com.mercadolibre.search.model.repository.search

import com.mercadolibre.search.model.remote.search.SearchDataSource
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class SearchRepositoryTest {

    @Mock
    lateinit var searchDataSource: SearchDataSource

    private lateinit var searchRepository: SearchRepository

    @Before
    fun setup(){
        searchRepository = SearchRepositoryImpl(searchDataSource)
    }



}