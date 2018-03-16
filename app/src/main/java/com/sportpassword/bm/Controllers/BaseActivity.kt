package com.sportpassword.bm.Controllers

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IInterface
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.Adapters.SignupsAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_temp_play.*
import kotlinx.android.synthetic.main.tab.*
import org.jetbrains.anko.*


open class BaseActivity : AppCompatActivity() {

    protected lateinit var refreshLayout: SwipeRefreshLayout
    protected lateinit var refreshListener: SwipeRefreshLayout.OnRefreshListener

    lateinit var data: Map<String, Map<String, Any>>
    lateinit var signupsAdapter: SignupsAdapter
    lateinit var name: String
    lateinit var memberToken: String
    lateinit var nearDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _setURLConstants()

        //member.reset()
        //member.print()
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
        URL_LIST = "${URL_HOME}%s"
        URL_SHOW = "${URL_HOME}%s/show/%s?device=app"
        URL_LOGIN = URL_HOME + "login"
        URL_FB_LOGIN = URL_HOME + "member/fb"
        URL_REGISTER = URL_HOME + "register"
        URL_FORGETPASSWORD = "$BASE_URL/member/forget_password"
        URL_CHANGE_PASSWORD = "$BASE_URL/member/change_password"
        URL_MEMBER_UPDATE = URL_HOME + "member/update"
        URL_CITYS = URL_HOME + "citys"
        URL_ARENA_BY_CITY_ID = URL_HOME + "arena_by_city"
        URL_TEAM_UPDATE = URL_HOME + "team/update"
        URL_ONE = "${URL_HOME}%s/one"
        URL_TEAM = URL_HOME + "team/"
        URL_TEAM_TEMP_PLAY = URL_TEAM + "tempPlay/onoff"
        URL_TEAM_TEMP_PLAY_LIST = URL_TEAM + "tempPlay/list"
        URL_TEAM_PLUSONE = BASE_URL + "/team/tempPlay/plusOne/"
        URL_TEAM_CANCELPLUSONE = BASE_URL + "/team/tempPlay/cancelPlusOne/"
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

    protected fun home(context: Context) {
        val intent : Intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    protected fun goEditTeam(token: String="") {
        val intent = Intent(this, EditTeamActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    protected fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
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

    fun TextView.setMyText(value: String, default: String="") {
        if (value.isEmpty()) text = default else text = value
    }

    open protected fun setRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            refresh()
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }
    open protected fun closeRefresh() {
        refreshLayout.isRefreshing = false
    }
    open protected fun refresh() {}

    open protected fun setTeamData(imageView: ImageView?=null) {
    }
}
