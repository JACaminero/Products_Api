package com.example.products.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.products.*
import com.example.products.services.ApiService
import com.example.products.services.IAuthService
import com.example.products.services.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide();

        findViewById<Button>(R.id.btn_register).setOnClickListener {
            startActivity(
                Intent(this@LoginActivity, SignUpActivity::class.java)
            )
        }

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            var username = findViewById<EditText>(R.id.txt_username).text.toString()
            var pass = findViewById<EditText>(R.id.password).text.toString()

            retroLogin( username, pass )
        }
    }

    private fun retroLogin(user: String, pass: String) {
        val retrofit = apiService.buildRetrofitRequest();
        val service = retrofit.create(IAuthService::class.java)

        var credentials = UserInfo(user, pass)
        service.login(credentials).enqueue(object : Callback<AuthInfo> {
            override fun onFailure(call: Call<AuthInfo>?, t: Throwable?) {
                Log.v("retrofit", "NO")
            }
            override fun onResponse(call: Call<AuthInfo>?, response: Response<AuthInfo>?) {
                if (!response!!.isSuccessful) {
                    failMessage()
                    Log.v("retrofit", "NO (pero mejor)" + response.code().toString())
                    return
                }
                val sm = SessionManager(this@LoginActivity)
                sm.saveAuthToken(response.body()?.jwt.toString())
                successMessage()
                redirectToMain()
            }
        })
    }

    fun redirectToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }

    fun successMessage() {
        Toast.makeText(baseContext, "Operacion exitosa", Toast.LENGTH_SHORT)
            .show()
    }
    fun failMessage() {
        Toast.makeText(baseContext, "Operacion Fallida, revise sus credenciales", Toast.LENGTH_SHORT)
            .show()
    }
}