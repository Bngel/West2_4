package com.example.panwest.UI.Widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.panwest.R
import com.example.panwest.WidgetSetting
import kotlinx.android.synthetic.main.view_homebtn_main.view.*

class homeButtonView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_homebtn_main,this)
        val array = context.obtainStyledAttributes(attrs, R.styleable.HomeBtnView)
        homeBtn.text = array.getString(R.styleable.HomeBtnView_btn_text)
        setFonts()
    }

    private fun setFonts() {
        WidgetSetting.setFonts(context, listOf(homeBtn))
    }

}