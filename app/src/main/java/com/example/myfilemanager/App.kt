package com.example.myfilemanager

import android.app.Application
import android.content.Intent
import android.os.Process
import java.io.PrintWriter
import java.io.StringWriter

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Глобальный перехватчик ошибок
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            val sw = StringWriter()
            throwable.printStackTrace(PrintWriter(sw))
            val errorLog = sw.toString()

            val intent = Intent(this, ErrorActivity::class.java)
            intent.putExtra("error", errorLog)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

            Process.killProcess(Process.myPid())
            System.exit(1)
        }
    }
}
