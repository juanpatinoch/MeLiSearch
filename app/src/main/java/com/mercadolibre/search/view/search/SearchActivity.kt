package com.mercadolibre.search.view.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.search.databinding.ActivitySearchBinding
import com.mercadolibre.search.view_model.search.SearchViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModelSearch: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        getSearchQuery()
    }

    private fun setupBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getSearchQuery() {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                viewModelSearch.searchByQuery(query)
            }
        }
    }
}