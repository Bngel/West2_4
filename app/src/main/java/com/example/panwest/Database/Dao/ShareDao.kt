package com.example.panwest.Database.Dao

import androidx.room.*
import com.example.panwest.Database.Entity.ShareEntity

@Dao
interface ShareDao {
    @Query("SELECT * FROM share")
    fun getAllShares(): List<ShareEntity>

    @Query("SELECT * FROM share WHERE id = (:shareId)")
    fun findShareById(shareId: Int): ShareEntity

    @Insert
    fun insertAllShares(vararg shareEntities: ShareEntity)

    @Delete
    fun deleteShare(vararg share: ShareEntity)

    @Update
    fun updateShare(vararg share: ShareEntity)

}