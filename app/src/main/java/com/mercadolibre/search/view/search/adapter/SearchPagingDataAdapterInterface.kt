package com.mercadolibre.search.view.search.adapter

import com.mercadolibre.search.model.dto.search.ResultsDto

interface SearchPagingDataAdapterInterface {

    /**
     * Funcion de la interfaz para el Adapter de Search
     * Cuando da click en algun producto dispara esta funcion
     */
    fun onSearchPagingItemClick(resultsDto: ResultsDto)

}