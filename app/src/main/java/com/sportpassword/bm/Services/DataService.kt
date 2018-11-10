package com.sportpassword.bm.Services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
//import com.ohmerhe.kolley.request.Http
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Utilities.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File

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

    fun getList(context: Context, type:String, titleField:String, params: HashMap<String,Any>, page:Int, perPage:Int, filter:Array<Array<Any>>?, complete:CompletionHandler) {
        val url = "$URL_LIST".format(type)
        //println(url)

        val body = JSONObject()
        body.put("source", "app")
        body.put("page", page.toString())
        body.put("perPage", perPage.toString())

        for ((key, value) in params) {
            when (key) {
                "city_id" -> {
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

        var requestBody: String = ""
        if (filter != null) {
            requestBody = toJsonString(body, filter!!)
        } else {
            requestBody = body.toString()
        }
        //println(requestBody)
        //println("coach getList refresh: $refresh")
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
//                    if (vimeo.isNotEmpty()) {
//                        vimeo = VIMEO_PREFIX + vimeo
//                    }
//                    if (youtube.isNotEmpty()) {
//                        youtube = YOUTUBE_PREFIX + youtube
//                    }

                    //val dataList: SuperData = Coach(id, title, featured_path)
                    val data = setData(id, title, token, featured_path, vimeo, youtube)
                    val map = setData1(obj)
                    data.data = map
                    superDataLists.add(data)
                }
                //println(superDataLists.size)
//                for (data in superDataLists) {
//                    println(data.data)
//                }
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

    fun getOne(context: Context, type:String, titleField:String, token:String, complete: CompletionHandler) {
        val url = "$URL_ONE".format(type)
        //println(url)

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
    }

    fun update(context: Context, type: String, _params: MutableMap<String, Any>, image: String, complete: CompletionHandler) {

        //println(_params)
        val url = "$URL_UPDATE".format(type)
        println(url)
        val params: HashMap<String, String> = hashMapOf()
        for ((_key, row) in _params) {
            var key = _key
            if (key == "arena_id") {
                key = ARENA_KEY
            }
            if (key == "city_id") {
                key = CITY_KEY
            }

            if (key == TEAM_DEGREE_KEY) {
                val tmp: List<String> = row as ArrayList<String>
                //hashmap don't permit duplicate key
                var i = 1
                for (d in tmp) {
                    params.put("degree="+i, d)
                    i++
                }
            } else if (key == TEAM_DAYS_KEY) {
                val tmp: List<Int> = row as ArrayList<Int>
                var i = 1
                for (d in tmp) {
                    params.put("days="+i, d.toString())
                    i++
                }
            } else if (key == CAT_KEY) {
                val tmp: List<Int> = row as ArrayList<Int>
                var i = 1
                for (d in tmp) {
                    params.put("cat_id="+i, d.toString())
                }
                i++
            } else if (key == ARENA_KEY) {
                val value: Int = row as Int
                params.put(_key, value.toString())
            } else if (key == CITY_KEY) {
                val value: Int = row as Int
                params.put(_key, value.toString())
            } else {
                val vtype: String = model.data[key]!!["vtype"] as String
                if (vtype == "String") {
                    params.put(key, row.toString())
                } else if (vtype == "Int") {
                    val value: Int = row as Int
                    params.put(key, value.toString())
                } else if (vtype == "Bool") {
                    val value: Boolean = row as Boolean
                    params.put(key, value.toString())
                }
            }
        }
        params.put("type", type)
        params.putAll(PARAMS)
        println(params)

        val images: HashMap<String, File> = hashMapOf()
        if (image.length > 0) {
            val f = File(image)
            images.put("file", f)
        }
        val header: HashMap<String, String> = hashMapOf()

        val multipartRequest = MultipartRequest(url, images, params, header, Response.Listener { response ->
//            println(response)
//            complete(true)
            val json = JSONObject(response)
            success = json.getBoolean("success")
            msg = ""
            if (json.has("error")) {
                val errors = json.getJSONArray("error")
                for (i in 0..errors.length() - 1) {
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
                    } else if (key == TEAM_DAYS_KEY) {
                        val tmp: List<Int> = row as ArrayList<Int>
                        for (d in tmp) {
                            "days[]" - d.toString()
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

    open fun delete(context: Context, type: String, token: String, complete: CompletionHandler) {
        val url = "$URL_DELETE".format(type)
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
                msg = "無法刪除球隊，請稍後再試 " + e.localizedMessage
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
        //println(url)

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


