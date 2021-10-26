package com.mercadolibre.search.model.dto.response

/**
 * Clase sellada utilizada como respuesta de la API
 * @param T -> tipo de dato que va en la respuesta
 */
sealed class CustomResponse<out T> {
    data class Success<out T>(val data: T) : CustomResponse<T>()
    data class Failure<out T>(val exception: Exception) : CustomResponse<T>()
}