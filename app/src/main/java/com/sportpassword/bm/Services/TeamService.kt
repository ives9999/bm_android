package com.sportpassword.bm.Services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sportpassword.bm.Models.Arena
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Models.Data
import com.sportpassword.bm.Models.Team
import com.sportpassword.bm.Utilities.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URLEncoder

/**
 * Created by ives on 2018/2/17.
 */
object TeamService: DataService() {

    var tempPlayLists: ArrayList<Map<String, Map<String, Any>>> = arrayListOf()
    override val model: Team = Team(-1, "", "", "")

    override fun setData(id: Int, title: String, token: String, featured_path: String, vimeo: String, youtube: String): Team {
        val data = Team(id, title, token, featured_path, vimeo, youtube)
        return data
    }

    fun tempPlay_list(context: Context, page:Int, perPage:Int, complete: CompletionHandler) {
        val url = URL_TEAM_TEMP_PLAY_LIST
        //println(url)

        val body = JSONObject()
        body.put("source", "app")
        body.put("page", page.toString())
        body.put("perPage", perPage.toString())

        val requestBody = body.toString()
        //println(requestBody)
        //println("coach getList refresh: $refresh")
        tempPlayLists = arrayListOf()

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = true
                this.totalCount = json.getInt("totalCount")
                this.page = json.getInt("page")
                this.perPage = json.getInt("perPage")
                val rows = json.getJSONArray("rows")
                for (i in 0..rows.length()-1) {
                    val obj = rows.getJSONObject(i)
                    model.dataReset()
                    for ((key, value) in model.data) {
                        if (obj.has(key)) {
                            _jsonToData(obj, key, value)
                        }
                    }
                    //println(model.data)
                    model.updatePlayStartTime()
                    model.updatePlayEndTime()

                    var data = model.data

                    var near_date: MutableMap<String, Any> = mutableMapOf()
                    near_date = data[TEAM_NEAR_DATE_KEY]!!
                    val n1: String = obj.getString("near_date")
                    val n2: String = obj.getString("near_date_w")
                    near_date["value"] = n1
                    near_date["value1"] = n2
                    near_date["show"] = "$n1($n2)"
                    data[TEAM_NEAR_DATE_KEY] = near_date

                    var city: MutableMap<String, Any> = mutableMapOf()
                    city = data[TEAM_CITY_KEY]!!
                    city["value"] = obj.getInt("city_id")
                    city["show"] = obj.getString("city_name")
                    data[TEAM_CITY_KEY] = city

                    var arena: MutableMap<String, Any> = mutableMapOf()
                    arena = data[TEAM_ARENA_KEY]!!
                    arena["value"] = obj.getInt("arena_id")
                    arena["show"] = obj.getString("arena_name")
                    data[TEAM_ARENA_KEY] = arena

                    var count: MutableMap<String, Any> = mutableMapOf()
                    count["quantity"] = obj.getInt(TEAM_TEMP_QUANTITY_KEY)
                    count["signup"] = obj.getInt("temp_signup_count")
                    data["count"] = count
                    //println(data)

                    //model.lists.add(data)
                    tempPlayLists.add(data)
                }
                //tempPlayLists = model.lists
                //println(tempPlayLists.size)
                //println(tempPlayLists)
            } catch (e: JSONException) {
                println(e.localizedMessage)
                success = false
                msg = "無法getList，沒有傳回成功值 " + e.localizedMessage
            }
            if (this.success) {
                //jsonToMember(json)
            } else {
                //DataService.makeErrorMsg(json)
            }
            complete(true)
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

//        val request = object: StringRequest(Request.Method.POST, url,
//                Response.Listener { response ->
//                    println(response)
//                }, Response.ErrorListener { error ->
//                    println(error.localizedMessage)
//        }) {
//            override fun getBodyContentType(): String {
//                return HEADER
//            }
//
//            override fun getBody(): ByteArray {
//                println("parameter")
//                return requestBody.toByteArray()
//            }
//        }
//
        Volley.newRequestQueue(context).add(request)
    }
    fun plusOne(context: Context, title: String, near_date: String, token: String, complete: CompletionHandler) {
        val _title = URLEncoder.encode(title, "UTF-8")
        var url = URL_TEAM_PLUSONE + _title + "?source=app&date=" + near_date + "&token=" + token
        //println(url)

        val request = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                println(e.localizedMessage)
                success = false
                msg = "無法報名臨打，沒有傳回成功值 " + e.localizedMessage
            }
            if (!success) {
                makeErrorMsg(json)
            }
            complete(success)
        }, Response.ErrorListener { error ->
            //Log.d("ERROR", "Could not register user: $error")
            println(error.localizedMessage)
            this.msg = "取得失敗，網站或網路錯誤"
            complete(false)
        }){}
        Volley.newRequestQueue(context).add(request)
    }
    fun cancelPlusOne(context: Context, title: String, near_date: String, token: String, complete: CompletionHandler) {
        val _title = URLEncoder.encode(title, "UTF-8")
        var url = URL_TEAM_CANCELPLUSONE + _title + "?source=app&date=" + near_date + "&token=" + token
        //println(url)

        val request = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                println(e.localizedMessage)
                success = false
                msg = "無法取消報名臨打，沒有傳回成功值 " + e.localizedMessage
            }
            if (!success) {
                makeErrorMsg(json)
            }
            complete(success)
        }, Response.ErrorListener { error ->
            //Log.d("ERROR", "Could not register user: $error")
            println(error.localizedMessage)
            this.msg = "取得失敗，網站或網路錯誤"
            complete(false)
        }){}
        Volley.newRequestQueue(context).add(request)
    }
    override fun dealOne(json: JSONObject) {
        super.dealOne(json)
        for ((key, value) in model.data) {
            _jsonToData(json, key, value)
        }

        if (json.has("near_date_w")) {
            val n2: String = json.getString("near_date_w")
            model.data[TEAM_NEAR_DATE_KEY]!!["value1"] = n2
        }

        var signups: ArrayList<Map<String, String>> = arrayListOf()
        if (json.has("signups")) {
            val items: JSONArray = json.getJSONArray("signups")
            for (i in 0..items.length()-1) {
                val item: JSONObject = items[i] as JSONObject
                val member = item.getJSONObject("member")
                //print(member)
                val nickname: String = member.getString("nickname")
                val token: String = member.getString("token")
                val created_at: String = item.getString("created_at")
                signups.add(mapOf("nickname" to nickname, "token" to token,"created_at" to created_at))
                //println(signups)
            }
            if (!model.data.containsKey("signups")) {
                model.data["signups"] = mutableMapOf()
            }
            model.data["signups"]!!["value"] = signups
            model.data["signups"]!!["vtype"] = "array"
            //print(model.data)
        }

        model.updatePlayStartTime()
        model.updatePlayEndTime()
        model.updateTempContent()
        model.updateCharge()
        model.updateContent()
        model.updateNearDate()
        model.feeShow()
    }

    override fun _jsonToData(tmp: JSONObject, key: String, item: Map<String, Any>) {
        //println(key)
        val type = item["vtype"] as String
        //println("type: $type")
        if (type == "Int") {
            try {
                model.data[key]!!["value"] = tmp.getInt(key)
                model.data[key]!!["show"] = tmp.getInt(key).toString()
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
            if (key == TEAM_FEATURED_KEY) {
                if (value.isNotEmpty()) {
                    value = BASE_URL + value
                }
            }
            if (value.isEmpty() || value == "null") value = "未提供"
            //println("$key => $value")
            model.data[key]!!["value"] = value
            model.data[key]!!["show"] = value
        } else if (type == "array") {
            if (key == TEAM_CITY_KEY) {
                try {
                    val obj1 = tmp.getJSONObject(key)
                    val id = obj1.getInt("id")
                    val name = obj1.getString("name")
                    val city = City(id, name)
                    model.updateCity(city)
                } catch (e: JSONException) {
                    println(e.localizedMessage)
                }
            } else if (key == TEAM_ARENA_KEY) {
                try {
                    val obj1 = tmp.getJSONObject(key)
                    val id = obj1.getInt("id")
                    val name = obj1.getString("name")
                    val arena = Arena(id, name)
                    model.updateArena(arena)
                } catch (e: JSONException) {

                }
            } else if (key == TEAM_DAYS_KEY) {
                try {
                    val tmp1: JSONArray = tmp.getJSONArray(key)
                    var days: ArrayList<Int> = arrayListOf<Int>()
                    for (i in 0..tmp1.length() - 1) {
                        val obj: JSONObject = tmp1.getJSONObject(i)
                        val day = obj.getInt("day")
                        days.add(day)
                    }
                    model.updateDays(days)
                } catch (e: JSONException) {

                }
            } else if (key == TEAM_DEGREE_KEY) {
                try {
                    val tmp1: String = tmp.getString(key)
                    //println("tmp1: $tmp1")
                    val degrees = tmp1.split(",")
                    //println("degrees: $degrees")
                    model.updateDegree(degrees.toTypedArray())
                } catch (e: JSONException) {

                }
            }
        }
    }
}