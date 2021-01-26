package com.sportpassword.bm.Services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.internal.Mutable
//import com.ohmerhe.kolley.request.Http
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Utilities.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import kotlin.reflect.KClass


/**
 * Created by ives on 2018/2/14.
 */
open class DataService: BaseService() {
    
    var success: Boolean = false
    var id: Int = 0
    var totalCount: Int = 0
    var page: Int = 0
    var perPage: Int = 0
    var superDataLists: ArrayList<SuperData> = arrayListOf()
    open val model: SuperData = SuperData(-1, "", "", "")
    lateinit var data: MutableMap<String, MutableMap<String, Any>>
    var citys: ArrayList<City> = arrayListOf()
    var arenas: ArrayList<Arena> = arrayListOf()
    var citysandarenas: HashMap<Int, HashMap<String, Any>> = hashMapOf()
    var citysandareas: HashMap<Int, HashMap<String, Any>> = hashMapOf()
    lateinit var timetables: Timetables

    var superModel: SuperModel = SuperModel(JSONObject())

    var image: Bitmap? = null

    var able: SuperModel = SuperModel(JSONObject()) // for signup list able model
    var signup_date: JSONObject = JSONObject()//signup_date use

    open fun getList(context: Context, token: String?, _filter: HashMap<String, Any>?, page: Int, perPage: Int, complete: CompletionHandler) {

        var url = getListURL()
        if (token != null) {
            url = url + "/" + token
        }
        //println(url)

        val header: MutableList<Pair<String, String>> = mutableListOf()
        header.add(Pair("Accept","application/json"))
        header.add(Pair("Content-Type","application/json; charset=utf-8"))

        var filter: HashMap<String, Any>?
        if (_filter == null) {
            filter = hashMapOf()
        } else {
            filter = _filter
        }
        filter.put("source", "app")
        filter.put("channel", "bm")
        filter.put("status", "online")
        filter.put("page", page)
        filter.put("perPage", perPage)

        val body = filter.toJSONString()
        //println(body)

        MyHttpClient.instance.post(context, url, body) { success ->
            if (success) {
                val response = MyHttpClient.instance.response
                if (response != null) {
                    try {
                        val json = JSONObject(response.toString())
                        //println(json)
                        superModel = parseModels(json)
                        //superCourses = JSONParse.parse<SuperCourses>(json)!!
//                        for (row in superCourses.rows) {
//                            val citys = row.coach.citys
//                            for (city in citys) {
//                                city.print()
//                            }
//                        }
                        this.success = true
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

    fun getList(context: Context, type:String, titleField:String, params: HashMap<String,Any>, page:Int, perPage:Int, filter:Array<Array<Any>>?, complete:CompletionHandler) {
        val url = "$URL_LIST".format(type)
        //println(url)
//        println(params)

//        params.put("city_type", "simple")
//        params.put("city_id", arrayListOf(11))
        val header: MutableList<Pair<String, String>> = mutableListOf()
        header.add(Pair("Accept","application/json"))
        header.add(Pair("Content-Type","application/json; charset=utf-8"))

//        val params1: MutableList<Pair<String, String>> = mutableListOf()
        val body = JSONObject()
        body.put("source", "app")
        body.put("channel", "bm")
        body.put("page", page.toString())
        body.put("perPage", perPage.toString())
//        params1.add(Pair("source", "app"))
//        params1.add(Pair("channel", "bm"))
//        params1.add(Pair("page", page.toString()))
//        params1.add(Pair("perPage", perPage.toString()))

        for ((key, value) in params) {
            var valueStr: String = ""
            when (key) {
                "city_id" -> {
//                    valueStr += """["""
//                    val valueArr = value as ArrayList<Int>
//                    var i = 1
//                    for (id in valueArr) {
//                        valueStr += """${id}"""
//                        if (i < valueArr.size) {
//                            valueStr += ""","""
//                        }
//                        i++
//                    }
//                    valueStr += """]"""
//                    params1.add(Pair(key, valueStr))
                    var arr: JSONArray = JSONArray(value as ArrayList<Int>)
                    body.put(key, arr)
                }
                "city_type" -> {
                    body.put(key, value)
                }
                "area_id" -> {
                    var arr: JSONArray = JSONArray(value as ArrayList<Int>)
                    body.put(key, arr)
                }
                "play_days" -> {
                    var arr: JSONArray = JSONArray(value as ArrayList<Int>)
                    body.put(key, arr)
                }
                "play_time" -> {
                    body.put(key, value)
                }
                "use_date_range" -> {
                    body.put(key, value)
                }
                "arena_id" -> {
                    var arr: JSONArray = JSONArray(value as ArrayList<Int>)
                    body.put(key, arr)
                }
                "degree" -> {
                    var arr: JSONArray = JSONArray(value as ArrayList<String>)
                    body.put(key, arr)
                }
                "k" -> {
                    body.put(key, value)
                }
                ARENA_AIR_CONDITION_KEY -> {
                    body.put(key, value)
                }
                ARENA_BATHROOM_KEY -> {
                    body.put(key, value)
                }
                ARENA_PARKING_KEY -> {
                    body.put(key, value)
                }
            }
        }
        if (filter != null) {
            val whereArr = JSONArray()
            for (i in filter.indices) {
                val operateArr = JSONArray()
                for (item in filter[i]) {
                    operateArr.put(item)
                }
                whereArr.put(operateArr)
            }
            body.put("where", whereArr)
        }
        //println(params1)
//        println(body.toString())
//        val requestBody = MyHttpClient.instance.toJsonString(body, filter)
        //println(requestBody)
        //println("coach getList refresh: $refresh")

//        val params1: MutableList<Pair<String, String>> = mutableListOf()
//        val keys = body.keys()
//        while (keys.hasNext()) {
//            val key = keys.next()
//            val value = body.getString(key)
//            params1.add(Pair(key, value))
//        }
        //println(params1)
        MyHttpClient.instance.post(context, url, body.toString()) { success ->
            if (success) {
                superDataLists.clear()
                val response = MyHttpClient.instance.response
                if (response != null) {
//                    println(response.toString())
                    try {
                        val json = JSONObject(response.toString())
                        this.success = true
                        this.totalCount = json.getInt("totalCount")
                        this.page = json.getInt("page")
                        this.perPage = json.getInt("perPage")
                        val rows = json.getJSONArray("rows")
//                        println(rows)
                        for (i in 0..rows.length() - 1) {
                            val obj = rows.getJSONObject(i)
                            val title = obj.getString(titleField)
                            val token = obj.getString("token")
                            var vimeo = if (obj.has("vimeo")) obj.getString("vimeo") else ""
                            var youtube = if (obj.has("youtube")) obj.getString("youtube").toString() else ""
                            val id = obj.getInt("id")
                            var featured_path = if (obj.has("featured_path")) obj.get("featured_path").toString() else ""
                            //println(featured_path)
                            if (featured_path.isNotEmpty()) {
                                if (!featured_path.startsWith("http://") && !featured_path.startsWith("https://")) {
                                    featured_path = BASE_URL + featured_path
                                }
                            }

                            //val dataList: SuperData = Coach(id, title, featured_path)
                            val data = setData(id, title, token, featured_path, vimeo, youtube)
                            val map = setData1(obj)
                            data.data = map
                            superDataLists.add(data)
                        }
                    } catch (e: Exception) {
                        this.success = false
                        msg = "parse json failed，請洽管理員"
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

        /*


        superDataLists = arrayListOf()

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
                    val title = obj.getString(titleField)
                    val token = obj.getString("token")
                    var vimeo = if (obj.has("vimeo")) obj.getString("vimeo") else ""
                    var youtube = if (obj.has("youtube")) obj.getString("youtube").toString() else ""
                    val id = obj.getInt("id")
                    var featured_path = if (obj.has("featured_path")) obj.get("featured_path").toString() else ""
                    //println(featured_path)
                    if (featured_path.isNotEmpty()) {
                        if (!featured_path.startsWith("http://") && !featured_path.startsWith("https://")) {
                            featured_path = BASE_URL + featured_path
                        }
                    }

                    //val dataList: SuperData = Coach(id, title, featured_path)
                    val data = setData(id, title, token, featured_path, vimeo, youtube)
                    val map = setData1(obj)
                    data.data = map
                    superDataLists.add(data)
                }
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

        Volley.newRequestQueue(context).add(request)
        */

    }

    open fun getListURL(): String {return URL_LIST}
    open fun getOneURL(): String {return URL_ONE}
    open fun parseModel(json: JSONObject): SuperModel {return SuperModel(JSONObject())}
    open fun parseModels(json: JSONObject): SuperModel {return SuperModel(JSONObject())}
    open fun jsonToMember(json: JSONObject, context: Context){}

    open fun getOne(context: Context, type:String, titleField:String, token:String, complete: CompletionHandler) {
        val url = "$URL_ONE".format(type)
        val params: MutableList<Pair<String, String>> = mutableListOf()
        params.add(Pair("source", "app"))
        params.add(Pair("token", token))
        params.add(Pair("strip_html", true.toString()))
//        println(url)
//        println(params)

        MyHttpClient.instance.post(context, url, params, null) { success ->
            val response = MyHttpClient.instance.response
            if (response != null) {
                val responseStr = response.toString()
                //println(responseStr)
                val json = JSONObject(responseStr)
                try {
                    this.success = true
                    //println(json)
                    model.dataReset()
                    data = mutableMapOf()
                    dealOne(json)
                    data = model.data
//                println(data)
                } catch (e: JSONException) {
                    println("parse data error: " + e.localizedMessage)
                    this.success = false
                    msg = "無法getOne，沒有傳回成功值 " + e.localizedMessage
                }
                if (this.success) {
                    //jsonToMember(json)
                } else {
                    //DataService.makeErrorMsg(json)
                }
                complete(true)
            }
        }

        /*
        val body = JSONObject()
        body.put("source", "app")
        body.put("token", token)
        body.put("strip_html", true)
        val requestBody = body.toString()
        //println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println("json: " + json)
            try {
                success = true
                //println(json)
                model.dataReset()
                data = mutableMapOf()
                dealOne(json)
                data = model.data
//                println(data)
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
        */
    }

    open fun getOne(context: Context, id: Int, source: String, token: String, completion: CompletionHandler) {}
    open fun getOne(context: Context, params: HashMap<String, String>, complete: CompletionHandler) {

        val url = getOneURL()
        //println(url)

        val header: MutableList<Pair<String, String>> = mutableListOf()
        header.add(Pair("Accept","application/json"))
        header.add(Pair("Content-Type","application/json; charset=utf-8"))

        val body = JSONObject()
        body.put("source", "app")
        if (params.containsKey("token")) {
            body.put("token", params["token"])
        }
        if (params.containsKey("member_token")) {
            body.put("member_token", params["member_token"])
        }
        body.put("strip_html", false)
        //println(body)

        MyHttpClient.instance.post(context, url, body.toString()) { success ->

            if (success) {
                val response = MyHttpClient.instance.response
                if (response != null) {
                    try {
                        val json = JSONObject(response.toString())
//                        //println(json)
                        superModel = parseModel(json)
//                        superCourse.print()
                        this.success = true
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
    open fun update(context: Context, _params: MutableMap<String, String>, filePath: String, complete: CompletionHandler) {

        val url: String = getUpdateURL()
        val header: MutableList<Pair<String, String>> = mutableListOf()
        header.add(Pair("Accept","application/json"))
        header.add(Pair("Content-Type","application/json"))

        val params = _params.let { map1 ->
            PARAMS.let { map2 ->
                map1 + map2
            }
        }

        val params1: MutableList<Pair<String, String>> = mutableListOf()
        //var jsonString: String = "{"
        val tmps: ArrayList<String> = arrayListOf()
        for ((key, value) in params) {

            params1.add(Pair(key, value))
            tmps.add("\"$key\":\"$value\"")
        }
        //jsonString += tmps.joinToString(",")
        //jsonString += "}"
        //println(jsonString)

        var filePaths: ArrayList<String>? = null
        if (filePath.isNotEmpty()) {
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
                        //println(json)
                        this.success = json.getBoolean("success")
                        if (this.success) {
                            this.id = json.getInt("id")
                            val obj = json.getJSONObject("model")
                            jsonToMember(obj, context)
                        } else {
                            if (json.has("msg")) {
                                msg = json.getString("msg")
                            }
                            if (json.has("errors")) {
                                msg = ""
                                val errors = json.getJSONArray("errors")
                                for (i in 0..errors.length()-1) {
                                    msg += errors.getString(i) + "\n"
                                }
                            }
                        }
                    } catch (e: Exception) {
                        this.success = false
                        msg = "parse json failed，請洽管理員"
                    }
                    complete(true)
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

    open fun update(context: Context, type: String, _params: MutableMap<String, Any>, filePath: String, complete: CompletionHandler) {

//        println(_params)
        val url = URL_UPDATE.format(type)
//        println(url)
//        val params: MutableList<Pair<String, String>> = mutableListOf()
        var postString: String = """
{
"""

        val header: MutableList<Pair<String, String>> = mutableListOf()
        header.add(Pair("Accept","application/json"))
        header.add(Pair("Content-Type","application/json"))

        val params1: MutableList<Pair<String, String>> = mutableListOf()
        for ((key, value) in PARAMS) {
            params1.add(Pair(key, value))
        }

        val params: HashMap<String, String> = hashMapOf()
        var j = 1
        for ((_key, row) in _params) {
            var key = _key
            var valueStr: String = ""
            if (key == "arena_id") {
                key = ARENA_KEY
            }
            if (key == "city_id") {
                key = CITY_KEY
            }

            if (key == TEAM_DEGREE_KEY) {
                val tmp: List<String> = row as ArrayList<String>
                //hashmap don't permit duplicate key
                valueStr += """["""
                var i = 1
                for (d in tmp) {
                    valueStr += """"${d}""""
                    if (i < tmp.size) {
                        valueStr += ""","""
                    }
                    params.put("degree="+i, d)
                    i++
                }
                valueStr += """]"""
                params1.add(Pair(key, valueStr))
            } else if (key == TEAM_WEEKDAYS_KEY) {
                val tmp: List<Int> = row as ArrayList<Int>
                valueStr += """["""
                var i = 1
                for (d in tmp) {
                    valueStr += """${d}"""
                    if (i < tmp.size) {
                        valueStr += ""","""
                    }
                    params.put("weekdays="+i, d.toString())
                    i++
                }
                valueStr += """]"""
                params1.add(Pair(key, valueStr))
            } else if (key == CAT_KEY) {
                val tmp: List<Int> = row as ArrayList<Int>
                valueStr += """["""
                var i = 1
                for (d in tmp) {
                    valueStr += """${d}"""
                    if (i < tmp.size) {
                        valueStr += ""","""
                    }
                    params.put("cat_id="+i, d.toString())
                    i++
                }
                valueStr += """]"""
                params1.add(Pair(key, valueStr))
            } else if (key == ARENA_KEY) {
                val value: Int = row as Int
                params.put(_key, value.toString())
                valueStr += """${value}"""
                params1.add(Pair(_key, valueStr))
            } else if (key == CITY_KEY || key == CITYS_KEY) {
                val value: Int = row as Int
                params.put(_key, value.toString())
                valueStr += """${value}"""
                params1.add(Pair(_key, valueStr))
            } else {
                val vtype: String = model.data[key]!!["vtype"] as String
                if (vtype == "String") {
                    params.put(key, row.toString())
                    params1.add(Pair(key, row.toString()))
                } else if (vtype == "Int") {
                    val value: Int = row as Int
                    params.put(key, value.toString())
                    params1.add(Pair(key, value.toString()))
                } else if (vtype == "Bool") {
                    val value: Boolean = row as Boolean
                    params.put(key, value.toString())
                    params1.add(Pair(key, value.toString()))
                }
                valueStr += """"${row}""""
            }
            postString += """"${_key}":${valueStr},
"""
        }
        var i = 1
        for ((key, value) in PARAMS) {
            postString += """"${key}":"${value}""""
            postString += """,
"""
            i++
        }
        postString += """"type":"${type}""""
        postString += """
}
"""
//        println(postString)
//        params.put("type", type)
//        params.putAll(PARAMS)
//        println(params)

//        println(params1)


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

//        val images: HashMap<String, File> = hashMapOf()
//        if (image.length > 0) {
//            val f = File(image)
//            images.put("file", f)
//        }


        /*

        val multipartRequest1 = VolleyMultipartRequest(Request.Method.POST, url, Response.Listener<NetworkResponse> { response ->
            val str = String(response.data)
            println(str)
        }, Response.ErrorListener { error ->
            val networkResponse = error.networkResponse
            var errorMessage = "Unknown error"
            if (networkResponse == null) {
                if (error::class.equals(TimeoutError::class.java)) {
                    errorMessage = "Request timeout"
                } else if (error::class.equals(NoConnectionError::class.java)) {
                    errorMessage = "Failed to connect server"
                }
            } else {
                val result = String(networkResponse.data)
                try {
                    val response = JSONObject(result)
                    val status = response.getString("status")
                    val message = response.getString("message")

                    Log.e("Error Status", status)
                    Log.e("Error Message", message)

                    if (networkResponse.statusCode == 404) {
                        errorMessage = "Resource not found"
                    } else if (networkResponse.statusCode == 401) {
                        errorMessage = "$message Please login again"
                    } else if (networkResponse.statusCode == 400) {
                        errorMessage = "$message Check your inputs"
                    } else if (networkResponse.statusCode == 500) {
                        errorMessage = "$message Something is getting wrong"
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            Log.i("Error", errorMessage)
            error.printStackTrace()
        })





        val header: HashMap<String, String> = hashMapOf()
        header.put("Accept","application/json");
        header.put("Content-Type","application/json");

        val multipartRequest = MultipartRequest(url, images, params, header, Response.Listener { response ->
            //println(response)
//            complete(true)
            val json = JSONObject(response)
            success = json.getBoolean("success")
            msg = ""
            if (json.has("error")) {
                val errors = json.getJSONArray("error")
                for (i in 0..errors.length() - 1) {
                    println(errors.getString(i))
                    msg += errors.getString(i) + "\n"
                }
            }
            complete(success)

        }, Response.ErrorListener { error ->
            println(error)
            println("on fail ${error}")
            msg = "新增/修改 球隊失敗，網站或網路錯誤 " + error.localizedMessage
            complete(false)

        })

        Volley.newRequestQueue(context).add(multipartRequest)

*/

//        val smr = object: SimpleMultiPartRequest(Request.Method.POST, url, Response.Listener { response ->
//            println(response)
//        }, Response.ErrorListener { error ->
//            println(error.localizedMessage)
//        })






        /*println(image)
        println(params)

        Http.init(context)
        Http.upload {
            url = "$URL_UPDATE".format(type)
            if (image.length > 0) {
                files {
                    "file" - image
                }
            }
            params {
               // "id" - "1"
                //"arena_id" - "10"
                "source" - "app"
                "channel" - "bm"
                "type" - type

                for ((_key, row) in params) {
                    var key = _key
                    if (key == "arena_id") {
                        key = TEAM_ARENA_KEY
                    }
                    if (key == "city_id") {
                        key = TEAM_CITY_KEY
                    }

                    if (key == TEAM_DEGREE_KEY) {
                        val tmp: List<String> = row as ArrayList<String>
                        for (d in tmp) {
                            "degree[]" - d
                        }
                    } else if (key == TEAM_WEEKDAYS_KEY) {
                        val tmp: List<Int> = row as ArrayList<Int>
                        for (d in tmp) {
                            "weekdays[]" - d.toString()
                        }
                    } else if (key == TEAM_CAT_KEY) {
                        val tmp: List<Int> = row as ArrayList<Int>
                        for (d in tmp) {
                            "cat_id[]" - d.toString()
                        }
                    } else if (key == TEAM_ARENA_KEY) {
                        val value: Int = row as Int
                        _key - value.toString()
                    } else if (key == TEAM_CITY_KEY) {
                        val value: Int = row as Int
                        _key - value.toString()
                    } else {
                        val vtype: String = model.data[key]!!["vtype"] as String
                        if (vtype == "String") {
                            val value: String = row as String
                            key - value
                        } else if (vtype == "Int") {
                            val value: Int = row as Int
                            key - value.toString()
                        } else if (vtype == "Bool") {
                            val value: Boolean = row as Boolean
                            key - value.toString()
                        }
                    }
                }

            }
            onSuccess { bytes ->
                val res = bytes.toString(Charset.defaultCharset())
                println(res)
                complete(true)
//                val json = JSONObject(res)
//                success = json.getBoolean("success")
//                val errors = json.getJSONArray("error")
//                msg = ""
//                for (i in 0..errors.length()-1) {
//                    msg += errors.getString(i) + "\n"
//                }
//                complete(success)
            }
            onFail { error ->
                error.printStackTrace()
                println("on fail ${error}")
                msg = "新增/修改 球隊失敗，網站或網路錯誤 " + error.localizedMessage
                complete(false)
            }
        }
        */


        /*
        val url = "$URL_UPDATE".format(type)
        println(url)
        val request = object: StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    println(response)
                },
                Response.ErrorListener { error ->
                    println("error: " + error)
                }) {
            override fun getBodyContentType(): String {
                return HEADER
            }

            override fun getBody(): ByteArray {
                val postParams: HashMap<String, String> = hashMapOf()
                for ((key, value) in params) {
                    for ((key1, value1) in model.data) {
                        if (key == key1) {
                            val vtype: String = value1["vtype"]!! as String
                            if (vtype == "String") {
                                postParams.put(key, value as String)
                            } else if (vtype == "Int") {
                                postParams.put(key, (value as Int).toString())
                            } else if (vtype == "Bool") {
                                postParams.put(key, (value as Boolean).toString())
                            }
                        }
                    }
                }
                println(postParams.toString())
                return postParams.toString().toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "multipart/form-data")

                return headers
            }
        }
        Volley.newRequestQueue(context).add(request)
*/
    }

    fun getImage(url: String, completion: CompletionHandler) {
        try {
            val inStream: InputStream = URL(url).openStream()
            image = BitmapFactory.decodeStream(inStream)
            completion(true)
        } catch (e: Exception) {
            completion(false)
        }
    }

    open fun delete(context: Context, type: String, token: String, complete: CompletionHandler) {
        val url = URL_DELETE.format(type)
        //println(url)
        val body = JSONObject()
        body.put("source", "app")
        body.put("channel", "bm")
        body.put("token", token)
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
                makeErrorMsg(json)
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

    fun getAllCitys(context: Context, complete: CompletionHandler) {
        val url = URL_CITYS
        //println(url)

        val body = JSONObject()
        body.put("source", "app")
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
                    arenas.add(Arena(id, name))
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

    fun getArenaByCityIDs(context: Context, city_ids: ArrayList<Int>, city_type:String, complete: CompletionHandler) {
        val url = URL_ARENA_BY_CITY_IDS
//        println(url)

        val body = JSONObject()
        var arr: JSONArray = JSONArray()
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
                citysandarenas.clear()
                val keys = json.keys()
                for ((i, key) in keys.withIndex()) {
                    val obj = json.getJSONObject(key)
                    val city_id: Int = obj.getInt("id")
                    val city_name: String = obj.getString("name")
                    val _rows = obj.getJSONArray("rows")
                    var rows: ArrayList<HashMap<String, Any>> = arrayListOf()
                    for (j in 0.._rows.length()-1) {
                        val row = _rows.getJSONObject(j)
                        val arena_id = row.getInt("id")
                        val arena_name = row.getString("name")
                        rows.add(hashMapOf("id" to arena_id, "name" to arena_name))
                    }
                    citysandarenas.put(city_id, hashMapOf<String, Any>("id" to city_id, "name" to city_name, "rows" to rows))
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

    fun getAreaByCityIDs(context: Context, city_ids: ArrayList<Int>, city_type:String, complete: CompletionHandler) {
        val url = URL_AREA_BY_CITY_IDS
//        println(url)

        val body = JSONObject()
        var arr: JSONArray = JSONArray()
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
                timetables = JSONParse.parse<Timetables>(json)!!
                for (row in timetables.rows) {
                    row.filterRow()
                }
//                timetables.print()
                if (!timetables.success) {
                    msg = json.getString("msg")
                }
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

    fun updateTT(context: Context, type: String, params:HashMap<String, Any>, completion: CompletionHandler) {
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
                    timetables = JSONParse.parse<Timetables>(json)!!
                    for (row in timetables.rows) {
                        row.filterRow()
                    }
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

    fun deleteTT(context: Context, type: String, params:HashMap<String, Any>, completion: CompletionHandler) {
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
                    timetables = JSONParse.parse<Timetables>(json)!!
                    for (row in timetables.rows) {
                        row.filterRow()
                    }
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

    open fun getUpdateURL(): String {return ""}
    open fun getSignupURL(token: String): String { return ""}
    open fun getSignupDateURL(token: String): String { return ""}
    open fun getSignupListURL(token: String? = null): String { return ""}
    open fun parseAbleForSingupList(data: JSONObject): SuperModel { return SuperModel(data) }

    fun signup_date(context: Context, token: String, member_token: String, date_token: String, complete: CompletionHandler) {
        val url = getSignupDateURL(token)
        //println(url)
        val jsonString: String = "{\"device\": \"app\", \"channel\": \"bm\", \"member_token\": " + member_token + ",\"date_token\":" + date_token + "}"
        val body: JSONObject = JSONObject(jsonString)
        //println(body)

        MyHttpClient.instance.post(context, url, body.toString()) { success ->

            if (success) {
                val response = MyHttpClient.instance.response
                if (response != null) {
                    try {
                        val json = JSONObject(response.toString())
                        //println(json)
                        this.success = json.getBoolean("success")
                        if (this.success) {
                            this.signup_date = json
                        } else {
                            this.msg = json.getString("msg")
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

    fun signup(context: Context, token: String, member_token: String, date_token: String, course_deadline: String, complete: CompletionHandler) {
        val url = getSignupURL(token)
        //print(url)
//        val jsonString: String = "{\"device\": \"app\", \"channel\": \"bm\", \"member_token\": " + member_token + ", \"signup_id\": " + signup_id.toString() + ", \"course_date\": " + course_date + ", \"course_deadline\": " + course_deadline + "}"
        val body: JSONObject = JSONObject()
        body.put("device", "app")
        body.put("channel", "bm")
        body.put("member_token", member_token)
        body.put("able_date_token", date_token)
        body.put("cancel_deadline", course_deadline)

        MyHttpClient.instance.post(context, url, body.toString()) { success ->

            if (success) {
                val response = MyHttpClient.instance.response
                if (response != null) {
                    try {
                        val json = JSONObject(response.toString())
//                        println(json)
                        this.success = json.getBoolean("success")
                        if (json.has("msg")) {
                            this.msg = json.getString("msg")
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

    fun signup_list(context: Context, token: String? = null, page: Int = 1, perPage: Int = 8, complete: CompletionHandler) {
        val url: String = getSignupListURL(token)
        //print(url)
        val jsonString: String = "{\"device\": \"app\", \"channel\": \"bm\", \"page\": " + page.toString() + ", \"perPage\": " + perPage + "}"
        val body: JSONObject = JSONObject(jsonString)
        //print(body)

        MyHttpClient.instance.post(context, url, body.toString()) { success ->

            if (success) {
                val response = MyHttpClient.instance.response
                if (response != null) {
                    try {
                        val json = JSONObject(response.toString())
//                        println(json)
                        if (json.has("able")) {
                            able = parseAbleForSingupList(json.getJSONObject("able"))
                            //able.print()
                        }
                        val s: SuperSignups = JSONParse.parse<SuperSignups>(json)!!
//                        for (i in 0..s.rows.size-1) {
//                            val row = s.rows[i]
//                            row.print()
//                        }
                        superModel = s
                        complete(true)
                    } catch (e: Exception) {
                        this.success = false
                        msg = "parse json failed，請洽管理員"
                        println(e.localizedMessage)
                        complete(false)
                    }
                } else {
                    println("response is null")
                    complete(false)
                }
            } else {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }
        }
    }

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

    open fun setData(id: Int, title: String, token: String, featured_path: String, vimeo: String, youtube: String): SuperData {
        val data = SuperData(id, title, token, featured_path, vimeo, youtube)

        return data
    }

    open fun setData1(row: JSONObject): MutableMap<String, MutableMap<String, Any>> {
        val data: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()
        return data
    }

    open fun dealOne(json: JSONObject) {}

    open fun _jsonToData(tmp: JSONObject, key: String, item: Map<String, Any>){}
}

























