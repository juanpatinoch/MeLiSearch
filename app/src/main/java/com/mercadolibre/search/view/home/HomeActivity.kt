package com.mercadolibre.search.view.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.search.databinding.ActivityHomeBinding
import com.mercadolibre.search.di.appComponent
import com.mercadolibre.search.utils.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class HomeActivity : AppCompatActivity() {

    //binding de home
    private lateinit var binding: ActivityHomeBinding

    //variable que nos permite saber si estaba abierta la busqueda despues rotar la pantalla
    private var isSearchOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKoin(savedInstanceState)
        setupBinding()
        setListeners()
    }

    /**
     * Inicializa Koin para la inyección de dependencias
     * @param savedInstanceState != null quiere decir que rotó la pantalla entonces no se inicializa
     */
    private fun initKoin(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            startKoin {
                androidLogger()
                androidContext(this@HomeActivity)
                modules(appComponent(Constants.baseUrl))
            }
    }

    /**
     * Se asigna binding
     */
    private fun setupBinding() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * Se implementan todos los eventos listeners de la clase
     */
    private fun setListeners() {
        //Evento que nos permite saber cuando cerramos la busqueda
        (getSystemService(Context.SEARCH_SERVICE) as SearchManager).setOnDismissListener {
            binding.clContent.visibility = View.VISIBLE
            isSearchOpen = false
        }
        //Evento para abrir la ventana de busqueda
        binding.clSearch.setOnClickListener {
            onSearchRequested()
        }
    }

    /**
     * Funcion llamada cuando se abre la ventana de busqueda
     */
    override fun onSearchRequested(): Boolean {
        binding.clContent.visibility = View.GONE
        isSearchOpen = true
        return super.onSearchRequested()
    }

    /**
     * Funciona llamada cuando se restaura la vista despues de rotar la pantalla
     * @param savedInstanceState ultimo estado del UI antes de girar el dispositivo
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //Se abre la ventana de busqueda, si se tenia abierta al momento de girar la pantalla
        isSearchOpen = savedInstanceState.getBoolean(Constants.homeIsSearchOpen)
        if (isSearchOpen)
            onSearchRequested()
    }

    /**
     * Funcion llamada cuando se gira la pantalla
     * Se guardan los datos importantes para reconstruir el UI a su ultimo estado
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Constants.homeInitKoin, false)
        outState.putBoolean(Constants.homeIsSearchOpen, isSearchOpen)
    }

    /**
     * Funcion llamada cuando se destruye la actividad
     * Cuando se gira la pantalla NO se detiene Koin
     */
    override fun onDestroy() {
        if (isFinishing)
            stopKoin()
        super.onDestroy()
    }
}