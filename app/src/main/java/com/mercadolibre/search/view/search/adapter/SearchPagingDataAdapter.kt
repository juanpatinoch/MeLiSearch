package com.mercadolibre.search.view.search.adapter

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
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

/**
 * Adapter para mostrar item de Search
 */
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

        //Se asignar todos los datos al layout para mostrar la info del producto
        binding.tvItemSearchTitle.text = item.title
        binding.tvItemSearchAmount.text =
            Utils.formatAmountToCurrency(item.price, item.currencyId)
        setStandardPrice(item, binding.tvItemSearchOriginalAmount)
        setInstallments(item.installments, binding.tvItemSearchInstallments)
        setFreeShipping(item.shipping, binding.tvItemSearchShipping)
        setImage(binding.ivItemSearchThumbnail, binding.ivItemSearchRefresh, item.thumbnail)
        //Evento para reintentar la carga de la imagen del producto
        binding.ivItemSearchRefresh.setOnClickListener {
            binding.ivItemSearchRefresh.visibility = View.GONE
            setImage(binding.ivItemSearchThumbnail, binding.ivItemSearchRefresh, item.thumbnail)
        }
        //Evento que me envia al detalle del producto
        binding.clItemSearch.setOnClickListener {
            searchPagingDataAdapterInterface.onSearchPagingItemClick(item)
        }
    }

    /**
     * Funcion para mostrar el precio del producto sin descuento
     * @param resultsDto producto
     * @param tvOriginalAmount TextView
     */
    private fun setStandardPrice(resultsDto: ResultsDto, tvOriginalAmount: TextView) {
        if (resultsDto.originalPrice != null) {
            Utils.setStrikethroughText(tvOriginalAmount)
            tvOriginalAmount.text =
                Utils.formatAmountToCurrency(resultsDto.originalPrice, resultsDto.currencyId)
            tvOriginalAmount.visibility = View.VISIBLE
        } else {
            tvOriginalAmount.visibility = View.GONE
        }
    }

    /**
     * Funcion para mostrar el numero y valor de las cuotas para pago con TC
     * @param installments dto de las cuotas de la TC
     * @param tvInstallments TextView
     */
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

    /**
     * Funcion para mostrar si tiene envío gratis
     * @param shipping dto de shippin
     * @param tvShipping TextView
     */
    private fun setFreeShipping(shipping: ShippingDto, tvShipping: TextView) {
        if (shipping.freeShipping) {
            tvShipping.text = context.getText(R.string.free_shipping)
            tvShipping.visibility = View.VISIBLE
        } else {
            tvShipping.visibility = View.GONE
        }
    }

    /**
     * Funcion que carga la imagen del producto con Glide
     * De no cargarse correctamente da la opcion de intentarlo de nuevo
     * @param ivThumbnail ImageView donde se va mostrar la imagen del producto
     * @param ivRefresh ImageView que se muestra cuando no carga correctamente a imagen del prod.
     * @param uri Url de la imagen del producto
     */
    private fun setImage(ivThumbnail: ImageView, ivRefresh: ImageView, uri: String) {
        Glide.with(context)
            .load(uri)
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                /**
                 * Funcion cuando falla la carga de la imagen
                 * Nos da la opcion de reintentarlo
                 * ivRefresh tiene un evento click que vuelve a intentar la carga
                 */
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("GlideException", e.toString())
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
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ResultsDto, newItem: ResultsDto): Boolean =
        oldItem == newItem
}