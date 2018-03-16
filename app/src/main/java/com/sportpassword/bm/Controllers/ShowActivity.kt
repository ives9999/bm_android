package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.URL_SHOW
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        val type = intent.getStringExtra("type")
        val token = intent.getStringExtra("token")
        val url = "$URL_SHOW".format(type, token)
        println(url)

        val webViewClient = WebViewClient()
        showWebView.webViewClient = webViewClient
        showWebView.loadUrl(url)
    }
}
