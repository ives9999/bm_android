package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Fragments.MemberFragment
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_member_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlin.reflect.full.memberProperties


class LoginVC : BaseActivity() {

    var table: MemberTable? = null

    var memberVC: MemberFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setMyTitle("登入")
        hidekeyboard(login_layout)

        init()
        //loginEmailTxt.requestFocus()

//        loginEmailTxt.setText("ives@housetube.tw")
//        loginPasswordTxt.setText("K5SD23r6")

    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun loginSubmit(view: View) {

        _hideKeyboard(login_layout)
        Loading.show(mask)
        val email = loginEmailTxt.text.toString()
        if (email.isEmpty()) {
            Alert.show(this, "警告", "EMail沒填")
        }
        val password = loginPasswordTxt.text.toString()
        if (password.isEmpty()) {
            Alert.show(this, "警告", "密碼沒填")
        }

        val playerID = _getPlayerID();
        //println(playerID)

        MemberService.login(this, email, password, playerID) { success ->
            runOnUiThread {
                Loading.hide(mask)
            }
            //println(success)
            if (success) {
//                println(MemberService.jsonString)
                var t: SuccessTable? = null
                try {
                    t = Gson().fromJson<SuccessTable>(MemberService.jsonString, SuccessTable::class.java)
                } catch (e: JsonParseException) {
                    runOnUiThread {
                        warning(e.localizedMessage!!)
                    }
                }
                var t_success: Boolean = false
                var t_msg: String = ""
                if (t != null) {
                    t_success = t.success
                    t_msg = t.msg
                }
                if (!t_success) {
                    runOnUiThread {
                        warning(t_msg)
                    }
                } else {
                    table = jsonToModel<MemberTable>(MemberService.jsonString)
                    if (table != null) {
                        table!!.toSession(this, true)

//                        session.dump()
                        //val a = member.isLoggedIn
                        //val b = member.name
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        runOnUiThread {
                            warning(MemberService.msg)
                        }
                    }
                }
                //if (MemberService.success) {
                    //LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChangeIntent)
                    //finish()
                    //val token = member.token
                    //if (token != null) {
                        //_getMemberOne(token) {
                            //finish()
                        //}
                    //}
                //} else {
                    //Alert.show(this, "警告", MemberService.msg)
                //}
            } else {
                //Alert.show(this, "警告", MemberService.msg)
                warning(MemberService.msg)
            }
        }
    }

//    fun loginFBSubmit(view: View) {
//        loginFB()
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        //callbackManager?.onActivityResult(requestCode, resultCode, data)
//    }

    fun loginForgetPassword(view: View) {
        toForgetPassword()
    }

    fun loginRegister(view: View) {
        toRegister()
    }
}


