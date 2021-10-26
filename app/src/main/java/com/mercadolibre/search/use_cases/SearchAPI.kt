package com.mercadolibre.search.use_cases

import com.mercadolibre.search.model.repository.search.SearchRepository

class SearchAPI(private val searchRepository: SearchRepository) {

    /**
     * Caso de uso para ejecutar el llamado al repositorio de Search
     * @param query producto que se va buscar
     * @return Flow<PagingData<ResultsDto>> Listado de productos paginas con Flow
     */
    suspend fun execute(query: String) =
        searchRepository.searchByQuery(query)

}