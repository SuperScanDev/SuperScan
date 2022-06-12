package com.pemeluksenja.superscan.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productDetail: ProductDetail)

    @Query("SELECT SUM(price*qty) from productdetail")
    fun getTotal(): Int

    @Query("SELECT * from productdetail ORDER BY id ASC")
    fun getProducts(): LiveData<List<ProductDetail>>

    @Query("DELETE FROM productdetail")
    fun clear()
}