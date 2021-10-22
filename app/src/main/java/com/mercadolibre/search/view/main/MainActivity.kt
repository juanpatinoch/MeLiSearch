package com.mercadolibre.search.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.search.databinding.ActivityMainBinding
import org.koin.core.context.stopKoin

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        stopKoin()
        super.onDestroy()
    }
}