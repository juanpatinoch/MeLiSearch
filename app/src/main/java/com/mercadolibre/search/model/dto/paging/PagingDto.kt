package com.mercadolibre.search.model.dto.paging

import com.squareup.moshi.Json

data class PagingDto(
    @field:Json(name = "total") val total: Int = 0,
    @field:Json(name = "offset") val offset: Int = 0,
    @field:Json(name = "limit") val limit: Int = 0,
    @field:Json(name = "primary_results") val primaryResults: Int = 0
)