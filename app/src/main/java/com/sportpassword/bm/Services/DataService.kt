package com.sportpassword.bm.Services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sportpassword.bm.Models.Coach
import com.sportpassword.bm.Models.Data
import com.sportpassword.bm.Utilities.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ives on 2018/2/14.
 */
open class DataService: BaseService() {
    
    var success: Boolean = false
    var totalCount: Int = 0
    var page: Int = 0
    var perPage: Int = 0
    var dataLists: ArrayList<Data> = arrayListOf()
    open val model: Data = Data(-1, "", "", "")
    lateinit var data: Map<String, Map<String, Any>>

    fun getList(context: Context, type:String, titleField:String, page:Int, perPage:Int, filter:Array<Array<Any>>?, complete:(Boolean)->Unit) {
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

    fun getOne(context: Context, type:String, titleField:String, token:String, complete:(Boolean)->Unit) {
        val url = "$URL_ONE".format(type)
        println(url)

        val body = JSONObject()
        body.put("source", "app")
        body.put("token", token)
        body.put("strip_html", true)
        val requestBody = body.toString()
        println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = true
                //println(json)
                model.dataReset()
                data = mapOf()
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

    fun update(context: Context, params: Map<String, Any>, complete: CompletionHandler) {

    }

    open fun setData(id: Int, title: String, token: String, featured_path: String, vimeo: String, youtube: String): Data {
        val data = Data(id, title, token, featured_path, vimeo, youtube)

        return data
    }

    open fun dealOne(json: JSONObject) {}

    open fun _jsonToData(tmp: JSONObject, key: String, item: Map<String, Any>){}
}