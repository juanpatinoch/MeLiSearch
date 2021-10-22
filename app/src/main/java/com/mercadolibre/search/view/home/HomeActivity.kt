package com.mercadolibre.search.view.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.search.databinding.ActivityHomeBinding
import com.mercadolibre.search.view.main.MainApplication
import com.mercadolibre.search.view_model.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.stopKoin

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        hideNavigationBackIcon()
    }

    private fun setupBinding() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        MainApplication.mediator.setSearchEvent(onSearchEvent = ::onSearchRequested)
    }

    private fun hideNavigationBackIcon() {
        binding.layoutAppbar.materialToolbar.navigationIcon = null
        binding.layoutAppbar.g1.visibility = View.GONE
    }

    override fun onDestroy() {
        stopKoin()
        super.onDestroy()
    }
}