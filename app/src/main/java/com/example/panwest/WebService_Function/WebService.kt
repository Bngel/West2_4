package com.example.panwest.WebService_Function

import com.example.panwest.Data.LoginJson
import com.example.panwest.Data.PhotoJson
import com.example.panwest.Data.RegisterJson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface WebService {

    @GET("login")
    fun userLogin(
        @Query("username") username: String,
        @Query("password") password: String):
            Call<LoginJson>

    @GET("register")
    fun userRegister(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("email") email: String):
            Call<RegisterJson>

    @Multipart
    @POST("photoUpload")
    fun userPhoto(
        @Part file: MultipartBody.Part,
        @Part("username") username: String):
            Call<PhotoJson>

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