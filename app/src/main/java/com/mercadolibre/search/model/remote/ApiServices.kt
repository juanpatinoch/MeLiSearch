package com.mercadolibre.search.model.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("/sites/{site_id}")
    suspend fun search(
        @Path("site_id") siteId: String,
        @Query("search") query: String
    )

}