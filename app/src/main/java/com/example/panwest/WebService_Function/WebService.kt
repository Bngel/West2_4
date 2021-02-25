package com.example.panwest.WebService_Function

import com.example.panwest.data.LoginJson
import com.example.panwest.data.User
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface WebService {

    @POST("User/login")
    fun userLogin(@Body user: User): Call<LoginJson>

    companion object Factory {
        fun create() : WebService {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofit: Retrofit =  Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            val accountService = retrofit.create(WebService::class.java)
            return accountService
        }
    }


}