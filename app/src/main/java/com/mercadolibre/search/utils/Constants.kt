package com.mercadolibre.search.utils

import androidx.paging.PagingConfig

object Constants {
    const val baseUrl = "https://api.mercadolibre.com"
    const val pagingPageSize = 10
    const val typeStandard = "standard"
    const val defaultSite = "MCO"
    const val defaultCurrency = "COP"

    val PAGING_CONFIG = PagingConfig(pageSize = pagingPageSize, enablePlaceholders = false)
}