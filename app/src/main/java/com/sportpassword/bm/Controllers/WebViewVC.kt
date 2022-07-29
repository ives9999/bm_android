package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Button
import android.widget.Toast
import com.google.gson.Gson
import com.sportpassword.bm.Models.OrderTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.GATEWAY
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.URL_ECPAY2_C2C_MAP
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_web_view_vc.*
import kotlinx.android.synthetic.main.mask.*
import java.net.URLEncoder

class WebViewVC : BaseActivity() {

    var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_vc)

        token = intent.getStringExtra("token")

        setMyTitle("web view")

        if (token != null) {

            val params: HashMap<String, String> = hashMapOf("token" to token!!, "member_token" to member.token!!)
            OrderService.getOne(this, params) { success ->
                if (success) {
                    val jsonString = OrderService.jsonString
                    if (jsonString.isNotEmpty()) {
                        runOnUiThread {
                            try {
                                //println(jsonString)
                                val orderTable: OrderTable = Gson().fromJson(jsonString, OrderTable::class.java)
                                showWebView(orderTable)
                            } catch (e: java.lang.Exception) {
                                warning(e.localizedMessage)
                            }
                        }
                    }
                }
//                closeRefresh()
//                runOnUiThread {
//                    Loading.hide(mask)
//                }
            }
        }

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun showWebView(table: Table) {

        val orderTable: OrderTable = table as OrderTable
        //val url: String = "http://192.168.100.120/app/order/ecpay2_c2c_map"
        val url: String = URL_ECPAY2_C2C_MAP

        var params: String = "LogisticsType=${
            URLEncoder.encode("CVS", "UTF-8")}"
        if (orderTable.gateway != null) {
            val gatewayEnum: GATEWAY = GATEWAY.stringToEnum(orderTable.gateway!!.method)
            params += "&LogisticsSubType=${URLEncoder.encode(gatewayEnum.enumToECPay(), "UTF-8")}"
            params += "&IsCollection=${URLEncoder.encode("Y", "UTF-8")}"
            params += "&Device=${URLEncoder.encode("1", "UTF-8")}"
            params += "&order_token=${URLEncoder.encode(token!!, "UTF-8")}"
            params += "&phone=${URLEncoder.encode("android", "UTF-8")}"
        }
//        println(url)
//        println(params)


        findViewById<WebView>(R.id.webView)?. let {
            it.settings.javaScriptEnabled = true

            it.webViewClient = object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(
//                    view: WebView?,
//                    request: WebResourceRequest?
//                ): Boolean {
//                    view?.loadUrl(url)
//                    return true
//                }
            }


            it.evaluateJavascript("document.body.style.background = 'blue';", null)
            webView.addJavascriptInterface(MyJavascriptInterface(context, this), "MyJavascriptInterface")
            //webView.addJavascriptInterface(JSBridge(),"JSBridge")
            it.postUrl(url, params.toByteArray())
            //webView.loadUrl("http://192.168.100.120/c2c.html?n=6")
        }
    }
}

//class JSBridge(){
//    @JavascriptInterface
//    fun showMessageInNative(message:String){
//        //Received message from webview in native, process data
//        println(message)
//    }
//}

class MyJavascriptInterface(private val context: Context, private val baseActivity: BaseActivity) {

    @JavascriptInterface
    fun showToast(message: String) {
        //println(message)
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        //baseActivity.prev()
        val intent = Intent()
        baseActivity.setResult(Activity.RESULT_OK, intent)
        baseActivity.finish()
    }
}

















