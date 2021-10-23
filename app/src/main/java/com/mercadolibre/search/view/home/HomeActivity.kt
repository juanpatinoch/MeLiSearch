package com.mercadolibre.search.view.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.search.databinding.ActivityHomeBinding
import org.koin.core.context.stopKoin

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setListeners()
    }

    private fun setupBinding() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setListeners() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchManager.setOnDismissListener {
            binding.clContent.visibility = View.VISIBLE
        }
        binding.clSearch.setOnClickListener {
            onSearchRequested()
        }
    }

    override fun onSearchRequested(): Boolean {
        binding.clContent.visibility = View.GONE
        return super.onSearchRequested()
    }

    override fun onDestroy() {
        stopKoin()
        super.onDestroy()
    }
}