package com.pemeluksenja.superscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pemeluksenja.superscan.databinding.ActivityPaymentDetailBinding
import com.pemeluksenja.superscan.model.History

class PaymentDetailActivity : AppCompatActivity() {
    private lateinit var bind: ActivityPaymentDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityPaymentDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

//        val payment = intent?.getParcelableExtra<History>(EXTRA_PAYMENT) as History
        val code = intent.getStringExtra(EXTRA_CODE)
        val bills = intent.getStringExtra(EXTRA_BILLS)
        Log.d("Bills: ", bills.toString())
        val due = intent.getStringExtra(EXTRA_DUE)
        val duration = intent.getStringExtra(EXTRA_DURATION)

        if (code != null) {
            bind.paymentCodeText.text = code.chunked(10)[0]
        }
        val order = due?.substringAfter("2022")
        val sliceOrder = order?.let { due?.split(it) }
        bind.harga.text = bills.toString()
        bind.paymentDue.text = sliceOrder?.get(0) ?: "NaN"
        bind.durationCode.text = duration


        supportActionBar?.title = "Detail Pembayaran"

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        const val EXTRA_PAYMENT = "extra_payment"
        const val EXTRA_CODE = "extra_code"
        const val EXTRA_BILLS = "extra_bills"
        const val EXTRA_DUE = "extra_due"
        const val EXTRA_DURATION = "extra_duration"
    }
}