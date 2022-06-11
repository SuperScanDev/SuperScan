package com.pemeluksenja.superscan

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pemeluksenja.superscan.UriUtils.Companion.rotateBitmap
import com.pemeluksenja.superscan.databinding.ActivityResultScanBinding
import com.pemeluksenja.superscan.response.GetProductResponse
import com.pemeluksenja.superscan.retrofit.APIConfiguration
import com.pemeluksenja.superscan.room.ProductDetail
import com.pemeluksenja.superscan.viewmodel.ProductDetailViewModel
import com.pemeluksenja.superscan.viewmodelfactory.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ResultScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultScanBinding

    private val mModelPath = "SuperScanV3.tflite"
    private val mLabelPath = "ListBarang.txt"
    private lateinit var classifier: Classifier
    private lateinit var bitmap: Bitmap
    private val mInputSize = 300
    private lateinit var productDetailViewModel: ProductDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClassifier()
        productDetailViewModel = getViewModel(this@ResultScanActivity)

        var results: MutableList<Classifier.Recognition>?
        val myImage = intent.getSerializableExtra("picture") as File
        bitmap = BitmapFactory.decodeFile(myImage.path)
        results = classifier.recognizeImage(bitmap)

        binding.imgResultScan.setImageBitmap(rotateBitmap(bitmap))
        binding.resultPredict.text = results[0].toString()
        binding.btnNextResultPredict.setOnClickListener {
            getProduct(results.toString())
            startActivity(Intent(this@ResultScanActivity, ProductDetailActivity::class.java))
        }

        for (i in results) {
            Log.d("Hasil", "tes $i")
        }
    }

    private fun initClassifier() {
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
    }

    private fun getProduct(productName: String) {
        val client = APIConfiguration.getAPIServices().getProduct(productName)
        client.enqueue(object : Callback<GetProductResponse> {
            override fun onResponse(
                call: Call<GetProductResponse>,
                response: Response<GetProductResponse>
            ) {
                if (response.isSuccessful) {
//                    showLoadingProcess(false)
                    val resBody = response.body()!!
                    Log.d("Product: ", resBody.toString())
                    val productId = resBody.id
                    val productName = resBody.productName
                    val productPrice = resBody.productPrice
                    val productPicture = resBody.picture
                    val items = ProductDetail( 1, productId, productName, productPrice.toString(),1, productPicture)
                    productDetailViewModel.insert(items)
                    Log.d("GetProductResult: ", productId)
                    Log.d("GetProductResult: ", productName)
                    Log.d("GetProductResult: ", productPrice.toString())
                    Log.d("GetProductResult: ", productPicture)
//                    Log.d("GetProductResult: ", avatar)
//                    Toast.makeText(this@LoginActivity, "Login Berhasil. Selamat Datang, ${loginResult.name}!", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
//                else if (!response.isSuccessful) {
////                    showLoadingProcess(false)
//                    val resBody = JSONObject(response.errorBody()!!.string())
//                    val errorMsg = resBody.getString("message")
////                    Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
//                    Log.d("ErrorMessage", errorMsg)
//                }
            }

            override fun onFailure(call: Call<GetProductResponse>, t: Throwable) {
//                Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("ResultScanActivity:", "onFailure: ${t.message}")
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

