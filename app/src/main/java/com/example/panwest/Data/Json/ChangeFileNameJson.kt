package com.example.panwest.Data.Json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChangeFileNameJson(
    @Expose
    @SerializedName("changeFilenameStatus")
    val status: String
)
