package com.intershala.loadedtummy.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.intershala.loadedtummy.R
import com.intershala.loadedtummy.adapter.HomeRecyclerAdapter
import com.intershala.loadedtummy.model.RestaurantsDetails
import com.intershala.loadedtummy.util.ConnectionManager
import org.json.JSONException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerViewHome : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager

    val restaurantsInfoList = ArrayList<RestaurantsDetails>()

    lateinit var recyclerAdapter: HomeRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerViewHome = view.findViewById(R.id.recyclerViewHome)
        layoutManager = LinearLayoutManager(activity)



        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if(ConnectionManager().checkConnectivity(activity as Context)) {

            val JsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val response = data.getJSONObject("data")

                            for (i in 0 until response.length()) {
                                val restroDetailsJSONObj = response.getJSONObject(i)

                                val restroDetailsObject = RestaurantsDetails(
                                    restroDetailsJSONObj.getString("id"),
                                    restroDetailsJSONObj.getString("name"),
                                    restroDetailsJSONObj.getString("rating"),
                                    restroDetailsJSONObj.getString("cost_for_one"),
                                    restroDetailsJSONObj.getString("image_url")
                                )

                                restaurantsInfoList.add(restroDetailsObject)

                                //solve i problem, pass values to adapter, connect recyclerVie to adapter and layout

                            }

                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some error occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(activity as Context, "Volley error occurred", Toast.LENGTH_SHORT)
                        .show()
                }) {

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "5e86f87e49f3b2"
                        return headers
                    }
                }
            queue.add(JsonObjectRequest)

        }else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                activity?.finish()
            }
            dialog.setNegativeButton("Error") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}