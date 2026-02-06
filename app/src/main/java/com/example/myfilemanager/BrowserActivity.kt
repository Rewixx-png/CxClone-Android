package com.example.myfilemanager

import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import android.view.MenuItem

class BrowserActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pathView: TextView
    private lateinit var adapter: FileAdapter
    private var currentPath: File = Environment.getExternalStorageDirectory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        // Add Back button in Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Основная память"

        recyclerView = findViewById(R.id.recyclerView)
        pathView = findViewById(R.id.currentPathView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = FileAdapter(emptyList()) { file ->
            if (file.isDirectory) {
                loadFiles(file)
            } else {
                Toast.makeText(this, "File: ${file.name}", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView.adapter = adapter

        loadFiles(currentPath)
    }

    private fun loadFiles(directory: File) {
        currentPath = directory
        pathView.text = directory.absolutePath
        val files = directory.listFiles()
        if (files != null) {
            val sortedFiles = files.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
            adapter.updateList(sortedFiles)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (currentPath.absolutePath != Environment.getExternalStorageDirectory().absolutePath) {
            loadFiles(currentPath.parentFile ?: Environment.getExternalStorageDirectory())
        } else {
            super.onBackPressed()
        }
    }
}
