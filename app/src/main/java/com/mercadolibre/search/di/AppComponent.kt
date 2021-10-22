package com.mercadolibre.search.di

import com.mercadolibre.search.view_model.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { HomeViewModel() }
}

fun appComponent(baseUrl: String) = listOf(
    homeModule
)