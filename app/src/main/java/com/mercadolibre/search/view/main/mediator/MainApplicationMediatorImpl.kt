package com.mercadolibre.search.view.main.mediator

import android.content.Context

class MainApplicationMediatorImpl(private val context: Context) : MainApplicationMediator {

    private lateinit var onSearchEvent: (() -> Unit)

    override fun getContext() = context

    override fun setSearchEvent(onSearchEvent: () -> Unit) {
        this.onSearchEvent = onSearchEvent
    }

    override fun callSearchEvent() = onSearchEvent.invoke()

}