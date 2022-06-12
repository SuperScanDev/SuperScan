package com.pemeluksenja.superscan.model

import com.pemeluksenja.superscan.room.ProductDetail
import org.json.JSONObject

data class Order(
    val product: ArrayList<OrderDetail>,
    val totalBills: String,
)

data class OrderDetail(
    val product_name: String,
    val product_price: Int,
    val picture: String,
    val quantity: Int
)
