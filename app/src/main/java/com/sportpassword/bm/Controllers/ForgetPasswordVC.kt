package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.NOTIF_MEMBER_DID_CHANGE
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.mask.*

class ForgetPasswordVC : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        setMyTitle("忘記密碼")
        hidekeyboard(forgetPassword_layout)
        //forgetPasswordEmailTxt.setText("ives@housetube.tw")
        forgetPasswordEmailTxt.requestFocus()

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun forgetPasswordSubmit(view: View) {
        val email = forgetPasswordEmailTxt.text.toString()
        if (email.isEmpty()) {
            Alert.show(this, "警告", "EMail沒填")
        }

        Loading.show(mask)
        MemberService.forgetPassword(this, email) { success ->
            Loading.hide(mask)
            if (success) {

                var t: SuccessTable? = null
                try {
                    t = Gson().fromJson<SuccessTable>(MemberService.jsonString, SuccessTable::class.java)
                } catch (e: JsonParseException) {
                    warning(e.localizedMessage!!)
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
