package com.example.panwest.Login_Function

import android.util.Log
import com.example.panwest.Data.Json.LoginJson
import com.example.panwest.Data.Json.PhotoJson
import com.example.panwest.Data.Json.RegisterJson
import com.example.panwest.Data.User
import com.example.panwest.WebService_Function.WebService
import okhttp3.MultipartBody
import java.net.SocketTimeoutException
import kotlin.concurrent.thread


object AccountRepository {
    private val accountService = WebService.create()

    fun accountLogin(account: String, pswd: String): LoginJson? {
        val user = User(account, pswd, "", "", 1024)
        val login = accountService.userLogin(user.username, user.password)
        var res : LoginJson? = null
        thread {
            try {
                val body = login.execute().body()
                Log.d("TEXT_TTT", body.toString())
                res = body
            } catch (e: SocketTimeoutException) {
            }
        }.join(2000)
        return res
    }

    fun accountRegister(account: String, pswd: String, email: String): RegisterJson? {
        val user = User(account, pswd, email, "", 1024)
        val login = accountService.userRegister(user.username, user.password, user.email)
        var res: RegisterJson? = null
        thread {
            try{
                val body = login.execute().body()
                Log.d("TEXT_TTT", body.toString())
                res = body
            } catch (e: SocketTimeoutException) {
            }
        }.join(2000)
        return res
    }

    fun accountPhoto(photo: MultipartBody.Part, username: String): PhotoJson? {
        var res: PhotoJson? = null
        val upload = accountService.userPhoto(photo, username)
        thread {
            try{
            val body = upload.execute().body()
            Log.d("TEXT_TTT", body!!.toString())
            res = body
            } catch (e: SocketTimeoutException) {
            }
        }.join(2000)
        return res
    }

    fun accountGetPhoto(username: String): String? {
        var res: String? = null
        val upload = accountService.getUserPhoto(username)
        thread {
            try{
            val body = upload.execute().body()
            Log.d("TEXT_TTT", body!!)
            res = body
            } catch (e: SocketTimeoutException) {
            }
        }.join(2000)
        return res
    }
}