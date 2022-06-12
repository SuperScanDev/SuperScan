package com.pemeluksenja.superscan.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    val id: String,
    val productName: String,
    val productPrice: String,
    val productPicture: String,
    val productQty: String,
    val paymentCode: String,
    val totalBills: String,
    val orderAt: String,
    val paymentCodeDuration: String,
) : Parcelable
