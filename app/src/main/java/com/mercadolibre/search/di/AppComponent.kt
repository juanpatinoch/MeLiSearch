package com.mercadolibre.search.di

import com.mercadolibre.search.model.remote.di.createRemoteModule
import com.mercadolibre.search.model.repository.di.repositoryModule
import com.mercadolibre.search.use_cases.SearchAPI
import com.mercadolibre.search.view_model.app_bar.AppBarViewModel
import com.mercadolibre.search.view_model.home.HomeViewModel
import com.mercadolibre.search.view_model.search.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appBarModule = module {
    viewModel { AppBarViewModel() }
}

val homeModule = module {
    viewModel { HomeViewModel() }
}

val searchModule = module {
    factory { SearchAPI(get()) }
    viewModel { SearchViewModel(get()) }
}

fun appComponent(baseUrl: String) = listOf(
    createRemoteModule(baseUrl),
    repositoryModule,
    appBarModule,
    homeModule,
    searchModule
)