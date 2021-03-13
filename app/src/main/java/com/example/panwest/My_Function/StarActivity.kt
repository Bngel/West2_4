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
import com.example.panwest.Adapter.MyAdapter.StarAdapter
import com.example.panwest.BaseActivity
import com.example.panwest.Data.FileData
import com.example.panwest.Data.getTypeFormat
import com.example.panwest.Database.Database.AppDatabase
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.Main_Function.Pan_Function.PanRepository
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.activity_star.*
import kotlinx.android.synthetic.main.item_file.*
import kotlinx.android.synthetic.main.view_search_space.*
import java.io.File
import kotlin.concurrent.thread

class StarActivity : BaseActivity() {
    private val MUSIC_STRING = "MUSIC"
    private val MOVIE_STRING = "MOVIE"
    private val PHOTO_STRING = "PHOTO"
    private val RAR_STRING = "RAR"
    private var editStatus = false
    private val EDIT_OPEN = true
    private val EDIT_CLOSE = false
    private var adapter : StarAdapter?  = null
    private val starFiles = ArrayList<FileData>()
    private val displayItem = ArrayList<FileData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star)
        initData()
        star_fileList.layoutManager = LinearLayoutManager(this)
        star_fileList.adapter = adapter
        setClickEvent()
        MyRepository.starListFlush.observe(this, Observer { flush ->
            if (flush) {
                initData()
                MyRepository.starListFlush.value = false
            }
        })
        MyRepository.starCount.observe(this, Observer { newCount ->
            star_edit_count.text = newCount.toString()
            if (newCount == adapter?.itemCount && newCount != 0) {
                star_edit_all.text = "取消全选"
            } else {
                star_edit_all.text = "全选"
            }
        })
    }

    private fun setClickEvent() {
        star_filtrate.setOnClickListener {
            val popupView = layoutInflater.inflate(
                R.layout.item_pop_filtrate,
                null, false
            )
            val popupWindow = PopupWindow(
                popupView, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true
            )
            popupWindow.showAsDropDown(star_filtrate, 0, 35)

            val popbtn_photo = popupView.findViewById<Button>(R.id.filtrate_popbutton_photo)
            val popbtn_movie = popupView.findViewById<Button>(R.id.filtrate_popbutton_movie)
            val popbtn_music = popupView.findViewById<Button>(R.id.filtrate_popbutton_music)
            val popbtn_rar = popupView.findViewById<Button>(R.id.filtrate_popbutton_rar)
            val popbtn_file = popupView.findViewById<Button>(R.id.filtrate_popbutton_file)

            popbtn_photo.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                MyRepository.staredItem.clear()
                MyRepository.starCount.value = 0
                displayItem.addAll(starFiles.filter { file ->
                    getTypeFormat(file.type) == PHOTO_STRING
                })
                adapter = StarAdapter(displayItem)
                star_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                star_bottom_edit.visibility = View.GONE
            }
            popbtn_movie.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                MyRepository.staredItem.clear()
                MyRepository.starCount.value = 0
                displayItem.addAll(starFiles.filter { file ->
                    getTypeFormat(file.type) == MOVIE_STRING
                })
                adapter = StarAdapter(displayItem)
                star_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                star_bottom_edit.visibility = View.GONE

            }
            popbtn_music.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                MyRepository.staredItem.clear()
                MyRepository.starCount.value = 0
                displayItem.addAll(starFiles.filter { file ->
                    getTypeFormat(file.type) == MUSIC_STRING
                })
                adapter = StarAdapter(displayItem)
                star_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                star_bottom_edit.visibility = View.GONE
            }
            popbtn_rar.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                MyRepository.staredItem.clear()
                MyRepository.starCount.value = 0
                displayItem.addAll(starFiles.filter { file ->
                    getTypeFormat(file.type) == RAR_STRING
                })
                adapter = StarAdapter(displayItem)
                star_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                star_bottom_edit.visibility = View.GONE
            }
            popbtn_file.setOnClickListener {
                popupWindow.dismiss()
                initData()
            }
        }
        star_edit.setOnClickListener {
            if (editStatus == EDIT_CLOSE) {
                Log.d("TEXT_TTT", MyRepository.starCount.value.toString())
                editStatus = EDIT_OPEN
                star_bottom_edit.visibility = View.VISIBLE
                if (item_check != null)
                    item_check.visibility = View.VISIBLE
                adapter?.setEditMode(EDIT_OPEN)
                star_fileList.adapter = adapter
            }
            else if (editStatus == EDIT_OPEN) {
                editStatus = EDIT_CLOSE
                star_bottom_edit.visibility = View.GONE
                if (item_check != null)
                    item_check.visibility = View.GONE
                adapter?.setEditMode(EDIT_CLOSE)
                MyRepository.staredItem.clear()
                MyRepository.starCount.value = 0
                star_fileList.adapter = adapter
            }
        }
        star_edit_all.setOnClickListener {
            if (star_edit_all.text == "全选") {
                MyRepository.starItemSelectAll(displayItem)
                adapter?.notifyDataSetChanged()
            }
            else {
                MyRepository.starItemRemoveAll(displayItem)
                adapter?.notifyDataSetChanged()
            }
        }
        space_search_img.setOnClickListener {
            val regex = Regex(space_search_edit.text.toString())
            displayItem.clear()
            MyRepository.staredItem.clear()
            MyRepository.starCount.value = 0
            displayItem.addAll(starFiles.filter { file ->
                regex.containsMatchIn(file.filename)
            })
            adapter = StarAdapter(displayItem)
            star_fileList.adapter = adapter
            editStatus = EDIT_CLOSE
            star_bottom_edit.visibility = View.GONE
        }
        star_edit_delete.setOnClickListener {
            Log.d("TEXT_STAR", MyRepository.staredItem.toString())
            MyRepository.staredItemDeleteAll(this, MyRepository.staredItem)
        }
        star_edit_download.setOnClickListener {
            for (file in MyRepository.staredItem)
                if (file.type != "wjj")
                    PanRepository.downloadFile(this,
                        AccountRepository.user?.username?:"",
                        file.url,
                        file.filename)
            MyRepository.starListFlush.value = true
        }
    }

    private fun initData() {
        displayItem.clear()
        starFiles.clear()
        MyRepository.staredItem.clear()
        MyRepository.starCount.value = 0
        loadFileInformation()
        displayItem.addAll(starFiles)
        adapter = StarAdapter(displayItem)
        star_fileList.adapter = adapter
        editStatus = EDIT_CLOSE
        star_bottom_edit.visibility = View.GONE
    }

    private fun loadFileInformation(){
        val star_list = MyRepository.getStarItems(this)
        Log.d("star_size", star_list.size.toString())
        starFiles.addAll(star_list)
    }
}