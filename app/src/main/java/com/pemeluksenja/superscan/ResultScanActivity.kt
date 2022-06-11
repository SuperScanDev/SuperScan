package com.pemeluksenja.superscan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pemeluksenja.superscan.UriUtils.Companion.rotateBitmap
import com.pemeluksenja.superscan.databinding.ActivityResultScanBinding
import java.io.File


class ResultScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultScanBinding

    private val mModelPath = "SuperScanV3.tflite"
    private val mLabelPath = "ListBarang.txt"
    private lateinit var classifier: Classifier
    private lateinit var bitmap: Bitmap
    private val mInputSize = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClassifier()

        var results: MutableList<Classifier.Recognition>? = null
        val myImage = intent.getSerializableExtra("picture") as File
        bitmap = BitmapFactory.decodeFile(myImage.path)
        binding.imgResultScan.setImageBitmap(rotateBitmap(bitmap))
        results = classifier.recognizeImage(bitmap)
        binding.resultPredict.text = results[0].toString()

        for (i in results) {
            Log.d("Hasil", "tes $i")
        }

    }

    private fun initClassifier() {
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
    }

}