package com.sportpassword.bm.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.NOTIF_MEMBER_DID_CHANGE
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_login.*

class ForgetPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        setMyTitle("忘記密碼")
        hidekyboard(forgetPassword_layout)
        //forgetPasswordEmailTxt.setText("ives@housetube.tw")
        forgetPasswordEmailTxt.requestFocus()
    }

    fun forgetPasswordSubmit(view: View) {
        val loading = Loading.show(this)
        val email = forgetPasswordEmailTxt.text.toString()
        if (email.isEmpty()) {
            Alert.show(this, "警告", "EMail沒填")
        }

        MemberService.forgetPassword(this, email) { success ->
            loading.dismiss()
            //println(success)
            if (success) {
                if (MemberService.success) {
                    Alert.show(this, "訊息", MemberService.msg)
                } else {
                    Alert.show(this, "警告", MemberService.msg)
                }
            } else {
                Alert.show(this, "警告", MemberService.msg)
            }
        }
    }
}
