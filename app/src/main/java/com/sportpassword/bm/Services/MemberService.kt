package com.sportpassword.bm.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sportpassword.bm.Utilities.URL_REGISTER
import org.json.JSONObject

/**
 * Created by ives on 2018/2/4.
 */
object MemberService {

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

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { response ->
            println(response)
            val success = response.getBoolean("success")
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not register user: $error")
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
}