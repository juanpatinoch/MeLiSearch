package com.mercadolibre.search.di

import com.mercadolibre.search.model.remote.di.createRemoteModule
import com.mercadolibre.search.model.repository.di.repositoryModule
import com.mercadolibre.search.use_cases.SearchAPI
import com.mercadolibre.search.view_model.search.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Se declara un modulo de Search utilizando Koin
 * Dentro del modulo se crea una nueva instancia del Caso de Uso SearchAPI
 * Dentro del modulo se crea una nueva instancia del ViewModel SearchViewModel
 */
val searchModule = module {
    factory { SearchAPI(get()) }
    viewModel { SearchViewModel(get()) }
}

/**
 * Se crea una lista con todos los modulos de la app
 * esta función se llama al iniciar la app para inicializar todos los modulos
 * @see createRemoteModule -> modulo del DataSource
 * @see repositoryModule -> modulo del repositorio
 * @see searchModule -> modulo de Search (ViewModel y casos de uso)
 */
fun appComponent(baseUrl: String) = listOf(
    createRemoteModule(baseUrl),
    repositoryModule,
    searchModule
)