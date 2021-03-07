package com.example.panwest.Database.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.panwest.Database.Dao.DeleteDao
import com.example.panwest.Database.Dao.DownloadDao
import com.example.panwest.Database.Dao.ShareDao
import com.example.panwest.Database.Dao.StarDao
import com.example.panwest.Database.Entity.DeleteEntity
import com.example.panwest.Database.Entity.DownloadEntity
import com.example.panwest.Database.Entity.ShareEntity
import com.example.panwest.Database.Entity.StarEntity

@Database(entities = [StarEntity::class, ShareEntity::class, DownloadEntity::class, DeleteEntity::class],
    version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase(){

    //获取数据表操作实例
    abstract fun starDao(): StarDao
    abstract fun shareDao(): ShareDao
    abstract fun downloadDao(): DownloadDao
    abstract fun deleteDao(): DeleteDao

    //单例模式
    companion object {
        private const val DB_STAR = "star_database"
        private const val DB_SHARE = "share_database"
        private const val DB_DOWNLOAD = "download_database"
        private const val DB_DELETE = "delete_database"

        @Volatile
        private var STAR_INSTANCE: AppDatabase? = null
        @Volatile
        private var SHARE_INSTANCE: AppDatabase? = null
        @Volatile
        private var DOWNLOAD_INSTANCE: AppDatabase? = null
        @Volatile
        private var DELETE_INSTANCE: AppDatabase? = null

        fun getStarDatabase(context: Context): AppDatabase{
            val tempInstance = STAR_INSTANCE
            if(tempInstance != null) { return tempInstance }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, DB_STAR).build()
                STAR_INSTANCE = instance
                return instance
            }
        }

        fun getShareDatabase(context: Context): AppDatabase{
            val tempInstance = SHARE_INSTANCE
            if(tempInstance != null) { return tempInstance }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, DB_SHARE).build()
                SHARE_INSTANCE = instance
                return instance
            }
        }

        fun getDownloadDatabase(context: Context): AppDatabase{
            val tempInstance = DOWNLOAD_INSTANCE
            if(tempInstance != null) { return tempInstance }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, DB_DOWNLOAD).build()
                DOWNLOAD_INSTANCE = instance
                return instance
            }
        }

        fun getDeleteDatabase(context: Context): AppDatabase{
            val tempInstance = DELETE_INSTANCE
            if(tempInstance != null) { return tempInstance }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, DB_DELETE).build()
                DELETE_INSTANCE = instance
                return instance
            }
        }
    }
}