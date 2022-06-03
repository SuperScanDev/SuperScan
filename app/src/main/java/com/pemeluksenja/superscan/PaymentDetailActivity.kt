package com.pemeluksenja.superscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PaymentDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_detail)
        supportActionBar?.title = "Detail Pembayaran"

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}