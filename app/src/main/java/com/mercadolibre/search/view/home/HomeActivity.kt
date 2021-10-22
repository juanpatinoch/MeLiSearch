package com.mercadolibre.search.view.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.View
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
        setViewModels()
        setSearchEvent()
        hideNavigationBackIcon()
        setListeners()
    }

    private fun setupBinding() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setViewModels() {
        binding.viewModel = viewModelHome
        binding.layoutAppbar.viewModel = viewModelAppBar
    }

    private fun setSearchEvent() {
        MainApplication.mediator.setSearchEvent(onSearchEvent = ::onSearchRequested)
    }

    private fun hideNavigationBackIcon() {
        binding.layoutAppbar.materialToolbar.navigationIcon = null
    }

    private fun setListeners() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchManager.setOnDismissListener {
            binding.layoutAppbar.materialToolbar.visibility = View.VISIBLE
        }
    }

    override fun onSearchRequested(): Boolean {
        binding.layoutAppbar.materialToolbar.visibility = View.GONE
        return super.onSearchRequested()
    }

    override fun onDestroy() {
        stopKoin()
        super.onDestroy()
    }
}