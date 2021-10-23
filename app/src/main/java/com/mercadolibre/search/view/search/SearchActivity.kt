package com.mercadolibre.search.view.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadolibre.search.databinding.ActivitySearchBinding
import com.mercadolibre.search.model.dto.search.SearchDto
import com.mercadolibre.search.view.search.adapter.SearchPagingDataAdapter
import com.mercadolibre.search.view_model.search.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchPagingDataAdapter

    private val viewModelSearch: SearchViewModel by viewModel()
    private val pagingDataObserver = Observer<PagingData<SearchDto>> { handlePagingData(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupRecyclerView()
        startObserver()
        getSearchQuery()
    }

    private fun setupBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupRecyclerView() {
        adapter = SearchPagingDataAdapter()
        binding.apply {
            rvSearchItems.adapter = adapter
            rvSearchItems.layoutManager = LinearLayoutManager(this@SearchActivity)

        }
    }

    private fun startObserver() {
        viewModelSearch.pagingData.observe(this, pagingDataObserver)
    }

    private fun getSearchQuery() {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                viewModelSearch.searchByQuery(query)
            }
        }
    }

    private fun handlePagingData(pagingData: PagingData<SearchDto>) {
        lifecycleScope.launch {
            adapter.submitData(pagingData)
        }
    }
}