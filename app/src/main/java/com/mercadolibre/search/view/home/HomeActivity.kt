package com.mercadolibre.search.view.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.search.databinding.ActivityHomeBinding
import com.mercadolibre.search.di.appComponent
import com.mercadolibre.search.utils.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private var isSearchOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKoin(savedInstanceState)
        setupBinding()
        setListeners()
    }

    private fun initKoin(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            startKoin {
                androidLogger()
                androidContext(this@HomeActivity)
                modules(appComponent(Constants.baseUrl))
            }
    }

    private fun setupBinding() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setListeners() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchManager.setOnDismissListener {
            binding.clContent.visibility = View.VISIBLE
            isSearchOpen = false
        }
        binding.clSearch.setOnClickListener {
            onSearchRequested()
        }
    }

    override fun onSearchRequested(): Boolean {
        binding.clContent.visibility = View.GONE
        isSearchOpen = true
        return super.onSearchRequested()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isSearchOpen = savedInstanceState.getBoolean(Constants.homeIsSearchOpen)
        if (isSearchOpen)
            onSearchRequested()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Constants.homeInitKoin, false)
        outState.putBoolean(Constants.homeIsSearchOpen, isSearchOpen)
    }

    override fun onDestroy() {
        if (isFinishing)
            stopKoin()
        super.onDestroy()
    }
}