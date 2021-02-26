package com.example.panwest.Login_Function

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val REGISTER_ACTIVITY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_pan_register.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(registerIntent, REGISTER_ACTIVITY)
        }

        pan_login_btn.setOnClickListener {
            val userAccount = login_pan_id.text.toString()
            val userPassword = login_pan_pswd.text.toString()
            AccountRepository.accountLogin(userAccount, userPassword)
        }
    }

}