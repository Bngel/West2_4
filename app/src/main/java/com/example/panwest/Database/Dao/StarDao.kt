package com.example.panwest.Database.Dao

import androidx.room.*
import com.example.panwest.Database.Entity.StarEntity

@Dao
interface StarDao {
    @Query("SELECT * FROM star")
    fun getAllStars(): List<StarEntity>

    @Query("SELECT * FROM star WHERE id = (:userId)")
    fun findStarById(starId: Int): StarEntity

    @Insert
    fun insertAllStars(vararg starEntities: StarEntity)

    @Delete
    fun deleteStar(vararg star: StarEntity)

    @Update
    fun updateStar(vararg star: StarEntity)

}