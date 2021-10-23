package com.mercadolibre.search.model.remote

import com.mercadolibre.search.model.dto.search.SearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("/sites/{site_id}/search")
    suspend fun search(
        @Path("site_id") siteId: String,
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<SearchResponseDto>

}