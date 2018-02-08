package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sportpassword.bm.Models.MEMBER_ROLE
import com.sportpassword.bm.Models.Member
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlin.reflect.full.memberProperties

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _setURLConstants()

        //member.reset()
        member.print()
        //printMember()
    }

//    protected fun printMember() {
//        for (a in Member::class.memberProperties) {
//            println("${a.name} = ${a.get(member)}")
//        }
//    }

    private fun _setURLConstants() {
        //gSimulate = isEmulator()
        BASE_URL = if (gSimulate) LOCALHOST_BASE_URL else REMOTE_BASE_URL
        //println("os: " + BASE_URL)
        URL_HOME = BASE_URL + "/app/"
        URL_LIST = "${URL_HOME}%@"
        URL_SHOW = "${URL_HOME}%@/show/%@?device=app"
        URL_LOGIN = URL_HOME + "login"
        URL_FB_LOGIN = URL_HOME + "member/fb"
        URL_REGISTER = URL_HOME + "register"
        URL_MEMBER_UPDATE = URL_HOME + "member/update"
        URL_CITYS = URL_HOME + "citys"
        URL_ARENA_BY_CITY_ID = URL_HOME + "arena_by_city"
        URL_TEAM_UPDATE = URL_HOME + "team/update"
        URL_ONE = "${URL_HOME}%@/one"
        URL_TEAM = URL_HOME + "team/"
        URL_TEAM_TEMP_PLAY = URL_TEAM + "tempPlay/onoff"
        URL_TEAM_TEMP_PLAY_LIST = URL_TEAM + "tempPlay/list"
        URL_TEAM_PLUSONE = BASE_URL + "/team/tempPlay/plusOne/"
    }

    protected fun goLogin() {
        val loginIntent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    protected fun goRegister() {
        val registerIntent: Intent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }

    protected fun goForgetPassword() {
        val forgetPasswordIntent: Intent = Intent(this, ForgetPasswordActivity::class.java)
        startActivity(forgetPasswordIntent)
    }

    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }
}
