package com.example.myfilemanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup Tabs
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText("ЛОКАЛЬНО"))
        tabLayout.addTab(tabLayout.newTab().setText("БИБЛИОТЕКА"))
        tabLayout.addTab(tabLayout.newTab().setText("СЕТЬ"))

        // Handle Clicks
        val btnMainStorage = findViewById<LinearLayout>(R.id.btnMainStorage)
        btnMainStorage.setOnClickListener {
            if (checkPermission()) {
                val intent = Intent(this, BrowserActivity::class.java)
                startActivity(intent)
            } else {
                requestPermission()
            }
        }

        // Check permissions and update UI
        if (!checkPermission()) {
            requestPermission()
        } else {
            updateStorageInfo()
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission()) {
            updateStorageInfo()
        }
    }

    private fun updateStorageInfo() {
        try {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val total = stat.blockCountLong * stat.blockSizeLong
            val available = stat.availableBlocksLong * stat.blockSizeLong
            val used = total - available
            
            if (total == 0L) return

            val percent = (used.toFloat() / total.toFloat() * 100).toInt()
            
            val circle = findViewById<ProgressBar>(R.id.storageCircle)
            val text = findViewById<TextView>(R.id.percentText)
            
            circle.progress = percent
            text.text = "$percent%"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, 100)
            } catch (e: Exception) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, 100)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }
    }
}
