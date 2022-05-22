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
                    email.length() < 6 -> {
                        email.error = "Email harus lebih panjang dari 6 karakter"
                    }
                    password.length() < 6 -> {
                        password.error = "Password harus lebih panjang dari 6 karakter"
                    }
                    name.length() < 6 -> {
                        name.error = "Nama harus lebih panjang dari 6 karakter"
                    }
                    !isValidEmail(email.text.toString()) -> {
                        email.error = "Invalid Email"
                    }
                }
            }
        }
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