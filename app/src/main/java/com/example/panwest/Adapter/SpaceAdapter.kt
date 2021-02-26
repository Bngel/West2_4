package com.example.panwest.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.panwest.R
import com.example.panwest.Data.PanFile

class SpaceAdapter(private val files: List<PanFile>): RecyclerView.Adapter<SpaceAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val fileImg : ImageView = view.findViewById(R.id.item_img)
        val fileName : TextView = view.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]
        holder.fileImg.setImageResource(file.TypeImg)
        holder.fileName.text = file.fileName
    }

    override fun getItemCount() = files.size
}