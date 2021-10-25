package com.mercadolibre.search.view.search.adapter

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mercadolibre.search.R
import com.mercadolibre.search.databinding.ItemSearchBinding
import com.mercadolibre.search.model.dto.search.InstallmentsDto
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.model.dto.search.ShippingDto
import com.mercadolibre.search.utils.Utils

class SearchPagingDataAdapter(
    private val searchPagingDataAdapterInterface: SearchPagingDataAdapterInterface,
    private val context: Context
) : PagingDataAdapter<ResultsDto, SearchViewHolder>(USER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.getInstance(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)!!
        val binding = holder.binding
        binding.tvItemSearchTitle.text = item.title
        binding.tvItemSearchAmount.text =
            Utils.formatAmountToCurrency(item.price, item.currencyId)
        setStandardPrice(item, binding.tvItemSearchOriginalAmount)
        setInstallments(item.installments, binding.tvItemSearchInstallments)
        setFreeShipping(item.shipping, binding.tvItemSearchShipping)
        setImage(binding.ivItemSearchThumbnail, binding.ivItemSearchRefresh, item.thumbnail)
        binding.ivItemSearchRefresh.setOnClickListener {
            binding.ivItemSearchRefresh.visibility = View.GONE
            setImage(binding.ivItemSearchThumbnail, binding.ivItemSearchRefresh, item.thumbnail)
        }
        binding.clItemSearch.setOnClickListener {
            searchPagingDataAdapterInterface.onSearchPagingItemClick(item)
        }
    }

    private fun setStandardPrice(resultsDto: ResultsDto, tvOriginalAmount: TextView) {
        if (resultsDto.originalPrice != null) {
            tvOriginalAmount.text =
                Utils.formatAmountToCurrency(resultsDto.originalPrice, resultsDto.currencyId)
            tvOriginalAmount.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            tvOriginalAmount.visibility = View.VISIBLE
        } else {
            tvOriginalAmount.visibility = View.GONE
        }
    }

    private fun setInstallments(installments: InstallmentsDto?, tvInstallments: TextView) {
        if (installments == null) {
            tvInstallments.visibility = View.GONE
        } else if (installments.amount != null && installments.currencyId != null) {
            tvInstallments.visibility = View.VISIBLE
            val amountString = Utils.formatAmountToCurrency(
                installments.amount,
                installments.currencyId
            )
            tvInstallments.text =
                String.format(
                    context.getString(R.string.product_detail_installments),
                    installments.quantity.toString(),
                    amountString
                )
        }
    }

    private fun setFreeShipping(shipping: ShippingDto, tvShipping: TextView) {
        if (shipping.freeShipping) {
            tvShipping.text = context.getText(R.string.free_shipping)
            tvShipping.visibility = View.VISIBLE
        } else {
            tvShipping.visibility = View.GONE
        }
    }

    private fun setImage(ivThumbnail: ImageView, ivRefresh: ImageView, uri: String) {
        Glide.with(context)
            .load(uri)
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    ivRefresh.visibility = View.VISIBLE
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(ivThumbnail)
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