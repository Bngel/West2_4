package com.example.panwest.Main_Function.Setting_Function

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.panwest.BaseActivity
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_reply_setting.*

class ReplySettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply_setting)
        setClickEvent()
    }

    private fun setClickEvent() {
        reply_setting_enter_btn.setOnClickListener {
            val text = reply_setting_edit.text.toString()
            if (text != "") {
                SettingRepository.getFeedback(this, AccountRepository.user?.username?:"", text)
            }
            else {
                Toast.makeText(this, "反馈内容不得为空", Toast.LENGTH_SHORT).show()
            }
        }
    }
}