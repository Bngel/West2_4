package com.example.panwest.Database.Dao

import androidx.room.*
import com.example.panwest.Database.Entity.DeleteEntity

@Dao
interface DeleteDao {
    @Query("SELECT * FROM deleted")
    fun getAllDeletes(): List<DeleteEntity>

    @Query("SELECT * FROM deleted WHERE id = (:deleteId)")
    fun findDeleteById(deleteId: Int): DeleteEntity

    @Insert
    fun insertAllDeletes(vararg deleteEntities: DeleteEntity)

    @Delete
    fun deleteDelete(vararg delete: DeleteEntity)

    @Update
    fun updateDelete(vararg delete: DeleteEntity)

}