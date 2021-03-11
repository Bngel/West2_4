package com.example.panwest.My_Function

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.panwest.Adapter.MyAdapter.DownloadAdapter
import com.example.panwest.Data.getTypeFormat
import java.io.File
import com.example.panwest.BaseActivity
import com.example.panwest.Data.FileData
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.Main_Function.Pan_Function.PanRepository
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.item_file.*
import kotlinx.android.synthetic.main.view_search_space.*

class DownloadActivity : BaseActivity() {
    private val MUSIC_STRING = "MUSIC"
    private val MOVIE_STRING = "MOVIE"
    private val PHOTO_STRING = "PHOTO"
    private val RAR_STRING = "RAR"
    private var editStatus = false
    private val EDIT_OPEN = true
    private val EDIT_CLOSE = false
    private var adapter :DownloadAdapter?  = null
    private val downloadFiles = ArrayList<FileData>()
    private val displayItem = ArrayList<FileData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        initData()
        download_fileList.layoutManager = LinearLayoutManager(this)
        download_fileList.adapter = adapter
        setClickEvent()
        MyRepository.downloadListFlush.observe(this, Observer { flush ->
            if (flush) {
                initData()
                MyRepository.downloadListFlush.value = false
            }
        })
    }

    private fun setClickEvent() {
        download_filtrate.setOnClickListener {
            val popupView = layoutInflater.inflate(
                R.layout.item_pop_filtrate,
                null, false
            )
            val popupWindow = PopupWindow(
                popupView, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true
            )
            popupWindow.showAsDropDown(download_filtrate, 0, 35)

            val popbtn_photo = popupView.findViewById<Button>(R.id.filtrate_popbutton_photo)
            val popbtn_movie = popupView.findViewById<Button>(R.id.filtrate_popbutton_movie)
            val popbtn_music = popupView.findViewById<Button>(R.id.filtrate_popbutton_music)
            val popbtn_rar = popupView.findViewById<Button>(R.id.filtrate_popbutton_rar)
            val popbtn_file = popupView.findViewById<Button>(R.id.filtrate_popbutton_file)

            popbtn_photo.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                MyRepository.downloadedItem.clear()
                MyRepository.downloadCount.value = 0
                displayItem.addAll(downloadFiles.filter { file ->
                    getTypeFormat(file.type) == PHOTO_STRING
                })
                adapter = DownloadAdapter(displayItem)
                download_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                download_bottom_edit.visibility = View.GONE
            }
            popbtn_movie.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                MyRepository.downloadedItem.clear()
                MyRepository.downloadCount.value = 0
                displayItem.addAll(downloadFiles.filter { file ->
                    getTypeFormat(file.type) == MOVIE_STRING
                })
                adapter = DownloadAdapter(displayItem)
                download_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                download_bottom_edit.visibility = View.GONE

            }
            popbtn_music.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                MyRepository.downloadedItem.clear()
                MyRepository.downloadCount.value = 0
                displayItem.addAll(downloadFiles.filter { file ->
                    getTypeFormat(file.type) == MUSIC_STRING
                })
                adapter = DownloadAdapter(displayItem)
                download_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                download_bottom_edit.visibility = View.GONE
            }
            popbtn_rar.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                MyRepository.downloadedItem.clear()
                MyRepository.downloadCount.value = 0
                displayItem.addAll(downloadFiles.filter { file ->
                    getTypeFormat(file.type) == RAR_STRING
                })
                adapter = DownloadAdapter(displayItem)
                download_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                download_bottom_edit.visibility = View.GONE
            }
            popbtn_file.setOnClickListener {
                popupWindow.dismiss()
                initData()
            }
        }
        download_edit.setOnClickListener {
            if (editStatus == EDIT_CLOSE) {
                Log.d("TEXT_TTT", MyRepository.downloadCount.value.toString())
                editStatus = EDIT_OPEN
                download_bottom_edit.visibility = View.VISIBLE
                if (item_check != null)
                    item_check.visibility = View.VISIBLE
                adapter?.setEditMode(EDIT_OPEN)
                download_fileList.adapter = adapter
            }
            else if (editStatus == EDIT_OPEN) {
                editStatus = EDIT_CLOSE
                download_bottom_edit.visibility = View.GONE
                if (item_check != null)
                    item_check.visibility = View.GONE
                adapter?.setEditMode(EDIT_CLOSE)
                MyRepository.downloadedItem.clear()
                MyRepository.downloadCount.value = 0
                download_fileList.adapter = adapter
            }
        }
        download_edit_all.setOnClickListener {
            if (download_edit_all.text == "全选") {
                MyRepository.downloadedItemAddAll(displayItem)
                adapter?.notifyDataSetChanged()
            }
            else {
                MyRepository.downloadedItemRemoveAll(displayItem)
                adapter?.notifyDataSetChanged()
            }
        }
        space_search_img.setOnClickListener {
            val regex = Regex(space_search_edit.text.toString())
            displayItem.clear()
            MyRepository.downloadedItem.clear()
            MyRepository.downloadCount.value = 0
            displayItem.addAll(downloadFiles.filter { file ->
                regex.containsMatchIn(file.filename)
            })
            adapter = DownloadAdapter(displayItem)
            download_fileList.adapter = adapter
            editStatus = EDIT_CLOSE
            download_bottom_edit.visibility = View.GONE
        }
        download_edit_delete.setOnClickListener {
            for (file in MyRepository.downloadedItem)
                if (file.type != "wjj") {
                    val tf = File(file.url)
                    if (tf.delete()) {
                        MyRepository.downloadListFlush.value = true
                        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initData() {
        displayItem.clear()
        downloadFiles.clear()
        MyRepository.downloadedItem.clear()
        MyRepository.downloadCount.value = 0
        loadFileInformation()
        displayItem.addAll(downloadFiles)
        adapter = DownloadAdapter(displayItem)
        download_fileList.adapter = adapter
        editStatus = EDIT_CLOSE
        download_bottom_edit.visibility = View.GONE
    }

    private fun loadFileInformation(){
        val defaultDir = File(PanRepository.DOWNLOAD_PATH)
        val files = defaultDir.listFiles()
        for (file in files) {
            val fileData = FileData(file.path, AccountRepository.user?.username?:"", file.name,
                PanRepository.DOWNLOAD_PATH!!, getFileType(file.name), file.length().toDouble()/1024/1024, "")
            downloadFiles.add(fileData)
        }
    }

    private fun getFileType(filename: String): String {
        val spt = filename.split('.')
        return if (spt.size < 2) {
            "wjj"
        } else {
            spt[1]
        }
    }
 }