package com.example.panwest.Data

import java.sql.Timestamp

data class PanFile(
    val Type: String,
    val TypeImg: Int,
    val fileName: String,
    val fileUrl: String
    /*
    val fileLength: Int
    val fileTime: Timestamp
     */
)