package com.example.panwest.Data.Json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeleteFileJson(
    @Expose
    @SerializedName("DeleteStatus")
    val status: String
)

