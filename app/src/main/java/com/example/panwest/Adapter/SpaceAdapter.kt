package com.example.panwest.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.panwest.R
import com.example.panwest.Data.PanFile
import com.example.panwest.Main_Function.Pan_Function.PanRepository

class SpaceAdapter(private val files: List<PanFile>): RecyclerView.Adapter<SpaceAdapter.ViewHolder>(){
    private var editMode = false

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val fileImg : ImageView = view.findViewById(R.id.item_img)
        val fileName : TextView = view.findViewById(R.id.item_name)
        val fileCheck : ImageView = view.findViewById(R.id.item_check)
        var fileSelected = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.setIsRecyclable(false)
        viewHolder.itemView.setOnClickListener {
            if (!viewHolder.fileSelected) {
                viewHolder.fileCheck.setImageResource(R.drawable.space_checked)
                viewHolder.fileSelected = !viewHolder.fileSelected
                PanRepository.selectedItemAdd(files[viewHolder.adapterPosition])

            }
            else {
                viewHolder.fileCheck.setImageResource(R.drawable.space_unchecked)
                viewHolder.fileSelected = !viewHolder.fileSelected
                PanRepository.selectedItemRemove(files[viewHolder.adapterPosition])
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]
        holder.fileImg.setImageResource(file.TypeImg)
        holder.fileName.text = file.fileName
        if (editMode) {
            holder.fileCheck.visibility = View.VISIBLE
            holder.fileSelected = PanRepository.selectedItemExists(file)
            if (holder.fileSelected) {
                holder.fileCheck.setImageResource(R.drawable.space_checked)
            }
            else {
                holder.fileCheck.setImageResource(R.drawable.space_unchecked)
            }
        }
        else {
            holder.fileCheck.visibility = View.GONE
        }

    }

    override fun getItemCount() = files.size

    fun setEditMode(editmode: Boolean) {
        editMode = editmode
        notifyDataSetChanged()
    }
}