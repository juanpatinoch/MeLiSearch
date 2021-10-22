package com.mercadolibre.search.view.main.mediator

import android.content.Context

interface MainApplicationMediator {

    fun getContext(): Context

    fun setSearchEvent(onSearchEvent: (() -> Unit))

    fun callSearchEvent()

}