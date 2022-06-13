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
import com.pemeluksenja.superscan.model.History
import com.pemeluksenja.superscan.room.ProductDetail

class HistoryAdapter(): RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {
    private val historyList = ArrayList<History>()

    fun setUserData(userItem: ArrayList<History>) {
        historyList.clear()
        historyList.addAll(userItem)
        notifyDataSetChanged()
    }
    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var transactionID: TextView = itemView.findViewById(R.id.transactionID)
        var orderAt: TextView = itemView.findViewById(R.id.orderAt)
        var price: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val createView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.historyitem, parent, false)
        return ListViewHolder(createView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, productName, productPrice, productPict, productQty, paymentCode, totalBills, orderAt, paymentCodeDuration  ) = historyList[position]
        val idTransaction = id.uppercase().chunked(8)
        val order = orderAt.substringAfter("2022")
        val sliceOrder = orderAt.split(order)
        holder.transactionID.text = idTransaction[0]
        holder.orderAt.text = sliceOrder[0]
        holder.price.text = totalBills

        Log.d("OnBindViewHolder: ", id.toString())
        Log.d("OnBindViewHolder: ", orderAt.toString())
        Log.d("OnBindViewHolder: ", totalBills.toString())
    }

    override fun getItemCount(): Int = historyList.size
}