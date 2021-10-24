package com.mercadolibre.search.view.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadolibre.search.databinding.ActivitySearchBinding
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.utils.Constants
import com.mercadolibre.search.view.product_detail.ProductDetailActivity
import com.mercadolibre.search.view.search.adapter.SearchPagingDataAdapter
import com.mercadolibre.search.view.search.adapter.SearchPagingDataAdapterInterface
import com.mercadolibre.search.view_model.search.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), SearchPagingDataAdapterInterface {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchPagingDataAdapter
    private lateinit var query: String

    private val viewModelSearch: SearchViewModel by viewModel()
    private val pagingDataObserver = Observer<PagingData<ResultsDto>> { handlePagingData(it) }
    private var isSearchOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupRecyclerView()
        startObserver()
        getSearchQuery(savedInstanceState)
        setListeners()
    }

    private fun setupBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupRecyclerView() {
        adapter = SearchPagingDataAdapter(this, this)
        binding.apply {
            rvSearchItems.adapter = adapter
            rvSearchItems.layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }

    private fun startObserver() {
        viewModelSearch.pagingData.observe(this, pagingDataObserver)
    }

    private fun getSearchQuery(savedInstanceState: Bundle?) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                binding.layoutAppbar.tvSearchTitle.text = query.trim().uppercase()
                this.query = query
                if (savedInstanceState == null)
                    viewModelSearch.searchByQuery(query)
            }
        }
    }

    private fun setListeners() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchManager.setOnDismissListener {
            binding.clSearchContent.visibility = View.VISIBLE
            isSearchOpen = false
        }
        binding.layoutAppbar.ivSearchBack.setOnClickListener {
            finish()
        }
        binding.layoutAppbar.ivSearch.setOnClickListener {
            onSearchRequested()
        }
    }

    private fun handlePagingData(pagingData: PagingData<ResultsDto>) {
        lifecycleScope.launch {
            Log.d("handlePagingData", "Llega data")
            adapter.submitData(pagingData)
        }
    }

    override fun onSearchRequested(): Boolean {
        binding.clSearchContent.visibility = View.GONE
        startSearch(query, false, null, false)
        isSearchOpen = true
        return true
    }

    override fun onSearchPagingItemClick(resultsDto: ResultsDto) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("resultsDto", resultsDto)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Constants.searchIsSearchOpen, isSearchOpen)
        outState.putParcelable(
            Constants.searchRecyclerView,
            binding.rvSearchItems.layoutManager?.onSaveInstanceState()
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val rvState: Parcelable? = savedInstanceState.getParcelable(Constants.searchRecyclerView)
        binding.rvSearchItems.layoutManager?.onRestoreInstanceState(rvState)
        isSearchOpen = savedInstanceState.getBoolean(Constants.searchIsSearchOpen)
        if (isSearchOpen)
            onSearchRequested()

    }
}