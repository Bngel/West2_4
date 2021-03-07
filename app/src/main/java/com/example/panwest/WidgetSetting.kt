package com.example.panwest

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.example.panwest.Adapter.SpaceAdapter
import com.example.panwest.Data.PanFile
import com.example.panwest.Main_Function.Pan_Function.PanRepository
import kotlinx.android.synthetic.main.activity_pan.*

object WidgetSetting {
    fun setFont(context: Context, view: TextView) {
        val mtypeface = Typeface.createFromAsset(context.assets,"font/HGHP_CNKI.TTF")
        view.setTypeface(mtypeface)
    }

    fun setFonts(context: Context, views: List<TextView>) {
        val mtypeface = Typeface.createFromAsset(context.assets,"font/HGHP_CNKI.TTF")
        for (view in views) {
            view.setTypeface(mtypeface)
        }
    }

}