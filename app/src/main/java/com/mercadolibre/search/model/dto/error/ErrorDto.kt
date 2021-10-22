package com.mercadolibre.search.model.dto.error

import com.squareup.moshi.Json

data class ErrorDto(
    @Json(name = "message") val errorMessage: String = "",
    @Json(name = "error") val errorText: String = "",
    @Json(name = "status") val errorStatus: Int = 0,
) : Exception()