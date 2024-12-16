package com.example.capstonesejahtera

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StockAdapter(
    private val stockList: List<StockItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    inner class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stockTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(stockName: String) {
            stockTextView.text = stockName
            itemView.setOnClickListener {
                onItemClick(stockName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(stockList[position].name)
    }

    override fun getItemCount(): Int = stockList.size
}
