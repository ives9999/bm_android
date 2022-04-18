package com.sportpassword.bm.Services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.onesignal.OneSignal
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

/**
 * Created by ives on 2018/2/14.
 */
open class DataService {
    
    var success: Boolean = false
    var msg: String = ""
    var id: Int = 0
    var totalCount: Int = 0
    var page: Int = 0
    var perPage: Int = 0
//    var superDataLists: ArrayList<SuperData> = arrayListOf()
//    open val model: SuperData = SuperData(-1, "", "", "")
    lateinit var data: MutableMap<String, MutableMap<String, Any>>
    var citys: ArrayList<City> = arrayListOf()
    var arenas: ArrayList<ArenaTable> = arrayListOf()
    var citysandarenas: HashMap<Int, HashMap<String, Any>> = hashMapOf()
    var citysandareas: HashMap<Int, HashMap<String, Any>> = hashMapOf()
    lateinit var timetables: Timetables

    //var superModel: SuperModel = SuperModel(JSONObject())

    var jsonString: String = ""

    var image: Bitmap? = null

    var able: CourseTable = CourseTable() // for signup list able model
    var signup_date: JSONObject = JSONObject()//signup_date use

    val okHttpClient = OkHttpClient()

    //open fun <T1: Table, T2: Tables<T1>> getList1(context: Context, token: String?, _filter: HashMap<String, Any>?, page: Int, perPage: Int, complete: CompletionHandler) {

//    inline fun <reified T: Tables> list(context: Context, token: String?, _filter: HashMap<String, Any>?, page: Int, perPage: Int, complete: CompletionHandler) = getList2(T, context, token, _filter, page, perPage)
//
//    open fun getList2(t: Tables, context: Context, token: String?, _filter: HashMap<String, Any>?, page: Int, perPage: Int) {
//
//    }

    open fun delete(context: Context, type: String, token: String, status: String = "trash", complete: CompletionHandler) {
        val url = getDeleteURL()
        //println(url)
        val body = JSONObject()
        body.put("device", "app")
        body.put("channel", "bm")
        body.put("token", token)
        body.put("type", type)
        body.put("status", status)
        val requestBody = body.toString()
        //println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                println(e.localizedMessage)
                success = false
                msg = "無法刪除，請稍後再試 " + e.localizedMessage
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

    fun deleteTT(context: Context, type: String, params:HashMap<String, String>, completion: CompletionHandler) {
        val body = JSONObject()
        body.put("source", "app")
        body.put("channel", CHANNEL)
        for ((key, value) in params) {
            body.put(key, value)
        }
        val requestBody = body.toString()
        //print(body)
        val url = "$URL_TT_DELETE".format(type)
        //print(url)
        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println("json: " + json)
            try {

                success = json.getBoolean("success")
                if (!success) {
                    msg = json.getString("msg")
                } else {
//                    timetables = JSONParse.parse<Timetables>(json)!!
//                    for (row in timetables.rows) {
//                        row.filterRow()
//                    }
                }
                //println(json)
//                println(data)
            } catch (e: JSONException) {
                println("parse data error: " + e.localizedMessage)
                success = false
                msg = "無法getOne，沒有傳回成功值 " + e.localizedMessage
            }
            completion(success)

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

    fun getArenaByCityID(context: Context, city_id: Int, complete: CompletionHandler) {
        val url = URL_ARENA_BY_CITY_ID
        //println(url)

        val body = JSONObject()
        body.put("source", "app")
        body.put("city", city_id)
        val requestBody = body.toString()
        arenas = arrayListOf()

        val request = object : JsonArrayRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = true
                //println(json)
                for (i in 0..json.length()-1) {
                    val obj = json.getJSONObject(i)
                    val id: Int = obj.getInt("id")
                    val name: String = obj.getString("name")
                    val arena: ArenaTable = ArenaTable()
                    arena.id = id
                    arena.name = name
                    arenas.add(arena)
                }
            } catch (e: JSONException) {
                println(e.localizedMessage)
                success = false
                msg = "無法get Arenas，沒有傳回成功值 " + e.localizedMessage
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
        Volley.newRequestQueue(context).add(request)
    }

    fun getAreaByCityIDs(context: Context, city_ids: ArrayList<Int>, city_type:String, complete: CompletionHandler) {
        val url = URL_AREA_BY_CITY_IDS
//        println(url)

        val body = JSONObject()
        val arr: JSONArray = JSONArray()
        for (city_id in city_ids) {
            arr.put(city_id)
        }
        body.put("source", "app")
        body.put("channel", "bm")
        body.put("citys", arr)
        body.put("city_type", city_type)
        body.put("version", "1.2.5")
        val requestBody = body.toString()
//        println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //            println(json)
            try {
                citysandareas.clear()
                val keys = json.keys()
                for ((i, key) in keys.withIndex()) {
                    val obj = json.getJSONObject(key)
                    val city_id: Int = obj.getInt("id")
                    val city_name: String = obj.getString("name")
                    val _rows = obj.getJSONArray("rows")
                    var rows: ArrayList<HashMap<String, Any>> = arrayListOf()
                    for (j in 0.._rows.length()-1) {
                        val row = _rows.getJSONObject(j)
                        val area_id = row.getInt("id")
                        val area_name = row.getString("name")
                        rows.add(hashMapOf("id" to area_id, "name" to area_name))
                    }
                    citysandareas.put(city_id, hashMapOf<String, Any>("id" to city_id, "name" to city_name, "rows" to rows))
                }
                complete(true)
            } catch (e: JSONException) {
                println("exception: " + e.localizedMessage)
                msg = "無法get Arenas，沒有傳回成功值 " + e.localizedMessage
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

    fun getCitys(context: Context, type: String="all", zone: Boolean=false, complete: CompletionHandler) {
        val url = URL_CITYS
//        println(url)

        val body = JSONObject()
        body.put("source", "app")
        body.put("channel", "bm")
        body.put("type", type)
        body.put("zone", zone)
        val requestBody = body.toString()
        citys = arrayListOf()

        val request = object : JsonArrayRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = true
                //println(json)
                for (i in 0..json.length()-1) {
                    val obj = json.getJSONObject(i)
                    val id: Int = obj.getInt("id")
                    val name: String = obj.getString("name")
                    citys.add(City(id, name))
                }
            } catch (e: JSONException) {
                println(e.localizedMessage)
                success = false
                msg = "無法get Citys，沒有傳回成功值 " + e.localizedMessage
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
        Volley.newRequestQueue(context).add(request)
    }

    open fun getDeleteURL(): String {return URL_DELETE}
    open fun getIsNameExistUrl(): String {return ""}
    open fun getLikeURL(token: String): String {return URL_LIST}

    open fun getList(context: Context, token: String?, _params: HashMap<String, String>?, page: Int, perPage: Int, complete: CompletionHandler) {
        var url = getListURL()
        if (token != null) {
            url = url + "/" + token
        }
        //println(url)

//        val header: MutableList<Pair<String, String>> = mutableListOf()
//        header.add(Pair("Accept","application/json"))
//        header.add(Pair("Content-Type","application/json; charset=utf-8"))

        var params: HashMap<String, String>? = _params
        if (params == null) {
            params = hashMapOf()
        }
        params.put("device", "app")
        params.put("channel", "bm")
        params.put("page", page.toString())
        params.put("perPage", perPage.toString())

        if (!params.containsKey("status")) {
            params.put("status", "online")
        }

        if (member.isLoggedIn) {
            params.put("member_token", member.token!!)
        }

        val j: JSONObject = JSONObject(params as Map<*, *>)
        //println(j.toString())
//        val body = j.toString().toRequestBody(HEADER.toMediaTypeOrNull())

        val request: okhttp3.Request = getRequest(url, params)
//        val request = okhttp3.Request.Builder()
//            .url(url)
//            .headers()
//            .post(body)
//            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

                try {
                    jsonString = response.body!!.string()
//                    println(jsonString)
                    success = true
                } catch (e: Exception) {
                    success = false
                    msg = "parse json failed，請洽管理員"
                    println(e.localizedMessage)
                }
                complete(success)
            }
        })

//        val objectMapper = ObjectMapper()
//        val body: String = objectMapper.writeValueAsString(params)
//        MyHttpClient.instance.post(context, url, body) { success ->
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//                        this.jsonString = response.toString()
//                        this.success = true
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
    }

    open fun getListURL(): String {return URL_LIST}

    open fun getOne(context: Context, params: HashMap<String, String>, complete: CompletionHandler) {

        val url = getOneURL()
//        println(url)

//        val header: MutableList<Pair<String, String>> = mutableListOf()
//        header.add(Pair("Accept","application/json"))
//        header.add(Pair("Content-Type","application/json; charset=utf-8"))

        params.put("strip_html", "false")
        params.put("device", "app")
//        val objectMapper = ObjectMapper()
//        val body: String = objectMapper.writeValueAsString(params)
//        println(params)

        val request: okhttp3.Request = getRequest(url, params)
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

                try {
                    jsonString = response.body!!.string()
//                    println(jsonString)
                    success = true
                } catch (e: Exception) {
                    success = false
                    msg = "parse json failed，請洽管理員"
                    println(e.localizedMessage)
                }
                complete(success)
            }
        })


//        MyHttpClient.instance.post(context, url, body) { success ->
//
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//                        jsonString = response.toString()
//                        //println(jsonString)
//
//                        this.success = true
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
    }

    open fun getOneURL(): String { return URL_ONE }

    protected fun getPlayerID(): String {
        var playerID = ""
        val deviceState = OneSignal.getDeviceState()
        if (deviceState != null) {
            playerID = deviceState.userId
        }
        return playerID
    }

    fun getRequest(url: String, params: Map<String, String>): okhttp3.Request {

        val j: JSONObject = JSONObject(params as Map<*, *>)
        val body: RequestBody = j.toString().toRequestBody(HEADER.toMediaTypeOrNull())

        return okhttp3.Request.Builder()
            .url(url)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .post(body)
            .build()
    }

    open fun getSignupDateURL(token: String): String { return ""}
    open fun getSignupListURL(token: String? = null): String { return ""}
    open fun getSignupURL(token: String): String { return ""}

    fun getTT(context: Context, token: String, type:String, complete: CompletionHandler) {
        val url = "$URL_TT".format(type)
//        println(url)

        val body = JSONObject()
        body.put("source", "app")
        body.put("channel", "bm")
        body.put("token", token)
        val requestBody = body.toString()
//        println(requestBody)
        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            println("json: " + json)
            try {
//                timetables = JSONParse.parse<Timetables>(json)!!
//                for (row in timetables.rows) {
//                    row.filterRow()
//                }
////                timetables.print()
//                if (!timetables.success) {
//                    msg = json.getString("msg")
//                }
                complete(timetables.success)
            } catch (e: JSONException) {
                println("parse data error: " + e.localizedMessage)
                success = false
                msg = "無法getOne，沒有傳回成功值 " + e.localizedMessage
            }
            if (this.success) {
                //jsonToMember(json)
            } else {
                //DataService.makeErrorMsg(json)
            }
            //complete(true)
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

    open fun getUpdateURL(): String {return ""}

    fun isNameExist(context: Context, name: String, complete: CompletionHandler) {
        val url: String = getIsNameExistUrl()
        //println(url)
        val params: HashMap<String, String> = hashMapOf("device" to "app", "channel" to CHANNEL, "name" to name, "member_token" to member.token!!)
        //println(params)

        val request: okhttp3.Request = getRequest(url, params)
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

                try {
                    jsonString = response.body!!.string()
                    //println(jsonString)
                    success = true
                } catch (e: Exception) {
                    success = false
                    msg = "parse json failed，請洽管理員"
                    println(e.localizedMessage)
                }
                complete(success)
            }
        })
    }

//    open fun getList(context: Context, token: String?, _filter: HashMap<String, Any>?, page: Int, perPage: Int, complete: CompletionHandler) {
//
//        var url = getListURL()
//        if (token != null) {
//            url = url + "/" + token
//        }
//        //println(url)
//
//        val header: MutableList<Pair<String, String>> = mutableListOf()
//        header.add(Pair("Accept","application/json"))
//        header.add(Pair("Content-Type","application/json; charset=utf-8"))
//
//        val filter: HashMap<String, Any>?
//        if (_filter == null) {
//            filter = hashMapOf()
//        } else {
//            filter = _filter
//        }
//        filter.put("source", "app")
//        filter.put("channel", "bm")
//        filter.put("status", "online")
//        filter.put("page", page)
//        filter.put("perPage", perPage)
//
//        val body = filter.toJSONString()
//        //println(body)
//
//        MyHttpClient.instance.post(context, url, body) { success ->
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//
//                        //val s = Gson().fromJson<SuperCourses>(response.toString(), SuperCourses::class.java)
//
//                        val json = JSONObject(response.toString())
//                        //println(json)
//                        superModel = parseModels(json)
//                        //superCourses = JSONParse.parse<SuperCourses>(json)!!
////                        for (row in superCourses.rows) {
////                            val citys = row.coach.citys
////                            for (city in citys) {
////                                city.print()
////                            }
////                        }
//                        this.success = true
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

//    fun getList(context: Context, type:String, titleField:String, params: HashMap<String,Any>, page:Int, perPage:Int, filter:Array<Array<Any>>?, complete:CompletionHandler) {
//        val url = "$URL_LIST".format(type)
//        val header: MutableList<Pair<String, String>> = mutableListOf()
//        header.add(Pair("Accept","application/json"))
//        header.add(Pair("Content-Type","application/json; charset=utf-8"))
//
//        val body = JSONObject()
//        body.put("source", "app")
//        body.put("channel", "bm")
//        body.put("page", page.toString())
//        body.put("perPage", perPage.toString())
//
//        for ((key, value) in params) {
//            var valueStr: String = ""
//            when (key) {
//                "city_id" -> {
//                    var arr: JSONArray = JSONArray(value as ArrayList<Int>)
//                    body.put(key, arr)
//                }
//                "city_type" -> {
//                    body.put(key, value)
//                }
//                "area_id" -> {
//                    var arr: JSONArray = JSONArray(value as ArrayList<Int>)
//                    body.put(key, arr)
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
//                ARENA_AIR_CONDITION_KEY -> {
//                    body.put(key, value)
//                }
//                ARENA_BATHROOM_KEY -> {
//                    body.put(key, value)
//                }
//                ARENA_PARKING_KEY -> {
//                    body.put(key, value)
//                }
//            }
//        }
//        if (filter != null) {
//            val whereArr = JSONArray()
//            for (i in filter.indices) {
//                val operateArr = JSONArray()
//                for (item in filter[i]) {
//                    operateArr.put(item)
//                }
//                whereArr.put(operateArr)
//            }
//            body.put("where", whereArr)
//        }
//        MyHttpClient.instance.post(context, url, body.toString()) { success ->
//            if (success) {
////                superDataLists.clear()
//                val response = MyHttpClient.instance.response
//                if (response != null) {
////                    println(response.toString())
//                    try {
//                        val json = JSONObject(response.toString())
//                        this.success = true
//                        this.totalCount = json.getInt("totalCount")
//                        this.page = json.getInt("page")
//                        this.perPage = json.getInt("perPage")
//                        val rows = json.getJSONArray("rows")
////                        println(rows)
//                        for (i in 0..rows.length() - 1) {
//                            val obj = rows.getJSONObject(i)
//                            val title = obj.getString(titleField)
//                            val token = obj.getString("token")
//                            var vimeo = if (obj.has("vimeo")) obj.getString("vimeo") else ""
//                            var youtube = if (obj.has("youtube")) obj.getString("youtube").toString() else ""
//                            val id = obj.getInt("id")
//                            var featured_path = if (obj.has("featured_path")) obj.get("featured_path").toString() else ""
//                            //println(featured_path)
//                            if (featured_path.isNotEmpty()) {
//                                if (!featured_path.startsWith("http://") && !featured_path.startsWith("https://")) {
//                                    featured_path = BASE_URL + featured_path
//                                }
//                            }
//
//                        }
//                    } catch (e: Exception) {
//                        this.success = false
//                        msg = "parse json failed，請洽管理員"
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

    fun like(context: Context, token: String, able_id: Int) {

        val url: String = getLikeURL(token)
//        println(url)

        val params: HashMap<String, String> = hashMapOf(
            "member_token" to member.token!!,
            "able_id" to able_id.toString(),
            "device" to "app"
        )
//        println(params)
//        val objectMapper = ObjectMapper()
//        val body: String = objectMapper.writeValueAsString(params)
//
//        println(body)
//
//        val header: MutableList<Pair<String, String>> = mutableListOf()
//        header.add(Pair("Accept","application/json"))
//        header.add(Pair("Content-Type","application/json; charset=utf-8"))
//
//        MyHttpClient.instance.post(context, likeUrl, body) {
//
//        }

        val request: okhttp3.Request = getRequest(url, params)
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
//                msg = "網路錯誤，無法跟伺服器更新資料"
//                complete(success)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

//                try {
//                    jsonString = response.body!!.string()
////                    println(jsonString)
//                    success = true
//                } catch (e: Exception) {
//                    success = false
//                    msg = "parse json failed，請洽管理員"
//                    println(e.localizedMessage)
//                }
//                complete(success)
            }
        })
    }

    fun managerSignupList(able_type: String, able_token: String, page: Int, perPage: Int, complete: CompletionHandler) {

        val url: String = URL_MANAGER_SIGNUPLIST.format(able_type)
        //println(url)

        val params: HashMap<String, String> = hashMapOf(
            "channel" to CHANNEL,
            "device" to "app",
            "able_token" to able_token,
            "manager_token" to member.token!!,
            "page" to page.toString(),
            "perPage" to perPage.toString()
        )
        //println(params)

        val request: okhttp3.Request = getRequest(url, params)

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
//                msg = "網路錯誤，無法跟伺服器更新資料"
//                complete(success)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                try {
                    jsonString = response.body!!.string()
                    //println(jsonString)
                    success = true
                } catch (e: Exception) {
                    success = false
                    msg = "parse json failed，請洽管理員"
                    println(e.localizedMessage)
                }
                complete(success)
            }
        })
    }

//    open fun parseModel(json: JSONObject): SuperModel {return SuperModel(JSONObject())}
//    open fun parseModels(json: JSONObject): SuperModel {return SuperModel(JSONObject())}
//    open fun jsonToMember(json: JSONObject, context: Context){}

//    open fun getOne(context: Context, type:String, titleField:String, token:String, complete: CompletionHandler) {
//        val url = "$URL_ONE".format(type)
//        val params: MutableList<Pair<String, String>> = mutableListOf()
//        params.add(Pair("source", "app"))
//        params.add(Pair("token", token))
//        params.add(Pair("strip_html", true.toString()))
////        println(url)
////        println(params)
//
//        MyHttpClient.instance.post(context, url, params, null) { success ->
//            val response = MyHttpClient.instance.response
//            if (response != null) {
//                val responseStr = response.toString()
//                //println(responseStr)
//                val json = JSONObject(responseStr)
//                try {
//                    this.success = true
//                    //println(json)
////                    model.dataReset()
//                    data = mutableMapOf()
////                    dealOne(json)
////                    data = model.data
////                println(data)
//                } catch (e: JSONException) {
//                    println("parse data error: " + e.localizedMessage)
//                    this.success = false
//                    msg = "無法getOne，沒有傳回成功值 " + e.localizedMessage
//                }
//                if (this.success) {
//                    //jsonToMember(json)
//                } else {
//                    //DataService.makeErrorMsg(json)
//                }
//                complete(true)
//            }
//        }
//    }

    //open fun getOne(context: Context, id: Int, source: String, token: String, completion: CompletionHandler) {}

//    open fun getOne(context: Context, params: HashMap<String, String>, complete: CompletionHandler) {
//
//        val url = getOneURL()
//        //println(url)
//
//        val header: MutableList<Pair<String, String>> = mutableListOf()
//        header.add(Pair("Accept","application/json"))
//        header.add(Pair("Content-Type","application/json; charset=utf-8"))
//
//        val body = JSONObject()
//        //body.put("source", "app")
//        if (params.containsKey("token")) {
//            body.put("token", params["token"])
//        }
//        if (params.containsKey("member_token")) {
//            body.put("member_token", params["member_token"])
//        }
//        body.put("strip_html", false)
//        //println(body)
//
//        MyHttpClient.instance.post(context, url, body.toString()) { success ->
//
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//                        val j = response.toString()
//
//                        val json = JSONObject(response.toString())
////                        //println(json)
//
//                        superModel = parseModel(json)
////                        superCourse.print()
//                        this.success = true
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

    open fun requestManager(context: Context, _params: MutableMap<String, String>, filePaths: ArrayList<String>, complete: CompletionHandler) {

        val url: String = URL_REQUEST_MANAGER
        val params: Map<String, String> = _params.mergeWith(PARAMS)

        msg = ""

        val bodyBuilder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        for ((key, value) in params) {
            bodyBuilder.addFormDataPart(key, value)
        }

        for ((idx, filePath) in filePaths.withIndex()) {
            val file = File(filePath)
            if (file.exists()) {
                val filePart = file.asRequestBody("image/png".toMediaType())
                val withName: String = "image" + (idx + 1).toString()
                bodyBuilder.addFormDataPart(withName, file.name, filePart)
            }
        }

        val body: RequestBody = bodyBuilder.build()

        val request = okhttp3.Request.Builder()
            .addHeader("Content-Type", "multipart/form-data")
            .url(url)
            .post(body)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

                try {
                    jsonString = response.body!!.string()
                    //println(jsonString)
                    success = true
                } catch (e: Exception) {
                    success = false
                    msg = "parse json failed，請洽管理員"
                    println(e.localizedMessage)
                }
                complete(success)
            }
        })
    }

    fun signup(context: Context, token: String, member_token: String, date_token: String, complete: CompletionHandler) {
        val url = getSignupURL(token)
//        println(url)
//        val jsonString: String = "{\"device\": \"app\", \"channel\": \"bm\", \"member_token\": " + member_token + ", \"signup_id\": " + signup_id.toString() + ", \"course_date\": " + course_date + ", \"course_deadline\": " + course_deadline + "}"
        val player_id: String = getPlayerID()
        val params: HashMap<String, String> = hashMapOf()
        params.put("device", "app")
        params.put("channel", "bm")
        params.put("member_token", member_token)
        params.put("able_date_token", date_token)
        params.put("player_id", player_id)
//        params.put("cancel_deadline", course_deadline)
//        println(params)

        val request: okhttp3.Request = getRequest(url, params)
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

                try {
                    jsonString = response.body!!.string()
//                    println(jsonString)
                    success = true
                } catch (e: Exception) {
                    success = false
                    msg = "parse json failed，請洽管理員"
                    println(e.localizedMessage)
                }
                complete(success)
            }
        })

//        MyHttpClient.instance.post(context, url, body.toString()) { success ->
//
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//                        val json = JSONObject(response.toString())
////                        println(json)
//                        this.success = json.getBoolean("success")
//                        if (json.has("msg")) {
//                            this.msg = json.getString("msg")
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
    }

    fun signup_date(context: Context, token: String, member_token: String, date_token: String, complete: CompletionHandler) {
        val url = getSignupDateURL(token)
//        println(url)

        val params: HashMap<String, String> = hashMapOf()
        params.put("device", "app")
        params.put("channel", "bm")
        params.put("member_token", member_token)
        params.put("date_token", date_token)

        val request: okhttp3.Request = getRequest(url, params)
//        val j: JSONObject = JSONObject(params as Map<*, *>)
//        println(j.toString())


//        val j: String = "{\"device\": \"app\", \"channel\": \"bm\", \"member_token\": " + member_token + ",\"date_token\":" + date_token + "}"
//        val body1: JSONObject = JSONObject(j)
//        println(body1)
//        val body: RequestBody = j.toString().toRequestBody(HEADER.toMediaTypeOrNull())
//        val request = okhttp3.Request.Builder()
//            .url(url)
//            .addHeader("Accept", "application/json")
//            .addHeader("Content-Type", "application/json; charset=utf-8")
//            .post(body)
//            .build()

        //val request: okhttp3.Request = getRequest(url, params)
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

                try {
                    jsonString = response.body!!.string()
//                    println(jsonString)
                    success = true
                } catch (e: Exception) {
                    success = false
                    msg = "parse json failed，請洽管理員"
                    println(e.localizedMessage)
                }
                complete(success)
            }
        })

//        MyHttpClient.instance.post(context, url, body.toString()) { success ->
//
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//                        val json = JSONObject(response.toString())
//                        //println(json)
//                        this.success = json.getBoolean("success")
//                        if (this.success) {
//                            this.signup_date = json
//                        } else {
//                            this.msg = json.getString("msg")
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
//                msg = "從伺服器取得報名日期資料錯誤，請洽管理員"
//                complete(success)
//            }
//        }
    }

    open fun update(context: Context, _params: MutableMap<String, String>, filePath: String, complete: CompletionHandler) {
//        jsonString = ""
        val url: String = getUpdateURL()
        //println(url)


//        val header: MutableList<Pair<String, String>> = mutableListOf()
//        header.add(Pair("Accept","application/json"))
//        header.add(Pair("Content-Type","application/json"))

        val params: Map<String, String> = _params.mergeWith(PARAMS)
//        var jsonString1: String = "{"
//        val tmps: ArrayList<String> = arrayListOf()
//        for ((key, value) in params) {
//            tmps.add("\"$key\":\"$value\"")
//        }
//        jsonString1 += tmps.joinToString(",")
//        jsonString1 += "}"
//        println(jsonString1)


        //val j: JSONObject = JSONObject(params as Map<*, *>)
        //println(j)

        val bodyBuilder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        for ((key, value) in params) {
            bodyBuilder.addFormDataPart(key, value)
        }

        val file: File = File(filePath)
        if (file.exists()) {
            val filePart = file.asRequestBody("image/png".toMediaType())
            bodyBuilder.addFormDataPart("file", file.name, filePart)
        }

        val body: RequestBody = bodyBuilder.build()

//        val formPart: RequestBody = j.toString().toRequestBody(MULTIPART.toMediaTypeOrNull())

//        val filePart: RequestBody = file.asRequestBody("image/png".toMediaType())

//        val body: RequestBody = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addPart(filePart)
//            .addPart(formPart)
//            .build()

        val request = okhttp3.Request.Builder()
            .addHeader("Content-Type", "multipart/form-data")
            .url(url)
            .post(body)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

                try {
                    jsonString = response.body!!.string()
                    println(jsonString)
                    success = true
                } catch (e: Exception) {
                    success = false
                    msg = "parse json failed，請洽管理員"
                    println(e.localizedMessage)
                }
                complete(success)
            }
        })


//        val params: Map<String, String> = _params.let { map1 ->
//            PARAMS.let { map2 ->
//                map1 + map2
//            }
//        }
        //println(_params)

//        val params1: MutableList<Pair<String, String>> = mutableListOf()
//        var jsonString1: String = "{"
//        val tmps: ArrayList<String> = arrayListOf()
//        for ((key, value) in params) {
//
//            params1.add(Pair(key, value))
//            tmps.add("\"$key\":\"$value\"")
//        }
//        jsonString1 += tmps.joinToString(",")
//        jsonString1 += "}"
////        println(jsonString1)
//
//        var filePaths: ArrayList<String>? = null
//        if (filePath.isNotEmpty()) {
//            filePaths = arrayListOf()
//            filePaths.add(filePath)
//        }
//
//        MyHttpClient.instance.uploadFile(context, url, null, filePaths, params1, header) { success ->
//
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//
//                    try {
//                        jsonString = response.toString()
////                        println(jsonString)
//                        this.success = true
//                    } catch (e: Exception) {
//                        this.success = false
//                        msg = "update parse json failed，請洽管理員"
//                    }
//                    complete(true)
//                } else {
//                    msg = "伺服器沒有傳回任何值，更新失敗，請洽管理員"
//                    complete(false)
//                }
//
//            } else {
//                msg = "網路錯誤，無法跟伺服器更新資料"
//                complete(success)
//            }
//        }
    }

    fun update(context: Context, token: String = "", params: HashMap<String, String>, complete: CompletionHandler) {

        jsonString = ""
        val url: String = getUpdateURL()
//        println(url)

//        val header: MutableList<Pair<String, String>> = mutableListOf()
//        header.add(Pair("Accept","application/json"))
//        header.add(Pair("Content-Type","application/json; charset=utf-8"))

//        val body = JSONObject()
//        for ((key, value) in params) {
//            body.put(key, value)
//        }
//        println(body)

        val request: okhttp3.Request = getRequest(url, params)
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

                try {
                    jsonString = response.body!!.string()
//                    println(jsonString)
                    success = true
                } catch (e: Exception) {
                    success = false
                    msg = "parse json failed，請洽管理員"
                    println(e.localizedMessage)
                }
                complete(success)
            }
        })

//        MyHttpClient.instance.post(context, url, body.toString()) { success ->
//
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//                        jsonString = response.toString()
//                        this.success = true
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
    }

    fun update(context: Context, params: HashMap<String, String>, complete: CompletionHandler) {
        update(context, "", params, complete)
    }

//    fun getImage(url: String, completion: CompletionHandler) {
//        try {
//            val inStream: InputStream = URL(url).openStream()
//            image = BitmapFactory.decodeStream(inStream)
//            completion(true)
//        } catch (e: Exception) {
//            completion(false)
//        }
//    }

//    fun getAllCitys(context: Context, complete: CompletionHandler) {
//        val url = URL_CITYS
//        //println(url)
//
//        val body = JSONObject()
//        body.put("source", "app")
//        val requestBody = body.toString()
//        citys = arrayListOf()
//
//        val request = object : JsonArrayRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            //println(json)
//            try {
//                success = true
//                //println(json)
//                for (i in 0..json.length()-1) {
//                    val obj = json.getJSONObject(i)
//                    val id: Int = obj.getInt("id")
//                    val name: String = obj.getString("name")
//                    citys.add(City(id, name))
//                }
//            } catch (e: JSONException) {
//                println(e.localizedMessage)
//                success = false
//                msg = "無法get Citys，沒有傳回成功值 " + e.localizedMessage
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
//        Volley.newRequestQueue(context).add(request)
//    }

//    fun getArenaByCityIDs(context: Context, city_ids: ArrayList<Int>, city_type:String, complete: CompletionHandler) {
//        val url = URL_ARENA_BY_CITY_IDS
////        println(url)
//
//        val body = JSONObject()
//        var arr: JSONArray = JSONArray()
//        for (city_id in city_ids) {
//            arr.put(city_id)
//        }
//        body.put("source", "app")
//        body.put("channel", "bm")
//        body.put("citys", arr)
//        body.put("city_type", city_type)
//        body.put("version", "1.2.5")
//        val requestBody = body.toString()
////        println(requestBody)
//
//        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
////            println(json)
//            try {
//                citysandarenas.clear()
//                val keys = json.keys()
//                for ((i, key) in keys.withIndex()) {
//                    val obj = json.getJSONObject(key)
//                    val city_id: Int = obj.getInt("id")
//                    val city_name: String = obj.getString("name")
//                    val _rows = obj.getJSONArray("rows")
//                    var rows: ArrayList<HashMap<String, Any>> = arrayListOf()
//                    for (j in 0.._rows.length()-1) {
//                        val row = _rows.getJSONObject(j)
//                        val arena_id = row.getInt("id")
//                        val arena_name = row.getString("name")
//                        rows.add(hashMapOf("id" to arena_id, "name" to arena_name))
//                    }
//                    citysandarenas.put(city_id, hashMapOf<String, Any>("id" to city_id, "name" to city_name, "rows" to rows))
//                }
//                complete(true)
//            } catch (e: JSONException) {
//                println("exception: " + e.localizedMessage)
//                msg = "無法get Arenas，沒有傳回成功值 " + e.localizedMessage
//                complete(false)
//            }
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
//        Volley.newRequestQueue(context).add(request)
//    }

    fun updateTT(context: Context, type: String, params:HashMap<String, String>, completion: CompletionHandler) {
        val body = JSONObject()
        body.put("source", "app")
        body.put("channel", CHANNEL)
        for ((key, value) in params) {
            body.put(key, value)
        }
        val requestBody = body.toString()
        //println(body)
        val url = "$URL_TT_UPDATE".format(type)
        //println(url)
        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            println("json: " + json)
            try {

                success = json.getBoolean("success")
                if (!success) {
                    msg = json.getString("msg")
                } else {
//                    timetables = JSONParse.parse<Timetables>(json)!!
//                    for (row in timetables.rows) {
//                        row.filterRow()
//                    }
                }
                //println(json)
//                println(data)
            } catch (e: JSONException) {
                println("parse data error: " + e.localizedMessage)
                success = false
                msg = "無法getOne，沒有傳回成功值 " + e.localizedMessage
            }
            completion(success)

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

//    open fun parseAbleForSingupList(data: JSONObject): SuperModel { return SuperModel(data) }


//    {
//        "success": true,
//        "isSignup": false, 報名過了嗎？
//        "cancel": false,
//        "date": "2021-06-22",
//        "deadline": "2021-06-22 15:00:00",
//        "standby": true, 可以候補嗎？
//        "isStandby": true,
//        "cantSingup": false, 不能報名
//        "msg": ""
//    }

//    fun signup_list(context: Context, token: String? = null, page: Int = 1, perPage: Int = 8, complete: CompletionHandler) {
//        val url: String = getSignupListURL(token)
//        //print(url)
//        val jsonString: String = "{\"device\": \"app\", \"channel\": \"bm\", \"page\": " + page.toString() + ", \"perPage\": " + perPage + "}"
//        val body: JSONObject = JSONObject(jsonString)
//        //print(body)
//
//        MyHttpClient.instance.post(context, url, body.toString()) { success ->
//
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//                        val json = JSONObject(response.toString())
////                        println(json)
//                        if (json.has("able")) {
//                            able = parseAbleForSingupList(json.getJSONObject("able"))
//                            //able.print()
//                        }
//                        //val s: SuperSignups = JSONParse.parse<SuperSignups>(json)!!
////                        for (i in 0..s.rows.size-1) {
////                            val row = s.rows[i]
////                            row.print()
////                        }
//                        //superModel = s
//                        complete(true)
//                    } catch (e: Exception) {
//                        this.success = false
//                        msg = "parse json failed，請洽管理員"
//                        println(e.localizedMessage)
//                        complete(false)
//                    }
//                } else {
//                    println("response is null")
//                    complete(false)
//                }
//            } else {
//                msg = "網路錯誤，無法跟伺服器更新資料"
//                complete(success)
//            }
//        }
//    }

//    fun signup(context: Context, type: String, token: String, member_token: String, tt_id: Int, complete: CompletionHandler) {
//        val url = "$URL_SIGNUP".format(type, token)
        //println(url)
//        val body = JSONObject()
//        body.put("source", "app")
//        body.put("channel", "bm")
//        body.put("member_token", member_token)
//        body.put("tt_id", tt_id)
//        val requestBody = body.toString()
        //println(requestBody)

        /*
        val client = OkHttpClient()
        val body1 = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestBody)
        val request1 = okhttp3.Request.Builder()
                .url(url)
                .post(body1)
                .build()
        val call = client.newCall(request1)
        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val str = response.body()!!.string()
                println(str)
                val json = JSONObject(str)
                try {
                    success = json.getBoolean("success")
                } catch (e: JSONException) {
                    println(e.localizedMessage)
                    success = false
                    msg = "無法刪除球隊，請稍後再試 " + e.localizedMessage
                }
                if (!success) {
                    msg = json.getString("msg")
                }
                complete(success)
            }

            override fun onFailure(call: Call, e: IOException) {
                println(e.localizedMessage)
            }
        })
        */

        /*
        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            println(json)
            try {
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                println(e.localizedMessage)
                success = false
                msg = "無法刪除球隊，請稍後再試 " + e.localizedMessage
            }
            if (!success) {
                msg = json.getString("msg")
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
        */
//    }

//    fun cancelSignup(context: Context, type: String, member_token: String, signup_id: Int, complete: CompletionHandler) {
//        val url = "$URL_CANCEL_SIGNUP".format(type, signup_id)
//        println(url)
//        val body = JSONObject()
//        body.put("source", "app")
//        body.put("channel", "bm")
//        body.put("member_token", member_token)
//        val requestBody = body.toString()
//        println(requestBody)
//
//        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            //println(json)
//            try {
//                success = json.getBoolean("success")
//            } catch (e: JSONException) {
//                println(e.localizedMessage)
//                success = false
//                msg = "無法刪除球隊，請稍後再試 " + e.localizedMessage
//            }
//            if (!success) {
//                msg = json.getString("msg")
//            }
//            complete(success)
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
//        Volley.newRequestQueue(context).add(request)
//    }

//    open fun setData(id: Int, title: String, token: String, featured_path: String, vimeo: String, youtube: String): SuperData {
//        val data = SuperData(id, title, token, featured_path, vimeo, youtube)
//
//        return data
//    }
//
//    open fun setData1(row: JSONObject): MutableMap<String, MutableMap<String, Any>> {
//        val data: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()
//        return data
//    }
//
//    open fun dealOne(json: JSONObject) {}
//
//    open fun _jsonToData(tmp: JSONObject, key: String, item: Map<String, Any>){}
}

























