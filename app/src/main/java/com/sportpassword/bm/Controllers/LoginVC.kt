package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.MainEditText2
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.Views.SubmitDelegate
import com.sportpassword.bm.Views.SubmitOnly
import com.sportpassword.bm.databinding.ActivityLoginBinding
import com.sportpassword.bm.databinding.MytablevcBinding
import com.sportpassword.bm.extensions.Alert
import com.sportpassword.bm.functions.jsonToModel

class LoginVC : BaseActivity(), SubmitDelegate {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var view: ViewGroup

    var showTop2: ShowTop2? = null

    var table: MemberTable? = null

    var emailET2: MainEditText2? = null
    var passwordET2: MainEditText2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)
        //setMyTitle("登入")
        hidekeyboard(binding.root)

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            it.setTitle("登入")
            it.showPrev(true)
        }

        findViewById<MainEditText2>(R.id.emailET2) ?. let {
            emailET2 = it
        }

        findViewById<MainEditText2>(R.id.passwordET2) ?. let {
            passwordET2 = it
        }

        findViewById<TextView>(R.id.forgetPasswordTV) ?. let {
            it.setOnClickListener {
                toForgetPassword()
            }
        }

        findViewById<TextView>(R.id.registerTV) ?. let {
            it.setOnClickListener {
                toRegister()
            }
        }

        findViewById<SubmitOnly>(R.id.submitSO) ?. let {
            it.delegate = this
        }

        //init()
        //loginEmailTxt.requestFocus()

//        loginEmailTxt.setText("ives@housetube.tw")
//        loginPasswordTxt.setText("K5SD23r6")

    }

//    override fun init() {
//        isPrevIconShow = true
//        super.init()
//
//        val dp: Int = 300
//        val width: Int = dp.dpToPx(this)
//        val margin: Int = (screenWidth - width) / 2
//    }

    override fun submit2() {

        _hideKeyboard(binding.root)
        val email: String = emailET2?.text!!
        if (email.isEmpty()) {
            Alert.show(this, "警告", "Email沒填")
            return
        }
        val password = passwordET2?.text!!
        if (password.isEmpty()) {
            Alert.show(this, "警告", "密碼沒填")
            return
        }

        //val playerID = _getPlayerID();
        //println(playerID)

        loadingAnimation.start()
        MemberService.login(this, email, password) { success ->
            runOnUiThread {
                loadingAnimation.stop()
            }
            //println(success)
            if (success) {
                //println(MemberService.jsonString)
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
                        finishAffinity()
                        toMember()
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


