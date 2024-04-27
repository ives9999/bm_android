package com.sportpassword.bm.v2.base

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.LoadingAnimation
import com.sportpassword.bm.v2.error.Error

open class BaseActivity : AppCompatActivity() {

    lateinit var loadingAnimation: LoadingAnimation
    var error: Error = Error(0, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}