package com.example.panwest.Data.Json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PhotoJson(
    @Expose
    @SerializedName("PhotoUploadStatus")
    val status: String
)
