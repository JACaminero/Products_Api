package com.example.products.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.products.R

import androidx.recyclerview.widget.RecyclerView
import com.example.products.Product
import com.squareup.picasso.Picasso

class ProductsRecyclerAdapter(private val dataSet: MutableList<Product>, private val onClick: (Product) -> Unit) :
        ListAdapter<Product, ProductsRecyclerAdapter.ProductViewHolder>(ProductDiffCallback) {

    class ProductViewHolder(view: View, val onClick: (Product) -> Unit) : RecyclerView.ViewHolder(view) {
        var name: TextView? = view.findViewById(R.id.recyclerName)
        var shipping: TextView? = view.findViewById(R.id.recyclerShipping)
        var price: TextView? = view.findViewById(R.id.recyclerPrice)
        var img: ImageView? = view.findViewById(R.id.recyclerImg)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycler_row, viewGroup, false)
        return ProductViewHolder(view, onClick)
    }

    override fun onBindViewHolder(viewHolder: ProductViewHolder, position: Int) {
        var current = dataSet.elementAt(position)
        viewHolder.name?.text = current.name
        viewHolder.price?.text = "Precio: ${current.price.toString()}$"
        viewHolder.shipping?.text = "Envio: ${current.shipping.toString()}$"
        val imgUrl = current.image
        if (imgUrl != null) {
            Picasso.get()
                .load(imgUrl)
                .into(viewHolder.img)
        }

        viewHolder.itemView.setOnClickListener {
            onClick(current)
        }
    }

    override fun getItemCount() = dataSet.size

    object ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return false
        }
    }
}
