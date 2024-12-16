import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonesejahtera.R
import android.view.View
import com.example.capstonesejahtera.SummaryItem

class SummaryAdapter(
    private val summaryList: List<SummaryItem>,
    private val onDetailClick: (Int) -> Unit // Callback untuk detail button
) : RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder>() {

    class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val nominalTextView: TextView = itemView.findViewById(R.id.nominalTextView)
        val detailButton: Button = itemView.findViewById(R.id.detailButton) // Tambahkan ini
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

        // Set up button click listener
        holder.detailButton.setOnClickListener {
            onDetailClick(position) // Panggil callback dengan posisi
        }
    }

    override fun getItemCount(): Int = summaryList.size
}
