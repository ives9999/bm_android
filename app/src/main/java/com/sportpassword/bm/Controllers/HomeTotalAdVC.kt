package com.sportpassword.bm.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R

class HomeTotalAdVC : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_total_ad_vc)
    }

    fun cancelBtnPressed(view: View) {
//        val intent: Intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
        prev()
    }
}