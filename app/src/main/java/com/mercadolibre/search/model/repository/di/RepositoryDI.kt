package com.mercadolibre.search.model.repository.di

import com.mercadolibre.search.model.repository.search.SearchRepositoryImpl
import org.koin.dsl.module

val RepositoryDI = module {
    factory { SearchRepositoryImpl(get()) }
}