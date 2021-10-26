package com.mercadolibre.search.model.repository.di

import com.mercadolibre.search.model.repository.search.SearchRepository
import com.mercadolibre.search.model.repository.search.SearchRepositoryImpl
import org.koin.dsl.module

/**
 * Instancia para el modulo del repositorio de Search
 */
val repositoryModule = module {
    factory { SearchRepositoryImpl(get()) as SearchRepository }
}