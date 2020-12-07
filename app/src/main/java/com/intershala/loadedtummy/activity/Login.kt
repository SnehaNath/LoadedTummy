package com.intershala.loadedtummy.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.intershala.loadedtummy.R
import com.intershala.loadedtummy.util.ConnectionManager
import com.intershala.loadedtummy.util.SessionManager
import com.intershala.loadedtummy.util.Validations
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {

    lateinit var etMobileNo: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegister: TextView

    lateinit var sessionManager: SessionManager
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        etMobileNo = findViewById(R.id.etMobileNo)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)                                    //min. 4 char
        txtRegister = findViewById(R.id.txtRegister)

        sessionManager = SessionManager(this)
        sharedPreferences = this.getSharedPreferences(sessionManager.PREF_NAME, Context.MODE_PRIVATE)

        if(sessionManager.isLoggedIn()) {
            startActivity(Intent(this@Login, MainHomeActivity::class.java))
            finish()
        }

        txtForgotPassword.setOnClickListener {

        }

        txtRegister.setOnClickListener {

        }

        btnLogin.setOnClickListener {

            btnLogin.visibility = View.INVISIBLE

            if (Validations().validateMobile(etMobileNo.text.toString()) && Validations().validatePasswordLength(etPassword.text.toString())) {

                if(ConnectionManager().checkConnectivity(this@Login)) {

                    val queue = Volley.newRequestQueue(this@Login)
                    val url = "http://13.235.250.119/v2/login/fetch_result/"

                    var jsonParams = JSONObject()
                    jsonParams.put("mobile_number", etMobileNo.text.toString())
                    jsonParams.put("password", etPassword.text.toString())

                    val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if(success) {
                                val response = data.getJSONObject("data")
// if login data is correct then all the data will be stored in shared preference o/w value of data(JSONObj.) will show some error
                                sessionManager.editor.putString("user_id", response.getString("user_id")).apply()
                                sessionManager.editor.putString("user_name",response.getString("name")).apply()
                                sessionManager.editor.putString("user_email",response.getString("email")).apply()
                                sessionManager.editor.putString("user_mobile_no",response.getString("mobile_number")).apply()
                                sessionManager.editor.putString("user_address", response.getString("address")).apply()
                                sessionManager.setLogin(true)
                                startActivity(Intent(this@Login, MainHomeActivity::class.java))
                                finish()
                            } else {
                                btnLogin.visibility = View.VISIBLE
                                val errorMsg = data.getString("errorMessage")
                                Toast.makeText(this@Login, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        }catch (e : JSONException) {
                            btnLogin.visibility = View.VISIBLE
                            Toast.makeText(this@Login, "Some unexpected error occurred { JSON }", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()                             //------------------------------
                        }
                    },Response.ErrorListener {
                        btnLogin.visibility = View.VISIBLE
                        Toast.makeText(this@Login, "Volley error occurred", Toast.LENGTH_SHORT).show()
                        Log.e("error:::::", "post request fail, Error: ${it.message}")                  //------------------------
                    }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "5e86f87e49f3b2"
                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)

                } else {
                    btnLogin.visibility = View.VISIBLE
                    val dialog = AlertDialog.Builder(this@Login)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection is not Found")
                    dialog.setPositiveButton("Open Settings") { text, listener ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                        finish()
                    }
                    dialog.setNegativeButton("Error") { text, listener ->
                        ActivityCompat.finishAffinity(this@Login)
                    }
                    dialog.create()
                    dialog.show()
                }

            } else {
                btnLogin.visibility = View.VISIBLE
                Toast.makeText(this@Login, "Wrong credentials filled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}