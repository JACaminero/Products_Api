package com.example.products

import java.time.LocalDate

data class UserInfo(var identifier: String, var password: String)
data class UserSingUpInfo(var email: String, var username: String, var password: String)

class AuthInfo() {
    lateinit var jwt: String
}

class Product(var name: String, var price: Float?, var description: String, var shipping: Float? = 0f, var categories: List<Category>?) {
    var manufacturer: String? = null
    var model: String? = null
    var sku: Int? = 0
    var type: String? = null
    var upc: String? = null
    var url: String? = null
    var image: String? = null
}

data class Category(var name: String, val publishedAt: LocalDate, val createdAt: LocalDate = publishedAt)
