package com.mercadolibre.search.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.search.R
import org.koin.core.context.stopKoin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onDestroy() {
        stopKoin()
        super.onDestroy()
    }
}