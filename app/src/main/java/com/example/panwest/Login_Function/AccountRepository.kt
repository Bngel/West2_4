package com.example.panwest.Login_Function

import android.util.Log
import com.example.panwest.WebService_Function.WebService
import com.example.panwest.Data.LoginJson
import com.example.panwest.Data.RegisterJson
import com.example.panwest.Data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

object AccountRepository {
    private val accountService = WebService.create()

    fun accountLogin(account: String, pswd: String): LoginJson? {
        val user = User(account, pswd, "")
        val login = accountService.userLogin(user.username, user.password)
        var res : LoginJson? = null
        thread {
            val body = login.execute().body()
            Log.d("TEXT_TTT", body.toString())
            res = body
        }.join()
        return res
    }

    fun accountRegister(account: String, pswd: String, email: String): RegisterJson? {
        val user = User(account, pswd, email)
        val login = accountService.userRegister(user.username, user.password, user.email)
        var res: RegisterJson? = null
        thread {
            val body = login.execute().body()
            Log.d("TEXT_TTT", body.toString())
            res = body
        }.join()
        return res
    }
}