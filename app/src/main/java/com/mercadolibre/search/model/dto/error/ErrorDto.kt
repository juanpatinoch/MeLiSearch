package com.mercadolibre.search.model.dto.error

import com.squareup.moshi.Json

data class ErrorDto(
    @field:Json(name = "message") val errorMessage: String = "",
    @field:Json(name = "error") val errorText: String = "",
    @field:Json(name = "status") val errorStatus: Int = 0,
) : Exception()