package com.mercadolibre.search.model.dto.search

import com.mercadolibre.search.model.dto.paging.PagingDto
import com.squareup.moshi.Json
import java.io.Serializable

data class SearchResponseDto(
    @field:Json(name = "paging") val paging: PagingDto,
    @field:Json(name = "results") val results: List<ResultsDto>
) : Serializable

data class ResultsDto(
    @field:Json(name = "id") val id: String = "",
    @field:Json(name = "title") val title: String = "",
    @field:Json(name = "price") val price: Double = 0.0,
    @field:Json(name = "original_price") val originalPrice: Double?,
    @field:Json(name = "currency_id") val currencyId: String = "",
    @field:Json(name = "permalink") val permalink: String = "",
    @field:Json(name = "thumbnail") val thumbnail: String = "",
    @field:Json(name = "installments") val installments: InstallmentsDto?,
    @field:Json(name = "shipping") val shipping: ShippingDto,
    @field:Json(name = "attributes") val attributes: List<AttributesDto>
) : Serializable

data class InstallmentsDto(
    @field:Json(name = "quantity") val quantity: Int?,
    @field:Json(name = "amount") val amount: Double?,
    @field:Json(name = "currency_id") val currencyId: String?
) : Serializable

data class ShippingDto(
    @field:Json(name = "free_shipping") val freeShipping: Boolean = false
) : Serializable

data class AttributesDto(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "value_name") val valueName: String,
) : Serializable