package com.mercadolibre.search.view.product_detail

import android.app.SearchManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mercadolibre.search.R
import com.mercadolibre.search.databinding.ActivityProductDetailBinding
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.utils.Constants
import com.mercadolibre.search.utils.Utils
import com.mercadolibre.search.view.product_detail.adapter.ProductDetailAttributesAdapter

class ProductDetailActivity : AppCompatActivity() {

    //Binding de Product Detail
    private lateinit var binding: ActivityProductDetailBinding

    //Dto con todos los datos del producto
    private lateinit var resultsDto: ResultsDto

    //variable que nos permite saber si estaba abierta la busqueda despues rotar la pantalla
    private var isSearchOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        getInfoArguments()
        setListeners()
        setUiData()
        setupRecyclerView()
    }

    /**
     * Se asigna binding
     */
    private fun setupBinding() {
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * Se obtiene el parametro que se recibe con los datos del producto
     */
    private fun getInfoArguments() {
        resultsDto = intent.getSerializableExtra("resultsDto") as ResultsDto
    }

    /**
     * Funcion implementada para asignar todos los datos a los elementos del layout
     */
    private fun setUiData() {
        setStandardPrice()
        setInstallments()
        setShipping()
        setImage()
        binding.layoutAppbar.tvSearchTitle.text =
            getString(R.string.product_detail_title).uppercase()
        binding.tvProductDetailName.text = resultsDto.title
        binding.tvProductDetailPrice.text =
            Utils.formatAmountToCurrency(resultsDto.price, resultsDto.currencyId)
    }

    /**
     * Funcion para mostrar el precio del producto sin descuento
     */
    private fun setStandardPrice() {
        if (resultsDto.originalPrice != null) {
            Utils.setStrikethroughText(binding.tvProductDetailStandardPrice)
            binding.tvProductDetailStandardPrice.text =
                Utils.formatAmountToCurrency(resultsDto.originalPrice!!, resultsDto.currencyId)
        } else {
            binding.tvProductDetailStandardPrice.visibility = View.GONE
        }
    }

    /**
     * Funcion para mostrar el numero y valor de las cuotas para pago con TC
     */
    private fun setInstallments() {
        if (resultsDto.installments != null) {
            binding.tvProductDetailInstallments.text = String.format(
                getString(R.string.product_detail_installments),
                resultsDto.installments?.quantity.toString(),
                Utils.formatAmountToCurrency(
                    resultsDto.installments?.amount!!,
                    resultsDto.installments?.currencyId!!
                )
            )
        } else {
            binding.tvProductDetailInstallments.visibility = View.GONE
        }
    }

    /**
     * Funcion para mostrar si tiene envío gratis
     */
    private fun setShipping() {
        if (resultsDto.shipping.freeShipping)
            binding.tvProductDetailShipping.text = getString(R.string.free_shipping)
        else
            binding.tvProductDetailInstallments.visibility = View.GONE
    }

    /**
     * Funcion que carga la imagen del producto con Glide
     * De no cargarse correctamente da la opcion de intentarlo de nuevo
     */
    private fun setImage() {
        Glide.with(this)
            .load(resultsDto.thumbnail)
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                /**
                 * Funcion cuando falla la carga de la imagen
                 * Nos da la opcion de reintentarlo
                 * ivProductDetailRefresh tiene un evento click que vuelve a intentar la carga
                 */
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("GlideException", e.toString())
                    binding.ivProductDetailRefresh.visibility = View.VISIBLE
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
            .into(binding.ivThumbnail)
    }

    /**
     * Funcion para inicializar el recycler view con su adapter
     */
    private fun setupRecyclerView() {
        val adapter = ProductDetailAttributesAdapter()
        binding.rvProductDetail.adapter = adapter
        binding.rvProductDetail.layoutManager = LinearLayoutManager(this@ProductDetailActivity)
        adapter.submitList(resultsDto.attributes)
    }

    /**
     * Se implementan todos los eventos listeners de la clase
     */
    private fun setListeners() {
        //Evento que nos permite saber cuando cerramos la busqueda
        (getSystemService(Context.SEARCH_SERVICE) as SearchManager).setOnDismissListener {
            binding.svProductDetailContent.visibility = View.VISIBLE
            isSearchOpen = false
        }
        //Evento de ir atras
        binding.layoutAppbar.ivSearchBack.setOnClickListener {
            finish()
        }
        //Evento que abre una nueva busqueda
        binding.layoutAppbar.ivSearch.setOnClickListener {
            onSearchRequested()
        }
        //Evento para reintentar la carga de la imagen del producto
        binding.ivProductDetailRefresh.setOnClickListener {
            binding.ivProductDetailRefresh.visibility = View.GONE
            setImage()
        }
    }

    /**
     * Funcion llamada cuando se abre la ventana de busqueda
     */
    override fun onSearchRequested(): Boolean {
        binding.svProductDetailContent.visibility = View.GONE
        isSearchOpen = true
        return super.onSearchRequested()
    }

    /**
     * Funcion llamada cuando se gira la pantalla
     * Se guardan los datos importantes para reconstruir el UI a su ultimo estado
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Constants.productDetailIsSearchOpen, isSearchOpen)
    }

    /**
     * Funciona llamada cuando se restaura la vista despues de rotar la pantalla
     * @param savedInstanceState ultimo estado del UI antes de girar el dispositivo
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isSearchOpen = savedInstanceState.getBoolean(Constants.productDetailIsSearchOpen)
        if (isSearchOpen)
            onSearchRequested()
    }
}