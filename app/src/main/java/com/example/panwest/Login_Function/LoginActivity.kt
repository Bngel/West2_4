package com.example.panwest.Login_Function

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.panwest.BaseActivity
import com.example.panwest.MainActivity
import com.example.panwest.R
import com.example.panwest.WidgetSetting
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    private val REGISTER_ACTIVITY = 0X01
    private val FIND_ACTIVITY = 0X02
    private val LOGIN_STATE = "login_state"
    private val LOGIN_FAIL_RESULT = "登录失败"
    private val LOGIN_SUCCESS_RESULT = "登录成功"

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_pan_register.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(registerIntent, REGISTER_ACTIVITY)
        }

        login_pan_find.setOnClickListener {
            val findIntent = Intent(this, FindPasswordActivity::class.java)
            startActivityForResult(findIntent, REGISTER_ACTIVITY)
        }

        pan_login_btn.setOnClickListener {
            val userAccount = login_pan_id.text.toString()
            val userPassword = login_pan_pswd.text.toString()
            AccountRepository.accountLogin(userAccount, userPassword)
            if (AccountRepository.user != null && AccountRepository.status != null && AccountRepository.status!!) {
                val userInfo = getSharedPreferences(LOGIN_STATE, Context.MODE_PRIVATE).edit()
                userInfo.apply {
                    putBoolean("STATE", true)
                    putString("ID", userAccount)
                    putString("PSWD", userPassword)
                    apply()
                }
                val mainIntent = Intent(this, MainActivity::class.java)
                setResult(RESULT_OK, mainIntent)
                finish()
                Toast.makeText(this, LOGIN_SUCCESS_RESULT, Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, LOGIN_FAIL_RESULT, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REGISTER_ACTIVITY -> {
                    val username = data?.getStringExtra("userAccount")
                    val userpassword = data?.getStringExtra("userPassword")
                    login_pan_id.setText(username)
                    login_pan_pswd.setText(userpassword)
                }
                FIND_ACTIVITY -> {
                    val username = data?.getStringExtra("userAccount")
                    val userpassword = data?.getStringExtra("userPassword")
                    login_pan_id.setText(username)
                    login_pan_pswd.setText(userpassword)
                }
            }
        }
    }

}