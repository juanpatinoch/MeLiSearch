package com.mercadolibre.search.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mercadolibre.search.databinding.FragmentSearchBinding
import com.mercadolibre.search.view_model.search.SearchViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    //private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupBinding(inflater)
        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater) {
        binding = FragmentSearchBinding.inflate(inflater)
        //binding.viewModel = viewModel
    }
}