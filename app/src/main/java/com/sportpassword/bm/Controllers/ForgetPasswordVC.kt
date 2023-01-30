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
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.databinding.ActivityForgetPasswordBinding
import com.sportpassword.bm.databinding.MytablevcBinding

class ForgetPasswordVC : BaseActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var view: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        setMyTitle("忘記密碼")
        hidekeyboard(binding.root)
        //forgetPasswordEmailTxt.setText("ives@housetube.tw")
        binding.forgetPasswordEmailTxt.requestFocus()

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun forgetPasswordSubmit(view: View) {
        val email = binding.forgetPasswordEmailTxt.text.toString()
        if (email.isEmpty()) {
            Alert.show(this, "警告", "EMail沒填")
        }

        loadingAnimation.start()
        MemberService.forgetPassword(this, email) { success ->
            loadingAnimation.stop()
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
                                finish()
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
