package com.example.panwest.Main_Function.Pan_Function

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.panwest.Data.PanFile

object PanRepository {

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
}