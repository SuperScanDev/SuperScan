package com.pemeluksenja.superscan.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pemeluksenja.superscan.R
import com.pemeluksenja.superscan.room.ProductDetail

class ProductDetailAdapter(): RecyclerView.Adapter<ProductDetailAdapter.ListViewHolder>() {
    private val productList = ArrayList<ProductDetail>()

    fun setUserData(userItem: ArrayList<ProductDetail>) {
        productList.clear()
        productList.addAll(userItem)
        notifyDataSetChanged()
    }
    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var productName: TextView = itemView.findViewById(R.id.productName)
        var productPrice: TextView = itemView.findViewById(R.id.productPrice)
        var productQty: TextView = itemView.findViewById(R.id.productQty)
        var productPict: ImageView = itemView.findViewById(R.id.productImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val createView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.productdetailitems, parent, false)
        return ListViewHolder(createView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, productId, productName, productPrice, productQty,  productPict) = productList[position]
        val price = productPrice.toString().toInt() * productQty

        holder.productName.text = productName
        holder.productPrice.text = "Rp ${price.toString()}"
        holder.productQty.text = "$productQty buah"
        Glide.with(holder.itemView.context)
            .load(productPict)
            .circleCrop()
            .into(holder.productPict)

        Log.d("OnBindViewHolder: ", productName.toString())
        Log.d("OnBindViewHolder: ", productPrice.toString())
        Log.d("OnBindViewHolder: ", productQty.toString())
        Log.d("OnBindViewHolder: ", productPict.toString())
    }

    override fun getItemCount(): Int = productList.size
}