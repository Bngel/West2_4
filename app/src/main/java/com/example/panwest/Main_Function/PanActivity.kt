package com.example.panwest.Main_Function

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog.Builder
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.panwest.Adapter.SpaceAdapter
import com.example.panwest.BaseActivity
import com.example.panwest.Data.FileData
import com.example.panwest.Data.getTypeFormat
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.Main_Function.Pan_Function.FileUtils
import com.example.panwest.Main_Function.Pan_Function.PanRepository
import com.example.panwest.R
import kotlinx.android.synthetic.main.activity_pan.*
import kotlinx.android.synthetic.main.item_file.*
import kotlinx.android.synthetic.main.view_meform_main.*
import kotlinx.android.synthetic.main.view_search_space.*
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import java.io.File

class PanActivity : BaseActivity() {
    private val LOGIN_STATE = "login_state"
    private val MUSIC_STRING = "MUSIC"
    private val MOVIE_STRING = "MOVIE"
    private val PHOTO_STRING = "PHOTO"
    private val FILE_STRING = "FILE"
    private val RAR_STRING = "RAR"
    private val FILE_CHOOSE = 0X11
    private val STRING_CHOOSE = "请选择上传的文件"
    private var editStatus = false
    private val EDIT_OPEN = true
    private val EDIT_CLOSE = false

    private var adapter :SpaceAdapter?  = null
    private val panItems = ArrayList<FileData>()
    private val displayItem = ArrayList<FileData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pan)
        initData(PanRepository.current_dir.value!!)
        space_fileList.adapter = adapter
        space_fileList.layoutManager = LinearLayoutManager(this)
        PanRepository.selectedCount.observe(this, Observer { newCount ->
            space_edit_count.text = newCount.toString()
            if (newCount == adapter?.itemCount && newCount != 0) {
                space_edit_all.text = "取消全选"
            } else {
                space_edit_all.text = "全选"
            }
        })
        PanRepository.flushCheck.observe(this, Observer { check ->
            if (check == true) {
                initData(PanRepository.current_dir.value!!)
                PanRepository.flushCheck.value = false
            }
        })
        PanRepository.current_dir.observe(this, Observer { cur ->
            initData(cur)
            PanRepository.flushCheck.value = true
        })
        setClickEvent()
    }

    @SuppressLint("InflateParams")
    private fun setClickEvent() {
        space_filtrate.setOnClickListener {
            val popupView = layoutInflater.inflate(
                R.layout.item_pop_filtrate,
                null, false
            )
            val popupWindow = PopupWindow(
                popupView, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true
            )
            popupWindow.showAsDropDown(space_filtrate, 0, 35)

            val popbtn_photo = popupView.findViewById<Button>(R.id.filtrate_popbutton_photo)
            val popbtn_movie = popupView.findViewById<Button>(R.id.filtrate_popbutton_movie)
            val popbtn_music = popupView.findViewById<Button>(R.id.filtrate_popbutton_music)
            val popbtn_rar = popupView.findViewById<Button>(R.id.filtrate_popbutton_rar)
            val popbtn_file = popupView.findViewById<Button>(R.id.filtrate_popbutton_file)

            popbtn_photo.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                displayItem.addAll(panItems.filter { file ->
                    getTypeFormat(file.type) == PHOTO_STRING
                })
                adapter = SpaceAdapter(displayItem)
                space_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE
            }
            popbtn_movie.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                displayItem.addAll(panItems.filter { file ->
                    getTypeFormat(file.type) == MOVIE_STRING
                })
                adapter = SpaceAdapter(displayItem)
                space_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE

            }
            popbtn_music.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                displayItem.addAll(panItems.filter { file ->
                    getTypeFormat(file.type) == MUSIC_STRING
                })
                adapter = SpaceAdapter(displayItem)
                space_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE
            }
            popbtn_rar.setOnClickListener {
                popupWindow.dismiss()
                displayItem.clear()
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                displayItem.addAll(panItems.filter { file ->
                    getTypeFormat(file.type) == RAR_STRING
                })
                adapter = SpaceAdapter(displayItem)
                space_fileList.adapter = adapter
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE
            }
            popbtn_file.setOnClickListener {
                popupWindow.dismiss()
                initData(PanRepository.current_dir.value!!)
            }
        }
        space_add.setOnClickListener {
            val popupView = layoutInflater.inflate(
                R.layout.item_pop_add,
                null, false
            )
            val popupWindow = PopupWindow(
                popupView, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true
            )
            popupWindow.showAsDropDown(space_add, 0, 35)

            val popbtn_upload = popupView.findViewById<Button>(R.id.add_popbutton_upload)
            val popbtn_dir = popupView.findViewById<Button>(R.id.add_popbutton_dir)

            popbtn_upload.setOnClickListener {
                popupWindow.dismiss()
                uploadFile()
            }
            popbtn_dir.setOnClickListener {
                popupWindow.dismiss()
                showInputDialog()
            }
        }
        space_edit.setOnClickListener {
            if (editStatus == EDIT_CLOSE) {
                Log.d("TEXT_TTT", PanRepository.selectedCount.value.toString())
                editStatus = EDIT_OPEN
                space_bottom_edit.visibility = View.VISIBLE
                if (item_check != null)
                    item_check.visibility = View.VISIBLE
                adapter?.setEditMode(EDIT_OPEN)
                space_fileList.adapter = adapter
            }
            else if (editStatus == EDIT_OPEN) {
                editStatus = EDIT_CLOSE
                space_bottom_edit.visibility = View.GONE
                if (item_check != null)
                    item_check.visibility = View.GONE
                adapter?.setEditMode(EDIT_CLOSE)
                PanRepository.selectedItem.clear()
                PanRepository.selectedCount.value = 0
                space_fileList.adapter = adapter
            }
        }
        space_edit_all.setOnClickListener {
            if (space_edit_all.text == "全选") {
                PanRepository.selectedItemAddAll(displayItem)
                adapter?.notifyDataSetChanged()
            }
            else {
                PanRepository.selectedItemRemoveAll(displayItem)
                adapter?.notifyDataSetChanged()
            }
        }
        space_search_img.setOnClickListener {
            val regex = Regex(space_search_edit.text.toString())
            displayItem.clear()
            PanRepository.selectedItem.clear()
            PanRepository.selectedCount.value = 0
            displayItem.addAll(panItems.filter { file ->
                regex.containsMatchIn(file.filename)
            })
            adapter = SpaceAdapter(displayItem)
            space_fileList.adapter = adapter
            editStatus = EDIT_CLOSE
            space_bottom_edit.visibility = View.GONE
        }
        space_flush.setOnClickListener {
            initData(PanRepository.current_dir.value!!)
        }
        space_edit_download.setOnClickListener {
            for (file in PanRepository.selectedItem)
                if (file.type != "wjj")
                    PanRepository.downloadFile(this,
                        AccountRepository.user?.username?:"",
                        file.url,
                        file.filename)
        }
        space_edit_delete.setOnClickListener {
            for (file in PanRepository.selectedItem)
                if (file.type != "wjj")
                    PanRepository.deleteFile(this,
                        AccountRepository.user?.username?:"",
                        file.url)
        }
    }

    private fun showInputDialog() {
        /*@setView 装入一个EditView
     */
        val editText = EditText(this)
        val inputDialog = Builder(this)
            .setTitle("请输入新建文件夹名称(不得为空)")
            .setView(editText)
        inputDialog.setPositiveButton("确定",
            DialogInterface.OnClickListener { _, _ ->
                val package_name = editText.text.toString()
                if(package_name != "") {
                    PanRepository.createPackage(
                        this,
                        AccountRepository.user?.username ?: "",
                        package_name,
                        PanRepository.current_dir.value!!)
                }
                else {
                    Toast.makeText(this, "文件夹名称不得为空", Toast.LENGTH_SHORT).show()
                }
            }).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                FILE_CHOOSE -> {
                    val uri = data?.data // 待上传的文件链接
                    if (uri != null) {
                        val file = File(FileUtils.getFilePathByUri(this, uri))
                        val requestFile = RequestBody.create(
                            MediaType.parse("multipart/form-data"),
                            file
                        )
                        val part = MultipartBody.Part.createFormData("file", file.name, requestFile)
                        PanRepository.uploadFile(
                            this,
                            part,
                            AccountRepository.user?.username ?: "",
                            PanRepository.current_dir.value!!
                        )
                        Log.d("TEXT_TTT",PanRepository.current_dir.value!!)
                    }
                }
            }
        }
    }

    private fun initData(url: String) {
        displayItem.clear()
        panItems.clear()
        PanRepository.selectedItem.clear()
        PanRepository.selectedCount.value = 0
        loadFileInformation(url)
        displayItem.addAll(panItems)
        /*PanRepository.selectedItemAddAll(panItems)*/
        adapter = SpaceAdapter(panItems)
        space_fileList.adapter = adapter
        editStatus = EDIT_CLOSE
        space_bottom_edit.visibility = View.GONE
    }

    private fun getLoginState(): Pair<Boolean, Pair<String?, String?>> {
        val userInfo = getSharedPreferences(LOGIN_STATE, Context.MODE_PRIVATE)
        val userState = userInfo.getBoolean("STATE", false)
        val userID = userInfo.getString("ID", "")
        val userPswd = userInfo.getString("PSWD", "")
        val user = Pair(userID, userPswd)
        return Pair(userState, user)
    }

    private fun loadFileInformation(url: String){
        val addList = PanRepository.loadFileInformation(
            this,
            AccountRepository.user?.username ?: "",
            url
        )
        if (addList.first) {
            if (addList.second != null){
                panItems.clear()
                Log.d("TEXT_TTT", "文件路径:${url},获取文件数量:${addList.second!!.size}")
                panItems.addAll(addList.second!!)
            }
            else{
                Log.d("TEXT_TTT", "获取文件为空")
                panItems.clear()
            }
        }
        else {
            Toast.makeText(this, "获取文件失败, 请检查网络链接", Toast.LENGTH_SHORT).show()
            Log.d("TEXT_TTT", "获取文件失败, 请检查网络链接")
            panItems.clear()
        }
    }

    private fun uploadFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"//"video/*;image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, FILE_CHOOSE)
    }

    override fun onBackPressed() {
        if(PanRepository.current_dir.value == "/ck/data")
            finish()
        else
            PanRepository.current_dir.value = PanRepository.parent_dir.pop()
    }
}