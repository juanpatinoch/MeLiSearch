package com.mercadolibre.search.model.repository.search

import androidx.paging.PagingData
import com.mercadolibre.search.model.dto.search.ResultsDto
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    /**
     * Funcion de la interfaz del repositorio para buscar un producto
     */
    suspend fun searchByQuery(query: String): Flow<PagingData<ResultsDto>>

}