package com.mercadolibre.search.model.remote

import com.mercadolibre.search.model.dto.search.SearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    /**
     * Funcion de la interfaz API para traer el listado de productos
     * @param query Producto que se va buscar
     * @param limit Cantidad de resultados que va retornar la busqueda
     * @param offset A partir de que posicion va retornar los resultados
     */
    @GET("/sites/MCO/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<SearchResponseDto>

}