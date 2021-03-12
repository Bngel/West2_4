package com.example.panwest.Main_Function.Setting_Function

import android.content.Context
import android.widget.Toast
import com.example.panwest.Data.Json.FeedbackJson
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.WebService_Function.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SettingRepository {

    val settingService = WebService.create()

    fun getFeedback(context: Context, username: String, feedback: String) {
        val fb = settingService.getFeedback(username, feedback, AccountRepository.token!!)
        fb.enqueue(object: Callback<FeedbackJson> {
            override fun onResponse(call: Call<FeedbackJson>?, response: Response<FeedbackJson>?) {
                val body = response?.body()
                if (body != null) {
                    when (body.status) {
                        "success" -> {
                            Toast.makeText(context, "反馈成功", Toast.LENGTH_SHORT).show()
                        }
                        "TokenWrong" -> {
                            Toast.makeText(context, "登录时间过长, Token已失效, 请重新登录", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(context, "反馈失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FeedbackJson>?, t: Throwable?) {
                Toast.makeText(context, "反馈失败", Toast.LENGTH_SHORT).show()
            }
        })
    }
}