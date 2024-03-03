import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recuperarapi.Drivers
import com.example.recuperarapi.R

class DriverAdapter(private val drivers: List<Drivers>) :
    RecyclerView.Adapter<DriverAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val givenNameTextView: TextView = itemView.findViewById(R.id.givenNameTextView)
        val nationalityTextView: TextView = itemView.findViewById(R.id.nationalityTextView)
        val dateofBirthTextView: TextView = itemView.findViewById(R.id.dateofBirthTextView)
        val urlTextView: TextView = itemView.findViewById(R.id.urlTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val url = drivers[position].url
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
            .inflate(R.layout.item_driver, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val driver = drivers[position]
        holder.givenNameTextView.text = driver.givenName + " " + driver.familyName
        //holder.familyNameTextView.text = driver.familyName
        holder.nationalityTextView.text = "Nationality: " + driver.nationality
        holder.dateofBirthTextView.text = "Date of Birth: " + driver.dateOfBirth
        holder.urlTextView.text = "Biography: " + driver.url
    }

    override fun getItemCount(): Int = drivers.size
}
