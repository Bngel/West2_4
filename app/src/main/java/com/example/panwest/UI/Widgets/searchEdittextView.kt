package com.example.panwest.UI.Widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.panwest.R
import com.example.panwest.WidgetSetting
import kotlinx.android.synthetic.main.view_search_space.view.*

class searchEdittextView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_search_space,this)
        setFonts()
    }

    private fun setFonts() {
        WidgetSetting.setFonts(context, listOf(space_search_edit))
    }

}