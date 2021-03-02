package com.example.panwest.Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose
    @SerializedName("username")
    val username: String,
    @Expose
    @SerializedName("password")
    val password: String,
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("photo")
    val photo: String,
    @Expose
    @SerializedName("space")
    val space: Int
    )