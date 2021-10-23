package com.mercadolibre.search.view.product_detail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mercadolibre.search.databinding.ActivityProductDetailBinding
import com.mercadolibre.search.model.dto.search.ResultsDto

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var resultsDto: ResultsDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        getInfoArguments()
        setUiData()
    }

    private fun setupBinding() {
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getInfoArguments() {
        resultsDto = intent.getSerializableExtra("resultsDto") as ResultsDto
    }

    private fun setUiData() {
        try {
            Glide.with(this)
                .load(resultsDto.thumbnail)
                .circleCrop()
                .thumbnail(0.1f)
                .into(binding.ivThumbnail)
        } catch (e: Exception) {
            Log.e("ERROR", e.message ?: "ERROR")
        }
    }
}