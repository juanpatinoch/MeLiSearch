package com.mercadolibre.search.view.main

import android.app.Application
import com.mercadolibre.search.di.appComponent
import com.mercadolibre.search.utils.Constants
import com.mercadolibre.search.view.main.mediator.MainApplicationMediator
import com.mercadolibre.search.view.main.mediator.MainApplicationMediatorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mediator = MainApplicationMediatorImpl(this)
        initKoin()
    }

    companion object {
        @JvmStatic
        lateinit var mediator: MainApplicationMediator
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appComponent(Constants.baseUrl))
        }
        val nada = this@MainApplication
    }

}