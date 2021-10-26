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

/**
 * Clase del ViewModel de Search
 * @param searchAPI Caso de Uso para consultar el listado de productos x query
 */
class SearchViewModel(private val searchAPI: SearchAPI) : ViewModel() {

    //Esta es el dato que se va estar escuchando en el Observer del Activity
    private val _pagingData = MutableLiveData<PagingData<ResultsDto>>()
    val pagingData: LiveData<PagingData<ResultsDto>>
        get() = _pagingData

    /**
     * Funcion que inicia con el flujo de buscar productos por query
     * @param query producto que estoy buscando
     * @return PagingData<ResultsDto> Lista de productos paginados
     */
    fun searchByQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchAPI.execute(query)
                .cachedIn(viewModelScope)
                .distinctUntilChanged()
                .collectLatest {
                    validateSearchResult(it)
                }
        }
    }

    /**
     * Asigna el resultado de la consulta al PagingData
     * @param pagingData resutados de la consulta paginados
     */
    private fun validateSearchResult(pagingData: PagingData<ResultsDto>) {
        viewModelScope.launch(Dispatchers.Main) {
            _pagingData.value = pagingData
        }
    }

}