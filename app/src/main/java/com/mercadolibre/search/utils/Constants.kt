package com.mercadolibre.search.utils

import androidx.paging.PagingConfig

object Constants {
    const val baseUrl = "https://api.mercadolibre.com"
    const val databaseVersion = 2
    const val pagingPageSize = 10

    val PAGING_CONFIG = PagingConfig(pageSize = pagingPageSize, enablePlaceholders = false)
}