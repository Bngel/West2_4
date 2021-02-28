package com.example.panwest.WebService_Function

import com.example.panwest.Data.LoginJson
import com.example.panwest.Data.RegisterJson
import com.example.panwest.Data.User
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WebService {

    @GET("login")
    fun userLogin(@Query("username") username: String, @Query("password") password: String): Call<LoginJson>

    @GET("register")
    fun userRegister(@Query("username") username: String, @Query("password") password: String, @Query("email") email: String): Call<RegisterJson>

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