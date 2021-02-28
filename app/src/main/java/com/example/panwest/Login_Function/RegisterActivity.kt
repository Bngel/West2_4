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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    private val PSWD_CHECK = "两次密码输入不一致, 请重新输入"
    private val LOGIN_STATE = "login_state"
    private val REGISTER_ACTIVITY = 1

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
            if (userPassword == userPswdCheck) {
                AccountRepository.accountRegister(userAccount, userPassword, userEmail)
                val registerIntent = Intent(this, LoginActivity::class.java)
                registerIntent.putExtra("userAccount", userAccount)
                registerIntent.putExtra("userPassword", userPassword)
                setResult(RESULT_OK, registerIntent)
                finish()
            }
            else {
                Toast.makeText(this, PSWD_CHECK, Toast.LENGTH_SHORT).show()
            }
        }
    }
}