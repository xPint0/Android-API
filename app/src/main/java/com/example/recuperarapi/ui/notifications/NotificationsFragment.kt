package com.example.recuperarapi.ui.notifications

import ConstructorAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.recuperarapi.Constructors
import com.example.recuperarapi.Drivers
import com.example.recuperarapi.MainActivity
import com.example.recuperarapi.R
import com.example.recuperarapi.databinding.FragmentNotificationsBinding
import org.json.JSONObject

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var requestQueue: RequestQueue
    private lateinit var recyclerView: RecyclerView
    private lateinit var constructorAdapter: ConstructorAdapter
    private val constructorsList = mutableListOf<Constructors>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = root.findViewById(R.id.rv_escuderias)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        constructorAdapter = ConstructorAdapter(constructorsList)
        recyclerView.adapter = constructorAdapter

        requestQueue = Volley.newRequestQueue(context)
        requestConstructors()


        return root
    }

    private fun requestConstructors() {
        val url = "https://ergast.com/api/f1/2023/constructors.json?limit=12"

        // Crear la solicitud GET
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Manejar la respuesta JSON aquí
                // Por ejemplo, puedes cambiar el valor del booleano en el MainActivity
                parseJsonResponse(response)
                (requireActivity() as MainActivity).setCargado(true)
            },
            Response.ErrorListener { error ->
                Log.d("errorAPI", "Error al cargar las escuderias")
                (requireActivity() as MainActivity).setCargado(false)
            }
        )

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest)
    }

    private fun parseJsonResponse(response: JSONObject?) {
        response?.let { jsonResponse ->
            val constructorTable = jsonResponse.getJSONObject("MRData").getJSONObject("ConstructorTable")
            val constructorsArray = constructorTable.getJSONArray("Constructors")

            if (constructorsArray != null) {
                for (i in 0 until constructorsArray.length()) {
                    val contructorObject = constructorsArray.getJSONObject(i)
                    val constructor = Constructors(
                        contructorObject.getString("constructorId"),
                        contructorObject.optString("url", ""),
                        contructorObject.getString("name"),
                        contructorObject.getString("nationality")
                    )
                    constructorsList.add(constructor)
                }
            }

            // Notificar al adaptador que los datos han cambiado
            constructorAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}