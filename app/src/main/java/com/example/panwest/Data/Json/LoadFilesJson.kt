package com.example.panwest.Data.Json

import com.example.panwest.Data.FileData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoadFilesJson(
    @Expose
    @SerializedName("FileDataList")
    val file_data_list: List<FileData>,
    @Expose
    @SerializedName("getFileInformationStatus")
    val getFileInformationStatus: String
)
