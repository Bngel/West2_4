package com.example.panwest.Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterJson(
    @Expose
    @SerializedName("RegisterStatus")
    val status: Boolean,
    @Expose
    @SerializedName("user")
    val user: User
)
