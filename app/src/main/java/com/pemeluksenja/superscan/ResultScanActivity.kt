package com.pemeluksenja.superscan

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.pemeluksenja.superscan.UriUtils.Companion.rotateBitmap
import com.pemeluksenja.superscan.databinding.ActivityResultScanBinding
import com.pemeluksenja.superscan.ml.SuperScan
import com.pemeluksenja.superscan.model.Login
import com.pemeluksenja.superscan.response.GetProductResponse
import com.pemeluksenja.superscan.response.LoginResponse
import com.pemeluksenja.superscan.retrofit.APIConfiguration
import com.pemeluksenja.superscan.room.ProductDetail
import com.pemeluksenja.superscan.viewmodel.ProductDetailViewModel
import com.pemeluksenja.superscan.viewmodelfactory.ViewModelFactory
import org.json.JSONObject
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ResultScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultScanBinding
    private var getFile: File? = null
    private lateinit var productDetailViewModel: ProductDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productDetailViewModel = getViewModel(this@ResultScanActivity)

        val myFile = intent.getSerializableExtra("picture") as File
        getFile = myFile
        val result = BitmapFactory.decodeFile(getFile?.path)
        //load file txt
        val fileName = "List Barang.txt"
        val productList = application.assets.open(fileName).bufferedReader().use{it.readText()}.split("\n")
        //scaling bitmap
        var resized = Bitmap.createScaledBitmap(result, 600, 600, true)
        val model = SuperScan.newInstance(this)
        var tbuffer = TensorImage.fromBitmap(resized)
        var byteBuffer = tbuffer.buffer

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 300, 300, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)

        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        var max = getMax(outputFeature0.floatArray)
        var resultScan = productList[max].replace("\r", "")
        binding.resultPredict.text = productList[max]
        // Releases model resources if no longer used.

        for (i in 0..49){
            Log.d("Hasil", "${outputFeature0.floatArray[i].toString()} + ${productList[i]}")
        }
        Log.d("qwwqd", outputs.toString())


        model.close()

        binding.imgResultScan.setImageBitmap(result)

        binding.btnNextResultPredict.setOnClickListener {
            getProduct(resultScan)
            startActivity(Intent(this@ResultScanActivity, ProductDetailActivity::class.java))
        }
    }

    private fun getMax(arr:FloatArray) : Int{
        var ind = 0;
        var min = 0.0f;

        for(i in 0..49)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i;
            }
        }
        return ind
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

