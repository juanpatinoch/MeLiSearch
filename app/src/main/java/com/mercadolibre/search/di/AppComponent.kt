package com.mercadolibre.search.di

import org.koin.dsl.module

val exampleModule = module {

}

fun appComponent(baseUrl: String) = listOf(
    exampleModule
)