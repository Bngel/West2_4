package com.example.panwest.Adapter.MyAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
import com.example.panwest.Data.FileData
import com.example.panwest.Data.getTypeFormat
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.Main_Function.Pan_Function.PanRepository
import com.example.panwest.My_Function.MyRepository
import com.example.panwest.My_Function.MyRepository.starItemRemove
import com.example.panwest.My_Function.MyRepository.starItemSelect
import com.example.panwest.My_Function.MyRepository.staredItemAdd
import com.example.panwest.R

class StarAdapter(private val files: ArrayList<FileData>): RecyclerView.Adapter<StarAdapter.ViewHolder>() {
    private var editMode = false

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileImg: ImageView = view.findViewById(R.id.item_img)
        val fileName: TextView = view.findViewById(R.id.item_name)
        val fileCheck: ImageView = view.findViewById(R.id.item_check)
        var fileSelected = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.setIsRecyclable(false)
        viewHolder.itemView.setOnClickListener {
            if (editMode) {
                if (!viewHolder.fileSelected) {
                    viewHolder.fileCheck.setImageResource(R.drawable.space_checked)
                    viewHolder.fileSelected = !viewHolder.fileSelected
                    starItemSelect(files[viewHolder.adapterPosition])
                } else {
                    viewHolder.fileCheck.setImageResource(R.drawable.space_unchecked)
                    viewHolder.fileSelected = !viewHolder.fileSelected
                    starItemRemove(files[viewHolder.adapterPosition])
                }
            } else {
                val popupView = LayoutInflater.from(parent.context).inflate(
                    R.layout.star_item_pop_click,
                    parent, false
                )
                val popupWindow = PopupWindow(
                    popupView, ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT, true
                )
                popupWindow.showAsDropDown(view, view.width, -view.height / 2)

                val popbtn_download = popupView.findViewById<Button>(R.id.star_click_popbutton_download)
                val popbtn_delete = popupView.findViewById<Button>(R.id.star_click_popbutton_delete)

                popbtn_download.setOnClickListener {
                    popupWindow.dismiss()
                    PanRepository.downloadFile(parent.context,
                        AccountRepository.user?.username?:"",
                        files[viewHolder.adapterPosition].url,
                        files[viewHolder.adapterPosition].filename)
                }
                popbtn_delete.setOnClickListener {
                    popupWindow.dismiss()
                    MyRepository.staredItemDelete(parent.context, files[viewHolder.adapterPosition])
                }
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]
        val MUSIC_STRING = "MUSIC"
        val MOVIE_STRING = "MOVIE"
        val PHOTO_STRING = "PHOTO"
        val FILE_STRING = "FILE"
        val RAR_STRING = "RAR"
        val PACKAGE_STRING = "WJJ"
        holder.fileImg.setImageResource(
            when(getTypeFormat(file.type)) {
                MUSIC_STRING -> R.drawable.type_music
                MOVIE_STRING -> R.drawable.type_movie
                PHOTO_STRING -> R.drawable.type_photo
                RAR_STRING -> R.drawable.type_rar
                PACKAGE_STRING -> R.drawable.type_dir
                else -> R.drawable.type_file
            }
        )
        holder.fileName.text = file.filename
        if (editMode) {
            holder.fileCheck.visibility = View.VISIBLE
            holder.fileSelected = file in MyRepository.staredItem
            if (holder.fileSelected) {
                holder.fileCheck.setImageResource(R.drawable.space_checked)
            } else {
                holder.fileCheck.setImageResource(R.drawable.space_unchecked)
            }
        } else {
            holder.fileCheck.visibility = View.GONE
        }

    }

    override fun getItemCount() = files.size

    fun setEditMode(editmode: Boolean) {
        editMode = editmode
        notifyDataSetChanged()
    }
}