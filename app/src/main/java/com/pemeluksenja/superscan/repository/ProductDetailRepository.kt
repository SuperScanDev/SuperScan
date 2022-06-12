package com.pemeluksenja.superscan.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.pemeluksenja.superscan.room.ProductDetail
import com.pemeluksenja.superscan.room.ProductDetailDao
import com.pemeluksenja.superscan.room.ProductDetailRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ProductDetailRepository(application: Application) {
    private val productDetailDao: ProductDetailDao

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val database = ProductDetailRoomDatabase.getDatabase(application)
        productDetailDao = database.productDao()
    }

    fun insert(productDetail: ProductDetail) {
        executorService.execute { productDetailDao.insert(productDetail) }
    }

    fun clear() = productDetailDao.clear()
    fun getTotal(): Int = productDetailDao.getTotal()

    fun getProducts(): LiveData<List<ProductDetail>> = productDetailDao.getProducts()
}