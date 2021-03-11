package com.example.panwest.Data.Json

import com.example.panwest.Data.FileData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetFavorJson(
    @Expose
    @SerializedName("FavorFileList")
    val starList: List<FileData>,
    @Expose
    @SerializedName("getFavorFileStatus")
    val status: String
)
