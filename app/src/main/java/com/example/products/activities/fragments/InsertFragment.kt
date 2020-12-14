package com.example.products.activities.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.products.services.IProductService
import com.example.products.Product
import com.example.products.R
import com.example.products.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertFragment : Fragment() {
    private var nameView: EditText? = null
    private var priceView: EditText? = null
    private var shippingView: EditText? = null
    private var upcView: EditText? = null
    private var skuView: EditText? = null
    private var typeView: EditText? = null
    private var urlView: EditText? = null
    private var descriptionView: EditText? = null

    private val apiService = ApiService()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameView = view.findViewById(R.id.productName)
        priceView = view.findViewById(R.id.productPriceInsert)
        skuView = view.findViewById(R.id.sku)
        descriptionView = view.findViewById(R.id.productDescription)
        typeView = view.findViewById(R.id.type)
        urlView = view.findViewById(R.id.url)
        upcView = view.findViewById(R.id.upc)
        shippingView = view.findViewById(R.id.productShippingInsert)

        view.findViewById<Button>(R.id.btnPublish)
                .setOnClickListener {
                    publish()

                }
    }

    fun publish() {
        val retrofit = apiService.buildRetrofitRequest();
        val service = retrofit.create(IProductService::class.java)
        val productData = getProductData()

        service.insert(productData).enqueue(object : Callback<Product> {
            override fun onFailure(call: Call<Product>?, t: Throwable?) {
                Log.v("retrofit *raging* NOOOOOOOOOOOO", "NO")
                failMessage()
            }

            override fun onResponse(call: Call<Product>?, response: Response<Product>?) {
                if (!response!!.isSuccessful) {
                    Log.v("retrofit NOP", "NO (pero mejor)" + response.code().toString())
                    failMessage()
                    return
                }
                Log.v("retrofit SIII", "SIIII " + response.code().toString())
                successMessage()
            }
        })
    }

    private fun getProductData(): Product {
        val productName = nameView?.text.toString()
        val price = priceView?.text.toString().toFloatOrNull()
        val shipping = shippingView?.text.toString().toFloatOrNull()
        val upc = upcView?.text.toString()
        val sku = skuView?.text.toString()
        val url = urlView?.text.toString()
        val type = typeView?.text.toString()
        val desc = descriptionView?.text.toString()

        return Product(productName, price, desc, shipping, null)
    }

    fun successMessage() {
        Toast.makeText(context, "Operacion exitosa", Toast.LENGTH_SHORT)
                .show()
    }

    fun failMessage() {
        Toast.makeText(context, "Operacion Fallida, revise sus credenciales", Toast.LENGTH_SHORT)
                .show()
    }
}