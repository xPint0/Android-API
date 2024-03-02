package com.example.recuperarapi.ui.dashboard

import DriverAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.recuperarapi.Drivers
import com.example.recuperarapi.MainActivity
import com.example.recuperarapi.R
import com.example.recuperarapi.databinding.FragmentDashboardBinding
import org.json.JSONObject

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var requestQueue: RequestQueue
    private lateinit var recyclerView: RecyclerView
    private lateinit var driverAdapter: DriverAdapter
    private val driversList = mutableListOf<Drivers>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = root.findViewById(R.id.rv_pilotos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        driverAdapter = DriverAdapter(driversList)
        recyclerView.adapter = driverAdapter

        requestQueue = Volley.newRequestQueue(context)
        requestDrivers()

        return root
    }

    private fun requestDrivers() {
        val url = "https://ergast.com/api/f1/2023/drivers.json?limit=25"

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
                Log.d("errorAPI", "Error al cargar los conductores")
                (requireActivity() as MainActivity).setCargado(false)
            }
        )

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest)
    }

    private fun parseJsonResponse(response: JSONObject?) {
        response?.let { jsonResponse ->
            val driverTable = jsonResponse.getJSONObject("MRData").getJSONObject("DriverTable")
            val driversArray = driverTable.getJSONArray("Drivers")

            if (driversArray != null) {
                for (i in 0 until driversArray.length()) {
                    val driverObject = driversArray.getJSONObject(i)
                    val driver = Drivers(
                        driverObject.getString("driverId"),
                        driverObject.optString("url", ""),
                        driverObject.getString("givenName"),
                        driverObject.getString("familyName"),
                        driverObject.getString("dateOfBirth"),
                        driverObject.getString("nationality")
                    )
                    driversList.add(driver)
                }
            }

            // Notificar al adaptador que los datos han cambiado
            driverAdapter.notifyDataSetChanged()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}