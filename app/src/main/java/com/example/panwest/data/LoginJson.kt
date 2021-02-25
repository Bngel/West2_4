package com.example.panwest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginJson(
    @Expose
    @SerializedName("loginStatus")
    val status: Boolean,
    @Expose
    @SerializedName("userInformation")
    val user: User
    )
