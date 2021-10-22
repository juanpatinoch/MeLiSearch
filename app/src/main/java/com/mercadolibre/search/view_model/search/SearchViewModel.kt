package com.mercadolibre.search.view_model.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadolibre.search.use_cases.SearchAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val searchAPI: SearchAPI) : ViewModel() {

    fun searchByQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchAPI.execute("MCO", query).also {

            }
        }
    }

}