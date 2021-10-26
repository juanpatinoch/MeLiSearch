package com.mercadolibre.search.utils

import android.graphics.Paint
import android.widget.TextView
import java.text.NumberFormat
import java.util.*

object Utils {

    /**
     * Esta funcion convierte un numero (monto) en un texto formateado como moneda
     * @param amount valor a convertir
     * @param currencyId codigo de la moneda
     * @return String -> Retorna un texto formateado a la moneda que se le envia como param
     */
    fun formatAmountToCurrency(amount: Double, currencyId: String): String {
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance(currencyId)
        return format.format(amount.toInt())
    }

    /**
     * Funcion que tacha el texto de un TextView
     */
    fun setStrikethroughText(tv: TextView) {
        tv.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
    }
}