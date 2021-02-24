package com.example.panwest.Login_Function

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.panwest.R
import com.example.panwest.WebService_Function.WebService
import com.example.panwest.WebService_Function.baseUrl
import com.example.panwest.data.LoginJson
import com.example.panwest.data.User
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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