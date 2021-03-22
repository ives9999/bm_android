package com.sportpassword.bm.Services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Utilities.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ives on 2018/2/23.
 */
object CoachService: DataService() {

    override val model: Coach = Coach(-1, "", "", "")
    lateinit var superCoach: SuperCoach

    override fun getListURL(): String {
        return URL_COURSE_LIST
    }
    override fun getOneURL(): String {
        return "$URL_ONE".format("coach")
    }

    override fun parseModels(json: JSONObject): SuperModel {
        return JSONParse.parse<SuperCoaches>(json)!!
    }
    override fun parseModel(json: JSONObject): SuperModel {
        return JSONParse.parse<SuperCoach>(json)!!
    }

    override fun getOne(context: Context, type: String, titleField: String, token: String, complete: CompletionHandler) {
        val url = "$URL_ONE".format(type)
//        println(url)

        val body = JSONObject()
        body.put("source", "app")
        body.put("token", token)
        body.put("strip_html", false)
        val requestBody = body.toString()
//        println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println("json: " + json)
            try {
                success = true
                if (type == "coach") {
                    this.superCoach = JSONParse.parse<SuperCoach>(json)!!
//                    this.superCoach.citys.print()
                }
                model.dataReset()
                data = mutableMapOf()
                dealOne(json)
                data = model.data
            } catch (e: JSONException) {
                println("parse data error: " + e.localizedMessage)
                success = false
                msg = "無法getOne，沒有傳回成功值 " + e.localizedMessage
            }
            complete(success)
        }, Response.ErrorListener { error ->
            //Log.d("ERROR", "Could not register user: $error")
            println(error.localizedMessage)
            this.msg = "取得失敗，網站或網路錯誤"
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return HEADER
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(request)
    }

    override fun setData(id: Int, title: String, token: String, featured_path: String, vimeo: String, youtube: String): Coach {
        val data = Coach(id, title, token, featured_path, vimeo, youtube)
        return data
    }

    override fun setData1(obj: JSONObject): MutableMap<String, MutableMap<String, Any>> {
        model.dataReset()
        for ((key, value) in model.data) {
            if (obj.has(key)) {
                _jsonToData(obj, key, value)
            }
        }
//        if (obj.has("citys")) {
//            _jsonToData(obj, "citys", model.data["city"]!!)
//        }
        return model.data
    }

    override fun dealOne(json: JSONObject) {
        super.dealOne(json)
        for ((key, value) in model.data) {
            _jsonToData(json, key, value)
        }
    }

    override fun _jsonToData(tmp: JSONObject, key: String, item: Map<String, Any>) {
        //println(key)
        val type = item["vtype"] as String
        //println("type: $type")
        if (type == "Int") {
            try {
                if (key == COACH_SENIORITY_KEY) {
                    var show = "未提供"
                    if (tmp != JSONObject.NULL) {
                        show = tmp.getInt(key).toString() + "年"
                    }
                    model.data[key]!!["show"] = show
                } else {
                    model.data[key]!!["show"] = tmp.getInt(key).toString()
                }
                model.data[key]!!["value"] = tmp.getInt(key)

                if (key == CITY_ID_KEY) {
                    val city_id: Int = tmp.getInt(key)
                    model.data[key]!!["show"] = Global.zoneIDToName(city_id)
                }
            } catch (e: JSONException) {
                //println(e.localizedMessage)
                model.data[key]!!["value"] = -1
                model.data[key]!!["show"] = "未提供"
            }
        } else if (type == "String") {
            var value: String = ""
            try {
                value = tmp.getString(key)
            } catch (e: JSONException) {

            }
            if (key == FEATURED_KEY) {
                if (value.isNotEmpty()) {
                    value = BASE_URL + value
                }
            } else {
                if (value.isEmpty() || value == "null") value = ""
            }
            model.data[key]!!["value"] = value
            model.data[key]!!["show"] = value
            if (key == MOBILE_KEY) {
                model.mobileShow()
            }
        } else if (type == "array") {
            if (key == CITYS_KEY) {
                try {
                    var arr = tmp.getJSONArray(key)
                    for (i in 0..arr.length() - 1) {
                        val obj1 = arr.getJSONObject(i)
                        val id = obj1.getInt("id")
                        val name = obj1.getString("name")
                        val city = City(id, name)
                        model.updateCity(city)
                    }
                } catch (e: JSONException) {
                    println(e.localizedMessage)
                }
            }
        }
    }
}