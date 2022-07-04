package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.webkit.WebView
import com.sportpassword.bm.R

class WebViewVC : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_vc)

        setMyTitle("web view")

        findViewById<WebView>(R.id.webView) ?. let {
            it.loadUrl("http://192.168.100.120/c2c.html?LogisticsType&LogisticsSubType=UNIMARTC2C&IsCollection=Y&Device=1&n=4")
            //it.loadUrl("http://192.160.100.120/test.html")
        }

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }
}