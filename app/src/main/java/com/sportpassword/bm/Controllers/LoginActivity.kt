package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.gson.JsonParseException
import com.sportpassword.bm.Fragments.MemberFragment
import com.sportpassword.bm.Models.ArenaTable
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.mask.*
import org.json.JSONObject
import java.util.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties

class LoginActivity : BaseActivity() {

    var table: MemberTable? = null

    var memberVC: MemberFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setMyTitle("登入")
        hidekeyboard(login_layout)
        //loginEmailTxt.requestFocus()

        loginEmailTxt.setText("ives@housetube.tw")
        loginPasswordTxt.setText("K5SD23r6")

    }

    fun loginSubmit(view: View) {
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
            Loading.hide(mask)
            //println(success)
            if (success) {
                try {
                    //println(MemberService.jsonString)
                    table = jsonToModel<MemberTable>(MemberService.jsonString)
                } catch (e: JsonParseException) {
                    warning(e.localizedMessage!!)
                }
                if (table != null) {
                    table!!.filterRow()
                    table!!.isLoggedIn = true
                    //table!!.printRow()
                    table!!.toSession(this)
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    warning(MemberService.msg)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun loginForgetPassword(view: View) {
        toForgetPassword()
    }

    fun loginRegister(view: View) {
        toRegister()
    }
}
