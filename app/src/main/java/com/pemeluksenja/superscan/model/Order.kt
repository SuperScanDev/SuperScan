package com.pemeluksenja.superscan.model

import org.json.JSONObject

data class Order(
    val product: ArrayList<JSONObject>,
    val totalBills: String,
)
