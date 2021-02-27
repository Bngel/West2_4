package com.example.panwest.Main_Function

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.panwest.BaseActivity
import com.example.panwest.Main_Function.Setting_Function.AccountSettingActivity
import com.example.panwest.Main_Function.Setting_Function.ReplySettingActivity
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {
    private val ACCOUNT_ACTIVITY = 0X01
    private val REPLY_ACTIVITY = 0X02

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setClickEvent()
    }

    private fun setClickEvent() {
        setting_item_account.setOnClickListener {
            val accountIntent = Intent(this, AccountSettingActivity::class.java)
            startActivityForResult(accountIntent, ACCOUNT_ACTIVITY)
        }

        setting_item_reply.setOnClickListener {
            val replyIntent = Intent(this, ReplySettingActivity::class.java)
            startActivityForResult(replyIntent, REPLY_ACTIVITY)
        }
    }
}