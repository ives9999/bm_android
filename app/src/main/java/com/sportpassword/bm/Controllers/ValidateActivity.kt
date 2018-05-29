package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_validate.*

class ValidateActivity : BaseActivity() {

    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate)

        type = intent.getStringExtra("type")
        if (type == "email") {
            setMyTitle("email認證")
            typeLbl.text = "email"
            typeTxt.setMyText(member.email, "")
            typeTxt.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        } else if (type == "mobile") {
            setMyTitle("手機認證")
            typeLbl.text = "手機"
            typeTxt.setMyText(member.mobile, "")
            typeTxt.inputType = InputType.TYPE_CLASS_PHONE
        }
        hidekeyboard(validate_layout)
    }

    fun submit(view: View) {

    }
}
