package com.sportpassword.bm

import android.app.Application
import com.sportpassword.bm.Models.Member


/**
 * Created by ives on 2018/2/6.
 */

val member: Member by lazy {
    App.member!!
}
class App: Application() {

    companion object {
        var member: Member? = null
    }

    override fun onCreate() {
        member = Member(applicationContext)
        super.onCreate()
    }
}