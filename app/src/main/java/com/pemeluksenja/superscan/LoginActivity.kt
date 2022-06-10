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
import com.pemeluksenja.superscan.model.Login
import com.pemeluksenja.superscan.response.LoginResponse
import com.pemeluksenja.superscan.response.LoginResult
import com.pemeluksenja.superscan.retrofit.APIConfiguration
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                    email.length() == 0 -> {
                        email.error = "Email tidak boleh kosong"
                    }
                    password.length() == 0 -> {
                        password.error = "Password tidak boleh kosong"
                    }
                    !isValidEmail(email.text.toString()) -> {
                        email.error = "Format Email salah"
                    }
                    else -> {
                        login(email.text.toString(), password.text.toString())
                        var context = application
                        var sharedPref = context.getSharedPreferences(
                            R.string.tokenPref.toString(),
                            Context.MODE_PRIVATE
                        )
                        val editor = sharedPref.edit()
                        editor.putString(getString(R.string.email), email.text.toString())
                        editor.putString(getString(R.string.password), password.text.toString())
                        editor.apply()
//                        showLoadingProcess(true)
                    }
                }
            }
        }
    }
    private fun login(email: String, password: String) {
        val model = Login(email, password)
        val client = APIConfiguration.getAPIServices().login(model)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
//                    showLoadingProcess(false)
                    val resBody = response.body()!!
                    val loginResult = resBody.loginResult
                    Log.d("Login: ", resBody.toString())
                    val token = loginResult.token
                    val userId = loginResult.userId
                    val userName = loginResult.name
                    val avatar = loginResult.avatar
                    Log.d("Token: ", token)
                    val context = application
                    val sharedPref = context.getSharedPreferences(
                        R.string.tokenPref.toString(),
                        Context.MODE_PRIVATE
                    )
                    val editor = sharedPref.edit()
                    editor.putString(R.string.tokenValue.toString(), token)
                    editor.putString(R.string.userId.toString(), userId)
                    editor.putString(R.string.userName.toString(), userName)
                    editor.putString(R.string.avatar.toString(), avatar)
                    Log.d("LoginResult: ", token)
                    Log.d("LoginResult: ", userId)
                    Log.d("LoginResult: ", userName)
                    Log.d("LoginResult: ", avatar)
                    editor.apply()
                    Toast.makeText(this@LoginActivity, "Login Berhasil. Selamat Datang, ${loginResult.name}!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else if (!response.isSuccessful) {
//                    showLoadingProcess(false)
                    val resBody = JSONObject(response.errorBody()!!.string())
                    val errorMsg = resBody.getString("message")
                    Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    Log.d("ErrorMessage", errorMsg)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
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
    }
}