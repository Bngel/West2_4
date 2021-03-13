package com.example.panwest.WebService_Function

import com.example.panwest.Data.FileData
import com.example.panwest.Data.Json.*
import com.example.panwest.Login_Function.AccountRepository
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
            Call<ResponseBody>

    @POST("getFileInformation")
    fun getFileInformation(
        @Query("username") username: String,
        @Query("parentFile") parentFile: String,
        @Header("token") token: String):
            Call<LoadFilesJson>

    @Multipart
    @POST("upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("username") username: String,
        @Part("parentFile") parentFile: String):
            Call<UploadFileJson>
        //@Header("token") token: String


    @POST("delete")
    fun deleteFile(
        @Query("username") username: String,
        @Query("url") url: String,
        @Header("token") token: String):
            Call<DeleteFileJson>

    @POST("deleteFile")
    fun deletePackage(
        @Query("username") username: String,
        @Query("url") url: String,
        @Header("token") token: String):
            Call<DeletePackageJson>

    @POST("createNewFile")
    fun createPackage(
        @Query("username") username: String,
        @Query("Filename") package_name: String,
        @Query("parentFile") parentFile: String,
        @Header("token") token: String):
            Call<CreatePackageJson>

    @POST("download")
    fun downloadFile(
        @Query("username") username: String,
        @Query("url") url: String,
        @Header("token") token: String):
            Call<ResponseBody>

    @POST("changePassword")
    fun changePassword(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("newPassword") new_password: String,
        @Query("yzm") check: String,
        @Header("token") token: String):
            Call<PasswordChangeJson>

    @POST("sendEmail")
    fun getPasswordCheck(
        @Query("username") username: String,
        @Query("type") type: String):
            Call<PasswordCheckJson>

    @POST("addFavor")
    fun addFavor(
        @Query("username") username: String,
        @Query("url") url: String,
        @Header("token") token: String):
            Call<AddFavorJson>

    @POST("removeFavor")
    fun removeFavor(
        @Query("username") username: String,
        @Query("url") url: String,
        @Header("token") token: String):
            Call<RemoveFavorJson>

    @POST("getFavor")
    fun getFavor(
        @Query("username") username: String,
        @Header("token") token: String):
            Call<GetFavorJson>

    @POST("getFeedback")
    fun getFeedback(
        @Query("username") username: String,
        @Query("feedback") feedback: String,
        @Header("token") token: String):
            Call<FeedbackJson>

    @POST("changeFilename")
    fun changeFilename(
        @Query("username") username: String,
        @Query("newFilename") newFilename: String,
        @Query("url") url: String,
        @Header("token") token: String):
            Call<ChangeFileNameJson>

    @POST("findPassword")
    fun findPassword(
        @Query("username") username: String,
        @Query("newPassword") newPassword: String,
        @Query("yzm") check: String):
            Call<FindPasswordJson>

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