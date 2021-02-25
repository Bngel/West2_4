package com.example.panwest.Login_Function

import android.util.Log
import com.example.panwest.WebService_Function.WebService
import com.example.panwest.data.LoginJson
import com.example.panwest.data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AccountRepository {
    private val accountService = WebService.create()

    fun accountLogin(account: String, pswd: String) {
        val user = User(account, pswd, "")
        val login = accountService.userLogin(user)
        login.enqueue(object : Callback<LoginJson> {
            override fun onResponse(call: Call<LoginJson>?, response: Response<LoginJson>?) {
                val loginjson = response!!.body()!!
                Log.d("TEXT_TTT", loginjson.status.toString())
            }

            override fun onFailure(call: Call<LoginJson>?, t: Throwable?) {
                Log.d("TEXT_TTT", t.toString())
            }
        })
    }
}