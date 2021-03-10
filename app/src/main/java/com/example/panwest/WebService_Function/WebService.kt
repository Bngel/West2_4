package com.example.panwest.WebService_Function

import com.example.panwest.Data.FileData
import com.example.panwest.Data.Json.*
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
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

    @POST("getPhoto")
    fun getUserPhoto(
        @Query("username") username: String):
            Call<String>

    @POST("getFileInformation")
    fun getFileInformation(
        @Query("username") username: String,
        @Query("parentFile") parentFile: String):
            Call<LoadFilesJson>

    @Multipart
    @POST("upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("username") username: String,
        @Part("parentFile") parentFile: String):
            Call<UploadFileJson>

    @POST("delete")
    fun deleteFile(
        @Query("username") username: String,
        @Query("url") url: String):
            Call<DeleteFileJson>

    @POST("deleteFile")
    fun deletePackage(
        @Query("username") username: String,
        @Query("url") url: String):
            Call<DeletePackageJson>

    @POST("createNewFile")
    fun createPackage(
        @Query("username") username: String,
        @Query("Filename") package_name: String,
        @Query("parentFile") parentFile: String):
            Call<CreatePackageJson>

    @POST("download")
    fun downloadFile(
        @Query("username") username: String,
        @Query("url") url: String):
            Call<ResponseBody>



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