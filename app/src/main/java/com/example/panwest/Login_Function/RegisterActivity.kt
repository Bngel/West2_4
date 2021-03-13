package com.example.panwest.Login_Function

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.panwest.BaseActivity
import com.example.panwest.R
import com.example.panwest.WidgetSetting
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    private val PSWD_CHECK = "两次密码输入不一致, 请重新输入"
    private val LOGIN_STATE = "login_state"
    private val REGISTER_ACTIVITY = 1
    private val REGISTER_FAIL_RESULT = "注册失败"
    private val REGISTER_SUCCESS_RESULT = "注册成功"
    private val REGISTER_ERROREMAIL_RESULT = "邮箱格式有误"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setClickEvent()
    }

    @SuppressLint("CommitPrefEdits")
    private fun setClickEvent() {
        pan_register_btn.setOnClickListener {
            val userEmail = register_pan_email.text.toString()
            val userAccount = register_pan_id.text.toString()
            val userPassword = register_pan_pswd.text.toString()
            val userPswdCheck = register_pan_pswd_again.text.toString()
            val regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$".toRegex()
            if (!regex.matches(userEmail)) {
                Toast.makeText(this, REGISTER_ERROREMAIL_RESULT, Toast.LENGTH_SHORT).show()
            }
            else if (userPassword != userPswdCheck) {
                Toast.makeText(this, PSWD_CHECK, Toast.LENGTH_SHORT).show()
            }
            else {
                val registerStatus = AccountRepository.accountRegister(userAccount, userPassword, userEmail)
                if (registerStatus != null && registerStatus.status) {
                    val registerIntent = Intent(this, LoginActivity::class.java)
                    registerIntent.putExtra("userAccount", userAccount)
                    registerIntent.putExtra("userPassword", userPassword)
                    setResult(RESULT_OK, registerIntent)
                    finish()
                    Toast.makeText(this, REGISTER_SUCCESS_RESULT, Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, REGISTER_FAIL_RESULT, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}