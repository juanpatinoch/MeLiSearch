package com.mercadolibre.search.view.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadolibre.search.R
import com.mercadolibre.search.databinding.ActivitySearchBinding
import com.mercadolibre.search.model.dto.search.ResultsDto
import com.mercadolibre.search.utils.Constants
import com.mercadolibre.search.view.product_detail.ProductDetailActivity
import com.mercadolibre.search.view.search.adapter.SearchPagingDataAdapter
import com.mercadolibre.search.view.search.adapter.SearchPagingDataAdapterInterface
import com.mercadolibre.search.view_model.search.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), SearchPagingDataAdapterInterface {

    //binding de activity
    private lateinit var binding: ActivitySearchBinding

    //adapter para el listado de productos
    private lateinit var adapter: SearchPagingDataAdapter

    //Producto que se estan buscando
    private lateinit var query: String

    //ViewModel de Search | ViewModel inyectado por Koin
    private val viewModelSearch: SearchViewModel by viewModel()

    //Observer para la paginación, va estar escuchando cuando lleguen resultados de la consulta de productos
    private val pagingDataObserver = Observer<PagingData<ResultsDto>> { handlePagingData(it) }

    //variable que nos permite saber si estaba abierta la busqueda despues rotar la pantalla
    private var isSearchOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupAdapter()
        setupRecyclerView()
        startObserver()
        getSearchQuery(savedInstanceState)
        setListeners()
    }

    /**
     * Se asigna binding
     */
    private fun setupBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * Funcion que asigna el adapter
     * Aqui se define los diferentes estados de la lista de productos mientras pagina
     */
    private fun setupAdapter() {
        //Se asigna adapter
        adapter = SearchPagingDataAdapter(this, this)
        lifecycleScope.launch {
            //Se obtiene mediante flow en tiempo real el estado de la paginación
            adapter.loadStateFlow.collectLatest { loadState ->
                //Se obtiene si hay algún error
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }

                //Se muestra un progress bar al final del recycler view mientras carga los resultados
                if (loadState.refresh is LoadState.Loading) {
                    showLoading()
                }

                //Si hay un error se muesta una vista con mensaje de error
                if (error != null) {
                    binding.tvError.text =
                        String.format(getString(R.string.search_error), error.error.message)
                    showError()
                }
                //Si ya terminó de paginar y hay productos en la lista oculta el progress bar
                else if (loadState.append.endOfPaginationReached && adapter.itemCount > 0) {
                    hideLoading()
                }
                //Si ya terminó de paginar y no hay items, muestra una vista donde indica que no se encontraron productos
                else if (loadState.append.endOfPaginationReached && adapter.itemCount == 0) {
                    showEmptyState()
                }
            }
        }
    }

    /**
     * Funcion para inicializar el recycler view con su adapter
     */
    private fun setupRecyclerView() {
        binding.rvSearchItems.adapter = adapter
        binding.rvSearchItems.layoutManager = LinearLayoutManager(this@SearchActivity)
    }

    /**
     * Se inicializa el observer conectado al ViewModel
     */
    private fun startObserver() {
        viewModelSearch.pagingData.observe(this, pagingDataObserver)
    }

    /**
     * Funcion que obtiene el Query de busqueda
     * Se hace un llamado al ViewModel para consultar los productos por el query recibido
     */
    private fun getSearchQuery(savedInstanceState: Bundle?) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                binding.layoutAppbar.tvSearchTitle.text = query.trim().uppercase()
                this.query = query
                //Verificamos si se giró la pantalla NO consulta de nuevo
                if (savedInstanceState == null)
                    viewModelSearch.searchByQuery(query)
            }
        }
    }

    /**
     * Se implementan todos los eventos listeners de la clase
     */
    private fun setListeners() {
        //Evento que nos permite saber cuando cerramos la busqueda
        (getSystemService(Context.SEARCH_SERVICE) as SearchManager).setOnDismissListener {
            binding.clSearchContent.visibility = View.VISIBLE
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
        //Cuando hay un error al dar click intenta la busqueda de nuevo
        binding.clError.setOnClickListener {
            showRecyclerView()
            viewModelSearch.searchByQuery(query)
        }
    }

    /**
     * Funcion que se llama cuando el evento del observer se dispara
     */
    private fun handlePagingData(pagingData: PagingData<ResultsDto>) {
        lifecycleScope.launch {
            //Se envian los datos de la paginacion al adapter
            adapter.submitData(pagingData)
        }
    }

    /**
     * Funcion llamada cuando se abre la ventana de busqueda
     */
    override fun onSearchRequested(): Boolean {
        binding.clSearchContent.visibility = View.GONE
        startSearch(query, false, null, false)
        isSearchOpen = true
        return true
    }

    /**
     * Funcion de la interfaz del Adapter
     * Al dar click en un Item del RecyclerView lo envia a la vista de Detalle
     */
    override fun onSearchPagingItemClick(resultsDto: ResultsDto) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("resultsDto", resultsDto)
        startActivity(intent)
    }

    /**
     * Funcion llamada cuando se gira la pantalla
     * Se guardan los datos importantes para reconstruir el UI a su ultimo estado
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Constants.searchIsSearchOpen, isSearchOpen)
        //Se guarda el ultimo estado de UI del RecyclerView
        outState.putParcelable(
            Constants.searchRecyclerView,
            binding.rvSearchItems.layoutManager?.onSaveInstanceState()
        )
    }

    /**
     * Funciona llamada cuando se restaura la vista despues de rotar la pantalla
     * @param savedInstanceState ultimo estado del UI antes de girar el dispositivo
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        //Se restablece el ultimo estado del RecyclerView
        val rvState: Parcelable? = savedInstanceState.getParcelable(Constants.searchRecyclerView)
        binding.rvSearchItems.layoutManager?.onRestoreInstanceState(rvState)

        isSearchOpen = savedInstanceState.getBoolean(Constants.searchIsSearchOpen)
        if (isSearchOpen)
            onSearchRequested()
    }

    /**
     * Eventos para mostrar y ocultar estados en la vista del activity
     */

    private fun showLoading() {
        showRecyclerView()
        hideEmptyState()
        hideError()
        binding.pbSearch.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        showRecyclerView()
        hideEmptyState()
        hideError()
        binding.pbSearch.visibility = View.GONE
    }

    private fun showRecyclerView() {
        hideEmptyState()
        hideError()
        binding.clRecyclerView.visibility = View.VISIBLE
    }

    private fun hideRecyclerView() {
        binding.clRecyclerView.visibility = View.GONE
    }

    private fun showError() {
        hideEmptyState()
        hideRecyclerView()
        binding.clError.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.clError.visibility = View.GONE
    }

    private fun showEmptyState() {
        hideError()
        hideRecyclerView()
        binding.clEmptyState.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.clEmptyState.visibility = View.GONE
    }
}