package com.sportpassword.bm.Services

import android.content.Context
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Utilities.*
import org.json.JSONObject
import java.lang.Exception

object StoreService: DataService() {

    lateinit var superStores: SuperStores
    lateinit var superStore: SuperStore


    override fun getListURL(): String {
        return URL_STORE_LIST
    }
    override fun getOneURL(): String {
        return URL_ONE.format("store")
    }

    override fun parseModels(json: JSONObject): SuperModel {
        return JSONParse.parse<SuperStores>(json)!!
    }
    override fun parseModel(json: JSONObject): SuperModel {
        return JSONParse.parse<SuperStore>(json)!!
    }

    override fun update(context: Context, _params: MutableMap<String, String>, filePath: String, complete: CompletionHandler) {

        val url = URL_UPDATE.format("store")
        //println(url)

        val header: MutableList<Pair<String, String>> = mutableListOf()
        header.add(Pair("Accept","application/json"))
        header.add(Pair("Content-Type","application/json"))

        //val PARAMS1: MutableMap<String, String> = PARAMS.toMutableMap()
        //var params: MutableMap<String, String> = _params
        val params = _params.let { map1 ->
            PARAMS.let { map2 ->
                map1 + map2
            }
        }

        val params1: MutableList<Pair<String, String>> = mutableListOf()
        var json: String = "{"
        val tmps: ArrayList<String> = arrayListOf()
        for ((key, value) in params) {

            params1.add(Pair(key, value))
            tmps.add("\""+key+"\""+":"+"\""+value+"\"")
        }
        json += tmps.joinToString(",")
        json += "}"
        //println(json)

        var filePaths: ArrayList<String>? = null
        if (filePath.length > 0) {
            filePaths = arrayListOf()
            filePaths.add(filePath)
        }

        MyHttpClient.instance.uploadFile(context, url, null, filePaths, params1, header) { success ->
            if (success) {
                val response = MyHttpClient.instance.response
                if (response != null) {
                    val responseStr = response.toString()
//                    println(responseStr)
                    try {
                        val json = JSONObject(responseStr)
                        this.success = json.getBoolean("success")
                        if (!this.success) {
                            if (json.has("msg")) {
                                msg = json.getString("msg")
                            }
                            if (json.has("error")) {
                                msg = ""
                                val errors = json.getJSONArray("error")
                                for (i in 0..errors.length()-1) {
                                    msg += errors.getString(i)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        this.success = false
                        msg = "parse json failed，請洽管理員"
                    }
                    complete(this.success)
                } else {
                    msg = "伺服器沒有傳回任何值，更新失敗，請洽管理員"
                    complete(false)
                }

            } else {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }
        }

    }
}