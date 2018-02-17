package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerEmailTxt.setText("ives@housetube.tw")
        registerPasswordTxt.setText("1234")
        registerRePasswordTxt.setText("1234")
    }

    fun registerSubmit(view: View) {
        hideKeyboard()
        val loading = Loading.show(this)
        val email: String = registerEmailTxt.text.toString()
        if (email.isEmpty()) {
            Alert.show(this, "警告", "EMail沒填")
        }
        val password: String = registerPasswordTxt.text.toString()
        if (password.isEmpty()) {
            Alert.show(this, "警告", "密碼沒填")
        }
        val repassword: String = registerRePasswordTxt.text.toString()
        if (repassword.isEmpty()) {
            Alert.show(this, "警告", "密碼確認欄位沒填")
        }
        if (password != repassword) {
            Alert.show(this, "警告", "密碼不一致")
        }

        //println("submit: " + URL_REGISTER)

        MemberService.register(this, email, password, repassword) { success ->
            loading.dismiss()
            if (success) {
                //println("register ok")
                if (MemberService.success) {
                    Alert.show(this, "成功", "註冊成功，請儘速通過email認證，才能使用更多功能！！") {
                        home(this)
                    }
                } else {
                    Alert.show(this, "警告", MemberService.msg)
                }
            } else {
                Alert.show(this, "警告", MemberService.msg)
            }
        }
    }

    fun registerFBSubmit(view: View) {
        home(this)
    }

    fun registerForgetPassword(view: View) {
        goForgetPassword()
    }

    fun registerLogin(view: View) {
        goLogin()
    }
}
