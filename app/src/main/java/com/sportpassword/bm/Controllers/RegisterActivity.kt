package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.BASE_URL
import com.sportpassword.bm.Utilities.URL_REGISTER
import com.sportpassword.bm.Utilities.gSimulate
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
        val email: String = registerEmailTxt.text.toString()
        //println(email)
        val password: String = registerPasswordTxt.text.toString()
        val repassword: String = registerRePasswordTxt.text.toString()

        //println("submit: " + URL_REGISTER)

        MemberService.register(this, email, password, repassword) { success ->
            if (success) {
                println("register ok")
                if (MemberService.success) {
                    // register success
                } else {
                    // register fail
                }
            } else {
                //register fail
            }
        }
    }

    fun registerFBSubmit(view: View) {

    }

    fun registerForgetPassword(view: View) {
        goForgetPassword()
    }

    fun registerLogin(view: View) {
        goLogin()
    }
}
