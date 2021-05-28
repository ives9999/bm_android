package com.sportpassword.bm.Controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.URL_SHOW
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.activity_test.*

class ShowActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        val type: String = intent.getStringExtra("type")!!
        val token: String = intent.getStringExtra("token")!!
        val title: String = intent.getStringExtra("title")!!
        val url = URL_SHOW.format(type, token)
        //println(url)
        setMyTitle(title)

        val webViewClient = WebViewClient()
        showWebView.webViewClient = webViewClient
        showWebView.webChromeClient = WebChromeClient()
        showWebView.settings.javaScriptEnabled = true
        showWebView.settings.setAppCacheEnabled(true)
        showWebView.loadUrl(url)
    }
}

















