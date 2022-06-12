package com.pemeluksenja.superscan

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.pemeluksenja.superscan.databinding.ActivityCameraBinding
import com.pemeluksenja.superscan.response.GetProductResponse
import com.pemeluksenja.superscan.retrofit.APIConfiguration
import com.pemeluksenja.superscan.room.ProductDetail
import com.pemeluksenja.superscan.viewmodel.ProductDetailViewModel
import com.pemeluksenja.superscan.viewmodelfactory.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var capture: ImageCapture? = null

    private val mModelPath = "SuperScanV4.tflite"
    private val mLabelPath = "ListBarang.txt"
    private lateinit var classifier: Classifier
    private lateinit var bitmap: Bitmap
    private val mInputSize = 300
    private lateinit var productDetailViewModel: ProductDetailViewModel
    private var resultScan : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textInputLayout.isEnabled = false
        binding.captureImage.isEnabled = true

        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        initClassifier()
        productDetailViewModel = getViewModel(this@CameraActivity)


        binding.captureImage.setOnClickListener {
            takePhoto()
        }

        binding.btnNextResultPredict.setOnClickListener{
            getProduct(resultScan)
            startActivity(Intent(this@CameraActivity, ProductDetailActivity::class.java))
            finish()
        }

        binding.btnGoBackHome.setOnClickListener{
            startActivity(Intent(this@CameraActivity, MainActivity::class.java))
            finish()
        }

        startCamera()

    }

    private fun takePhoto() {

        val imageCapture = capture ?: return
        val photoFile = UriUtils.createFile(application)
        var results: MutableList<Classifier.Recognition>
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mendapatkan gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    bitmap = BitmapFactory.decodeFile(photoFile.path)
                    results = classifier.recognizeImage(bitmap)
                    binding.resultPredict.text = results.get(0).toString()
                    resultScan = results.get(0).toString()
                    binding.textInputLayout.isEnabled = true
                    binding.captureImage.isEnabled = false
                }

            }
        )


    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        finish()
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            capture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    capture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
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
//                    Log.d("Product: ", resBody.toString())
                    var items = ProductDetail()
                    items.produtId= resBody.id
                    items.name= resBody.productName
                    items.price = resBody.productPrice.toString()
                    items.picture = resBody.picture
                    items.quantity = binding.etProduct.text.toString().toInt()

                    productDetailViewModel.insert(items)
                    Log.d("GetProductResult: ", items.produtId.toString())
                    Log.d("GetProductResult: ", items.name.toString())
                    Log.d("GetProductResult: ", items.price.toString())
                    Log.d("GetProductResult: ", items.quantity.toString())
                    Log.d("GetProductResult: ", items.picture.toString())
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

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun getViewModel(activity: AppCompatActivity): ProductDetailViewModel {
        val detailViewModelFactory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(
            activity,
            detailViewModelFactory
        ).get(ProductDetailViewModel::class.java)
    }
}