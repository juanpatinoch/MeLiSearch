package com.mercadolibre.search.model.dto.error

import com.squareup.moshi.Json

/**
 * Dto utilizado para el manejo de errores que retorna la API
 * @param errorMessage -> mensaje que se debe mostrar
 * @param errorText -> error en lenguaje de maquina
 * @param errorStatus -> codigo del error
 */
data class ErrorDto(
    @field:Json(name = "message") val errorMessage: String = "",
    @field:Json(name = "error") val errorText: String = "",
    @field:Json(name = "status") val errorStatus: Int = 0,
) : Exception()