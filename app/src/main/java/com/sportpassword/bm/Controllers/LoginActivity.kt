package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.NOTIF_MEMBER_DID_CHANGE
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.util.*

class LoginActivity : BaseActivity() {

    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginEmailTxt.setText("ives@housetube.tw")
        loginPasswordTxt.setText("K5SD23r6")
        //setSupportActionBar(toolbar)
        //supportActionBar!!.setTitle(resources.getText(R.string.login))

        //supportActionBar!!.setDisplayShowTitleEnabled(false)
        //setTitle(resources.getText(R.string.login))

        //val t: Toolbar = find<Toolbar>(R.id.toolbar)
        //val v: TextView = findViewById(R.id.toolbar_title) as TextView
        val l = LayoutInflater.from(this).inflate(R.layout.activity_main, null, false)
        //val toolbar: Toolbar = l.findViewById<Toolbar>(R.id.toolbar)
        //println(toolbar)
        val toolbar_title: TextView = l.findViewById<TextView>(R.id.toolbar_title)
        toolbar_title.text = "登入"
        supportActionBar!!.setCustomView(toolbar_title)
    }

    fun loginSubmit(view: View) {
        val loading = Loading.show(this)
        val email = loginEmailTxt.text.toString()
        if (email.isEmpty()) {
            Alert.show(this, "警告", "EMail沒填")
        }
        val password = loginPasswordTxt.text.toString()
        if (password.isEmpty()) {
            Alert.show(this, "警告", "密碼沒填")
        }

        MemberService.login(this, email, password) { success ->
            loading.dismiss()
            //println(success)
            if (success) {
                if (MemberService.success) {
                    val memberDidChange = Intent(NOTIF_MEMBER_DID_CHANGE)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChange)
                    finish()
                } else {
                    Alert.show(this, "警告", MemberService.msg)
                }
            } else {
                Alert.show(this, "警告", MemberService.msg)
            }
        }
    }

    fun loginFBSubmit(view: View) {
        val context = this
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create()
        //LoginManager.getInstance().logOut()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email,public_profile,user_birthday"))
        LoginManager.getInstance().registerCallback(callbackManager,
             object: FacebookCallback<LoginResult> {
                 override fun onSuccess(result: LoginResult?) {
                     MemberService.FBLogin(context) {success ->
                         val memberDidChange = Intent(NOTIF_MEMBER_DID_CHANGE)
                         LocalBroadcastManager.getInstance(context).sendBroadcast(memberDidChange)
                         finish()
                     }
                 }

                 override fun onCancel() {
                    println("cancel")
                 }

                 override fun onError(error: FacebookException?) {
                     println(error)

                 }
             })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun loginForgetPassword(view: View) {
        goForgetPassword()
    }

    fun loginRegister(view: View) {
        goRegister()
    }
}
