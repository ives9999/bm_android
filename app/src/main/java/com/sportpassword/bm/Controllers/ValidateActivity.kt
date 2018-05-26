package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R

class ValidateActivity : BaseActivity() {

    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate)

        type = intent.getStringExtra("type")
        if (type == "email") {
            setMyTitle("email認證")
        } else if (type == "mobile") {
            setMyTitle("手機認證")
        }
    }

    fun submit(view: View) {

    }
}
