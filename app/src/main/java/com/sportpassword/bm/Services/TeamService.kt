package com.sportpassword.bm.Services

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Utilities.*
import kotlinx.coroutines.GlobalScope
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URLEncoder

/**
 * Created by ives on 2018/2/17.
 */
object TeamService: DataService() {

    var tempPlayLists: ArrayList<Map<String, Map<String, Any>>> = arrayListOf()
//    override val model: Team = Team(-1, "", "", "")
    lateinit var temp_play_data: MutableMap<String, MutableMap<String, Any>>
    lateinit var tempPlayDate: TempPlayDate
    lateinit var tempPlayDatePlayers: TempPlayDatePlayers

    override fun getListURL(): String {
        return URL_TEAM_LIST
    }

    override fun getOneURL(): String {
        return URL_ONE.format("team")
    }

    override fun getLikeURL(token: String): String {
        return URL_TEAM_LIKE.format(token)
    }

//    fun tempPlay_list(context: Context, params: HashMap<String,Any>, page:Int, perPage:Int, complete: CompletionHandler) {
//        val url = URL_TEAM_TEMP_PLAY_LIST
////        println(url)
//
//        val body = JSONObject()
//        body.put("source", "app")
//        body.put("channel", "bm")
//        body.put("page", page.toString())
//        body.put("perPage", perPage.toString())
//
////        println(params)
//        for ((key, value) in params) {
//            when (key) {
//                "city_id" -> {
//                    var arr: JSONArray = JSONArray(value as ArrayList<Int>)
//                    body.put(key, arr)
//                }
//                "city_type" -> {
//                    body.put(key, value)
//                }
//                "play_days" -> {
//                    var arr: JSONArray = JSONArray(value as ArrayList<Int>)
//                    body.put(key, arr)
//                }
//                "play_time" -> {
//                    body.put(key, value)
//                }
//                "use_date_range" -> {
//                    body.put(key, value)
//                }
//                "arena_id" -> {
//                    var arr: JSONArray = JSONArray(value as ArrayList<Int>)
//                    body.put(key, arr)
//                }
//                "degree" -> {
//                    var arr: JSONArray = JSONArray(value as ArrayList<String>)
//                    body.put(key, arr)
//                }
//                "k" -> {
//                    body.put(key, value)
//                }
//            }
//        }
//
//        val requestBody = body.toString()
//        //println(requestBody)
//        //println("coach getList refresh: $refresh")
//        tempPlayLists = arrayListOf()
//
//        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
////            println(json)
//            try {
//                success = true
//                this.totalCount = json.getInt("totalCount")
//                this.page = json.getInt("page")
//                this.perPage = json.getInt("perPage")
//                val rows = json.getJSONArray("rows")
//                for (i in 0..rows.length()-1) {
//                    val obj = rows.getJSONObject(i)
//                    model.dataReset()
//                    for ((key, value) in model.data) {
//                        if (obj.has(key)) {
//                            _jsonToData(obj, key, value)
//                        }
//                    }
//                    //println(model.data)
//                    model.initTimeData()
//
//                    var data = model.data
//
//                    var near_date: MutableMap<String, Any> = mutableMapOf()
//                    near_date = data[TEAM_NEAR_DATE_KEY]!!
//                    val n1: String = obj.getString("near_date")
//                    val n2: String = obj.getString("near_date_w")
//                    near_date["value"] = n1
//                    near_date["value1"] = n2
//                    near_date["show"] = "$n1($n2)"
//                    data[TEAM_NEAR_DATE_KEY] = near_date
//
//                    var city: MutableMap<String, Any> = mutableMapOf()
//                    city = data[CITY_KEY]!!
//                    city["value"] = 0
//                    try {
//                        city["value"] = obj.getInt("city_id")
//                    } catch (e: Exception) {
//
//                    }
//                    city["show"] = obj.getString("city_name")
//                    data[CITY_KEY] = city
//
//                    var arena: MutableMap<String, Any> = mutableMapOf()
//                    arena = data[ARENA_KEY]!!
//                    arena["value"] = 0
//                    try {
//                        arena["value"] = obj.getInt("arena_id")
//                    } catch (e: Exception) {
//
//                    }
//                    arena["show"] = obj.getString("arena_name")
//                    data[ARENA_KEY] = arena
//
//                    var count: MutableMap<String, Any> = mutableMapOf()
//                    count["quantity"] = 0
//                    try {
//                        count["quantity"] = obj.getInt(TEAM_TEMP_QUANTITY_KEY)
//                    } catch (e: Exception) {
//                    }
//                    count["signup"] = 0
//                    try {
//                        count["signup"] = obj.getInt("temp_signup_count")
//                    } catch (e: Exception) {
//                    }
//                    data["count"] = count
//                    //println(data)
//
//                    //model.lists.add(data)
//                    tempPlayLists.add(data)
//                }
//                //tempPlayLists = model.lists
//                //println(tempPlayLists.size)
//                //println(tempPlayLists)
//            } catch (e: JSONException) {
//                println(e.localizedMessage)
//                success = false
//                msg = "無法getList，沒有傳回成功值 " + e.localizedMessage
//            }
//            if (this.success) {
//                //jsonToMember(json)
//            } else {
//                //DataService.makeErrorMsg(json)
//            }
//            complete(true)
//        }, Response.ErrorListener { error ->
//            //Log.d("ERROR", "Could not register user: $error")
//            println(error.localizedMessage)
//            this.msg = "取得失敗，網站或網路錯誤"
//            complete(false)
//        }) {
//            override fun getBodyContentType(): String {
//                return HEADER
//            }
//
//            override fun getBody(): ByteArray {
//                return requestBody.toByteArray()
//            }
//        }
//
////        val request = object: StringRequest(Request.Method.GET, url,
////                Response.Listener { response ->
////                    println(response)
////                }, Response.ErrorListener { error ->
////                    println(error.localizedMessage)
////        }) {
////            override fun getBodyContentType(): String {
////                return HEADER
////            }
////
////            override fun getBody(): ByteArray {
////                println("parameter")
////                return requestBody.toByteArray()
////            }
////        }
//
//        Volley.newRequestQueue(context).add(request)
//    }
    fun tempPlay_onoff(context: Context, token: String, complete: CompletionHandler) {
        val url = URL_TEAM_TEMP_PLAY
        //println(url)

        val body = JSONObject()
        body.put("source", "app")
        body.put("channel", "bm")
        body.put("token", token)
        body.put("strip_html", true)

        val requestBody = body.toString()
        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
//            try {
//                for ((key, item) in model.temp_play_data) {
//                    if (json.has(key)) {
//                        //println(key)
//                        var value = json.get(key)
//                        val vtype: String = item["vtype"]!! as String
//                        if (vtype == "Int") {
//                            try {
//                                value = value as Int
//                            } catch (e: Exception) {
//                                value = 0
//                            }
//                            model.temp_play_data[key]!!["value"] = value
//                            model.temp_play_data[key]!!["show"] = value
//                        } else if (vtype == "String") {
//                            model.temp_play_data[key]!!["value"] = value
//                            model.temp_play_data[key]!!["show"] = value
//                        } else if (vtype == "array") {
//
//                        }
//                    }
//                }
//                temp_play_data = model.temp_play_data
//                complete(true)
//            } catch (e: JSONException) {
//                println(e.localizedMessage)
//                success = false
//                msg = "無法取得臨打資訊，請稍後再試 " + e.localizedMessage
//                complete(false)
//            }
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
    fun plusOne(context: Context, title: String, near_date: String, token: String, complete: CompletionHandler) {
        val _title = URLEncoder.encode(title, "UTF-8")
        var url = URL_TEAM_PLUSONE + _title + "?source=app&date=" + near_date + "&token=" + token
//        println(url)

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
                //makeErrorMsg(json)
            }
            complete(success)
        }, Response.ErrorListener { error ->
            //Log.d("ERROR", "Could not register user: $error")
            println(error.localizedMessage)
            this.msg = "取得失敗，網站或網路錯誤"
            complete(false)
        }){}
        request.setRetryPolicy(DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
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
                //makeErrorMsg(json)
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

    fun tempPlay_date(context: Context, token: String, complete: CompletionHandler) {
        val url = URL_TEAM_TEMP_PLAY_DATE
        val body = JSONObject()
        body.put("source", "app")
        body.put("channel", CHANNEL)
        body.put("token", token)
        val requestBody = body.toString()

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            val s = json.toString()
            //println(s)
            try {
                tempPlayDate = Klaxon().parse<TempPlayDate>(s)!!
//                tempPlayDate = TempPlayDate(json)
//                println(tempPlayDate.success)
//                println(tempPlayDate.rows)
                if (tempPlayDate.success) {
                } else {
                    msg = json.getString("msg")
                }
                complete(tempPlayDate.success)
            } catch (e: JSONException) {
                println(e.localizedMessage)
                success = false
                msg = "無法取得臨打資訊，請稍後再試 " + e.localizedMessage
                complete(false)
            }
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
    fun tempPlay_datePlayer(context: Context, date: String, token: String, complete: CompletionHandler) {
        val url = URL_TEAM_TEMP_PLAY_DATE_PLAYER
        val body = JSONObject()
        body.put("source", "app")
        body.put("channel", CHANNEL)
        body.put("date", date)
        body.put("token", token)
        val requestBody = body.toString()

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            println(json)
//            val s = json.toString()
            //println(s)
            try {
                tempPlayDatePlayers = JSONParse.parse<TempPlayDatePlayers>(json)!!
                for (row in tempPlayDatePlayers.rows) {
                    row.filterRow()
                }
//                timetables.print()
                if (!tempPlayDatePlayers.success) {
                    msg = json.getString("msg")
                }



//                tempPlayDatePlayer = Klaxon().parse<TempPlayDatePlayer>(s)!!
//                tempPlayDate = TempPlayDate(json)
//                println(tempPlayDatePlayer.success)
//                println(tempPlayDatePlayers.rows)
                if (tempPlayDatePlayers.success) {
                } else {
                    msg = json.getString("msg")
                }
                complete(tempPlayDatePlayers.success)
            } catch (e: Exception) {
                println(e.localizedMessage)
                success = false
                msg = "無法取得臨打資訊，請稍後再試 " + e.localizedMessage
                complete(false)
            }
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

//    override fun dealOne(json: JSONObject) {
//        super.dealOne(json)
//        for ((key, value) in model.data) {
//            _jsonToData(json, key, value)
//        }
//
//        if (json.has("near_date_w")) {
//            val n2: String = json.getString("near_date_w")
//            model.data[TEAM_NEAR_DATE_KEY]!!["value1"] = n2
//        }
//
//        var signups: ArrayList<Map<String, String>> = arrayListOf()
//        if (json.has("signups")) {
//            val items: JSONArray = json.getJSONArray("signups")
//            for (i in 0..items.length()-1) {
//                val item: JSONObject = items[i] as JSONObject
//                val member = item.getJSONObject("member")
//                //print(member)
//                val nickname: String = member.getString("nickname")
//                val token: String = member.getString("token")
//                val created_at: String = item.getString("created_at")
//                val status: String = item.getString("status")
//                val off_at: String = item.getString("off_at")
//                signups.add(mapOf("nickname" to nickname, "token" to token,"created_at" to created_at,"status" to status,"off_at" to off_at))
//                //println(signups)
//            }
//            if (!model.data.containsKey("signups")) {
//                model.data["signups"] = mutableMapOf()
//            }
//            model.data["signups"]!!["value"] = signups
//            model.data["signups"]!!["vtype"] = "array"
//            //print(model.data)
//        }
//        //println(model.data)
//
//        model.initTimeShow()
////        model.updateInterval()
////        model.updateTempContent()
////        model.updateCharge()
////        model.updateContent()
////        model.updateNearDate()
////        model.feeShow()
//    }

    fun addBlackList(context: Context,teamToken:String,playerToken:String,managerToken:String,reason:String,completion: CompletionHandler) {
        val body = JSONObject()
        body.put("token", teamToken)
        body.put("playerToken", playerToken)
        body.put("managerToken", managerToken)
        body.put("memo", reason)
        body.put("do", "add")
        _blacklist(context, body, completion)
    }
    fun removeBlackList(context: Context,teamToken:String,playerToken: String,managerToken: String,completion: CompletionHandler) {
        val body = JSONObject()
        body.put("token", teamToken)
        body.put("playerToken", playerToken)
        body.put("managerToken", managerToken)
        body.put("do", "remove")
        _blacklist(context, body, completion)
    }
    private fun _blacklist(context: Context,body:JSONObject,completion: CompletionHandler) {
        body.put("source", "app")
        body.put("channel", CHANNEL)
        val requestBody = body.toString()
        val url: String = URL_TEAM_TEMP_PLAY_BLACKLIST
        //println(url)
        //println(body)
        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = json.getBoolean("success")
                if (!success) {
                    msg = json.getString("msg")
                }
                completion(success)
            } catch (e: JSONException) {
                println(e.localizedMessage)
                success = false
                msg = "無法取得臨打資訊，請稍後再試 " + e.localizedMessage
                completion(false)
            }
        }, Response.ErrorListener { error ->
            //Log.d("ERROR", "Could not register user: $error")
            println(error.localizedMessage)
            this.msg = "取得失敗，網站或網路錯誤"
            completion(false)
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

//    override fun setData(id: Int, title: String, token: String, featured_path: String, vimeo: String, youtube: String): Team {
//        val data = Team(id, title, token, featured_path, vimeo, youtube)
//        return data
//    }
//
//    override fun setData1(obj: JSONObject): MutableMap<String, MutableMap<String, Any>> {
//        super.setData1(obj)
//        model.dataReset()
//        for ((key, value) in model.data) {
//            if (obj.has(key)) {
//                _jsonToData(obj, key, value)
//            }
//        }
//        model.updateInterval()
////        println(model.data)
////        val citys = row.getJSONObject("city")
////        val city_name = citys.getString("name")
////        val city_id = citys.getInt("id")
//        return model.data
//    }
//
//    override fun _jsonToData(tmp: JSONObject, key: String, item: Map<String, Any>) {
//        //println(key)
//        val type = item["vtype"] as String
//        //println("type: $type")
//        if (type == "Int") {
//            try {
//                model.data[key]!!["value"] = tmp.getInt(key)
//                model.data[key]!!["show"] = tmp.getInt(key).toString()
//                if (key == CITY_KEY) {
//                    val city_id: Int = tmp.getInt(key)
//                    model.data[key]!!["show"] = Global.zoneIDToName(city_id)
//                }
//            } catch (e: JSONException) {
//                //println(e.localizedMessage)
//                model.data[key]!!["value"] = -1
//                model.data[key]!!["show"] = "未提供"
//            }
//        } else if (type == "String") {
//            var value: String = ""
//            try {
//                value = tmp.getString(key)
//            } catch (e: JSONException) {
//
//            }
//            if (key == FEATURED_KEY) {
//                if (value.isNotEmpty()) {
//                    value = BASE_URL + value
//                }
//            } else {
//                if (value.isEmpty() || value == "null") value = "未提供"
//            }
////            println("$key => $value")
//            model.data[key]!!["value"] = value
//            model.data[key]!!["show"] = value
//            if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
//                model.updateTime(key, value)
//            }
//        } else if (type == "array") {
//            if (key == CITY_KEY) {
//                try {
//                    val obj1 = tmp.getJSONObject(key)
//                    val id = obj1.getInt("id")
//                    val name = obj1.getString("name")
//                    val city = City(id, name)
//                    model.updateCity(city)
//                } catch (e: JSONException) {
//                    println(e.localizedMessage)
//                }
//            } else if (key == ARENA_KEY) {
//                try {
//                    val obj1 = tmp.getJSONObject(key)
//                    val id = obj1.getInt("id")
//                    val name = obj1.getString("name")
//                    val arena = Arena(id, name)
//                    model.updateArena(arena)
//                } catch (e: JSONException) {
//
//                }
//            } else if (key == TEAM_WEEKDAYS_KEY) {
//                try {
//                    val tmp1: JSONArray = tmp.getJSONArray(key)
//                    var days: ArrayList<Int> = arrayListOf<Int>()
//                    for (i in 0..tmp1.length() - 1) {
//                        val obj: JSONObject = tmp1.getJSONObject(i)
//                        val day = obj.getInt("weekday")
//                        days.add(day)
//                    }
//                    model.updateWeekdays(days)
//                } catch (e: JSONException) {
//
//                }
//            } else if (key == TEAM_DEGREE_KEY) {
//                try {
//                    val tmp1: String = tmp.getString(key)
//                    if (tmp1.count() > 0 && tmp1 != "null") {
//                        //println("tmp1: $tmp1")
//                        var degrees: ArrayList<DEGREE> = arrayListOf()
//                        val _degrees = tmp1.split(",")
//                        //println("degrees: $degrees")
//                        _degrees.forEach {
//                            degrees.add(DEGREE.fromEnglish(it))
//                        }
//                        model.updateDegree(degrees)
//                    }
//                } catch (e: JSONException) {
//
//                }
//            }
//        }
//    }
}