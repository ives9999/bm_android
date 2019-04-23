package com.sportpassword.bm.Utilities

import android.content.SharedPreferences
import com.sportpassword.bm.App

object Session {
    private val UD: SharedPreferences = App.instance.getSharedPreferences(SESSION_FILENAME, 0)

    val loginResetKey = "login_reset"

    var loginReset: Boolean
        get() = UD.getBoolean(loginResetKey, false)
        set(value) = UD.edit().putBoolean(loginResetKey, value).apply()

    fun clear(key: String) {
        if (exist(key)) {
            UD.edit().remove(key).apply()
        }
    }

    fun exist(key: String): Boolean {
        var isExist = false
        if (UD.contains(key)) {
            isExist = true
        }

        return isExist
    }
}