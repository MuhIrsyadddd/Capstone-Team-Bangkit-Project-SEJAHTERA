package com.example.capstonesejahtera

import com.github.mikephil.charting.formatter.ValueFormatter

class CurrencyValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return try {
            String.format("Rp.%,.2f", value).replace(".", ".")
        } catch (e: Exception) {
            "Rp0,00"
        }
    }
}