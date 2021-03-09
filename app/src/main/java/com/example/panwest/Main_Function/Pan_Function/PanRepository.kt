package com.example.panwest.Main_Function.Pan_Function

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Delete
import com.example.panwest.Data.FileData
import com.example.panwest.Data.Json.CreatePackageJson
import com.example.panwest.Data.Json.DeleteFileJson
import com.example.panwest.Data.Json.DeletePackageJson
import com.example.panwest.Data.Json.UploadFileJson
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.WebService_Function.WebService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

object PanRepository {
    var current_dir = MutableLiveData<String>("/ck/data")
    var parent_dir = Stack<String>()
    val flushCheck = MutableLiveData<Boolean>(false)
    private val panService = WebService.create()

    val selectedCount = MutableLiveData(0)

    val selectedItem = ArrayList<FileData>()

    fun selectedItemAdd(fileData: FileData) {
        selectedItem.add(fileData)
        selectedCount.value = selectedItem.size
    }

    fun selectedItemRemove(fileData: FileData) {
        selectedItem.remove(fileData)
        selectedCount.value = selectedItem.size
    }

    fun selectedItemExists(FileData: FileData) = FileData in selectedItem

    fun selectedItemAddAll(fileData: List<FileData>) {
        for (file in fileData) {
            if (!selectedItemExists(file))
                selectedItemAdd(file)
        }
    }

    fun selectedItemRemoveAll(fileData: List<FileData>) {
        for (file in fileData) {
            if (selectedItemExists(file))
                selectedItemRemove(file)
        }
    }

    fun loadFileInformation(username: String, parentFile: String): List<FileData>? {
        val fileInfo = panService.getFileInformation(username, parentFile)
        var res: List<FileData>? = null
        thread {
            try {
                val body = fileInfo.execute().body()
                res = body.file_data_list
            } catch (e: SocketTimeoutException) {
            }
        }.join(2000)
        flushCheck.value = true
        return res
    }

    fun uploadFile(context: Context, file: MultipartBody.Part, username: String, parentFile: String) {
        val upload = panService.uploadFile(file, username, parentFile)
        upload.enqueue(object : Callback<UploadFileJson> {
            override fun onResponse(
                call: Call<UploadFileJson>?,
                response: Response<UploadFileJson>?
            ) {
                val body = response?.body()
                if (body != null) {
                    if (body.status == "success") {
                        Toast.makeText(context, "上传成功, 用时${body.costTime}s", Toast.LENGTH_SHORT).show()
                        flushCheck.value = true
                    }
                    else {
                        Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<UploadFileJson>?, t: Throwable?) {
                Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun deleteFile(context: Context, username: String, url: String){
        val delete = panService.deleteFile(username, url)
        delete.enqueue(object : Callback<DeleteFileJson> {
            override fun onResponse(
                call: Call<DeleteFileJson>?,
                response: Response<DeleteFileJson>?
            ) {
                val body = response?.body()
                Log.d("TEXT_TTTTT", body?.status.toString())
                if (body != null) {
                    if (body.status == "success") {
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show()
                        AccountRepository.accountLogin(AccountRepository.user?.username?:"", AccountRepository.user?.password?:"")
                        flushCheck.value = true
                    }
                    else {
                        Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<DeleteFileJson>?, t: Throwable?) {
                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun deletePackage(context: Context, username: String, url: String){
        val delete = panService.deletePackage(username, url)
        delete.enqueue(object : Callback<DeletePackageJson> {
            override fun onResponse(
                call: Call<DeletePackageJson>?,
                response: Response<DeletePackageJson>?
            ) {
                val body = response?.body()
                Log.d("TEXT_TTTTT", body?.status.toString())
                if (body != null) {
                    if (body.status == "success") {
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show()
                        AccountRepository.accountLogin(AccountRepository.user?.username?:"", AccountRepository.user?.password?:"")
                        flushCheck.value = true
                    }
                    else {
                        Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<DeletePackageJson>?, t: Throwable?) {
                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun createPackage(context: Context, username: String, package_name: String, parentFile: String){
        val delete = panService.createPackage(username, package_name, parentFile)
        delete.enqueue(object : Callback<CreatePackageJson> {
            override fun onResponse(
                call: Call<CreatePackageJson>?,
                response: Response<CreatePackageJson>?
            ) {
                val body = response?.body()
                Log.d("TEXT_TTTT", body?.status.toString())
                if (body != null) {
                    if (body.status == "success") {
                        Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show()
                        AccountRepository.accountLogin(AccountRepository.user?.username?:"", AccountRepository.user?.password?:"")
                        flushCheck.value = true
                    }
                    else {
                        Toast.makeText(context, "创建失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<CreatePackageJson>?, t: Throwable?) {
                Toast.makeText(context, "创建失败", Toast.LENGTH_SHORT).show()
            }

        })
    }
}