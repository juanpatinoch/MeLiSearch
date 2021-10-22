package com.mercadolibre.search.model.dto

sealed class CustomResponse<out T> {
    data class Success<out T>(val data: T) : CustomResponse<T>()
    data class Failure<out T>(val exception: Exception) : CustomResponse<T>()
}