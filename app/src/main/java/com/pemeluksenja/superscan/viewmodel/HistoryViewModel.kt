package com.pemeluksenja.superscan.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pemeluksenja.superscan.R
import com.pemeluksenja.superscan.model.History
import com.pemeluksenja.superscan.response.HistoryResponse
import com.pemeluksenja.superscan.retrofit.APIConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel(application: Application): ViewModel() {
    val historyList = MutableLiveData<ArrayList<History>>()
    private val list = ArrayList<History>()
    val context = application

    fun getHistory(id: String){

        val sharedPref = context.getSharedPreferences(
            R.string.tokenPref.toString(),
            Context.MODE_PRIVATE
        )
        val token = sharedPref.getString(R.string.tokenValue.toString(), "")
        val client = APIConfiguration.getAPIServices().getHistory("Bearer $token", id)
        client.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()
                    if (resBody !== null) {
                        val items = resBody.history
                        for (item in items){
                            val orderAt = item.orderAt
                            val paymentCode = item.paymentCode
                            val totalBills = item.totalBills
                            val id = item.id
                            for (each in item.product){
                                val productName = each.productName
                                val productPrice = each.productPrice
                                val productPicture = each.picture
                                val productQty = each.quantity
                                val history = History(id, productName, productPrice.toString(), productPicture, productQty.toString(), paymentCode, totalBills.toString(), orderAt, "30 Minute")
                                list.add(history)
                            }
                        }
                        historyList.postValue(list)

                    }
                }
            }
            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Log.e("HistoryViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getHistory(): LiveData<ArrayList<History>> {
        return historyList
    }

}