package com.example.panwest.Data.Json

import com.example.panwest.Data.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginJson(
    @Expose
    @SerializedName("LoginStatus")
    val status: Boolean,
    @Expose
    @SerializedName("user")
    val user: User
    )
