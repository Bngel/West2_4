package com.example.panwest.Database.Dao

import androidx.room.*
import com.example.panwest.Data.FileData
import com.example.panwest.Database.Entity.StarEntity

@Dao
interface StarDao {
    @Query("SELECT * FROM star")
    fun getAllStars(): List<StarEntity>

    @Query("SELECT * FROM star WHERE id = (:starId)")
    fun findStarById(starId: Int): StarEntity

    @Query("SELECT * FROM star WHERE filename = (:starFilename)")
    fun findStarByFilename(starFilename: String): StarEntity

    @Insert
    fun insertAllStars(vararg starEntities: StarEntity)

    @Delete
    fun deleteStar(vararg star: StarEntity)

    @Query("DELETE FROM star")
    fun deleteAllStars()

    @Update
    fun updateStar(vararg star: StarEntity)

}