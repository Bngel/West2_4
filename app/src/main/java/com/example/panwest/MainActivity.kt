package com.example.panwest

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.example.panwest.Data.User
import com.example.panwest.Database.Database.AppDatabase
import com.example.panwest.Login_Function.AccountRepository
import com.example.panwest.Login_Function.LoginActivity
import com.example.panwest.Main_Function.MoreActivity
import com.example.panwest.Main_Function.PanActivity
import com.example.panwest.Main_Function.Pan_Function.PanRepository
import com.example.panwest.Main_Function.SettingActivity
import com.example.panwest.My_Function.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_meform_main.*
import kotlinx.android.synthetic.main.view_my_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class MainActivity : BaseActivity() {

    private val LOGIN_STATE = "login_state"
    private val LOGIN_ACTIVITY = 0x01
    private val MORE_ACTIVITY = 0X02
    private val SETTING_ACTIVITY = 0X03
    private val SPACE_ACTIVITY = 0X04
    private val STAR_ACTIVITY = 0X05
    private val SHARE_ACTIVITY = 0X06
    private val DOWNLOAD_ACTIVITY = 0X07
    private val DELETE_ACTIVITY = 0X08
    private val REQUEST_CODE_GALLERY = 0x10 // 图库选取图片标识请求码
    private val CROP_PHOTO = 0x12 // 裁剪图片标识请求码
    private val STORAGE_PERMISSION = 0x20 // 动态申请存储权限标识
    private var imageFile: File? = null // 声明File对象
    private var imageUri: Uri? = null // 裁剪后的图片uri
    private val path = ""



    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this);// 控件绑定
        // 动态申请存储权限，后面读取文件有用
        requestStoragePermission();
        setClickEvent()
        PanRepository.DOWNLOAD_PATH = applicationContext.filesDir.absolutePath + "/PanWestDownload"
        AccountRepository.PORTRAIT_PATH = applicationContext.filesDir.absolutePath + "/PortraitPic"
        makeDir(PanRepository.DOWNLOAD_PATH!!)
        makeDir(PanRepository.DOWNLOAD_PATH!!)
        clearPortraits()


        if (intent.getBooleanExtra("switch_success", false))
            ActivityCollector.onlyActivity(this)
        defaultLogin()
    }

    private fun clearPortraits() {
        if (AccountRepository.PORTRAIT_PATH != null) {
            val port = File(AccountRepository.PORTRAIT_PATH)
            val ports = port.listFiles()
            if (ports != null && ports.isNotEmpty()) {
                for (file in ports) {
                    if (file.isFile && file.exists()) {
                        file.delete()
                    }
                }
            }
        }
    }

    private fun makeDir(dir: String){
        // I/O logic
        val sciezka = File(dir)
        if (sciezka.mkdirs()) {
            Log.d("TEXT_TTT", dir)
        }
        else {
            Log.d("TEXT_TTT", "existed")
        }
    }

    private fun getLoginState(): Pair<Boolean, Pair<String?, String?>> {
        val userInfo = getSharedPreferences(LOGIN_STATE, Context.MODE_PRIVATE)
        val userState = userInfo.getBoolean("STATE", false)
        val userID = userInfo.getString("ID", "")
        val userPswd = userInfo.getString("PSWD", "")
        val user = Pair(userID, userPswd)
        return Pair(userState, user)
    }

    private fun defaultLogin() {
        val user = getLoginState()
        val userState = user.first
        val userAccount = user.second
        if (userState) {
            val userName = userAccount.first!!
            val userPassword = userAccount.second!!
            Log.d("PORTRAIT_TEXT", "登录时加载头像")
            //AccountRepository.accountGetPhoto(this, userName, me_portraitImage)
            if (userPassword != "" && userName != "") {
                val loginStatus = AccountRepository.accountLogin(userName, userPassword)
                if (AccountRepository.user != null && AccountRepository.status != null && AccountRepository.status!!) {
                    defaultLoad(AccountRepository.user!!)
                }
                else {
                    /*val loginIntent = Intent(this, LoginActivity::class.java)
                    startActivityForResult(loginIntent, LOGIN_ACTIVITY)*/
                    Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show()
                    if (AccountRepository.user != null)
                        defaultLoad(AccountRepository.user!!)
                    else {
                        val PORTRAIT = "portrait.png"
                        val filepath = "${AccountRepository.PORTRAIT_PATH}/${AccountRepository.user?.username}${PORTRAIT}"
                        defaultLoad(User(userAccount.first!!,"","", filepath, 0.0))
                    }
                }
            }
        }
        else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivityForResult(loginIntent, LOGIN_ACTIVITY)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun defaultLoad(user: User) {
        try {
            me_userName.text = user.username
            me_userSpace.text = "%.2fMB/1024MB".format(1024.0 - user.space)
            clearPortraits()
            AccountRepository.accountGetPhoto(this, user.username, me_portraitImage)
            Log.d("PORTRAIT_TEXT", "默认加载头像")
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun setClickEvent() {
        me_portraitImage.setOnClickListener {
            Log.d("PORTRAIT_TEXT", "更换头像 按下")
            gallery()
        }
        main_moreBtn.setOnClickListener {
            val moreIntent = Intent(this, MoreActivity::class.java)
            startActivityForResult(moreIntent, MORE_ACTIVITY)
        }
        main_settingBtn.setOnClickListener {
            val settingIntent = Intent(this, SettingActivity::class.java)
            startActivityForResult(settingIntent, SETTING_ACTIVITY)
        }
        main_spaceBtn.setOnClickListener {
            val spaceIntent = Intent(this, PanActivity::class.java)
            startActivityForResult(spaceIntent, SPACE_ACTIVITY)
        }
        my_star_img.setOnClickListener {
            val starIntent = Intent(this, StarActivity::class.java)
            startActivityForResult(starIntent, STAR_ACTIVITY)
        }
        my_share_img.setOnClickListener {
            val shareIntent = Intent(this, ShareActivity::class.java)
            startActivityForResult(shareIntent, SHARE_ACTIVITY)
        }
        my_download_img.setOnClickListener {
            val downloadIntent = Intent(this, DownloadActivity::class.java)
            startActivityForResult(downloadIntent, DOWNLOAD_ACTIVITY)
        }
        my_delete_img.setOnClickListener {
            val deleteIntent = Intent(this, DeleteActivity::class.java)
            startActivityForResult(deleteIntent, DELETE_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOGIN_ACTIVITY -> if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            } else {
                defaultLogin()
            }
            REQUEST_CODE_GALLERY -> if (resultCode == RESULT_OK) {
                Log.d("PORTRAIT_TEXT", "获取头像返回")
                val uri: Uri = data!!.data!! // 获取图片的uri
                val intent_gallery_crop = Intent("com.android.camera.action.CROP")
                intent_gallery_crop.setDataAndType(uri, "image/*")
                // 设置裁剪
                intent_gallery_crop.putExtra("crop", "true")
                intent_gallery_crop.putExtra("scale", true)
                // aspectX aspectY 是宽高的比例
                intent_gallery_crop.putExtra("aspectX", 1)
                intent_gallery_crop.putExtra("aspectY", 1)
                // outputX outputY 是裁剪图片宽高
                intent_gallery_crop.putExtra("outputX", 400)
                intent_gallery_crop.putExtra("outputY", 400)
                intent_gallery_crop.putExtra("return-data", false)
                // 创建文件保存裁剪的图片
                createImageFile()
                Log.d("PORTRAIT_TEXT", "裁剪头像")
                imageUri = Uri.fromFile(imageFile)
                if (imageUri != null) {
                    intent_gallery_crop.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    intent_gallery_crop.putExtra(
                        "outputFormat",
                        Bitmap.CompressFormat.JPEG.toString()
                    )
                }
                startActivityForResult(intent_gallery_crop, CROP_PHOTO)
            }
            CROP_PHOTO -> if (resultCode == RESULT_OK) {
                try {
                    if (imageUri != null)
                        Log.d("TAG", imageUri.toString())
                    displayImage(imageUri!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            SPACE_ACTIVITY -> {
                try {
                    val user = getLoginState()
                    if (user.first) {
                        val userAccount = user.second.first!!
                        val userPassword = user.second.second!!
                        AccountRepository.accountLogin(userAccount, userPassword)
                        // Log.d("TEXT_TTT", AccountRepository.user.toString() + "\n" + AccountRepository.status.toString())
                        if (AccountRepository.user != null && AccountRepository.status != null && AccountRepository.status!!) {
                            val userInfo = getSharedPreferences(LOGIN_STATE, Context.MODE_PRIVATE).edit()
                            userInfo.apply {
                                putBoolean("STATE", true)
                                putString("ID", userAccount)
                                putString("PSWD", userPassword)
                                apply()
                            }
                            //defaultLoad(AccountRepository.user!!)
                        }
                    }
                } catch (e: Exception) {

                }
            }
            SETTING_ACTIVITY -> {
                try {
                    val user = getLoginState()
                    if (user.first) {
                        val userAccount = user.second.first!!
                        val userPassword = user.second.second!!
                        AccountRepository.accountLogin(userAccount, userPassword)
                        // Log.d("TEXT_TTT", AccountRepository.user.toString() + "\n" + AccountRepository.status.toString())
                        if (AccountRepository.user != null && AccountRepository.status != null && AccountRepository.status!!) {
                            val userInfo = getSharedPreferences(LOGIN_STATE, Context.MODE_PRIVATE).edit()
                            userInfo.apply {
                                putBoolean("STATE", true)
                                putString("ID", userAccount)
                                putString("PSWD", userPassword)
                                apply()
                            }
                            //defaultLoad(AccountRepository.user!!)
                        }
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // 以startActivityForResult的方式启动一个activity用来获取返回的结果
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    /**
     * Android6.0后需要动态申请危险权限
     * 动态申请存储权限
     */
    private fun requestStoragePermission() {
        val hasCameraPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        Log.e("TAG", "开始$hasCameraPermission")
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            // 拥有权限，可以执行涉及到存储权限的操作
            Log.e("TAG", "你已经授权了该组权限")
        } else {
            // 没有权限，向用户申请该权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("TAG", "向用户申请该组权限")
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION
                )
            }
        }
    }

    /**
     * 动态申请权限的结果回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户同意，执行相应操作
                Log.e("TAG", "用户已经同意了存储权限")
            } else {
                ActivityCollector.finishAll()
                // 用户不同意，向用户展示该权限作用
            }
        }
    }

    /**
     * 创建File保存图片
     */
    private fun createImageFile() {
        try {
            if (imageFile != null && imageFile!!.exists()) {
                imageFile!!.delete()
            }
            // 新建文件
            val PORTRAIT = "portrait.png"
            val filepath = "${AccountRepository.PORTRAIT_PATH}/${AccountRepository.user?.username}${PORTRAIT}"
            imageFile = File(
                getExternalFilesDir(null),
                System.currentTimeMillis().toString() + PORTRAIT
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 显示图片
     * @param imageUri 图片的uri
     */
    private fun displayImage(imageUri: Uri) {
        Log.d("PORTRAIT_TEXT", "显示头像")
        val file = File(imageUri.path)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val part = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val result = AccountRepository.accountPhoto(this, part, AccountRepository.user?.username?:"")
        if (result != null && result.status == "success"){
            Log.d("PORTRAIT_TEXT", "加载头像成功")
            Glide.with(this)
                .load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.me_loading) // 占位图设置：加载过程中显示的图片
                .error(R.drawable.me_error) // 异常占位图
                .centerCrop()
                .into(me_portraitImage)
        }
        else {
            Log.d("PORTRAIT_TEXT", "加载头像失败")
            val PORTRAIT = "portrait.png"
            val filepath = "${AccountRepository.PORTRAIT_PATH}/${AccountRepository.user?.username}${PORTRAIT}"
            val portraitImg = File(filepath)
            if (portraitImg.exists()) {
                Glide.with(this)
                    .load(filepath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.me_loading) // 占位图设置：加载过程中显示的图片
                    .error(R.drawable.me_error) // 异常占位图
                    .centerCrop()
                    .into(me_portraitImage)
            }
        }
    }
}