package com.sportpassword.bm.Services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sportpassword.bm.Models.CoachTable
import com.sportpassword.bm.Models.Timetable
import com.sportpassword.bm.Utilities.CompletionHandler
import com.sportpassword.bm.Utilities.HEADER
import com.sportpassword.bm.Utilities.JSONParse
import com.sportpassword.bm.Utilities.URL_ONE
import org.json.JSONException
import org.json.JSONObject

object TimetableService: DataService() {
    lateinit var timetable: Timetable
    lateinit var coachTable: CoachTable

    fun getOne(context: Context, id: Int, source: String, token: String, completion: CompletionHandler) {
        val url = "$URL_ONE".format("timetable")
        //println(url)

        val body = JSONObject()
        body.put("device", "app")
        body.put("type", source)
        body.put("type_token", token)
        body.put("id", id)

        val requestBody = body.toString()

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println("json: " + json)
            try {
                success = true
                //println(json)
                val tt = json.getJSONObject("tt")
                val model = json.getJSONObject("model")
                this.timetable = JSONParse.parse<Timetable>(tt)!!
                //this.timetable.print()
                this.coachTable = JSONParse.parse<CoachTable>(model)!!

            } catch (e: JSONException) {
                println("parse data error: " + e.localizedMessage)
                success = false
                msg = "無法getOne，沒有傳回成功值 " + e.localizedMessage
            }
            if (this.success) {

            } else {
                //DataService.makeErrorMsg(json)
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
}