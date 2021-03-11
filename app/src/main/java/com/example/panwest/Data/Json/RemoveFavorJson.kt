package com.example.panwest.Data.Json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RemoveFavorJson(
    @Expose
    @SerializedName("removeFavorStatus")
    val status: String
)
