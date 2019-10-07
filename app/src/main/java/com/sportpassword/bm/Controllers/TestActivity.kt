package com.sportpassword.bm.Controllers

import android.content.Context
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.CompletionHandler
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

//    lateinit var vimeoClient: VimeoClient
    var embed = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


//        val mWm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val mDisplay = mWm.defaultDisplay
//        val mDisplayMetrics = DisplayMetrics()
//        mDisplay.getMetrics(mDisplayMetrics)
//        val mRealDisplayMetrics = DisplayMetrics()
//        mDisplay.getRealMetrics(mRealDisplayMetrics)
//
//        val mPoint = Point()
//        mDisplay.getSize(mPoint)
//        val w = mPoint.x
//        val h = mPoint.y
//        println("newx: " + w)
//        println("newy: " + h)

        val displayMetrics = resources.displayMetrics
        val density = displayMetrics.density
        println("density: " + density)
        var width = displayMetrics.widthPixels / density
        val _width: Float = width.toFloat()
        println("width: " + width)
        val _height: Float = _width*3.0f/5.0f
        //println("height: " + _height)
        var height: Int = _height.toInt()
        println("height: " + height)
        width -= 20
        //width = 440
        //height = 330

        //val html = "<span>This is a Test Activity</span>"
        //val html = "<iframe src=\"https://player.vimeo.com/video/265966500\" width=\"640\" height=\"360\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>"
        //val html = "<iframe src=\"https://player.vimeo.com/video/265966500?badge=0&autopause=0&player_id=0&app_id=121958\" width=\"540\" height=\"304\" frameborder=\"0\" title=\"上手殺球的三種變化\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>"
        val html =
                "<html><body><iframe type=\"text/html5\" width=\"" +
                width +
                "\" height=\"" +
                height +
                "\" src=\"https://www.youtube.com/embed/Uwj6vyYrhHo\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe></body></html>"
        //println(html)
        //val url = "https://player.vimeo.com/external/265966500.m3u8?s=34b38682f7d95a03185deb417e4c0945e3923882"
        //webView.loadUrl(url)
        //webView.settings.pluginState = WebSettings.PluginState.ON
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.loadData(html, "text/html; charset=utf-8", "UTF-8")
    }

//    private fun getEmbed(uri: String, complete: CompletionHandler) {
//        if (vimeoClient != null) {
//            vimeoClient!!.fetchNetworkContent(uri, object : ModelCallback<Video>(Video::class.java) {
//                override fun success(t: Video?) {
//                    //println(t)
//                    embed = t!!.embed.html
//                    //println(embed)
//                    complete(true)
//                }
//
//                override fun failure(error: VimeoError?) {
//                    //println(error!!.localizedMessage)
//                    complete(false)
//                }
//            })
//        }
//    }
}
