package com.sportpassword.bm.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.NOTIF_MEMBER_DID_CHANGE
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.mask.*

class ForgetPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        setMyTitle("忘記密碼")
        hidekeyboard(forgetPassword_layout)
        //forgetPasswordEmailTxt.setText("ives@housetube.tw")
        forgetPasswordEmailTxt.requestFocus()
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
