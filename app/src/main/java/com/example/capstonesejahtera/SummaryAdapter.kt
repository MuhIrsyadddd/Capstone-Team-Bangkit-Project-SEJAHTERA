package com.example.capstonesejahtera

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SummaryAdapter(
    private val summaryList: List<SummaryItem>,
    private val onDetailClick: (SummaryItem) -> Unit
) : RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder>() {

    class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val nominalTextView: TextView = itemView.findViewById(R.id.nominalTextView)
        val detailButton: Button = itemView.findViewById(R.id.detailButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_summary, parent, false)
        return SummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val item = summaryList[position]
        holder.titleTextView.text = item.title
        holder.nominalTextView.text = "Rp ${item.totalNominal}"

        // Tambahkan logika klik
        holder.detailButton.setOnClickListener {
            onDetailClick(item)
        }
    }

    override fun getItemCount(): Int = summaryList.size
}
