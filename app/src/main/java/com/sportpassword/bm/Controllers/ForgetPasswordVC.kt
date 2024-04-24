package com.sportpassword.bm.Controllers

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Views.MainEditText2
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.Views.SubmitDelegate
import com.sportpassword.bm.Views.SubmitOnly
import com.sportpassword.bm.databinding.ActivityForgetPasswordBinding
import com.sportpassword.bm.databinding.MytablevcBinding
import com.sportpassword.bm.extensions.Alert

class ForgetPasswordVC : BaseActivity(), SubmitDelegate {

    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var view: ViewGroup

    var showTop2: ShowTop2? = null
    var emailET2: MainEditText2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        //setMyTitle("忘記密碼")
        hidekeyboard(binding.root)
        //forgetPasswordEmailTxt.setText("ives@housetube.tw")

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            it.setTitle("忘記密碼")
            it.showPrev(true)
        }

        findViewById<MainEditText2>(R.id.emailET2) ?. let {
            emailET2 = it
            it.requestFocus()
        }

        findViewById<SubmitOnly>(R.id.submitSO) ?. let {
            it.delegate = this
        }

        //binding.forgetPasswordEmailTxt.requestFocus()

        //init()
    }

//    override fun init() {
//        isPrevIconShow = true
//        super.init()
//    }

    override fun submit2() {
        val email: String = emailET2!!.text
        if (email.isEmpty()) {
            Alert.show(this, "警告", "EMail沒填")
            return
        }

        loadingAnimation.start()
        MemberService.forgetPassword(this, email) { success ->
            runOnUiThread {
                loadingAnimation.stop()
            }
            if (success) {

                var t: SuccessTable? = null
                try {
                    t = Gson().fromJson<SuccessTable>(MemberService.jsonString, SuccessTable::class.java)
                } catch (e: JsonParseException) {
                    runOnUiThread {
                        warning(e.localizedMessage!!)
                    }
                }

                if (t != null) {
                    if (t.success) {
                        runOnUiThread {
                            info(t.msg, "", "關閉") {
                                toLogin()
                            }
                        }
                    } else {
                        runOnUiThread {
                            warning(t.msg)
                        }
                    }
                } else {
                    runOnUiThread {
                        warning("解析回傳值失敗")
                    }
                }

//                if (MemberService.success) {
//                    Alert.show(this, "訊息", MemberService.msg)
//                } else {
//                    Alert.show(this, "警告", MemberService.msg)
//                }
            } else {
                Alert.show(this, "警告", MemberService.msg)
            }
        }
    }
}
