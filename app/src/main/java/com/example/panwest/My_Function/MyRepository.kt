package com.example.panwest.My_Function

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.panwest.Data.FileData
import com.example.panwest.Data.Json.AddFavorJson
import com.example.panwest.Data.Json.RemoveFavorJson
import com.example.panwest.Data.PanFile
import com.example.panwest.Database.Database.AppDatabase
import com.example.panwest.Database.Entity.StarEntity
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.WebService_Function.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

object MyRepository {
    /**
     * starActivity Part
     */
    val MyService = WebService.create()
    val starCount = MutableLiveData(0)
    val staredItem = ArrayList<FileData>()
    val starListFlush = MutableLiveData<Boolean>(false)

    private fun StarEntity_to_FileData(fileData: StarEntity): FileData{
        return FileData(fileData.url, fileData.username, fileData.filename,
            fileData.parent, fileData.type, fileData.sizes, fileData.date)
    }

    private fun FileData_to_StarEntity(fileData: FileData): StarEntity{
        return StarEntity(0, fileData.url, fileData.username, fileData.filename,
            fileData.parent, fileData.type, fileData.sizes, fileData.date)
    }

    fun getStarItems(context: Context): List<FileData> {
        val favor = MyService.getFavor(AccountRepository.user?.username?:"", AccountRepository.token!!)
        val resList = ArrayList<FileData>()
        thread {
            try {
                val body = favor.execute().body()
                if (body != null && body.status == "success") {
                    resList.addAll(body.starList)
                    val dataDB = AppDatabase.getStarDatabase(context)
                    thread {
                        val dao = dataDB.starDao()
                        dao.deleteAllStars()
                        for (star in body.starList){
                            dao.insertAllStars(FileData_to_StarEntity(star))
                        }

                    }
                    Log.d("TEXT_TTT","加载收藏成功")
                }
                else {
                    val starDB = AppDatabase.getStarDatabase(context).starDao()
                    val starList = starDB.getAllStars()
                    for (star in starList)
                        resList.add(StarEntity_to_FileData(star))
                    Log.d("TEXT_TTT","加载默认收藏")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.join(2000)
        starListFlush.value = true
        return resList
    }

    fun starItemSelect(starFile: FileData) {
        staredItem.add(starFile)
        starCount.value = staredItem.size
    }

    fun starItemSelectAll(starFiles: List<FileData>) {
        for (starFile in starFiles) {
            if (starFile !in staredItem)
                staredItem.add(starFile)
        }
        starCount.value = staredItem.size
    }

    fun starItemRemove(starFile: FileData) {
        staredItem.remove(starFile)
        starCount.value = staredItem.size
    }

    fun starItemRemoveAll(starFiles: List<FileData>) {
        for (starFile in starFiles) {
            if (starFile in staredItem)
                staredItem.remove(starFile)
        }
        starCount.value = staredItem.size
    }

    fun staredItemAdd(context: Context, starFile: FileData) {
        val starDB = AppDatabase.getStarDatabase(context).starDao()
        val starFileEntity = FileData_to_StarEntity(starFile)
        val starAdd = MyService.addFavor(AccountRepository.user?.username?:"", starFile.url, AccountRepository.token!!)

        starAdd.enqueue(object : Callback<AddFavorJson> {
            override fun onResponse(call: Call<AddFavorJson>?, response: Response<AddFavorJson>?) {
                val body = response?.body()
                if (body != null){
                    when (body.status) {
                        "success" -> {
                            thread {
                                starDB.insertAllStars(starFileEntity)
                            }
                            Toast.makeText(context, "添加收藏成功", Toast.LENGTH_SHORT).show()
                            starListFlush.value = true
                        }
                        "ExistWrong" -> {
                            Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show()
                        }
                        "FileWrong" -> {
                            Toast.makeText(context, "该文件属于文件夹不能收藏", Toast.LENGTH_SHORT).show()
                        }
                        "UserWrong" -> {
                            Toast.makeText(context, "文件不属于该用户", Toast.LENGTH_SHORT).show()
                        }
                        "RepeatWrong" -> {
                            Toast.makeText(context, "文件已被收藏", Toast.LENGTH_SHORT).show()
                        }
                        "TokenWrong" -> {
                            Toast.makeText(context, "登录时间过长, Token已失效, 请重新登录", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(context, "添加收藏失败", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AddFavorJson>?, t: Throwable?) {
                Toast.makeText(context, "添加收藏失败", Toast.LENGTH_SHORT).show()
            }
        })
        starListFlush.value = true
    }

    fun staredItemDelete(context: Context, starFile: FileData) {
        val starDB = AppDatabase.getStarDatabase(context).starDao()
        val starFileEntity = FileData_to_StarEntity(starFile)
        val starDelete = MyService.removeFavor(AccountRepository.user?.username?:"", starFile.url, AccountRepository.token!!)

        starDelete.enqueue(object : Callback<RemoveFavorJson> {
            override fun onResponse(
                call: Call<RemoveFavorJson>?,
                response: Response<RemoveFavorJson>?
            ) {
                val body = response?.body()
                if (body != null){
                    when (body.status) {
                        "success" -> {
                            thread {
                                starDB.deleteStar(starFileEntity)
                            }
                            Toast.makeText(context, "删除收藏成功", Toast.LENGTH_SHORT).show()
                            starListFlush.value = true
                        }
                        "ExistWrong" -> {
                            Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show()
                        }
                        "UserWrong" -> {
                            Toast.makeText(context, "文件不属于该用户", Toast.LENGTH_SHORT).show()
                        }
                        "FileWrong" -> {
                            Toast.makeText(context, "文件未被收藏", Toast.LENGTH_SHORT).show()
                        }
                        "TokenWrong" -> {
                            Toast.makeText(context, "登录时间过长, Token已失效, 请重新登录", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(context, "删除收藏失败", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<RemoveFavorJson>?, t: Throwable?) {
                Toast.makeText(context, "删除收藏失败", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun staredItemAddAll(context: Context, starFiles: List<FileData>) {
        val starDB = AppDatabase.getStarDatabase(context).starDao()
        val starFileEntities = ArrayList<StarEntity>()
        for (starFile in starFiles){
            starFileEntities.add(FileData_to_StarEntity(starFile))
        }
        thread {
            for (starFileEntity in starFileEntities) {
                starDB.insertAllStars(starFileEntity)
            }
        }
        starListFlush.value = true
    }

    fun staredItemDeleteAll(context: Context, starFiles: List<FileData>) {
        for (starFile in starFiles) {
            staredItemDelete(context, starFile)
        }
        starListFlush.value = true
    }

    /**
     * shareActivity Part
     */
    val shareCount = MutableLiveData(0)
    val sharedItem = ArrayList<PanFile>()
    fun sharedItemAdd(panFile: PanFile) {
        sharedItem.add(panFile)
        shareCount.value = sharedItem.size
    }

    fun sharedItemRemove(panFile: PanFile) {
        sharedItem.remove(panFile)
        shareCount.value = sharedItem.size
    }

    fun sharedItemExists(panFile: PanFile) = panFile in sharedItem

    fun sharedItemAddAll(panFiles: List<PanFile>) {
        for (panFile in panFiles) {
            if (!sharedItemExists(panFile))
                sharedItemAdd(panFile)
        }
    }

    fun sharedItemRemoveAll(panFiles: List<PanFile>) {
        for (panFile in panFiles) {
            if (sharedItemExists(panFile))
                sharedItemRemove(panFile)
        }
    }

    /**
     * downloadActivity Part
     */
    val downloadCount = MutableLiveData(0)
    val downloadedItem = ArrayList<FileData>()
    val downloadListFlush = MutableLiveData<Boolean>(false)
    fun downloadedItemAdd(downloadFile: FileData) {
        downloadedItem.add(downloadFile)
        downloadCount.value = downloadedItem.size
    }

    fun downloadedItemRemove(downloadFile: FileData) {
        downloadedItem.remove(downloadFile)
        downloadCount.value = downloadedItem.size
    }

    fun downloadedItemExists(downloadFile: FileData) = downloadFile in downloadedItem

    fun downloadedItemAddAll(downloadFiles: List<FileData>) {
        for (downloadFile in downloadFiles) {
            if (!downloadedItemExists(downloadFile))
                downloadedItemAdd(downloadFile)
        }
    }

    fun downloadedItemRemoveAll(downloadFiles: List<FileData>) {
        for (downloadFile in downloadFiles) {
            if (downloadedItemExists(downloadFile))
                downloadedItemRemove(downloadFile)
        }
    }

    /**
     * deleteActivity Part
     */
    val deleteCount = MutableLiveData(0)
    val deletedItem = ArrayList<PanFile>()
    fun deletedItemAdd(panFile: PanFile) {
        deletedItem.add(panFile)
        deleteCount.value = deletedItem.size
    }

    fun deletedItemRemove(panFile: PanFile) {
        deletedItem.remove(panFile)
        deleteCount.value = deletedItem.size
    }

    fun deletedItemExists(panFile: PanFile) = panFile in deletedItem

    fun deletedItemAddAll(panFiles: List<PanFile>) {
        for (panFile in panFiles) {
            if (!deletedItemExists(panFile))
                deletedItemAdd(panFile)
        }
    }

    fun deletedItemRemoveAll(panFiles: List<PanFile>) {
        for (panFile in panFiles) {
            if (deletedItemExists(panFile))
                deletedItemRemove(panFile)
        }
    }
}