package com.example.panwest.Main_Function.Setting_Function

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.panwest.ActivityCollector
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.Login_Function.LoginActivity
import com.example.panwest.MainActivity
import com.example.panwest.Main_Function.Pan_Function.PanRepository
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_password_change.*
import kotlinx.android.synthetic.main.activity_register.*

class PasswordChangeActivity : AppCompatActivity() {
    private val PSWD_CHECK = "两次密码输入不一致, 请重新输入"
    private val LOGIN_STATE = "login_state"
    private val LOGOUT_ACTIVITY = 0X02

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_change)

        setClickEvent()
    }

    @SuppressLint("CommitPrefEdits")
    private fun setClickEvent() {
        password_pan_change_btn.setOnClickListener {
            val userPassword = password_pan_pswd.text.toString()
            val userPswdCheck = password_pan_pswd_again.text.toString()
            val userCheck = password_pan_pswd_check.text.toString()
            val defaultUser = AccountRepository.user
            if (userPassword != userPswdCheck) {
                Toast.makeText(this, PSWD_CHECK, Toast.LENGTH_SHORT).show()
            }
            else {
                val success = AccountRepository.accountPasswordChange(this, defaultUser?.username?:"",
                    defaultUser?.password?:"", userPassword, userCheck)
                if(success) {
                    Toast.makeText(this, "修改成功, 请重新登录", Toast.LENGTH_SHORT).show()
                    Log.d("TEXT_TTTTT", "修改密码成功")
                    val userInfo: SharedPreferences.Editor = getSharedPreferences(LOGIN_STATE, Context.MODE_PRIVATE).edit()
                    userInfo.apply {
                        putBoolean("STATE", false)
                        putString("ID", "")
                        putString("PSWD", "")
                        apply()
                    }
                    ActivityCollector.finishAll()
                    finish()
                }
                else {
                    Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
        password_pan_check_btn.setOnClickListener {
            AccountRepository.accountPasswordChangeCheck(this, AccountRepository.user?.username?:"")
        }
    }
}