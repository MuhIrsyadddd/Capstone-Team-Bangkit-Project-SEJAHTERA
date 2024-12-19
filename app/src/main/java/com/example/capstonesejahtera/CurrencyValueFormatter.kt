package com.example.capstonesejahtera

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.NumberFormat
import java.util.Locale

// Tambahkan kelas ini di luar EmasActivity
class CurrencyValueFormatter : ValueFormatter() {
    private val localeID = Locale("in", "ID")
    private val currencyFormat = NumberFormat.getCurrencyInstance(localeID)

    override fun getFormattedValue(value: Float): String {
        return currencyFormat.format(value.toDouble()).replace("Rp", "Rp ")
    }
}