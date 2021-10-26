package com.mercadolibre.search.model.dto.paging

import com.squareup.moshi.Json

/**
 * Dto utilizado para la paginacion de la lista de busqueda
 * @param total -> total de registros que devuelve
 * @param offset -> a partir de que posicion estan los resultados
 * @param limit -> cantidad de resultados que trae la lista
 */
data class PagingDto(
    @field:Json(name = "total") val total: Int = 0,
    @field:Json(name = "offset") val offset: Int = 0,
    @field:Json(name = "limit") val limit: Int = 0
)