package com.sportpassword.bm.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import com.sportpassword.bm.Models.Member
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member

/**
 * Created by ives on 2018/2/4.
 */
object MemberService {

    var msg: String = ""
    var success: Boolean = false

    fun register(context: Context, email: String, password: String, repassword: String, complete: (Boolean) -> Unit) {
        val url = URL_REGISTER
        //println(url)


        val body = JSONObject()
        body.put("email", email)
        body.put("password", password)
        body.put("repassword", repassword)
        body.put("source", "app")
        val requestBody = body.toString()
        println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(response)
            try {
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                success = false
                msg = "無法註冊，沒有傳回成功值 " + e.localizedMessage
            }
            if (success) {
                jsonToMember(json)
            } else {
                val errors = json.getJSONArray("msg")
                for (i in 0 .. errors.length()-1) {
                    val error = errors[i].toString()
                    msg += error
                }
            }
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not register user: $error")
            msg = "註冊失敗，網站或網路錯誤"
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    private fun jsonToMember(json: JSONObject) {
        json.put(ISLOGGEDIN_KEY, true)
        member.setMemberData(json)
    }
}