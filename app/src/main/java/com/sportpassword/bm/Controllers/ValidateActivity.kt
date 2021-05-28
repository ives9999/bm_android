package com.sportpassword.bm.Controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.text.InputType
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.memberDidChangeIntent
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_validate.*
import kotlinx.android.synthetic.main.mask.*

class ValidateActivity : BaseActivity() {

    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate)

        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")!!
        }

        if (type == "email") {
            setMyTitle("email認證")
            typeLbl.text = "email"
            typeTxt.setMyText(member.email!!, "")
            typeTxt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            codeTxt.inputType = InputType.TYPE_CLASS_TEXT
        } else if (type == "mobile") {
            setMyTitle("手機認證")
            typeLbl.text = "手機"
            typeTxt.setMyText(member.mobile!!, "")
            typeTxt.inputType = InputType.TYPE_CLASS_PHONE
            codeTxt.inputType = InputType.TYPE_CLASS_NUMBER
        }
        hidekeyboard(validate_layout)
    }

    fun submit(view: View) {
        _hideKeyboard(validate_layout)
        val code = codeTxt.text.toString()
        if (code.length == 0) {
            Alert.show(this, "警告", "請填寫認證碼")
        } else {
            Loading.show(mask)
            MemberService.validate(this, type, code, member.token!!) { success ->
                Loading.hide(mask)
                if (success) {
                    Alert.show(this, "訊息", "認證成功", {
                        //LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChangeIntent)
                        finish()
                    })
                } else {
                    Alert.show(this, "警告",  MemberService.msg)
                }
            }
        }
    }

    fun resend(view: View) {
        val value = typeTxt.text.toString()
        if (value.length <= 0) {
            var msg = ""
            if (type == "email") {
                msg = "請填寫email"
            } else if (type == "mobile") {
                msg = "請填寫手機號碼"
            }
            Alert.show(this, "警告", msg)
        } else {
            Loading.show(mask)
            MemberService.sendVaildateCode(this, type, value, member.token!!) { success ->
                Loading.hide(mask)
                if (success) {
                    var msg = ""
                    if (type == "email") {
                        msg = "已經將認證信寄到註冊的信箱了"
                    } else if (type == "mobile") {
                        msg = "已經將認證碼發送到註冊的手機了"
                    }
                    Alert.show(this, "訊息", msg)
                } else {
                    Alert.show(this, "警告",  MemberService.msg)
                }
            }
        }
    }
}
