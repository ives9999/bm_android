package com.sportpassword.bm.Services

import android.content.Context
import com.sportpassword.bm.Models.SuperModel
import com.sportpassword.bm.Models.SuperOrder
import com.sportpassword.bm.Models.SuperProduct
import com.sportpassword.bm.Utilities.CompletionHandler
import com.sportpassword.bm.Utilities.JSONParse
import com.sportpassword.bm.Utilities.URL_ORDER
import org.json.JSONObject
import java.lang.Exception

object OrderService: DataService() {

    var token: String = ""
    var tokenExpireDate: String = ""
    var order_token: String = ""

    fun update(context: Context, token: String = "", params: HashMap<String, String>, complete: CompletionHandler) {

        var url: String = URL_ORDER.format(token)
        if (token.length > 0) {
            url = URL_ORDER.format("/${token}")
        }
        //println(url)

        val header: MutableList<Pair<String, String>> = mutableListOf()
        header.add(Pair("Accept","application/json"))
        header.add(Pair("Content-Type","application/json; charset=utf-8"))

        val body = JSONObject()
        for ((key, value) in params) {
            body.put(key, value)
        }
        //println(body)

        MyHttpClient.instance.post(context, url, body.toString()) { success ->

            if (success) {
                val response = MyHttpClient.instance.response
                if (response != null) {
                    try {
                        //println(response.toString())
                        val json = JSONObject(response.toString())
//                        //println(json)
                        //val obj = json.getJSONObject("order")
                        this.success = json.getBoolean("success")
                        if (this.success) {
                            this.token = json.getString("token")
                            this.order_token = json.getString("order_token")
                            this.tokenExpireDate = json.getString("tokenExpireDate")
                        } else {
                            this.msg = json.getString("msg")
                            complete(false)
                        }
                    } catch (e: Exception) {
                        this.success = false
                        msg = "parse json failed，請洽管理員"
                        println(e.localizedMessage)
                    }
                    complete(this.success)
                } else {
                    println("response is null")
                }
            } else {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }
        }
    }

    override fun parseModel(json: JSONObject): SuperModel {
        return JSONParse.parse<SuperOrder>(json)!!
    }
}