package com.pemeluksenja.superscan

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.pemeluksenja.superscan.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding

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
        //custom navigation drawer
        val drawer = findViewById<DrawerLayout>(R.id.menudrawer)
        val navMenu = findViewById<NavigationView>(R.id.menunavdrawer)
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

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}