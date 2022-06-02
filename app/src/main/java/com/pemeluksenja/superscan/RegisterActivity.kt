package com.pemeluksenja.superscan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.pemeluksenja.superscan.databinding.ActivityRegisterBinding
import com.pemeluksenja.superscan.model.Register
import com.pemeluksenja.superscan.retrofit.APIConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var bind: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.hide()
        bind.registerButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.UsernameEditTextCustom)
            Log.d("EditText: ", name.text.toString())
            val email = findViewById<EditText>(R.id.emailEditTextCustom)
            val password = findViewById<EditText>(R.id.passwordEditTextCustom)
            if (email !== null && name !== null && password !== null) {
                when {
                    email.length() == 0 -> {
                        email.error = "Email tidak boleh kosong"
                    }
                    password.length() == 0 -> {
                        password.error = "Password Email tidak boleh kosong"
                    }
                    name.length() == 0 -> {
                        name.error = "Nama Email tidak boleh kosong"
                    }
                    !isValidEmail(email.text.toString()) -> {
                        email.error = "Invalid Email"
                    }
                    else -> {
                        val userName = name.text.toString()
                        val userEmail = email.text.toString()
                        val userPassword = password.text.toString()
                        register(userName, userEmail, userPassword)
                    }
                }
            }
        }
    }
    private fun register(name: String, email: String, password: String) {
        val model = Register(name, email, password)
        val client = APIConfiguration.getAPIServices().register(model)
        client.enqueue(object : Callback<Register> {
            override fun onResponse(
                call: Call<Register>,
                response: Response<Register>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()
                    Log.d("RegisterResponseBody: ", resBody.toString())
                    Toast.makeText(this@RegisterActivity, "Register Berhasil", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                }
                else if (!response.isSuccessful){
                    val resBody = response.message().toString()
                    Toast.makeText(this@RegisterActivity, resBody, Toast.LENGTH_SHORT)
                        .show()
                    Log.d("ErrorMessage: ", resBody)
                }
            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


//    private fun showLoadingProcess(isLoading: Boolean) {
//        if (isLoading) {
//            bind.loading.visibility = View.VISIBLE
//        } else {
//            bind.loading.visibility = View.GONE
//        }
//    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        private val TAG = "RegisterActivity"
    }
}