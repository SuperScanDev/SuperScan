package com.pemeluksenja.superscan

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Classifier (assetManager: AssetManager, modelPath: String,
                  labelPath: String, inputSize: Int) {

    private var interpreter: Interpreter
    private var labelList : MutableList<String>
    private var INPUT_SIZE : Int = inputSize
    private var PIXEL_SIZE : Int = 3
    private var IMAGE_MEAN = 0
    private var IMAGE_STD = 255.0f
    private var MAX_RESULTS = 3
    private var THRESHOLD = 0.4f

    private val TAG = "Classifier"

    data class Recognition(
        var id : String = "",
        var tittle : String = "",
        var confidence : Float = 0F
    ) {
        override fun toString(): String {
            return tittle
        }
    }

    init {
        val options = Interpreter.Options()
        options.setNumThreads(5)
        options.setUseNNAPI(true)
        interpreter = Interpreter(loadModelFile(assetManager, modelPath), options)
        labelList = loadLabelList(assetManager, labelPath)
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDictionary = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDictionary.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDictionary.startOffset
        val declaredLength = fileDictionary.declaredLength
        return  fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): MutableList<String> {
        return  assetManager.open(labelPath).bufferedReader().useLines { it.toMutableList() }
    }

    fun recognizeImage(bitmap: Bitmap): MutableList<Recognition>{
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        val result = Array(1){ FloatArray(labelList.size)}
        interpreter.run(byteBuffer, result)
        return getSortedResults(result)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)

        bitmap.getPixels(intValues, 0, bitmap.width, 0,0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until INPUT_SIZE){
            for (j in 0 until INPUT_SIZE){
                val input = intValues[pixel++]

                byteBuffer.putFloat((((input.shr(16) and 0xFF) - IMAGE_MEAN)/IMAGE_STD))
                byteBuffer.putFloat((((input.shr(8) and 0xFF) - IMAGE_MEAN)/IMAGE_STD))
                byteBuffer.putFloat((((input and 0xFF) - IMAGE_MEAN)/IMAGE_STD))
            }
        }
        return  byteBuffer
    }

    private fun getSortedResults(labelProbArray: Array<FloatArray>): MutableList<Recognition> {
        Log.d("Classifier", "List Size:(%d %d %d)".format(labelProbArray.size, labelProbArray[0].size, labelList.size))

        val pq = PriorityQueue(
            MAX_RESULTS,
            Comparator<Recognition>{
                    (_, _, confidence1), (_, _, confidence2)
                -> java.lang.Float.compare(confidence1, confidence2) * -1
            })

        for (i in labelList.indices){
            val confidence = labelProbArray[0][i]
            if (confidence >= THRESHOLD){
                pq.add(
                    Recognition("" + i,
                    if (labelList.size > i) {
                        labelList[i]
                    } else
                        "Unknown", confidence)
                )
            }
        }

        Log.d("Classifier", "pqsize:(%d)".format(pq.size))

        val recognitions = ArrayList<Recognition>()
        val recognitionSize = Math.min(pq.size, MAX_RESULTS)
        for (i in 0 until recognitionSize){
            recognitions.add(pq.poll())
        }
        Log.d("hasil", "hasil $recognitions")
        return recognitions
    }

}