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
        holder.urlTextView.text = constructor.url
    }

    override fun getItemCount(): Int = constructors.size
}
