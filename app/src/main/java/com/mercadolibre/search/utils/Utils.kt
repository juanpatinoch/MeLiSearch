package com.mercadolibre.search.utils

import android.graphics.Paint
import android.widget.TextView
import java.text.NumberFormat
import java.util.*

object Utils {

    fun formatAmountToCurrency(amount: Double, currencyId: String): String {
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance(currencyId)
        return format.format(amount.toInt())
    }

    fun setStrikethroughText(tv: TextView) {
        tv.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
    }
}