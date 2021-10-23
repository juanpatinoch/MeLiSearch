package com.mercadolibre.search.model.dto.search

import com.mercadolibre.search.model.dto.paging.PagingDto
import com.squareup.moshi.Json

data class SearchResponseDto(
    @Json(name = "paging") val paging: PagingDto,
    @Json(name = "results") val results: List<ResultsDto>
)

data class ResultsDto(
    @Json(name = "id") val id: String = "",
    @Json(name = "title") val title: String = "",
    @Json(name = "price") val price: Int = 0
)

data class PricesDto(
    @Json(name = "prices") val pricesDetail: List<PricesDetailDto>
)

data class PricesDetailDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "amount") val amount: Int = 0,
    @Json(name = "type") val type: String = "",
    @Json(name = "currency_id") val currencyId: String = ""
)