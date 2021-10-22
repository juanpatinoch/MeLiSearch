package com.mercadolibre.search.view_model.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel() : ViewModel() {

    private val _openSearch = MutableLiveData<Boolean>()
    val openSearch: LiveData<Boolean>
        get() = _openSearch

    fun openSearch() {
        _openSearch.value = true
    }

}