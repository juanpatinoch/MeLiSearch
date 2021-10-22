package com.mercadolibre.search.model.remote.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mercadolibre.search.model.remote.ApiServices
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

fun createRemoteDI(baseUrl: String) = module {

    single { createService(get()) }

    single { createRetrofit(get(), baseUrl) }

    single { createOkHttpClient() }

    single { createMoshiConverterFactory() }

    single { createMoshi() }

}

fun createService(retrofit: Retrofit): ApiServices {
    return retrofit.create(ApiServices::class.java)
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}

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

fun createMoshiConverterFactory(): MoshiConverterFactory {
    return MoshiConverterFactory.create()
}

fun createMoshi(): Moshi {
    return Moshi.Builder().build()
}