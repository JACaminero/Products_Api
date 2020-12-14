package com.example.products.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {

    fun buildRetrofitRequest(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        const val BASE_URL = "http://10.0.0.16:1337"
        private val client = OkHttpClient()
    }
}