package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.sportpassword.bm.R
import android.os.Handler
import android.os.Looper

class SplashVC : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_vc)

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this, SearchVC::class.java)
            startActivity(i)
            finish()
        }, 3000)// 3000 is the delayed time in milliseconds.
    }
}