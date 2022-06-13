package com.pemeluksenja.superscan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pemeluksenja.superscan.adapter.HistoryAdapter
import com.pemeluksenja.superscan.databinding.ActivityHistoryBinding
import com.pemeluksenja.superscan.model.History
import com.pemeluksenja.superscan.viewmodel.HistoryViewModel
import com.pemeluksenja.superscan.viewmodel.ProductDetailViewModel
import com.pemeluksenja.superscan.viewmodelfactory.ViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var bind: ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(bind.root)

        historyAdapter = HistoryAdapter()
        historyViewModel = getViewModel(this@HistoryActivity)
        var context = application
        var sharedPref = context.getSharedPreferences(
            R.string.tokenPref.toString(),
            Context.MODE_PRIVATE
        )
        var userId = sharedPref.getString(R.string.userId.toString(), "")
        if (userId != null) {
            historyViewModel.getHistory(userId)
        }
        historyViewModel.getHistory().observe(this@HistoryActivity){item ->
            if(item != null){
                historyAdapter.setUserData(item)
                if (item.size == 0 ){
                    bind.historyCardEmpty.visibility = View.VISIBLE
                }else {
                    bind.historyCardEmpty.visibility = View.INVISIBLE
                }
            }
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.customtoolbar)
        setSupportActionBar(toolbar)
        toolbar.setOnClickListener {
            startActivity(Intent(this@HistoryActivity, MainActivity::class.java))
            finish()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        displayRecycle()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun displayRecycle() {
        bind.historyRV.layoutManager = LinearLayoutManager(this)
        bind.historyRV.adapter = historyAdapter
    }

    private fun getViewModel(activity: AppCompatActivity): HistoryViewModel {
        val historyViewModelFactory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(
            activity,
            historyViewModelFactory
        ).get(HistoryViewModel::class.java)
    }
}