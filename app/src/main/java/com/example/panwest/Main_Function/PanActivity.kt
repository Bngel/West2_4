package com.example.panwest.Main_Function

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.panwest.Adapter.SpaceAdapter
import com.example.panwest.BaseActivity
import com.example.panwest.R
import com.example.panwest.Data.PanFile
import com.example.panwest.Main_Function.Pan_Function.PanRepository
import kotlinx.android.synthetic.main.activity_pan.*
import kotlinx.android.synthetic.main.item_file.*

class PanActivity : BaseActivity() {
    private val MUSIC_STRING = "MUSIC"
    private val MOVIE_STRING = "MOVIE"
    private val PHOTO_STRING = "PHOTO"
    private val FILE_STRING = "FILE"
    private val RAR_STRING = "RAR"
    private val FILE_CHOOSE = 0X11
    private val STRING_CHOOSE = "请选择上传的文件"
    private var editStatus = false
    private val EDIT_OPEN = true
    private val EDIT_CLOSE = false

    private var adapter :SpaceAdapter?  = null
    private val displayItem = ArrayList<PanFile>()

    val test_infos = listOf(
        PanFile("PHOTO", R.drawable.type_photo, "img1.png", "testUrl"),
        PanFile("PHOTO", R.drawable.type_photo, "img2.png", "testUrl"),
        PanFile("PHOTO", R.drawable.type_photo, "img3.png", "testUrl"),
        PanFile("PHOTO", R.drawable.type_photo, "img4.png", "testUrl"),
        PanFile("MUSIC", R.drawable.type_music, "music1.mp3", "testUrl"),
        PanFile("MUSIC", R.drawable.type_music, "music2.mp3", "testUrl"),
        PanFile("MUSIC", R.drawable.type_music, "music3.mp3", "testUrl"),
        PanFile("MOVIE", R.drawable.type_movie, "movie1.mp4", "testUrl"),
        PanFile("RAR", R.drawable.type_rar, "rar1.zip", "testUrl"),
        PanFile("FILE", R.drawable.type_file, "sb.ppp", "testUrl")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pan)
        adapter = SpaceAdapter(test_infos)
        space_fileList.adapter = adapter
        space_fileList.layoutManager = LinearLayoutManager(this)
        PanRepository.selectedCount.observe(this, Observer { newCount ->
            space_edit_count.text = newCount.toString()
            if (newCount == adapter?.itemCount) {
                space_edit_all.text = "取消全选"
            }
            else {
                space_edit_all.text = "全选"
            }
        })
        setClickEvent()
    }

    @SuppressLint("InflateParams")
    private fun setClickEvent() {
        space_filtrate.setOnClickListener {
            val popupView = layoutInflater.inflate(R.layout.item_pop_filtrate,
                null,false)
            val popupWindow = PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true)
            popupWindow.showAsDropDown(space_filtrate,0,35)

            val popbtn_photo = popupView.findViewById<Button>(R.id.filtrate_popbutton_photo)
            val popbtn_movie = popupView.findViewById<Button>(R.id.filtrate_popbutton_movie)
            val popbtn_music = popupView.findViewById<Button>(R.id.filtrate_popbutton_music)
            val popbtn_rar = popupView.findViewById<Button>(R.id.filtrate_popbutton_rar)
            val popbtn_file = popupView.findViewById<Button>(R.id.filtrate_popbutton_file)

            popbtn_photo.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                displayItem.addAll(test_infos.filter { file ->
                    file.Type == PHOTO_STRING
                })
                adapter = SpaceAdapter(displayItem)
                space_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE
            }
            popbtn_movie.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                displayItem.addAll(test_infos.filter { file ->
                    file.Type == MOVIE_STRING
                })
                adapter = SpaceAdapter(displayItem)
                space_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE

            }
            popbtn_music.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                displayItem.addAll(test_infos.filter { file ->
                    file.Type == MUSIC_STRING
                })
                adapter = SpaceAdapter(displayItem)
                space_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE
            }
            popbtn_rar.setOnClickListener {
                displayItem.clear()
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                displayItem.addAll(test_infos.filter { file ->
                    file.Type == RAR_STRING
                })
                adapter = SpaceAdapter(displayItem)
                space_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE
            }
            popbtn_file.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                displayItem.addAll(test_infos)
                adapter = SpaceAdapter(displayItem)
                space_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE
            }
        }
        space_add.setOnClickListener {
            val popupView = layoutInflater.inflate(R.layout.item_pop_add,
                null,false)
            val popupWindow = PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true)
            popupWindow.showAsDropDown(space_add,0,35)

            val popbtn_upload = popupView.findViewById<Button>(R.id.add_popbutton_upload)
            val popbtn_dir = popupView.findViewById<Button>(R.id.add_popbutton_dir)

            popbtn_upload.setOnClickListener {
                popupWindow.dismiss()
                val fileIntent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(fileIntent, STRING_CHOOSE), FILE_CHOOSE)
            }
            popbtn_dir.setOnClickListener {
                popupWindow.dismiss()
            }
        }
        space_edit.setOnClickListener {
            if (editStatus == EDIT_CLOSE) {
                editStatus = EDIT_OPEN
                space_bottom_edit.visibility = View.VISIBLE
                item_check.visibility = View.VISIBLE
                adapter?.setEditMode(EDIT_OPEN)
                space_fileList.adapter = adapter
            }
            else if (editStatus == EDIT_OPEN) {
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE
                item_check.visibility = View.GONE
                adapter?.setEditMode(EDIT_CLOSE)
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                space_fileList.adapter = adapter
            }
        }
        space_edit_all.setOnClickListener {
            if (space_edit_all.text == "全选") {
                PanRepository.selectedItemAddAll(displayItem)
                adapter?.notifyDataSetChanged()
            }
            else {
                PanRepository.selectedItemRemoveAll(displayItem)
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                FILE_CHOOSE -> {
                    val url = data?.data // 待上传的文件链接
                }
            }
        }
    }
}