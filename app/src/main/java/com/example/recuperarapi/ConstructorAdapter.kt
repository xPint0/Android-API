import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recuperarapi.Constructors
import com.example.recuperarapi.R

class ConstructorAdapter(private val constructors: List<Constructors>) :
    RecyclerView.Adapter<ConstructorAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val nationalityTextView: TextView = itemView.findViewById(R.id.nationalityTextView)
        val urlTextView: TextView = itemView.findViewById(R.id.urlTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val url = constructors[position].url
                    openUrlInChrome(url)
                }
            }
        }

        private fun openUrlInChrome(url: String) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main")
            intent.data = Uri.parse(url)
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_constructor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val constructor = constructors[position]
        holder.nameTextView.text = constructor.name
        holder.nationalityTextView.text = "Nationality: " + constructor.nationality
        holder.urlTextView.text = "Biography: " + constructor.url
    }

    override fun getItemCount(): Int = constructors.size
}
