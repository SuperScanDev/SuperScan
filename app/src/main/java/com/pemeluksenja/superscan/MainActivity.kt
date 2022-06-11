package com.pemeluksenja.superscan

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.android.material.navigation.NavigationView
import com.pemeluksenja.superscan.adapter.HistoryAdapter
import com.pemeluksenja.superscan.adapter.HistoryAdapterMain
import com.pemeluksenja.superscan.databinding.ActivityMainBinding
import com.pemeluksenja.superscan.viewmodel.HistoryViewModel
import com.pemeluksenja.superscan.viewmodelfactory.ViewModelFactory
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapterMain

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        //customToolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.customtoolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        //permission to use camera
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        bind.scanButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
        //get userName, email and avatar from sharedPref
        val context = application
        val sharedPref = context.getSharedPreferences(
            R.string.tokenPref.toString(),
            Context.MODE_PRIVATE
        )
        var userName = sharedPref.getString(R.string.userName.toString(), "")
        var userEmail = sharedPref.getString(getString(R.string.email), "")
        var userAvatar = sharedPref.getString(R.string.avatar.toString(), "")
        Log.d("AvatarMain", userAvatar.toString())

        //custom navigation drawer
        val drawer = findViewById<DrawerLayout>(R.id.menudrawer)
        val navMenu = findViewById<NavigationView>(R.id.menunavdrawer)
        val navHeader = navMenu.getHeaderView(0)

        //set pfp and welcome text
        val pfp = findViewById<CircleImageView>(R.id.pfpmain)
        bind.userNameMain.text = " ${userName}!"
        GlideToVectorYou.justLoadImage(this@MainActivity, Uri.parse(userAvatar), pfp)

        //set pfp, name and email in navigation header
        val pfpNav = navHeader.findViewById<CircleImageView>(R.id.userPfp)
        val userNameNav = navHeader.findViewById<TextView>(R.id.username)
        val userEmailNav = navHeader.findViewById<TextView>(R.id.email)

        GlideToVectorYou.justLoadImage(this@MainActivity, Uri.parse(userAvatar), pfpNav)
        userNameNav.text = userName
        userEmailNav.text = userEmail

        bind.pfpmain.setOnClickListener {
            drawer.openDrawer(Gravity.START)
        }
        navMenu.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.hub_kami -> hubKami()
                R.id.beriPenilaian-> startActivity(Intent(this, RateUsActivity::class.java))
                R.id.tentangKami -> startActivity(Intent(this, AboutUsActivity::class.java))
            }
            true
        }
        //navigate to history page
        bind.seeMore.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        historyAdapter = HistoryAdapterMain()
        historyViewModel = getViewModel(this@MainActivity)

        var userId = sharedPref.getString(R.string.userId.toString(), "")
        if (userId != null) {
            historyViewModel.getHistory(userId)
        }
        historyViewModel.getHistory().observe(this@MainActivity){item ->
            if(item != null){
                historyAdapter.setUserData(item)
                if (item.size == 0 ){
                    bind.historyCardEmpty.visibility = View.VISIBLE
                }else {
                    bind.historyCardEmpty.visibility = View.INVISIBLE
                }
            }
        }
        displayRecycle()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflaterMenu = menuInflater
        inflaterMenu.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_settings -> {
                val context = this
                val pref = context.getSharedPreferences(
                    R.string.tokenPref.toString(),
                    Context.MODE_PRIVATE
                )
                val editor = pref.edit()
                editor.remove(R.string.tokenValue.toString())
                editor.remove(getString(R.string.email))
                editor.remove(getString(R.string.password))
                editor.apply()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun hubKami(){
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data =
            Uri.parse("mailto:" + "superscandev@gmail.com")

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My email's subject")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "My email's body")

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayRecycle() {
        bind.historyRVMain.layoutManager = LinearLayoutManager(this)
        bind.historyRVMain.adapter = historyAdapter
    }

    private fun getViewModel(activity: AppCompatActivity): HistoryViewModel {
        val historyViewModelFactory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(
            activity,
            historyViewModelFactory
        ).get(HistoryViewModel::class.java)
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}