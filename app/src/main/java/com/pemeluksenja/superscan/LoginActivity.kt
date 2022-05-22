package com.pemeluksenja.superscan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.pemeluksenja.superscan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var bind: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.hide()
        bind.moveToRegisterButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        bind.loginButtonCustom.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditTextCustom)
            val password = findViewById<EditText>(R.id.passwordEditTextCustom)
            if (email !== null && password !== null) {
                when {
                    email.length() < 6 -> {
                        email.error = "Email harus lebih panjang dari 6 karakter"
                    }
                    password.length() < 6 -> {
                        password.error = "Password harus lebih panjang dari 6 karakter"
                    }
                    !isValidEmail(email.text.toString()) -> {
                        email.error = "Invalid Email"
                    }
                }
            }
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

//    private fun showLoadingProcess(isLoading: Boolean) {
//        if (isLoading) {
//            bind.loading.visibility = View.VISIBLE
//        } else {
//            bind.loading.visibility = View.GONE
//        }
//    }

    companion object {
        private val TAG = "LoginActivity"
        var TOKEN = ""
    }
}