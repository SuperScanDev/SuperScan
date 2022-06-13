package com.pemeluksenja.superscan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pemeluksenja.superscan.adapter.ProductDetailAdapter
import com.pemeluksenja.superscan.databinding.ActivityProductDetailBinding
import com.pemeluksenja.superscan.model.Order
import com.pemeluksenja.superscan.model.OrderDetail
import com.pemeluksenja.superscan.response.OrderResponse
import com.pemeluksenja.superscan.retrofit.APIConfiguration
import com.pemeluksenja.superscan.room.ProductDetail
import com.pemeluksenja.superscan.viewmodel.ProductDetailViewModel
import com.pemeluksenja.superscan.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var bind: ActivityProductDetailBinding
    private lateinit var productDetailViewModel: ProductDetailViewModel
    private lateinit var productDetailAdapter: ProductDetailAdapter
    private val productList = ArrayList<OrderDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.customtoolbar)
        setSupportActionBar(toolbar)
        toolbar.setOnClickListener {
            startActivity(Intent(this@ProductDetailActivity, CameraActivity::class.java))
            finish()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)


        productDetailAdapter = ProductDetailAdapter()
        productDetailViewModel = getViewModel(this@ProductDetailActivity)


        productDetailViewModel.getProducts().observe(this@ProductDetailActivity) { item ->
            val list = ArrayList<ProductDetail>()
            if (item != null) {
                Log.d("ProductDetailActivity", item.toString())
                for (each in item) {
                    val name = each.name
                    val qty = each.quantity
                    val price = each.price
                    val image = each.picture
                    val productId = each.produtId
                    val id = each.id

                    val product = ProductDetail(id, productId.toString(), name, price, qty, image)
                    list.add(product)
                    Log.d("JSON ProductList: ", list.toString())

                    val productObject = OrderDetail(name!!, price!!.toInt(), image!!, qty)
                    productList.add(productObject)
                    Log.d("JSONObject ProductList", productObject.toString())
                }


            }

            productDetailAdapter.setUserData(list)
            var bills = 0
            CoroutineScope(Dispatchers.IO).launch {
                bills = productDetailViewModel.getTotal()
                Log.d("BillsResult", bills.toString())
                bind.totalBills.text = "Rp $bills"
                val context = application
                val sharedPref = context.getSharedPreferences(
                    R.string.tokenPref.toString(),
                    Context.MODE_PRIVATE
                )
                val editor = sharedPref.edit()
                editor.putString(R.string.bills.toString(), bills.toString())
                editor.apply()
            }
            Log.d("ProductOrder", productList.toString())

            bind.pay.setOnClickListener {
                Log.d("BillsResultOrder", bills.toString())
                order(productList, bills.toString())
            }
        }

        bind.productDetailRV.adapter = productDetailAdapter
        bind.productDetailRV.layoutManager = LinearLayoutManager(this@ProductDetailActivity)

        bind.backToCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun order(product: ArrayList<OrderDetail>, totalBills: String) {
        val context = application
        val sharedPref = context.getSharedPreferences(
            R.string.tokenPref.toString(),
            Context.MODE_PRIVATE
        )
        val token = sharedPref.getString(R.string.tokenValue.toString(), "")
        val model = Order(product, totalBills)
        val client = APIConfiguration.getAPIServices().order("Bearer $token", model)
        client.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(
                call: Call<OrderResponse>,
                response: Response<OrderResponse>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()!!
                    Log.d("OrderResponseBody: ", resBody.toString())
                    Toast.makeText(this@ProductDetailActivity, "Order Berhasil", Toast.LENGTH_SHORT)
                        .show()
                    CoroutineScope(Dispatchers.IO).launch {
                        productDetailViewModel.clear()
                    }
                    val sendData = Intent(this@ProductDetailActivity, PaymentDetailActivity::class.java)
                    sendData.putExtra(PaymentDetailActivity.EXTRA_CODE, resBody.paymentCode)
//                    var total = sharedPref.getString(R.string.bills.toString(), "")
                    sendData.putExtra(PaymentDetailActivity.EXTRA_BILLS, resBody.totalBills.toString())
                    sendData.putExtra(PaymentDetailActivity.EXTRA_DUE, resBody.orderAt)
                    sendData.putExtra(PaymentDetailActivity.EXTRA_DURATION, "30 Menit")

                    startActivity(sendData)
                } else if (!response.isSuccessful) {
                    val resBody = response.message().toString()
                    Toast.makeText(this@ProductDetailActivity, resBody, Toast.LENGTH_SHORT)
                        .show()
                    Log.d("ErrorMessage: ", resBody)
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "${t.message}", Toast.LENGTH_SHORT)
                    .show()
                Log.e("ProductDetailActivity: ", "onFailure: ${t.message}")
            }
        })
    }



    private fun getViewModel(activity: AppCompatActivity): ProductDetailViewModel {
        val detailViewModelFactory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(
            activity,
            detailViewModelFactory
        ).get(ProductDetailViewModel::class.java)
    }
}