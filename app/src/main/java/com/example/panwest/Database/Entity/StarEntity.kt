package com.example.panwest.Database.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "star")
data class StarEntity(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
                      @ColumnInfo(name = "url") val url: String,
                      @ColumnInfo(name = "username") val username: String,
                      @ColumnInfo(name = "filename") val filename: String,
                      @ColumnInfo(name = "parent") val parent: String,
                      @ColumnInfo(name = "type") val type: String,
                      @ColumnInfo(name = "sizes") val sizes: Double,
                      @ColumnInfo(name = "date") val date: String)