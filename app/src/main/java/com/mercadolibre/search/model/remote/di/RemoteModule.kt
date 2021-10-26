package com.mercadolibre.search.model.remote.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mercadolibre.search.model.remote.ApiServices
import com.mercadolibre.search.model.remote.search.SearchDataSource
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Se crea un nuevo modulo para el DataSource
 * Con single creamos un singleton de cada instancia
 * Con factory se crea la instancia del DataSource de Search
 * @param baseUrl -> Url base que le enviamos a Retrofit para las peticiones
 */
fun createRemoteModule(baseUrl: String) = module {

    single { createService(get()) }

    single { createRetrofit(get(), baseUrl) }

    single { createOkHttpClient() }

    single { createMoshiConverterFactory() }

    single { createMoshi() }

    factory { SearchDataSource(get()) }

}

/**
 * Se crea el servicio para Retrofit
 * @param retrofit -> instancia creada de Retrofit
 */
fun createService(retrofit: Retrofit): ApiServices {
    return retrofit.create(ApiServices::class.java)
}

/**
 * Crea una nueva instancia de Retrofit
 * A traves de Retrofit se hacen las peticiones de la API
 * @param okHttpClient -> cliente para Retrofit
 * @param url -> url base para hacer las peticiones a la API
 */
fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}

/**
 * Crea una nueva instancia de OkHttp
 * Utilizado como cliente de Retrofit
 */
fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().apply {
        readTimeout(30L, TimeUnit.SECONDS)
        connectTimeout(30L, TimeUnit.SECONDS)
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(this)
        }
    }.build()
}

/**
 * Se crea una instancia para MoshiConverterFactory
 * Convierte las respuestas de JSON a Dto
 */
fun createMoshiConverterFactory(): MoshiConverterFactory {
    return MoshiConverterFactory.create()
}

/**
 * Se crea una instancia para Moshi
 */
fun createMoshi(): Moshi {
    return Moshi.Builder().build()
}