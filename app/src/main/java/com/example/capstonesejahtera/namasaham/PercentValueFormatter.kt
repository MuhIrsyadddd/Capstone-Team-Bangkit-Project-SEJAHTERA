package com.example.capstonesejahtera.namasaham

import com.github.mikephil.charting.formatter.ValueFormatter

class PercentValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return String.format("%.2f%%", value * 100) // Mengonversi ke persen
    }
}
