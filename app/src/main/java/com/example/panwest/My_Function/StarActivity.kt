package com.example.panwest.My_Function

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.panwest.Adapter.MyAdapter.StarAdapter
import com.example.panwest.BaseActivity
import com.example.panwest.Data.PanFile
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_star.*
import kotlinx.android.synthetic.main.activity_star.*
import kotlinx.android.synthetic.main.item_file.*

class StarActivity : BaseActivity() {
    private val MUSIC_STRING = "MUSIC"
    private val MOVIE_STRING = "MOVIE"
    private val PHOTO_STRING = "PHOTO"
    private val RAR_STRING = "RAR"
    private var editStatus = false
    private val EDIT_OPEN = true
    private val EDIT_CLOSE = false
    private var adapter : StarAdapter?  = null
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
        setContentView(R.layout.activity_star)
        flushList()
        initData()
        setClickEvent()
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
                displayItem.addAll(test_infos.filter { file ->
                    file.Type == PHOTO_STRING
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
                displayItem.addAll(test_infos.filter { file ->
                    file.Type == MOVIE_STRING
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
                displayItem.addAll(test_infos.filter { file ->
                    file.Type == MUSIC_STRING
                })
                adapter = StarAdapter(displayItem)
                star_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                star_bottom_edit.visibility = View.GONE
            }
            popbtn_rar.setOnClickListener {
                displayItem.clear()
                MyRepository.staredItem.clear()
                MyRepository.starCount.value = 0
                displayItem.addAll(test_infos.filter { file ->
                    file.Type == RAR_STRING
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
                editStatus = EDIT_OPEN
                star_bottom_edit.visibility = View.VISIBLE
                item_check.visibility = View.VISIBLE
                adapter?.setEditMode(EDIT_OPEN)
                star_fileList.adapter = adapter
            }
            else if (editStatus == EDIT_OPEN) {
                editStatus = EDIT_CLOSE
                star_bottom_edit.visibility = View.GONE
                item_check.visibility = View.GONE
                adapter?.setEditMode(EDIT_CLOSE)
                MyRepository.staredItem.clear()
                MyRepository.starCount.value = 0
                star_fileList.adapter = adapter
            }
        }
        star_edit_all.setOnClickListener {
            if (star_edit_all.text == "全选") {
                Log.d("TEXT_TTT", displayItem.size.toString())
                MyRepository.staredItemAddAll(displayItem)
                adapter?.notifyDataSetChanged()
            }
            else {
                MyRepository.staredItemRemoveAll(displayItem)
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun flushList() {
        starList.addAll(test_infos)
        val adapter = StarAdapter(starList)
        star_fileList.adapter = adapter
        star_fileList.layoutManager = LinearLayoutManager(this)
        MyRepository.starCount.observe(this, Observer { newCount ->
            star_edit_count.text = newCount.toString()
            if (newCount == adapter?.itemCount) {
                star_edit_all.text = "取消全选"
            }
            else {
                star_edit_all.text = "全选"
            }
        })
    }

    private fun initData() {
        displayItem.clear()
        MyRepository.staredItem.clear()
        MyRepository.starCount.value = 0
        displayItem.addAll(test_infos)
        adapter = StarAdapter(displayItem)
        star_fileList.adapter = adapter
        editStatus = EDIT_CLOSE
        star_bottom_edit.visibility = View.GONE
    }
}