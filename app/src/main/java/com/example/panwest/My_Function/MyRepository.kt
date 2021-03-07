package com.example.panwest.My_Function

import androidx.lifecycle.MutableLiveData
import com.example.panwest.Data.PanFile

object MyRepository {

    /**
     * starActivity Part
     */
    val starCount = MutableLiveData(0)
    val staredItem = ArrayList<PanFile>()
    fun staredItemAdd(panFile: PanFile) {
        staredItem.add(panFile)
        starCount.value = staredItem.size
    }

    fun staredItemRemove(panFile: PanFile) {
        staredItem.remove(panFile)
        starCount.value = staredItem.size
    }

    fun staredItemExists(panFile: PanFile) = panFile in staredItem

    fun staredItemAddAll(panFiles: List<PanFile>) {
        for (panFile in panFiles) {
            if (!staredItemExists(panFile))
                staredItemAdd(panFile)
        }
    }

    fun staredItemRemoveAll(panFiles: List<PanFile>) {
        for (panFile in panFiles) {
            if (staredItemExists(panFile))
                staredItemRemove(panFile)
        }
    }

    /**
     * starActivity Part
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
    val downloadedItem = ArrayList<PanFile>()
    fun downloadedItemAdd(panFile: PanFile) {
        downloadedItem.add(panFile)
        downloadCount.value = downloadedItem.size
    }

    fun downloadedItemRemove(panFile: PanFile) {
        downloadedItem.remove(panFile)
        downloadCount.value = downloadedItem.size
    }

    fun downloadedItemExists(panFile: PanFile) = panFile in downloadedItem

    fun downloadedItemAddAll(panFiles: List<PanFile>) {
        for (panFile in panFiles) {
            if (!downloadedItemExists(panFile))
                downloadedItemAdd(panFile)
        }
    }

    fun downloadedItemRemoveAll(panFiles: List<PanFile>) {
        for (panFile in panFiles) {
            if (downloadedItemExists(panFile))
                downloadedItemRemove(panFile)
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