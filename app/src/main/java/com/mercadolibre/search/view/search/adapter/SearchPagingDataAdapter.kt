package com.mercadolibre.search.view.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mercadolibre.search.databinding.ItemSearchBinding
import com.mercadolibre.search.model.dto.search.ResultsDto

class SearchPagingDataAdapter(
    private val searchPagingDataAdapterInterface: SearchPagingDataAdapterInterface
) : PagingDataAdapter<ResultsDto, SearchViewHolder>(USER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.getInstance(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        // Note that item may be null, ViewHolder must support binding null item as placeholder
        holder.binding.tvTitle.text = item?.title
        holder.binding.clItemSearch.setOnClickListener {
            searchPagingDataAdapterInterface.onSearchPagingItemClick(item!!)
        }
    }
}

class SearchViewHolder(val binding: ItemSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun getInstance(parent: ViewGroup): SearchViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSearchBinding.inflate(layoutInflater, parent, false)
            return SearchViewHolder(binding)
        }
    }
}

val USER_COMPARATOR = object : DiffUtil.ItemCallback<ResultsDto>() {
    override fun areItemsTheSame(oldItem: ResultsDto, newItem: ResultsDto): Boolean =
        // User ID serves as unique ID
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ResultsDto, newItem: ResultsDto): Boolean =
        // Compare full contents (note: Java users should call .equals())
        oldItem == newItem
}