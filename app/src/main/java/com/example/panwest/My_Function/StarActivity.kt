package com.example.panwest.My_Function

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.panwest.Adapter.SpaceAdapter
import com.example.panwest.BaseActivity
import com.example.panwest.Data.PanFile
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_star.*

class StarActivity : BaseActivity() {

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
    }

    private fun flushList() {
        starList.addAll(test_infos)
        val adapter = SpaceAdapter(starList)
        star_fileList.adapter = adapter
        star_fileList.layoutManager = LinearLayoutManager(this)
    }
}