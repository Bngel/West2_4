package com.example.panwest.Main_Function

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.PopupWindow
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.panwest.Adapter.SpaceAdapter
import com.example.panwest.BaseActivity
import com.example.panwest.R
import com.example.panwest.Data.PanFile
import kotlinx.android.synthetic.main.activity_pan.*
import kotlinx.android.synthetic.main.item_pop_filtrate.*

class PanActivity : BaseActivity() {
    private val MUSIC_STRING = "MUSIC"
    private val MOVIE_STRING = "MOVIE"
    private val PHOTO_STRING = "PHOTO"
    private val FILE_STRING = "FILE"
    private val RAR_STRING = "RAR"

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

        val adapter = SpaceAdapter(test_infos)
        space_fileList.adapter = adapter
        space_fileList.layoutManager = LinearLayoutManager(this)

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
                val adapter = SpaceAdapter(test_infos.filter { file ->
                    file.Type == PHOTO_STRING
                })
                space_fileList.adapter = adapter
            }
            popbtn_movie.setOnClickListener {
                popupWindow.dismiss()
                val adapter = SpaceAdapter(test_infos.filter { file ->
                    file.Type == MOVIE_STRING
                })
                space_fileList.adapter = adapter
            }
            popbtn_music.setOnClickListener {
                popupWindow.dismiss()
                val adapter = SpaceAdapter(test_infos.filter { file ->
                    file.Type == MUSIC_STRING
                })
                space_fileList.adapter = adapter
            }
            popbtn_rar.setOnClickListener {
                popupWindow.dismiss()
                val adapter = SpaceAdapter(test_infos.filter { file ->
                    file.Type == RAR_STRING
                })
                space_fileList.adapter = adapter
            }
            popbtn_file.setOnClickListener {
                popupWindow.dismiss()
                val adapter = SpaceAdapter(test_infos)
                space_fileList.adapter = adapter
            }
        }
    }
}