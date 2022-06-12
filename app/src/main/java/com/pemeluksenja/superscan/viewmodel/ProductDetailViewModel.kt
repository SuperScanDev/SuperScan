package com.pemeluksenja.superscan.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pemeluksenja.superscan.repository.ProductDetailRepository
import com.pemeluksenja.superscan.room.ProductDetail

class ProductDetailViewModel(application: Application) : ViewModel() {
    private val productDetailRepository: ProductDetailRepository =
        ProductDetailRepository(application)

    fun insert(productDetail: ProductDetail) {
        productDetailRepository.insert(productDetail)
    }

    fun clear() = productDetailRepository.clear()

    fun getTotal(): Int = productDetailRepository.getTotal()

    fun getProducts(): LiveData<List<ProductDetail>> = productDetailRepository.getProducts()
}