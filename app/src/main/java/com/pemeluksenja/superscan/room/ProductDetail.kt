package com.pemeluksenja.superscan.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class ProductDetail(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "productId")
    var produtId: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "price")
    var price: String? = null,

    @ColumnInfo (name = "qty")
    var quantity: Int = 1,

    @ColumnInfo (name = "picture")
    var picture: String? = null,
) : Parcelable
