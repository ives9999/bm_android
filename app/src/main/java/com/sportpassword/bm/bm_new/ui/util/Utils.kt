package com.sportpassword.bm.bm_new.ui.util

import android.annotation.SuppressLint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date

//是否爲可以報名的時間
@SuppressLint("SimpleDateFormat")
fun canSignUp(signupStart: String, signupEnd: String): Boolean {
    val currentTime = Date()
    Timber.d("currentTime ->$currentTime")
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val startTime = dateTimeFormat.parse(signupStart)
    val stopTime = dateTimeFormat.parse(signupEnd)
    return currentTime.before(stopTime) && currentTime.after(startTime)
}