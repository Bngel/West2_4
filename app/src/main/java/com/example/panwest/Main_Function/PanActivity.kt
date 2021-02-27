package com.example.panwest.Main_Function

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.panwest.Adapter.SpaceAdapter
import com.example.panwest.BaseActivity
import com.example.panwest.R
import com.example.panwest.Data.PanFile
import kotlinx.android.synthetic.main.activity_pan.*

class PanActivity : BaseActivity() {

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
    }
}