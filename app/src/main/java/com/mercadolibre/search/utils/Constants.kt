package com.mercadolibre.search.utils

import androidx.paging.PagingConfig

object Constants {
    const val baseUrl = "https://api.mercadolibre.com"
    const val pagingPageSize = 10
    const val defaultSite = "MCO"

    val PAGING_CONFIG = PagingConfig(pageSize = pagingPageSize, enablePlaceholders = false)

    //SaveInstanceStates
    const val homeInitKoin = "initKoin"
    const val homeIsSearchOpen = "isSearchOpen"
    const val searchRecyclerView = "searchRecyclerView"
    const val searchIsSearchOpen = "isSearchOpen"
}