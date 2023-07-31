package com.sportpassword.bm.bm_new.ui.util

import android.annotation.SuppressLint
import com.sportpassword.bm.R
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date

//是否可以修改報名
@SuppressLint("SimpleDateFormat")
fun canEditSignUp(signupEnd: String): Int? {
    val currentTime = Date()
    Timber.d("currentTime ->$currentTime")
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val stopTime = dateTimeFormat.parse(signupEnd)
    return when {
        currentTime.after(stopTime) -> R.string.match_sign_up_stop_modify
        else -> null
    }
}

//是否爲可以報名的時間
@SuppressLint("SimpleDateFormat")
fun canSignUp(signupStart: String, signupEnd: String, groupFull: Boolean): Int? {
    val currentTime = Date()
    Timber.d("currentTime ->$currentTime")
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val startTime = dateTimeFormat.parse(signupStart)
    val stopTime = dateTimeFormat.parse(signupEnd)
    return when {
        currentTime.after(stopTime) -> R.string.match_sign_up_finish
        currentTime.before(startTime) -> R.string.match_sign_up_not_start
        groupFull -> R.string.match_sign_up_group_limited
        else -> null
    }
}