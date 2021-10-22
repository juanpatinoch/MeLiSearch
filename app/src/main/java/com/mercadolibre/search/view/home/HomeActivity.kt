package com.mercadolibre.search.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.search.databinding.ActivityHomeBinding
import com.mercadolibre.search.view.main.MainApplication
import com.mercadolibre.search.view_model.app_bar.AppBarViewModel
import com.mercadolibre.search.view_model.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.stopKoin

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModelAppBar: AppBarViewModel by viewModel()
    private val viewModelHome: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        hideNavigationBackIcon()
    }

    private fun setupBinding() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModelHome
        binding.layoutAppbar.viewModel = viewModelAppBar
        MainApplication.mediator.setSearchEvent(onSearchEvent = ::onSearchRequested)
    }

    private fun hideNavigationBackIcon() {
        binding.layoutAppbar.materialToolbar.navigationIcon = null
    }

    override fun onDestroy() {
        stopKoin()
        super.onDestroy()
    }
}