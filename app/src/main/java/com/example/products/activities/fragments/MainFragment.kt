package com.example.products.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.products.Product
import com.example.products.R
import com.example.products.adapters.ProductsRecyclerAdapter
import com.example.products.services.ApiService
import com.example.products.services.IProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class   MainFragment : Fragment() {


    lateinit var productsData: MutableList<Product>
    private lateinit var productAdapter: ProductsRecyclerAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading: Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = ApiService().buildRetrofitRequest();
        val service = retrofit.create(IProductService::class.java)

        service.productsFetch(0).enqueue(object : Callback<MutableList<Product>> {
            override fun onFailure(call: Call<MutableList<Product>>?, t: Throwable?) {
                Log.v("retrofit *raging* NOOOOOOOOOOOO", "NO ${t?.message.toString()}")
            }

            override fun onResponse(call: Call<MutableList<Product>>, response: Response<MutableList<Product>>) {
                if (!response.isSuccessful) {
                    Log.v("retrofit NOP", "NO (pero mejor)" + response.code().toString())
                    return
                }
                productsData = response.body()!!
                val recycler = view?.findViewById<RecyclerView>(R.id.productRecycler)
                productAdapter = ProductsRecyclerAdapter(productsData) { adapterOnClick() }
                recycler?.adapter = productAdapter
                layoutManager = LinearLayoutManager(context)
                recycler?.layoutManager = layoutManager
                addScrollerListener(recycler)

                Log.v("retrofit SIII", "SIIII " + response.body())
            }
        })
    }

    fun adapterOnClick() {
        Log.v("PIPO", "CLICKEDDD")
    }

    private fun addScrollerListener(recycler: RecyclerView)
    {
        //attaches scrollListener with RecyclerView
        recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading)
                {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == productsData.size - 1)
                    {
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore()
    {
        var listSize = productsData.size
        var nextLimit = listSize + 10

        val retrofit = ApiService().buildRetrofitRequest();
        val service = retrofit.create(IProductService::class.java)

        service.productsFetch(listSize - 1).enqueue(object : Callback<MutableList<Product>> {
            override fun onFailure(call: Call<MutableList<Product>>?, t: Throwable?) {
                Log.v("retrofit *raging* NOOOOOOOOOOOO", "NO ${t?.message.toString()}")
            }

            override fun onResponse(call: Call<MutableList<Product>>, response: Response<MutableList<Product>>) {
                if (!response.isSuccessful) {
                    Log.v("retrofit NOP", "NO (pero mejor)" + response.code().toString())
                    return
                }
                var counter = 0
                for(i in listSize until nextLimit) {
                    productsData.add(response.body()!![++counter])
                    if (counter == 9){
                        counter = 0
                    }
                }
                productAdapter.notifyDataSetChanged()
                isLoading = false

                Log.v("retrofit SIII", "SIIII " + response.body())

            }
        })
    }
}

