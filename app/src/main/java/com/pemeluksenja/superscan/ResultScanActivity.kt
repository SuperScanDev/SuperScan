package com.pemeluksenja.superscan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pemeluksenja.superscan.UriUtils.Companion.rotateBitmap
import com.pemeluksenja.superscan.databinding.ActivityResultScanBinding
import com.pemeluksenja.superscan.ml.SuperScan
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File

class ResultScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultScanBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.resultPredict.text = productList[max]
        // Releases model resources if no longer used.

        for (i in 0..49){
            Log.d("Hasil", "${outputFeature0.floatArray[i].toString()} + ${productList[i]}")
        }
        Log.d("qwwqd", outputs.toString())


        model.close()

        binding.imgResultScan.setImageBitmap(result)
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

}