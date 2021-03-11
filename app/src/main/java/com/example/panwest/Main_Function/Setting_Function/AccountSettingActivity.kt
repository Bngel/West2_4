package com.example.panwest.Main_Function.Setting_Function

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.panwest.ActivityCollector
import com.example.panwest.ActivityCollector.finishAll
import com.example.panwest.ActivityCollector.onlyActivity
import com.example.panwest.BaseActivity
import com.example.panwest.Login_Function.LoginActivity
import com.example.panwest.MainActivity
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_account_setting.*

class AccountSettingActivity : BaseActivity() {
    private val LOGIN_STATE = "login_state"
    private val SWITCH_ACTIVITY = 0X01
    private val LOGOUT_ACTIVITY = 0X02
    private val PASSWORD_ACTIVITY = 0X03

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)

        setClickEvent()
    }

    @SuppressLint("CommitPrefEdits")
    private fun setClickEvent() {
        account_setting_item_switch.setOnClickListener {
            finish()
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivityForResult(loginIntent, SWITCH_ACTIVITY)
        }

        account_setting_item_logout.setOnClickListener {
            onlyActivity(this)
            val userInfo: SharedPreferences.Editor = getSharedPreferences(LOGIN_STATE, Context.MODE_PRIVATE).edit()
            userInfo.apply {
                putBoolean("STATE", false)
                putString("ID", "")
                putString("PSWD", "")
                apply()
            }
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivityForResult(loginIntent, LOGOUT_ACTIVITY)
        }

        account_setting_item_change_password.setOnClickListener {
            finish()
            val passwordIntent = Intent(this, PasswordChangeActivity::class.java)
            startActivityForResult(passwordIntent, PASSWORD_ACTIVITY)
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SWITCH_ACTIVITY -> if (resultCode == Activity.RESULT_CANCELED) {
                //finish()
            } else {
                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.putExtra("switch_success", true)
                startActivity(mainIntent)
            }
            LOGOUT_ACTIVITY -> if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            } else {
                val mainIntent = Intent(this, MainActivity::class.java)
                setResult(RESULT_OK, mainIntent)
                startActivity(mainIntent)
            }
        }
    }
}