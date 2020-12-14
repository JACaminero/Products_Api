package com.example.products.services

import com.example.products.AuthInfo
import com.example.products.Product
import com.example.products.UserInfo
import com.example.products.UserSingUpInfo
import retrofit2.Call
import retrofit2.http.*

interface IAuthService {
    @POST("auth/local")
    fun login(@Body userInfo: UserInfo): Call<AuthInfo>
    @POST("auth/local/register")
    fun signup(@Body userInfo: UserSingUpInfo): Call<AuthInfo>
}

interface IProductService {
    @POST("products")
    fun insert(@Body product: Product): Call<Product>

    @GET("categories")
    fun categoryFetch()

    @GET("products")
    fun productsFetch(@Query("_start") start: Int, @Query("_limit") limit: Int = 10): Call<MutableList<Product>>
}