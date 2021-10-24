package com.mercadolibre.search.view.product_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mercadolibre.search.databinding.ItemAttributesBinding
import com.mercadolibre.search.model.dto.search.AttributesDto

class ProductDetailAttributesAdapter() :
    ListAdapter<AttributesDto, AttributesViewHolder>(AttributeDiffCallback()) {

    private lateinit var binding: ItemAttributesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributesViewHolder {
        val viewHolder = AttributesViewHolder.from(parent)
        binding = viewHolder.binding
        return viewHolder
    }

    override fun onBindViewHolder(holder: AttributesViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.apply {
            binding.tvAttributeName.text = item.name
            binding.tvAttributeValue.text = item.valueName
        }
    }
}

class AttributesViewHolder private constructor(val binding: ItemAttributesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): AttributesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemAttributesBinding.inflate(layoutInflater, parent, false)
            return AttributesViewHolder(binding)
        }
    }
}

class AttributeDiffCallback : DiffUtil.ItemCallback<AttributesDto>() {
    override fun areItemsTheSame(oldItem: AttributesDto, newItem: AttributesDto) =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: AttributesDto, newItem: AttributesDto) =
        oldItem == newItem
}