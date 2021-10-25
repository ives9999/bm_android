package com.sportpassword.bm.Controllers

import android.app.Activity
import android.os.Bundle
import com.sportpassword.bm.R

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashVC : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_vc)
    }
}