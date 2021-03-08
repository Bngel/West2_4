package com.example.panwest.Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FileData(
    @Expose
    @SerializedName("url")
    val url: String,
    @Expose
    @SerializedName("username")
    val username: String,
    @Expose
    @SerializedName("filename")
    val filename: String,
    @Expose
    @SerializedName("parent")
    val parent: String,
    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("sizes")
    val sizes: Double,
    @Expose
    @SerializedName("date")
    val date: String
)
