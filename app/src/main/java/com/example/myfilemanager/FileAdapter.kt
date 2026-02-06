package com.example.myfilemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileAdapter(
    private var files: List<File>,
    private val onClick: (File) -> Unit
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    class FileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iconImageView)
        val name: TextView = view.findViewById(R.id.fileNameTextView)
        val details: TextView = view.findViewById(R.id.fileDetailsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.name.text = file.name

        if (file.isDirectory) {
            holder.icon.setImageResource(R.drawable.ic_folder_cx)
            val count = file.list()?.size ?: 0
            holder.details.text = "$count объектов | " + formatDate(file.lastModified())
        } else {
            holder.icon.setImageResource(R.drawable.ic_file_cx)
            holder.details.text = formatSize(file.length()) + " | " + formatDate(file.lastModified())
        }

        holder.itemView.setOnClickListener { onClick(file) }
    }

    override fun getItemCount() = files.size

    fun updateList(newFiles: List<File>) {
        files = newFiles
        notifyDataSetChanged()
    }

    private fun formatSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return String.format("%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
    }

    private fun formatDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}
