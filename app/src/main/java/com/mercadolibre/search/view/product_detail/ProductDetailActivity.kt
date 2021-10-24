package com.mercadolibre.search.view.product_detail

import android.app.SearchManager
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mercadolibre.search.R
import com.mercadolibre.search.databinding.ActivityProductDetailBinding
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.utils.Constants
import com.mercadolibre.search.utils.Utils
import com.mercadolibre.search.view.product_detail.adapter.ProductDetailAttributesAdapter

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var resultsDto: ResultsDto

    private var isSearchOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        getInfoArguments()
        setListeners()
        setUiData()
        setupRecyclerView()
    }

    private fun setupBinding() {
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getInfoArguments() {
        resultsDto = intent.getSerializableExtra("resultsDto") as ResultsDto
    }

    private fun setUiData() {
        binding.apply {
            setStandardPrice()
            setInstallments()
            setShipping()
            setImage()
            layoutAppbar.tvSearchTitle.text = getString(R.string.product_detail_title).uppercase()
            tvProductDetailName.text = resultsDto.title
            tvProductDetailPrice.text =
                Utils.formatAmountToCurrency(resultsDto.price, resultsDto.currencyId)

        }
    }

    private fun setStandardPrice() {
        if (resultsDto.originalPrice != null) {
            binding.tvProductDetailStandardPrice.text =
                Utils.formatAmountToCurrency(resultsDto.originalPrice!!, resultsDto.currencyId)
            binding.tvProductDetailStandardPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            binding.tvProductDetailStandardPrice.visibility = View.GONE
        }
    }

    private fun setInstallments() {
        if (resultsDto.installments != null) {
            binding.tvProductDetailInstallments.text = String.format(
                getString(R.string.product_detail_installments),
                resultsDto.installments?.quantity.toString(),
                Utils.formatAmountToCurrency(
                    resultsDto.installments?.amount!!,
                    resultsDto.installments?.currencyId!!
                )
            )
        } else {
            binding.tvProductDetailInstallments.visibility = View.GONE
        }
    }

    private fun setShipping() {
        if (resultsDto.shipping.freeShipping)
            binding.tvProductDetailShipping.text = getString(R.string.free_shipping)
        else
            binding.tvProductDetailInstallments.visibility = View.GONE
    }

    private fun setImage() {
        try {
            Glide.with(this)
                .load(resultsDto.thumbnail)
                .centerCrop()
                .into(binding.ivThumbnail)
        } catch (e: Exception) {
            Log.e("ERROR", e.message ?: "ERROR")
        }
    }

    private fun setupRecyclerView() {
        val adapter = ProductDetailAttributesAdapter()
        binding.apply {
            rvProductDetail.adapter = adapter
            rvProductDetail.layoutManager = LinearLayoutManager(this@ProductDetailActivity)
            adapter.submitList(resultsDto.attributes)
        }
    }

    private fun setListeners() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchManager.setOnDismissListener {
            binding.svProductDetailContent.visibility = View.VISIBLE
            isSearchOpen = false
        }
        binding.layoutAppbar.ivSearchBack.setOnClickListener {
            finish()
        }
        binding.layoutAppbar.ivSearch.setOnClickListener {
            onSearchRequested()
        }
    }

    override fun onSearchRequested(): Boolean {
        binding.svProductDetailContent.visibility = View.GONE
        isSearchOpen = true
        return super.onSearchRequested()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Constants.productDetailIsSearchOpen, isSearchOpen)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isSearchOpen = savedInstanceState.getBoolean(Constants.productDetailIsSearchOpen)
        if (isSearchOpen)
            onSearchRequested()
    }
}