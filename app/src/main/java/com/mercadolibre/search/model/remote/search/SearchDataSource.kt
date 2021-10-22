package com.mercadolibre.search.model.remote.search

import com.mercadolibre.search.model.remote.ApiServices

class SearchDataSource(private val apiServices: ApiServices) {

suspend fun search(siteId:String, query:String){}

}