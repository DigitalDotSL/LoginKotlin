package com.example.loginkotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var service: ApiService
    private lateinit var user: User
    private var email: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Creamos el servicio
        service = createApiService()

        val emailInput = findViewById<TextInputEditText?>(R.id.username_input)
        val passwordInput = findViewById<TextInputEditText?>(R.id.password_input)

        val loginButton = findViewById<Button?>(R.id.login_button)
        loginButton.setOnClickListener {
            //Importante hacer trim para eliminar posibles espacios vacios
            email = emailInput.text.toString().trim()
            password = passwordInput.text.toString().trim()
            if (email.isNotEmpty()) {
                if (password.isNotEmpty()) {
                    executeLogin(email, password)
                }
                else {
                    Toast.makeText(this@MainActivity, "Contraseña vacia", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this@MainActivity, "Email vacio", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun createApiService() : ApiService {
        retrofit = Retrofit.Builder()
            .baseUrl("https://soporte.digitaldot.es/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun executeLogin(email: String, password: String) {
        val call = service.login("login", email, password)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    //Intentamos mapear el json a la clase Usuario
                    try {
                        val jsonUser = JSONObject(response.body()!!)
                        val jsonId = jsonUser.optInt("id")
                        val jsonEmail = jsonUser.optString("email")
                        val jsonPassword = jsonUser.optString("password")
                        user = User(jsonId, jsonEmail, jsonPassword)

                        Toast.makeText(this@MainActivity, "Login correcto", Toast.LENGTH_SHORT).show()
                    }
                    //Mostramos mensaje de error devuelto por la api
                    catch (e: Exception) {
                        Log.d("login", e.toString())
                        Toast.makeText(this@MainActivity, response.body(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("login", t.toString())
            }
        })
    }
}