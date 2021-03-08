package com.example.panwest.WebService_Function

import com.example.panwest.Data.FileData
import com.example.panwest.Data.Json.LoadFilesJson
import com.example.panwest.Data.Json.LoginJson
import com.example.panwest.Data.Json.PhotoJson
import com.example.panwest.Data.Json.RegisterJson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
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

    @POST("getPhoto")
    fun getUserPhoto(
        @Query("username") username: String):
            Call<String>

    @POST("getFileInformation")
    fun getFileInformation(
        @Query("username") username: String,
        @Query("parentFile") parentFile: String):
            Call<LoadFilesJson>



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