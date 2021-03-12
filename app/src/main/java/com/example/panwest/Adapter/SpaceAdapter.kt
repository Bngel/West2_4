package com.example.panwest.Adapter

import android.content.DialogInterface
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.panwest.Data.FileData
import com.example.panwest.Data.getTypeFormat
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.Main_Function.Pan_Function.PanRepository
import com.example.panwest.My_Function.MyRepository
import com.example.panwest.R
import java.util.regex.Matcher
import java.util.regex.Pattern


class SpaceAdapter(private val files: List<FileData>): RecyclerView.Adapter<SpaceAdapter.ViewHolder>(){
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
            if (editMode) {
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
            else {
                if (getTypeFormat(files[viewHolder.adapterPosition].type) == "WJJ") {
                    val popupView = LayoutInflater.from(parent.context).inflate(
                        R.layout.item_pop_click_package,
                        parent, false
                    )
                    val popupWindow = PopupWindow(
                        popupView, ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT, true
                    )
                    popupWindow.showAsDropDown(view, view.width, -view.height / 2)

                    val popbtn_open = popupView.findViewById<Button>(R.id.click_popbutton_open_package)
                    val popbtn_delete = popupView.findViewById<Button>(R.id.click_popbutton_delete_package)


                    popbtn_open.setOnClickListener {
                        popupWindow.dismiss()
                        PanRepository.parent_dir.push(PanRepository.current_dir.value)
                        PanRepository.current_dir.value = files[viewHolder.adapterPosition].url
                    }
                    popbtn_delete.setOnClickListener {
                        popupWindow.dismiss()
                        PanRepository.deletePackage(
                            parent.context,
                            AccountRepository.user?.username ?: "",
                            files[viewHolder.adapterPosition].url
                        )
                    }

                }
                else {
                    val popupView = LayoutInflater.from(parent.context).inflate(
                        R.layout.item_pop_click,
                        parent, false
                    )
                    val popupWindow = PopupWindow(
                        popupView, ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT, true
                    )
                    popupWindow.showAsDropDown(view, view.width, -view.height / 2)

                    val popbtn_download = popupView.findViewById<Button>(R.id.click_popbutton_download)
                    val popbtn_delete = popupView.findViewById<Button>(R.id.click_popbutton_delete)
                    val popbtn_star = popupView.findViewById<Button>(R.id.click_popbutton_star)
                    val popbtn_rename = popupView.findViewById<Button>(R.id.click_popbutton_rename)

                    popbtn_download.setOnClickListener {
                        popupWindow.dismiss()
                        Log.d("TEXT_TTT",files[viewHolder.adapterPosition].url)
                        PanRepository.downloadFile(
                            parent.context,
                            AccountRepository.user?.username ?: "",
                            files[viewHolder.adapterPosition].url,
                            files[viewHolder.adapterPosition].filename
                        )
                    }
                    popbtn_delete.setOnClickListener {
                        popupWindow.dismiss()
                        PanRepository.deleteFile(
                            parent.context,
                            AccountRepository.user?.username ?: "",
                            files[viewHolder.adapterPosition].url
                        )
                    }
                    popbtn_star.setOnClickListener {
                        popupWindow.dismiss()
                        MyRepository.staredItemAdd(
                            parent.context,
                            files[viewHolder.adapterPosition]
                        )
                    }
                    popbtn_rename.setOnClickListener {
                        popupWindow.dismiss()
                        val editText = EditText(parent.context)
                        val filter =
                            InputFilter { source, start, end, dest, dstart, dend ->
                                val speChat =
                                    "[`~!@#$%^&*()+=|{}':;',\\[\\].<>~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
                                val pattern: Pattern = Pattern.compile(speChat)
                                val matcher: Matcher = pattern.matcher(source.toString())
                                if (matcher.find()) "" else null
                            }
                        editText.filters = arrayOf(filter)
                        val inputDialog = AlertDialog.Builder(parent.context)
                            .setTitle("请输入文件名称(不得为空)")
                            .setView(editText)
                        inputDialog.setPositiveButton("确定",
                            DialogInterface.OnClickListener { _, _ ->
                                val file_name = editText.text.toString()
                                if (file_name != "") {
                                    PanRepository.changeFilename(
                                        parent.context,
                                        AccountRepository.user?.username ?: "",
                                        file_name + "." + files[viewHolder.adapterPosition].type,
                                        files[viewHolder.adapterPosition].url
                                    )
                                } else {
                                    Toast.makeText(parent.context, "文件名称不得为空", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }).show()
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
            when (getTypeFormat(file.type)) {
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