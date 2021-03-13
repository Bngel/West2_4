package com.example.panwest.Main_Function.Pan_Function

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.panwest.Data.FileData
import com.example.panwest.Data.Json.*
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.WebService_Function.WebService
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


object PanRepository {
    var DOWNLOAD_PATH :String? = null
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

    fun loadFileInformation(context: Context, username: String, parentFile: String): Pair<Boolean, List<FileData>?> {
        var res: List<FileData>? = null
        var success = true
        var fileInfo: Call<LoadFilesJson>? = null
        try {
            fileInfo = panService.getFileInformation(username, parentFile, AccountRepository.token!!)
        } catch (e: Exception) {
            success = false
        }
        if (success) {
            thread {
                try {
                    val body = fileInfo?.execute()?.body()
                    when (body?.getFileInformationStatus) {
                        "success" -> {
                            res = body.file_data_list
                            success = true
                        }
                        "ParentFileWrong" -> {
                            success = false
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    success = false
                }
            }.join(2000)
        }
        if (success)
            flushCheck.value = true
        return Pair<Boolean, List<FileData>?>(success,res)
    }

    fun uploadFile(context: Context, file: MultipartBody.Part, username: String, parentFile: String) {
        //Log.d("ERROR_FILE",AccountRepository.token?:"")
        val upload = panService.uploadFile(file, username, parentFile)
        upload.enqueue(object : Callback<UploadFileJson> {
            override fun onResponse(
                call: Call<UploadFileJson>?,
                response: Response<UploadFileJson>?
            ) {
                val body = response?.body()
                if (body != null) {
                    when (body.status) {
                        "success" -> {
                            Toast.makeText(context, "上传成功, 用时${body.costTime}ms", Toast.LENGTH_SHORT).show()
                            flushCheck.value = true
                        }
                        "EmptyWrong" -> {
                            Toast.makeText(context, "文件为空", Toast.LENGTH_SHORT).show()
                        }
                        "FullOrUserFWrong" -> {
                            Toast.makeText(context, "用户云盘空间已满或用户不存在", Toast.LENGTH_SHORT).show()
                        }
                        "FileNameWrong" -> {
                            Toast.makeText(context, "文件名为空", Toast.LENGTH_SHORT).show()
                        }
                        "FileTypeWrong" -> {
                            Toast.makeText(context, "文件类型不支持", Toast.LENGTH_SHORT).show()
                        }
                        "InternetWrong" -> {
                            Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(context, "上传异常", Toast.LENGTH_SHORT).show()
                            // Toast.makeText(context, body.status, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<UploadFileJson>?, t: Throwable?) {
                //Toast.makeText(context, "上传异常", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun deleteFile(context: Context, username: String, url: String){
        val delete = panService.deleteFile(username, url, AccountRepository.token!!)
        delete.enqueue(object : Callback<DeleteFileJson> {
            override fun onResponse(
                call: Call<DeleteFileJson>?,
                response: Response<DeleteFileJson>?
            ) {
                val body = response?.body()
                if (body != null) {
                    when (body.status) {
                        "success" -> {
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show()
                            flushCheck.value = true
                        }
                        "UrlWrong" -> {
                            Toast.makeText(context, "url不匹配", Toast.LENGTH_SHORT).show()
                        }
                        "DeleteWrong" -> {
                            Toast.makeText(context, "服务器删除文件内部错误", Toast.LENGTH_SHORT).show()
                        }
                        "FileWrong" -> {
                            Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show()
                        }
                        "TokenWrong" -> {
                            Toast.makeText(context, "登录时间过长, Token已失效, 请重新登录", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DeleteFileJson>?, t: Throwable?) {
                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun deletePackage(context: Context, username: String, url: String){
        val delete = panService.deletePackage(username, url, AccountRepository.token!!)
        delete.enqueue(object : Callback<DeletePackageJson> {
            override fun onResponse(
                call: Call<DeletePackageJson>?,
                response: Response<DeletePackageJson>?
            ) {
                val body = response?.body()
                Log.d("TEXT_TTTTT", body?.status.toString())
                if (body != null) {
                    when (body.status) {
                        "success" -> {
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show()
                            flushCheck.value = true
                        }
                        "TokenWrong" -> {
                            Toast.makeText(context, "登录时间过长, Token已失效, 请重新登录", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DeletePackageJson>?, t: Throwable?) {
                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun createPackage(context: Context, username: String, package_name: String, parentFile: String){
        val delete = panService.createPackage(username, package_name, parentFile, AccountRepository.token!!)
        delete.enqueue(object : Callback<CreatePackageJson> {
            override fun onResponse(
                call: Call<CreatePackageJson>?,
                response: Response<CreatePackageJson>?
            ) {
                val body = response?.body()
                if (body != null) {
                    when(body.status) {
                         "success" -> {
                            Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show()
                            flushCheck.value = true
                         }
                        "TokenWrong" -> {
                            Toast.makeText(context, "登录时间过长, Token已失效, 请重新登录", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(context, "创建失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CreatePackageJson>?, t: Throwable?) {
                Toast.makeText(context, "创建失败", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun downloadFile(context: Context, username: String, url: String, filename: String) {
        val download = panService.downloadFile(username, url, AccountRepository.token!!)
        download.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                val body = response?.body()
                if (body != null) {
                    var fe = filename
                    var i = 1
                    val hp = filename.split('.')
                    while (File("$DOWNLOAD_PATH/$fe").exists()) {
                        fe = "${hp[0]}(${i}).${hp[1]}"
                        i += 1
                    }
                    writeResponseBodyToDisk(body,  "$DOWNLOAD_PATH/$fe")
                    Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun changeFilename(context: Context, username: String, newFilename: String, url: String) {
        val change = panService.changeFilename(username, newFilename, url, AccountRepository.token!!)
        change.enqueue(object : Callback<ChangeFileNameJson> {
            override fun onResponse(
                call: Call<ChangeFileNameJson>?,
                response: Response<ChangeFileNameJson>?
            ) {
                val body = response?.body()
                if (body != null) {
                    when (body.status) {
                        "success" -> {
                            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
                            flushCheck.value = true
                        }
                        "UrlWrong" -> {
                            Toast.makeText(context, "文件路径错误", Toast.LENGTH_SHORT).show()
                        }
                        "UserWrong" -> {
                            Toast.makeText(context, "文件不属于该用户", Toast.LENGTH_SHORT).show()
                        }
                        "RepeatWrong" -> {
                            Toast.makeText(context, "文件名重复", Toast.LENGTH_SHORT).show()
                        }
                        "TokenWrong" -> {
                            Toast.makeText(context, "登录时间过长, Token已失效, 请重新登录", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ChangeFileNameJson>?, t: Throwable?) {
                Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show()
            }
        })
    }



    fun writeResponseBodyToDisk(body: ResponseBody, filepath: String): Boolean {
        return try {
            val futureStudioIconFile =
                File(filepath)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096000)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    //Log.d("DOWNLOAD", "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }
}