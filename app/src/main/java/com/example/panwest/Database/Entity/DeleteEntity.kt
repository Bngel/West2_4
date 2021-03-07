package com.example.panwest.Database.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted")
data class DeleteEntity(@PrimaryKey @ColumnInfo(name = "id") val id: Int,
                      @ColumnInfo(name = "type") val Type: String?,
                      @ColumnInfo(name = "img") val TypeImg: Int?,
                      @ColumnInfo(name = "file_name") val fileName: String?,
                      @ColumnInfo(name = "file_url") val fileUrl: String?)
