package com.mercadolibre.search.view.search.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mercadolibre.search.R
import com.mercadolibre.search.databinding.ItemSearchBinding
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.utils.Utils

class SearchPagingDataAdapter(
    private val searchPagingDataAdapterInterface: SearchPagingDataAdapterInterface,
    private val context: Context
) : PagingDataAdapter<ResultsDto, SearchViewHolder>(USER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.getInstance(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        try {
            val item = getItem(position)!!
            // Note that item may be null, ViewHolder must support binding null item as placeholder
            holder.binding.tvItemSearchTitle.text = item.title
            holder.binding.tvItemSearchAmount.text =
                Utils.formatAmountToCurrency(item.price, item.currencyId)
            Log.e("steps", "step 1")
            setInstallments(item, holder.binding.tvItemSearchInstallments)
            setFreeShipping(item, holder.binding.tvItemSearchShipping)
            setImage(item.thumbnail, holder.binding.ivItemSearchThumbnail)
            holder.binding.clItemSearch.setOnClickListener {
                searchPagingDataAdapterInterface.onSearchPagingItemClick(item)
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message ?: "ERROR")
        }
    }

    private fun setInstallments(resultsDto: ResultsDto, tvInstallments: TextView) {
        if (resultsDto.installments == null) {
            tvInstallments.visibility = View.GONE
        } else if (resultsDto.installments.amount != null && resultsDto.installments.currencyId != null) {
            tvInstallments.visibility = View.VISIBLE
            val amountString = Utils.formatAmountToCurrency(
                resultsDto.installments.amount,
                resultsDto.installments.currencyId
            )
            tvInstallments.text =
                String.format(
                    context.getString(R.string.product_detail_installments),
                    resultsDto.installments.quantity.toString(),
                    amountString
                )
        }
    }

    private fun setFreeShipping(resultsDto: ResultsDto, tvShipping: TextView) {
        Log.e("dto", resultsDto.toString())
        if (resultsDto.shipping.freeShipping) {
            tvShipping.text = context.getText(R.string.free_shipping)
            tvShipping.visibility = View.VISIBLE
        }
        else {
            tvShipping.visibility = View.GONE
        }
    }

    private fun setImage(uri: String, iv: ImageView) {
        try {
            Glide.with(context)
                .load(uri)
                .centerCrop()
                .into(iv)
        } catch (e: Exception) {
            Log.e("ERROR", e.message ?: "ERROR")
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