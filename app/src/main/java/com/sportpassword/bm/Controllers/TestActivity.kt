package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.CompletionHandler
import com.sportpassword.bm.Utilities.VIMEO_TOKEN
import com.vimeo.networking.Configuration
import com.vimeo.networking.VimeoClient
import com.vimeo.networking.callbacks.ModelCallback
import com.vimeo.networking.model.Video
import com.vimeo.networking.model.error.VimeoError
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    lateinit var vimeoClient: VimeoClient
    var embed = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val configBuilder = Configuration.Builder(VIMEO_TOKEN)
        VimeoClient.initialize(configBuilder.build())
        vimeoClient = VimeoClient.getInstance()

        val uri = "/videos/265966500"
//        getEmbed(uri) { success ->
//            if (success) {
//                println(embed)
//                webView.loadData(embed, "text/html; charset=utf-8", "UTF-8")
//            }
//        }


        //val html = "<span>This is a Test Activity</span>"
        //val html = "<iframe src=\"https://player.vimeo.com/video/265966500\" width=\"640\" height=\"360\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>"
        //val html = "<iframe src=\"https://player.vimeo.com/video/265966500?badge=0&autopause=0&player_id=0&app_id=121958\" width=\"540\" height=\"304\" frameborder=\"0\" title=\"上手殺球的三種變化\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>"
        val html = "<iframe width=\"360\" height=\"202\" src=\"https://www.youtube.com/embed/Uwj6vyYrhHo\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe>"
        val url = "https://player.vimeo.com/external/265966500.m3u8?s=34b38682f7d95a03185deb417e4c0945e3923882"
        //webView.loadUrl(url)
        //webView.settings.pluginState = WebSettings.PluginState.ON
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.loadData(html, "text/html; charset=utf-8", "UTF-8")
    }

    private fun getEmbed(uri: String, complete: CompletionHandler) {
        if (vimeoClient != null) {
            vimeoClient!!.fetchNetworkContent(uri, object : ModelCallback<Video>(Video::class.java) {
                override fun success(t: Video?) {
                    //println(t)
                    embed = t!!.embed.html
                    //println(embed)
                    complete(true)
                }

                override fun failure(error: VimeoError?) {
                    //println(error!!.localizedMessage)
                    complete(false)
                }
            })
        }
    }
}
