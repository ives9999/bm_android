package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import kotlinx.android.synthetic.main.activity_update_password.*

class UpdatePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
    }

    fun updatePasswordSubmit(view: View) {
        val oldPwd = updatePasswordOldPasswordTxt.text.toString()
        if (oldPwd.isEmpty()) {
            Alert.show(this, "警告", "舊密碼沒填")
        }
        val newPwd = updatePasswordNewPasswordTxt.text.toString()
        if (newPwd.isEmpty()) {
            Alert.show(this, "警告", "新密碼沒填")
        }
        val rePwd = updatePasswordRePasswordTxt.text.toString()
        if (newPwd != rePwd) {
            Alert.show(this, "警告", "新密碼不符合")
        }
        val loading = Loading.show(this)
        MemberService.changePassword(this, oldPwd, newPwd, rePwd) { success ->
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
