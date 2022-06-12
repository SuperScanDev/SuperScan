package com.pemeluksenja.superscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pemeluksenja.superscan.databinding.ActivityPaymentDetailBinding
import com.pemeluksenja.superscan.model.History

class PaymentDetailActivity : AppCompatActivity() {
    private lateinit var bind: ActivityPaymentDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityPaymentDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val payment = intent.getParcelableExtra<History>(EXTRA_PAYMENT) as History
        bind.paymentCodeText.text = payment.paymentCode
        bind.harga.text = payment.totalBills
        bind.paymentDue.text = payment.orderAt
        bind.durationCode.text = payment.paymentCodeDuration


        supportActionBar?.title = "Detail Pembayaran"

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        const val EXTRA_PAYMENT = "extra_payment"
    }
}