package com.mercadolibre.search.view_model.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.use_cases.SearchAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel(private val searchAPI: SearchAPI) : ViewModel() {

    private val _pagingData = MutableLiveData<PagingData<ResultsDto>>()
    val pagingData: LiveData<PagingData<ResultsDto>>
        get() = _pagingData

    fun searchByQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchAPI.execute("MCO", query)
                .cachedIn(viewModelScope)
                .distinctUntilChanged()
                .collectLatest {
                    validateSearchResult(it)
                }
        }
    }

    private fun validateSearchResult(pagingData: PagingData<ResultsDto>) {
        viewModelScope.launch(Dispatchers.Main) {
            _pagingData.value = pagingData
        }
    }

}