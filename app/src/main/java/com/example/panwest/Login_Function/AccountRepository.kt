package com.example.panwest.Login_Function

import android.util.Log
import com.example.panwest.WebService_Function.WebService
import com.example.panwest.Data.LoginJson
import com.example.panwest.Data.RegisterJson
import com.example.panwest.Data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AccountRepository {
    private val accountService = WebService.create()

    fun accountLogin(account: String, pswd: String){
        val user = User(account, pswd, "")
        val login = accountService.userLogin(user.username, user.password)
        login.enqueue(object : Callback<LoginJson> {
            override fun onResponse(call: Call<LoginJson>?, response: Response<LoginJson>?) {
                val loginJson = response!!.body()!!
                Log.d("TEXT_TTT", loginJson.toString())
            }

            override fun onFailure(call: Call<LoginJson>?, t: Throwable?) {
                Log.d("TEXT_TTT", t.toString())
            }
        })
    }

    fun accountRegister(account: String, pswd: String, email: String) {
        val user = User(account, pswd, email)
        val login = accountService.userRegister(user.username, user.password, user.email)
        login.enqueue(object : Callback<RegisterJson> {
            override fun onResponse(call: Call<RegisterJson>?, response: Response<RegisterJson>?) {
                val registerJson = response!!.body()!!
                Log.d("TEXT_TTT", registerJson.toString())
            }

            override fun onFailure(call: Call<RegisterJson>?, t: Throwable?) {
                Log.d("TEXT_TTT", t.toString())
            }
        })
    }
}