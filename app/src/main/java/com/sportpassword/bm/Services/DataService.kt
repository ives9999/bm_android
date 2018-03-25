package com.sportpassword.bm.Services

import android.content.Context
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ohmerhe.kolley.request.Http
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Utilities.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

/**
 * Created by ives on 2018/2/14.
 */
open class DataService: BaseService() {
    
    var success: Boolean = false
    var id: Int = 0
    var totalCount: Int = 0
    var page: Int = 0
    var perPage: Int = 0
    var dataLists: ArrayList<Data> = arrayListOf()
    open val model: Data = Data(-1, "", "", "")
    lateinit var data: MutableMap<String, MutableMap<String, Any>>
    val citys: ArrayList<City> = arrayListOf()
    val arenas: ArrayList<Arena> = arrayListOf()

    fun getList(context: Context, type:String, titleField:String, page:Int, perPage:Int, filter:Array<Array<Any>>?, complete:CompletionHandler) {
        val url = "$URL_LIST".format(type)
        //println(url)

        val body = JSONObject()
        body.put("source", "app")
        body.put("page", page.toString())
        body.put("perPage", perPage.toString())

        var requestBody: String = ""
        if (filter != null) {
            requestBody = toJsonString(body, filter!!)
        } else {
            requestBody = body.toString()
        }
        //println(requestBody)
        //println("coach getList refresh: $refresh")
        dataLists = arrayListOf()

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
                    var vimeo = if (obj.has("vimeo")) obj.get("vimeo").toString() else ""
                    var youtube = if (obj.has("youbute")) obj.get("youbute").toString() else ""
                    val id = obj.getInt("id")
                    var featured_path = if (obj.has("featured_path")) obj.get("featured_path").toString() else ""
                    //println(featured_path)
                    if (featured_path.isNotEmpty()) {
                        featured_path = BASE_URL + featured_path
                    }
                    if (vimeo.isNotEmpty()) {
                        vimeo = VIMEO_PREFIX + vimeo
                    }
                    if (youtube.isNotEmpty()) {
                        youtube = YOUTUBE_PREFIX + youtube
                    }

                    //val dataList: Data = Coach(id, title, featured_path)
                    val data = setData(id, title, token, featured_path, vimeo, youtube)
                    dataLists.add(data)
                }
                //println(dataLists.size)
                //println(lists)
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
            //println(json)
            try {
                success = true
                //println(json)
                model.dataReset()
                data = mutableMapOf()
                dealOne(json)
                data = model.data
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
    }

    fun update(context: Context, type: String, params: MutableMap<String, Any>, image: String, complete: CompletionHandler) {
        //val url = "$URL_UPDATE".format(type)

        Http.init(context)
        Http.upload {
            url = "$URL_UPDATE".format(type)
            files {
                "file" - image
            }
            params {
                for ((key, row) in params) {
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
                println("on success ${bytes.toString(Charset.defaultCharset())}")
            }
            onFail { error ->
                println("on fail ${error.toString()}")
            }
        }

        /*
        val request = object: StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->

                },
                Response.ErrorListener { error ->

                }) {
            override fun getBodyContentType(): String {
                return super.getBodyContentType()
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

    fun getAllCitys(context: Context, complete: CompletionHandler) {
        val url = URL_CITYS
        //println(url)

        val body = JSONObject()
        body.put("source", "app")
        val requestBody = body.toString()

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

    open fun setData(id: Int, title: String, token: String, featured_path: String, vimeo: String, youtube: String): Data {
        val data = Data(id, title, token, featured_path, vimeo, youtube)

        return data
    }

    open fun dealOne(json: JSONObject) {}

    open fun _jsonToData(tmp: JSONObject, key: String, item: Map<String, Any>){}
}


class VolleyMultipartRequest(method: Int, url: String?, listener: Response.ErrorListener?) : Request<NetworkResponse>(method, url, listener) {

    override fun parseNetworkResponse(response: NetworkResponse?): Response<NetworkResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deliverResponse(response: NetworkResponse?) {

    }

}







