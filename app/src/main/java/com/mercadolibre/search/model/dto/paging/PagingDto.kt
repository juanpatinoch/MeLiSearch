package com.mercadolibre.search.model.dto.paging

import com.squareup.moshi.Json

data class PagingDto(
    @Json(name = "total") val total: Int = 0,
    @Json(name = "offset") val offset: Int = 0,
    @Json(name = "limit") val limit: Int = 0,
    @Json(name = "primary_results") val primary_results: Int = 0
)