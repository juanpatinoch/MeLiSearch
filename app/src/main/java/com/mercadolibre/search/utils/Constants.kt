package com.mercadolibre.search.utils

import androidx.paging.PagingConfig

/**
 * Clase de constantes utilizadas en el proyecto
 */
object Constants {
    //Url base API
    const val baseUrl = "https://api.mercadolibre.com"

    //Tamaño de la paginación
    const val pagingPageSize = 10

    //Configuración para la paginación
    val PAGING_CONFIG = PagingConfig(pageSize = pagingPageSize, enablePlaceholders = false)

    //SaveInstanceStates
    const val homeInitKoin = "initKoin"
    const val homeIsSearchOpen = "isSearchOpen"
    const val searchRecyclerView = "searchRecyclerView"
    const val searchIsSearchOpen = "isSearchOpen"
    const val productDetailIsSearchOpen = "productDetailIsSearchOpen"
}