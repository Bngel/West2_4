package com.example.panwest.Database.Dao

import androidx.room.*
import com.example.panwest.Database.Entity.DownloadEntity

@Dao
interface DownloadDao {
    @Query("SELECT * FROM download")
    fun getAllDownloads(): List<DownloadEntity>

    @Query("SELECT * FROM download WHERE id = (:downloadId)")
    fun findDownloadById(downloadId: Int): DownloadEntity

    @Insert
    fun insertAllDownloads(vararg downloadEntities: DownloadEntity)

    @Delete
    fun deleteDownload(vararg download: DownloadEntity)

    @Update
    fun updateDownload(vararg download: DownloadEntity)

}