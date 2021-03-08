package com.example.panwest.Main_Function.Pan_Function

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.panwest.Data.FileData
import com.example.panwest.Data.Json.LoadFilesJson
import com.example.panwest.Data.PanFile
import com.example.panwest.WebService_Function.WebService
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

object PanRepository {
    private val panService = WebService.create()

    val selectedCount = MutableLiveData(0)

    val selectedItem = ArrayList<PanFile>()

    fun selectedItemAdd(panFile: PanFile) {
        selectedItem.add(panFile)
        selectedCount.value = selectedItem.size
    }

    fun selectedItemRemove(panFile: PanFile) {
        selectedItem.remove(panFile)
        selectedCount.value = selectedItem.size
    }

    fun selectedItemExists(panFile: PanFile) = panFile in selectedItem

    fun selectedItemAddAll(panFiles: List<PanFile>) {
        for (panFile in panFiles) {
            if (!selectedItemExists(panFile))
                selectedItemAdd(panFile)
        }
    }

    fun selectedItemRemoveAll(panFiles: List<PanFile>) {
        for (panFile in panFiles) {
            if (selectedItemExists(panFile))
                selectedItemRemove(panFile)
        }
    }

    fun loadFileInformation(username: String, parentFile: String): List<FileData>? {
        val fileInfo = panService.getFileInformation(username, parentFile)
        var res: List<FileData>? = null
        thread {
            try {
                val body = fileInfo.execute().body()
                Log.d("TEXT_TTT", body.getFileInformationStatus + " ZHEGENIUDE")
                res = body.file_data_list
            } catch (e: SocketTimeoutException) {
            }
        }.join(2000)
        return res
    }
}