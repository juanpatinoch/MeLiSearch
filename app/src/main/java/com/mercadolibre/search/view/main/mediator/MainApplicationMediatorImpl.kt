package com.mercadolibre.search.view.main.mediator

import android.content.Context

class MainApplicationMediatorImpl(private val context: Context) : MainApplicationMediator {

    override fun getContext() = context

}