package com.sportpassword.bm.Services

import android.content.Context
import android.os.Bundle
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
import com.android.volley.VolleyError
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.Profile
import com.facebook.login.LoginManager


/**
 * Created by ives on 2018/2/4.
 */
object MemberService: BaseService() {

    var success: Boolean = false
    var one: JSONObject? = null

    fun register(context: Context, email: String, password: String, repassword: String, complete: CompletionHandler) {
        val url = URL_REGISTER
        //println(url)


        val body = JSONObject()
        body.put("email", email)
        body.put("password", password)
        body.put("repassword", repassword)
        body.put("source", "app")
        val requestBody = body.toString()
        //println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(response)
            try {
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                success = false
                msg = "無法註冊，沒有傳回成功值 " + e.localizedMessage
            }
            if (success) {
                //jsonToMember(json)
            } else {
                makeErrorMsg(json)
            }
            complete(true)
        }, Response.ErrorListener { error ->
            //Log.d("ERROR", "Could not register user: $error")
            msg = "註冊失敗，網站或網路錯誤"
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

    fun login(context: Context, email: String, password: String, playerID: String, complete: CompletionHandler) {
        val lowerCaseEmail = email.toLowerCase()
        val url = URL_LOGIN
        //println(url)

        val body = JSONObject()
        body.put("email", email)
        body.put("password", password)
        body.put("player_id", playerID)
        body.put("source", "app")
        val requestBody = body.toString()
        //println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = json.getBoolean("success")
                //println(success)
            } catch (e: JSONException) {
                success = false
                msg = "無法登入，沒有傳回成功值 " + e.localizedMessage
            }
            if (success) {
                jsonToMember(json)
                if (json.has("msg")) {
                    Alert.show(context, "警告", json.getString("msg"), {
                        complete(true)
                    })
                } else {
                    complete(true)
                }
            } else {
                makeErrorMsg(json)
                complete(true)
            }
        }, Response.ErrorListener { error ->
            println(error.localizedMessage)
            msg = "登入失敗，網站或網路錯誤"
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return HEADER
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }


        // Request a string response from the provided URL.
//        val request = object: StringRequest(Request.Method.POST, url,
//                Response.Listener { response ->
//                    println(response)
//                }, Response.ErrorListener { error ->
//                    println(error.localizedMessage)
//        }) {
//            override fun getBodyContentType(): String {
//                return "application/json: charset=utf-8"
//            }
//
//            override fun getBody(): ByteArray {
//                println("parameter")
//                return requestBody.toByteArray()
//            }
//        }
        Volley.newRequestQueue(context).add(request)
    }

    fun forgetPassword(context: Context, email: String, complete: CompletionHandler) {
        val lowerCaseEmail = email.toLowerCase()
        val url = URL_FORGETPASSWORD
        //println(url)

        val body = JSONObject()
        body.put("email", email)
        body.put("source", "app")
        val requestBody = body.toString()
        //println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            try {
                success = json.getBoolean("success")
                //println(json)
            } catch (e: JSONException) {
                success = false
                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
            }
            if (success) {
                msg = json.getString("msg")
            } else {
                makeErrorMsg(json)
            }
            complete(true)
        }, Response.ErrorListener { error ->
            println(error.localizedMessage)
            msg = "失敗，網站或網路錯誤"
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

    fun changePassword(context: Context, oldPassword: String, newPassword: String, rePassword: String, complete: CompletionHandler) {
        val url = URL_CHANGE_PASSWORD
        //println(url)

        val body = JSONObject()
        body.put("password_old", oldPassword)
        body.put("password", newPassword)
        body.put("repassword", rePassword)
        body.put("source", "app")
        body.put("token", member.token)
        val requestBody = body.toString()
        //println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                success = false
                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
            }
            if (success) {
                msg = "修改密碼成功，之後請用新密碼登入"
            } else {
                makeErrorMsg(json)
            }
            complete(true)
        }, Response.ErrorListener { error ->
            println(error.localizedMessage)
            msg = "失敗，網站或網路錯誤"
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

    fun update(context: Context, id: Int, field: String, _value: String, complete: CompletionHandler) {
        var value = _value
        if (field == EMAIL_KEY) {
            value = value.toLowerCase()
        }
        val url = URL_MEMBER_UPDATE
        //println(url)

        val body = JSONObject()
        body.put(field, value)
        body.put(ID_KEY, id)
        body.put("source", "app")
        val requestBody = body.toString()
        //println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                success = false
                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
            }
            if (success) {
                jsonToMember(json)
            } else {
                makeErrorMsg(json)
            }
            complete(true)
        }, Response.ErrorListener { error ->
            println(error.localizedMessage)
            msg = "失敗，網站或網路錯誤"
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

    fun sendVaildateCode(context: Context, type: String, value: String, token: String, complete: CompletionHandler) {
        var url: String = ""
        if (type == "email") {
            url = URL_SEND_EMAIL_VALIDATE
        } else if (type == "mobile") {
            url = URL_SEND_MOBILE_VALIDATE
        }
        val body = JSONObject()
        body.put(TOKEN_KEY, token)
        body.put("value", value)
        body.put("source", "app")
        val requestBody = body.toString()

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                success = false
                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
            }
            if (success) {
                complete(true)
            } else {
                if (json.has("msg")) {
                    msg = json.getString("msg")
                }
                complete(false)
            }
        }, Response.ErrorListener { error ->
            println(error.localizedMessage)
            msg = "失敗，網站或網路錯誤"
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

    fun validate(context: Context, type: String, code: String, token: String, complete: CompletionHandler) {
        var url: String = ""
        if (type == "email") {
            url = URL_EMAIL_VALIDATE
        } else if (type == "mobile") {
            url = URL_MOBILE_VALIDATE
        }
        val body = JSONObject()
        body.put(TOKEN_KEY, token)
        body.put("code", code)
        body.put("source", "app")
        val requestBody = body.toString()

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                success = false
                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
            }
            if (success) {
                complete(true)
            } else {
                if (json.has("msg")) {
                    msg = json.getString("msg")
                }
                complete(false)
            }
        }, Response.ErrorListener { error ->
            println(error.localizedMessage)
            msg = "失敗，網站或網路錯誤"
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

    fun getOne(context: Context, token: String, complete: CompletionHandler) {
        val body = JSONObject()
        body.put(TOKEN_KEY, token)
        body.put("source", "app")
        val requestBody = body.toString()

        val request = object : JsonObjectRequest(Request.Method.POST, URL_MEMBER_GETONE, null, Response.Listener { json ->
            //println(json)
            try {
                one = json
                success = json.getBoolean("success")
            } catch (e: JSONException) {
                success = false
                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
            }
            if (success) {
                jsonToMember(json)
                complete(true)
            } else {
                if (json.has("msg")) {
                    msg = json.getString("msg")
                }
                complete(false)
            }
        }, Response.ErrorListener { error ->
            println(error.localizedMessage)
            msg = "失敗，網站或網路錯誤"
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

    fun FBLogin(context: Context, playerID: String, complete: (Boolean) -> Unit) {
        val accessToken = AccessToken.getCurrentAccessToken()
        val request = GraphRequest.newMeRequest(accessToken) { json: JSONObject, response: GraphResponse? ->
            //println(json)
            var uid = ""
            var name = ""
            var email = ""
            var sex = ""
            var avatar = ""
            var first_name = ""
            var last_name = ""
            try {
                if (json.has("id")) {
                    uid = json.getString("id")
                }
                if (json.has("name")) {
                    name = json.getString("name")
                }
                if (json.has("first_name")) {
                    name = json.getString("first_name")
                }
                if (json.has("last_name")) {
                    name = json.getString("last_name")
                }
                if (json.has("email")) {
                    email = json.getString("email").toLowerCase()
                }
                if (json.has("gender")) {
                    sex = json.getString("gender")
                }
                if (json.has("picture")) {
                    avatar = json.getJSONObject("picture").getJSONObject("data").getString("url")
                }
//                println("uid: $uid")
//                println("name: $name")
//                println("first_name: $first_name")
//                println("last_name: $last_name")
//                println("email: $email")
//                println("sex: $sex")
                //println("avatar: $avatar")
                val body = JSONObject()
                if (uid.isNotEmpty()) {
                    body.put("uid", uid)
                }
                if (name.isNotEmpty()) {
                    body.put("name", name)
                }
                if (email.isNotEmpty()) {
                    body.put("email", email)
                }
                if (sex.isNotEmpty()) {
                    sex = if (sex == "male") "M" else "F"
                    body.put("sex", sex)
                }
                if (avatar.isNotEmpty()) {
                    body.put("avatar", avatar)
                }
                if (playerID.isNotEmpty()) {
                    body.put("player_id", playerID)
                }
                _FBLogin(context, body) {success ->
                    complete(success)
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,email,picture.width(300),gender,birthday")
        request.parameters = parameters
        request.executeAsync()
    }

    fun _FBLogin(context: Context, body: JSONObject, complete: CompletionHandler) {
        val url = URL_FB_LOGIN
        //println(url)

        body.put("source", "app")
        body.put("social", "fb")
        body.put("channel", CHANNEL)
        val requestBody = body.toString()
        //println(requestBody)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
            try {
                success = json.getBoolean("success")
                //println(success)
            } catch (e: JSONException) {
                FBLogout()
                success = false
                msg = "無法登入，沒有傳回成功值 " + e.localizedMessage
            }
            if (success) {
                //println(json)
                jsonToMember(json)
                complete(true)
            } else {
                FBLogout()
                makeErrorMsg(json)
                complete(false)
                msg = json.getString("msg")
            }
        }, Response.ErrorListener { error ->
            println(error.localizedMessage)
            msg = "登入失敗，網站或網路錯誤"
            FBLogout()
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

    fun logout() {
        FBLogout()
        member.reset()
    }
    fun FBLogout() {
        LoginManager.getInstance().logOut()
    }


    private fun jsonToMember(json: JSONObject) {
        json.put(ISLOGGEDIN_KEY, true)
        member.setMemberData(json)
    }
}