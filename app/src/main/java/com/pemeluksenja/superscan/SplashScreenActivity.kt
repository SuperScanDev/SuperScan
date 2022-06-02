package com.pemeluksenja.superscan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        val handlerSplash = Handler(Looper.getMainLooper())
        handlerSplash.postDelayed({
            var context = application
            var sharedPref = context.getSharedPreferences(
                R.string.tokenPref.toString(),
                Context.MODE_PRIVATE
            )
            var token = sharedPref.getString(R.string.tokenValue.toString(), "")

            if (token == ""){
                val splashScreen = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(splashScreen)
            } else {
                val splashScreen = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(splashScreen)
            }
            finish()
        }, timeInMilliSecond)
    }

    companion object {
        const val timeInMilliSecond = 1800L
    }
}