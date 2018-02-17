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
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import org.jetbrains.anko.*


open class BaseActivity : AppCompatActivity() {


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
        URL_SHOW = "${URL_HOME}%@/show/%@?device=app"
        URL_LOGIN = URL_HOME + "login"
        URL_FB_LOGIN = URL_HOME + "member/fb"
        URL_REGISTER = URL_HOME + "register"
        URL_FORGETPASSWORD = "$BASE_URL/member/forget_password"
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

    protected fun home(context: Context) {
        val intent : Intent = Intent(context, MainActivity::class.java)
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
}

object Loading {
    fun show(context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val rl = RelativeLayout(context)
        val rl_lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
        rl.layoutParams = rl_lp


        val loadingImg = ProgressBar(context)
        loadingImg.id = R.id.loadingImgID
        val color = ContextCompat.getColor(context, R.color.MY_GREEN)
        loadingImg.indeterminateDrawable.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY)

        val loadingText: TextView = TextView(context)
        loadingText.id = R.id.loadingTextID
        loadingText.text = LOADING

        val p1 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
        val p2 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
        //p1.addRule(RelativeLayout.ABOVE, loadingImg.id)
        p2.addRule(RelativeLayout.BELOW, loadingImg.id)
        val _20: Int = (context.resources.getDimension(R.dimen.loadingTextMarginTop)).toInt()
        p2.setMargins(0, _20, 0, 0)

        rl.addView(loadingImg, p1)
        rl.addView(loadingText, p2)

        val p = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.addContentView(rl, p)
        dialog.show()
        return dialog
    }
}

object Alert {
    fun show(context: Context, title: String, msg: String): AlertDialog {
        val alert = _show(context, title, msg)
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "確定", { Interface, j ->

        })
        alert.show()
        return alert
    }
    fun show(context: Context, title: String, msg: String, ok: ()->Unit): AlertDialog {
        val alert = _show(context, title, msg)
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "確定", { Interface, j ->
            ok()
        })
        alert.show()
        return alert
    }
    private fun _show(context: Context, title: String, msg: String): AlertDialog {
        val alert = AlertDialog.Builder(context).create()
        alert.setTitle(title)
        alert.setMessage(msg)

        return alert
    }
}