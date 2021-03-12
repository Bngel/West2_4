package com.example.panwest.Login_Function

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_find_password.*

class FindPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)
        setClickEvent()
    }

    private fun setClickEvent() {
        find_pan_btn.setOnClickListener {
            val userId = find_pan_id.text.toString()
            val userPassword = find_pan_pswd.text.toString()
            val userPswdCheck = find_pan_pswd_again.text.toString()
            val userCheck = find_pan_check.text.toString()
            if (userPassword != userPswdCheck) {
                Toast.makeText(this, "两次密码输入不一致, 请重新输入", Toast.LENGTH_SHORT).show()
            }
            else {
                val success = AccountRepository.findPassword(this, userId,
                    userPassword, userCheck)
                if(success) {
                    val findIntent = Intent(this, LoginActivity::class.java)
                    findIntent.putExtra("userAccount", userId)
                    findIntent.putExtra("userPassword", userPassword)
                    setResult(RESULT_OK, findIntent)
                    Log.d("TEXT_TTTTT", "找回密码成功")
                    finish()
                }
            }
        }
        find_pan_check_btn.setOnClickListener {
            val userId = find_pan_id.text.toString()
            AccountRepository.accountPasswordChangeCheck(this,
                userId,
                "findPassword")
        }
    }
}