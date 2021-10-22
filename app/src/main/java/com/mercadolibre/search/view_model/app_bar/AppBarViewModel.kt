package com.mercadolibre.search.view_model.app_bar

import androidx.lifecycle.ViewModel
import com.mercadolibre.search.view.main.MainApplication

class AppBarViewModel() : ViewModel() {

    fun openSearch() {
        MainApplication.mediator.callSearchEvent()
    }

}