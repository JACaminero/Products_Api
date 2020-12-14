package com.example.products.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.products.AuthInfo
import com.example.products.R
import com.example.products.UserSingUpInfo
import com.example.products.services.ApiService
import com.example.products.services.IAuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private var txtEmail: TextView? = null
    private var txtPassword: TextView? = null
    private var txtName: TextView? = null
    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        txtEmail = findViewById(R.id.email)
        txtPassword = findViewById(R.id.password)
        txtName = findViewById(R.id.txt_username_signup)

        findViewById<Button>(R.id.btn_signup).setOnClickListener {
            var email = txtEmail?.text.toString()
            var name = txtName?.text.toString()
            var password = txtPassword?.text.toString()
            val retrofit = apiService.buildRetrofitRequest();
            val service = retrofit.create(IAuthService::class.java)

            var credentials = UserSingUpInfo(email, name, password)
            service.signup(credentials).enqueue(object : Callback<AuthInfo> {
                override fun onFailure(call: Call<AuthInfo>?, t: Throwable?) {
                    Log.v("retrofit", "NO")
                }
                override fun onResponse(call: Call<AuthInfo>?, response: Response<AuthInfo>?) {
                    if (!response!!.isSuccessful) {
                        failMessage()
                        Log.v("retrofit", "NO (pero mejor) " + response.code().toString())
                        return
                    }
                    successMessage()
                    redirectToLogin()
                }
            })
        }
    }

    fun redirectToLogin() {
        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
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