package com.example.myfilemanager

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        val errorText = intent.getStringExtra("error") ?: "Неизвестная ошибка"
        
        val textView = findViewById<TextView>(R.id.errorTextView)
        val btnCopy = findViewById<Button>(R.id.btnCopyError)
        val btnRestart = findViewById<Button>(R.id.btnRestart)

        textView.text = errorText

        btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Crash Log", errorText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Ошибка скопирована!", Toast.LENGTH_SHORT).show()
        }

        btnRestart.setOnClickListener {
            // Перезапуск приложения (убить текущий процесс)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }
}
