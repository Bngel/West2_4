package com.example.panwest.Login_Function

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.panwest.Data.Json.*
import com.example.panwest.Data.User
import com.example.panwest.Main_Function.Pan_Function.PanRepository
import com.example.panwest.R
import com.example.panwest.WebService_Function.WebService
import com.example.panwest.WidgetSetting
import kotlinx.android.synthetic.main.view_meform_main.*
import retrofit2.Callback
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.net.SocketTimeoutException
import kotlin.concurrent.thread


object AccountRepository {

    var user: User? = null
    var status: Boolean? = null
    var token: String? = null
    private val accountService = WebService.create()
    var PORTRAIT_PATH :String? = null

    fun accountLogin(account: String, pswd: String){
        val login = accountService.userLogin(account, WidgetSetting.getMD5(pswd))
        thread {
            try {
                val body = login.execute().body()
                    user = body.user
                    status = body.status
                    token = body.token
            } catch (e: Exception) {
            }
        }.join(4000)
    }

    fun accountRegister(account: String, pswd: String, email: String): RegisterJson? {
        Log.d("TEXT_TTT", WidgetSetting.getMD5(pswd))
        val user = User(account, WidgetSetting.getMD5(pswd), email, "", 1024.0)
        val login = accountService.userRegister(user.username, user.password, user.email)
        var res: RegisterJson? = null
        thread {
            try{
                val body = login.execute().body()
                Log.d("TEXT_TTT", body.toString())
                res = body
            } catch (e: Exception) {
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
                Log.d("PHOTO_UPLOAD", body!!.toString())
                if (body.status == "success")
                    res = body
            } catch (e: SocketTimeoutException) {
            }
        }.join(2000)
        return res
    }

    fun accountGetPhoto(context: Context, username: String, portrait_pic: ImageView) {
        val PORTRAIT = "portrait.png"
        val filepath = "${PORTRAIT_PATH}/${user?.username}${PORTRAIT}"
        //val filepath = System.currentTimeMillis().toString() + "galleryDemo.jpg"
        val photo = accountService.getUserPhoto(username)
        photo.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                val body = response?.body()
                if (body != null) {
                    makeDir(PORTRAIT_PATH!!)
                    val photo = PanRepository.writeResponseBodyToDisk(body, filepath)
                    if (photo) {
                        Log.d("PHOTO_TEXT","读取成功")
                        Glide.with(context)
                            .load(filepath)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.me_loading) // 占位图设置：加载过程中显示的图片
                            .error(R.drawable.me_error) // 异常占位图
                            .centerCrop()
                            .into(portrait_pic)
                    }
                    else {
                        Log.d("PHOTO_TEXT","读取失败")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Toast.makeText(context, "头像更新失败", Toast.LENGTH_SHORT).show()
                val portrait = File(filepath)
                if (portrait.exists()){
                    Glide.with(context)
                        .load(filepath)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.me_loading) // 占位图设置：加载过程中显示的图片
                        .error(R.drawable.me_error) // 异常占位图
                        .centerCrop()
                        .into(portrait_pic)
                }
            }
        })
    }

    private fun makeDir(dir: String){
        // I/O logic
        val sciezka = File(dir)
        if (sciezka.mkdirs()) {
            Log.d("TEXT_TTT", dir)
        }
        else {
            Log.d("TEXT_TTT", "existed")
        }
    }

    fun accountPasswordChangeCheck(context: Context, username: String, type: String) {
        val check = accountService.getPasswordCheck(username, type)
        check.enqueue(object : Callback<PasswordCheckJson> {
            override fun onResponse(
                call: Call<PasswordCheckJson>?,
                response: Response<PasswordCheckJson>?
            ) {
                val body = response?.body()
                Log.d("TEXT_TTT", "SEND:"+body.toString())
                if(body != null) {
                    if (body.status == "success") {
                        Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<PasswordCheckJson>?, t: Throwable?) {
                Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun accountPasswordChange(context: Context, username: String, password: String, newPassword: String, check: String): Boolean {
        var success = false
        val change = accountService.changePassword(
            username,
            WidgetSetting.getMD5(password),
            WidgetSetting.getMD5(newPassword),
            check,
            token!!)
        thread {
            try{
                val body = change.execute().body()
                Log.d("TEXT_TTT", "password_change " + body.status)
                if(body != null) {
                    if (body.status == "success") {
                        success = true
                    }
                    else if (body.status == "TokenWrong"){
                        Toast.makeText(context, "登录时间过长, Token已失效, 请重新登录", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
            }
        }.join(3000)
        return success
    }

    fun findPassword(context: Context, username: String, newPassword: String, check: String): Boolean {
        val find = accountService.findPassword(username, WidgetSetting.getMD5(newPassword), check)
        var res = false
        var body: FindPasswordJson? = null
        thread{
            body = find.execute().body()
        }.join(4000)
        if (body != null) {
            when (body!!.status) {
                "success" -> {
                    Toast.makeText(context, "修改密码成功", Toast.LENGTH_SHORT).show()
                    res = true
                }
                "UserWrong" -> {
                    Toast.makeText(context, "用户名不存在", Toast.LENGTH_SHORT).show()
                }
                "YzmWrong" -> {
                    Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "找回密码失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            Toast.makeText(context, "找回密码失败", Toast.LENGTH_SHORT).show()
        }
        return res
    }
}