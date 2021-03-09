package com.example.panwest.Data.Json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadFileJson(
    @Expose
    @SerializedName("UploadCostTime")
    val costTime: Int,
    @Expose
    @SerializedName("UploadStatus")
    val status: String
)
