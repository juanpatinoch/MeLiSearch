package com.mercadolibre.search.model.dto.search

import com.mercadolibre.search.model.dto.paging.PagingDto
import com.squareup.moshi.Json

data class SearchResponseDto(
    @Json(name = "paging") val paging: PagingDto,

)