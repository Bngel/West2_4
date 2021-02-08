package com.example.panwest

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val LOGIN_STATE = "login_state"
    private val LOGIN_ACTIVITY = 1

    private fun getLoginState(): Pair<Boolean, Pair<String?, String?>> {
        val userInfo = getSharedPreferences(LOGIN_STATE, Context.MODE_PRIVATE)
        val userState = userInfo.getBoolean("STATE", false)
        val userID = userInfo.getString("ID", "")
        val userPswd = userInfo.getString("PSWD", "")
        val user = Pair(userID, userPswd)
        return Pair(userState, user)
    }

    private fun login(id: String, pswd: String) {
        TODO("default user's info to login")
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = getLoginState()
        val userState = user.first
        val userAccount = user.second
        if (userState) {
            login(userAccount.first?:"", userAccount.second?:"")
        }
        else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, LOGIN_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            } else {
                TODO("login successfully")
            }
        }
    }
}