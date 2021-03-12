package com.example.panwest.Data.Json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FindPasswordJson(
    @Expose
    @SerializedName("FindPasswordStatus")
    val status: String
)
