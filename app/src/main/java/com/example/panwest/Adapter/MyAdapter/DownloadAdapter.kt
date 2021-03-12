package com.example.panwest.Adapter.MyAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.panwest.Data.FileData
import com.example.panwest.Data.getTypeFormat
import com.example.panwest.Data.photoFormat
import com.example.panwest.My_Function.MyRepository
import com.example.panwest.R
import kotlinx.android.synthetic.main.item_pop_pic.view.*
import java.io.File

class DownloadAdapter(private val files: List<FileData>): RecyclerView.Adapter<DownloadAdapter.ViewHolder>() {
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
                    MyRepository.downloadedItemAdd(files[viewHolder.adapterPosition])
                } else {
                    viewHolder.fileCheck.setImageResource(R.drawable.space_unchecked)
                    viewHolder.fileSelected = !viewHolder.fileSelected
                    MyRepository.downloadedItemRemove(files[viewHolder.adapterPosition])
                }
            } else {
                val popupView = LayoutInflater.from(parent.context).inflate(
                    R.layout.download_item_pop_click,
                    parent, false
                )
                val popupWindow = PopupWindow(
                    popupView, ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT, true
                )
                popupWindow.showAsDropDown(view, view.width, -view.height / 2)

                val popbtn_open = popupView.findViewById<Button>(R.id.download_click_popbutton_open)
                val popbtn_delete = popupView.findViewById<Button>(R.id.download_click_popbutton_delete)

                popbtn_open.setOnClickListener {
                    popupWindow.dismiss()
                    val pic = files[viewHolder.adapterPosition]
                    if (pic.type in photoFormat){
                        val picView = LayoutInflater.from(parent.context).inflate(
                            R.layout.item_pop_pic,
                            parent, false
                        )
                        val loading = File(pic.url)
                        Glide.with(parent.context)
                            .load(loading)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .placeholder(R.drawable.me_loading) // 占位图设置：加载过程中显示的图片
                            .error(R.drawable.me_error) // 异常占位图
                            .fitCenter()
                            .into(picView.pic_show_img)
                        val picWindow = PopupWindow(
                            picView, ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.MATCH_PARENT, true
                        )
                        picWindow.showAsDropDown(view, view.width/2-picView.width/2, view.height/2-picView.height/2)
                    }
                    else {
                        Toast.makeText(parent.context, "仅支持图片内容的打开", Toast.LENGTH_SHORT).show()
                    }
                }
                popbtn_delete.setOnClickListener {
                    popupWindow.dismiss()
                    val tf = File(files[viewHolder.adapterPosition].url)
                    if (tf.delete()) {
                        MyRepository.downloadListFlush.value = true
                        Toast.makeText(parent.context, "删除成功", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(parent.context, "删除失败", Toast.LENGTH_SHORT).show()
                    }
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
            holder.fileSelected = MyRepository.downloadedItemExists(file)
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