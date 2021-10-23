package com.mercadolibre.search.view.product_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.search.databinding.ActivityProductDetailBinding
import com.mercadolibre.search.model.dto.search.ResultsDto

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var resultsDto: ResultsDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        getInfoArguments()
    }

    private fun setupBinding() {
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getInfoArguments() {
        resultsDto = intent.getSerializableExtra("resultsDto") as ResultsDto
    }
}