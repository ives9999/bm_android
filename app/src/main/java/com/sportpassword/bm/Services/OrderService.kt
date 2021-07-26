package com.sportpassword.bm.Services

import android.content.Context
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Utilities.*
import org.json.JSONObject
import java.lang.Exception

object OrderService: DataService() {

    var token: String = ""
    var tokenExpireDate: String = ""
    var order_token: String = ""

    override fun getOneURL(): String {
        return URL_ONE.format("order")
    }

    override fun getListURL(): String {
        return URL_ORDER_LIST
    }

    override fun getUpdateURL(): String {
        return URL_ORDER_UPDATE
    }

//    fun update(context: Context, token: String = "", params: HashMap<String, String>, complete: CompletionHandler) {
//
//        var url: String = URL_ORDER.format(token)
//        if (token.length > 0) {
//            url = URL_ORDER.format("/${token}")
//        }
//        //println(url)
//
//        val header: MutableList<Pair<String, String>> = mutableListOf()
//        header.add(Pair("Accept","application/json"))
//        header.add(Pair("Content-Type","application/json; charset=utf-8"))
//
//        val body = JSONObject()
//        for ((key, value) in params) {
//            body.put(key, value)
//        }
//        //println(body)
//
//        MyHttpClient.instance.post(context, url, body.toString()) { success ->
//
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//                        //println(response.toString())
//                        val json = JSONObject(response.toString())
////                        //println(json)
//                        //val obj = json.getJSONObject("order")
//                        this.success = json.getBoolean("success")
//                        if (this.success) {
//                            if (json.has("token")) {
//                                this.token = json.getString("token")
//                            }
//                            if (json.has("order_token")) {
//                                this.order_token = json.getString("order_token")
//                            }
//                            if (json.has("tokenExpireDate")) {
//                                this.tokenExpireDate = json.getString("tokenExpireDate")
//                            }
//                        } else {
//                            this.msg = json.getString("msg")
//                            complete(false)
//                        }
//                    } catch (e: Exception) {
//                        this.success = false
//                        msg = "parse json failed，請洽管理員"
//                        println(e.localizedMessage)
//                    }
//                    complete(this.success)
//                } else {
//                    println("response is null")
//                }
//            } else {
//                msg = "網路錯誤，無法跟伺服器更新資料"
//                complete(success)
//            }
//        }
//    }

//    override fun parseModel(json: JSONObject): SuperModel {
//        return JSONParse.parse<SuperOrder>(json)!!
//    }
//
//    override fun parseModels(json: JSONObject): SuperModel {
//        return JSONParse.parse<SuperOrders>(json)!!
//    }
}