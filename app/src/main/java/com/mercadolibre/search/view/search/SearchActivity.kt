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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadolibre.search.R
import com.mercadolibre.search.databinding.ActivitySearchBinding
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.utils.Constants
import com.mercadolibre.search.view.product_detail.ProductDetailActivity
import com.mercadolibre.search.view.search.adapter.SearchPagingDataAdapter
import com.mercadolibre.search.view.search.adapter.SearchPagingDataAdapterInterface
import com.mercadolibre.search.view_model.search.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
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
        setupAdapter()
        setupRecyclerView()
        startObserver()
        getSearchQuery(savedInstanceState)
        setListeners()
    }

    private fun setupBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupAdapter() {
        adapter = SearchPagingDataAdapter(this, this)
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                if (loadState.refresh is LoadState.Loading) {
                    showLoading()
                    Log.e("loadState", "showLoading")
                }
                if (error != null) {
                    binding.tvError.text =
                        String.format(getString(R.string.search_error), error.error.message)
                    showError()
                    Log.e("loadState", "showError")
                } else if (loadState.append.endOfPaginationReached && adapter.itemCount > 0) {
                    hideLoading()
                    Log.e("loadState", "hideLoading")
                } else if (loadState.append.endOfPaginationReached && adapter.itemCount == 0) {
                    showEmptyState()
                    Log.e("loadState", "showEmptyState")
                }
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            showRecyclerView()
            hideEmptyState()
            hideError()
            pbSearch.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            showRecyclerView()
            hideEmptyState()
            hideError()
            pbSearch.visibility = View.GONE
        }
    }

    private fun showRecyclerView() {
        binding.apply {
            hideEmptyState()
            hideError()
            clRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun hideRecyclerView() {
        binding.apply {
            clRecyclerView.visibility = View.GONE
        }
    }

    private fun showError() {
        binding.apply {
            hideEmptyState()
            hideRecyclerView()
            clError.visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        binding.apply {
            clError.visibility = View.GONE
        }
    }

    private fun showEmptyState() {
        binding.apply {
            hideError()
            hideRecyclerView()
            clEmptyState.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyState() {
        binding.apply {
            clEmptyState.visibility = View.GONE
        }
    }


    private fun setupRecyclerView() {
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
        binding.clError.setOnClickListener {
            showRecyclerView()
            viewModelSearch.searchByQuery(query)
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