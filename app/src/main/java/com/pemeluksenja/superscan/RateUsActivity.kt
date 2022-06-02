package com.pemeluksenja.superscan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.pemeluksenja.superscan.databinding.ActivityLoginBinding
import com.pemeluksenja.superscan.databinding.ActivityRateUsBinding
import com.pemeluksenja.superscan.model.Rate
import com.pemeluksenja.superscan.model.Register
import com.pemeluksenja.superscan.response.RateResponse
import com.pemeluksenja.superscan.retrofit.APIConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RateUsActivity : AppCompatActivity() {
    private lateinit var bind: ActivityRateUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRateUsBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.kirim.setOnClickListener {
            val critic = bind.inputText1.text.toString()
            val suggestion = bind.inputText2.text.toString()
            rate(critic, suggestion)
        }
    }

    private fun rate(critic: String, suggestion: String){
        val model = Rate(critic, suggestion)
        val context = application
        val sharedPref = context.getSharedPreferences(
            R.string.tokenPref.toString(),
            Context.MODE_PRIVATE
        )
        val token = sharedPref.getString(R.string.tokenValue.toString(), "")
        val client = APIConfiguration.getAPIServices().rate("Bearer $token", model)
        client.enqueue(object : Callback<RateResponse> {
            override fun onResponse(
                call: Call<RateResponse>,
                response: Response<RateResponse>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()
                    Log.d(TAG, resBody.toString())
                    Toast.makeText(this@RateUsActivity, "Berhasil isi form", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@RateUsActivity, MainActivity::class.java))
                }
                else if (!response.isSuccessful){
                    val resBody = response.message().toString()
                    Toast.makeText(this@RateUsActivity, resBody, Toast.LENGTH_SHORT)
                        .show()
                    Log.d("ErrorMessage: ", resBody)
                }
            }

            override fun onFailure(call: Call<RateResponse>, t: Throwable) {
                Toast.makeText(this@RateUsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    companion object {
        private val TAG = "RateUsActivity"
    }
}