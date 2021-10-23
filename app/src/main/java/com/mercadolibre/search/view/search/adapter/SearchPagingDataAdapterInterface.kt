package com.mercadolibre.search.view.search.adapter

import com.mercadolibre.search.model.dto.search.ResultsDto

interface SearchPagingDataAdapterInterface {

    fun onSearchPagingItemClick(resultsDto: ResultsDto)

}