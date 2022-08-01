package com.sportpassword.bm.Services

import android.content.Context
import org.json.JSONObject
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.sportpassword.bm.Controllers.MainActivity
import com.sportpassword.bm.Models.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.lang.Exception

/**
 * Created by ives on 2018/2/4.
 */
object MemberService: DataService() {

    var one: JSONObject? = null
    lateinit var blackLists: BlackLists

    override fun getOneURL(): String {
        return "$URL_ONE".format("member")
    }

    override fun getUpdateURL(): String {
        return URL_MEMBER_UPDATE
    }

    override fun getDeleteURL(): String {
        return URL_MEMBER_DELETE
    }

    fun login(context: Context, email: String, password: String, playerID: String, complete: CompletionHandler) {
        val lowerCaseEmail = email.toLowerCase()
        val url = URL_LOGIN
//        println(url)

//        val header: MutableList<Pair<String, String>> = mutableListOf()
//        header.add(Pair("Accept","application/json"))
//        header.add(Pair("Content-Type","application/json; charset=utf-8"))

        val params: HashMap<String, String> = hashMapOf()
        params.put("device", "app")
        params.put("email", email)
        params.put("password", password)
        params.put("player_id", playerID)

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
    }

    fun forgetPassword(context: Context, email: String, complete: CompletionHandler) {
        val lowerCaseEmail = email.toLowerCase()
        val url = URL_FORGETPASSWORD
        //println(url)

        val params: HashMap<String, String> = hashMapOf()
//        val body = JSONObject()
        params.put("email", email)
        params.put("source", "app")
//        val requestBody = body.toString()
        //println(requestBody)

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
    }

    fun changePassword(context: Context, oldPassword: String, newPassword: String, rePassword: String, complete: CompletionHandler) {
        val url = URL_CHANGE_PASSWORD
        //println(url)

        //val body = JSONObject()
        val params: HashMap<String, String> = hashMapOf()
        params.put("password_old", oldPassword)
        params.put("password", newPassword)
        params.put("repassword", rePassword)
        params.put("source", "app")
        params.put("token", member.token!!)
        //val requestBody = body.toString()
        //println(requestBody)

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
    }

    fun sendVaildateCode(context: Context, type: String, value: String, token: String, complete: CompletionHandler) {
        var url: String = ""
        if (type == "email") {
            url = URL_SEND_EMAIL_VALIDATE
        } else if (type == "mobile") {
            url = URL_SEND_MOBILE_VALIDATE
        }
        //val body = JSONObject()
        val params: HashMap<String, String> = hashMapOf()
        params.put(TOKEN_KEY, token)
        params.put("value", value)
        params.put("source", "app")
        //val requestBody = body.toString()

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
    }

    fun validate(context: Context, type: String, code: String, token: String, complete: CompletionHandler) {
        var url: String = ""
        if (type == "email") {
            url = URL_EMAIL_VALIDATE
        } else if (type == "mobile") {
            url = URL_MOBILE_VALIDATE
        }
//        val body = JSONObject()
        val params: HashMap<String, String> = hashMapOf()
        params.put(TOKEN_KEY, token)
        params.put("code", code)
        params.put("source", "app")
//        val requestBody = body.toString()

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
    }

    fun logout(mainActivity: MainActivity) {
        //FBLogout()
        mainActivity.session.resetMember()
        member.reset()
    }

    fun bank(context: Context, _params: MutableMap<String, String>, complete: CompletionHandler) {

        val url: String = URL_MEMBER_BANK
        val params: Map<String, String> = _params.mergeWith(PARAMS)

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

    fun blacklist(context: Context, token: String, complete: CompletionHandler) {

        val url = URL_MEMBER_BLACKLIST
        val params = hashMapOf<String, String>()
        params.put("source", "app")
        params.put("channel", CHANNEL)
        params.put("token", token)

//        val params: MutableList<Pair<String, String>> = mutableListOf()
//        val keys = body.keys()
//        while (keys.hasNext()) {
//            val key = keys.next()
//            val value = body.getString(key)
//            params.add(Pair(key, value))
//        }

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

//        MyHttpClient.instance.post(context, url, params, null) { success ->
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
////                    println(response.toString())
//                    val json = JSONObject(response.toString())
//                    this.success = json.getBoolean("success")
//                    if (!this.success) {
//                        this.msg = json.getString("msg")
//                    } else {
//                        //this.blackLists = JSONParse.parse<BlackLists>(json)!!
//                    }
//                    complete(this.success)
//                } else {
//                    this.msg = "沒有取得伺服器的回應，請洽管理員"
//                    complete(false)
//                }
//            } else {
//                this.msg = "由伺服器取得黑名單失敗，請洽管理員"
//                complete(success)
//            }
//        }

        /*

        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
            //println(json)
//            val s = json.toString()
            //println(s)
            try {
                blackList = JSONParse.parse<BlackList>(json)!!
//                blackList.print()
                if (!blackList.success) {
                    msg = json.getString("msg")
                }
                complete(true)
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
        */
    }

    fun coinlist(context: Context, member_token: String?, page: Int=1, perPage: Int=20, complete: CompletionHandler) {

        var _member_token: String = member.token!!
        if (member_token != null) {
            _member_token = member_token
        }
        val params = hashMapOf<String, String>(
            "device" to "app",
            "channel" to CHANNEL,
            "member_token" to _member_token,
            "page" to page.toString(),
            "perpage" to perPage.toString()
        )
        //println(params)
//        val objectMapper = ObjectMapper()
//        val body: String = objectMapper.writeValueAsString(params)
//        println(body)

        val url: String = URL_MEMBER_COINLIST
        //println(url)

        _simpleService(context, url, params, complete)
    }

    fun coinReturn(context: Context, member_token: String?, complete: CompletionHandler) {
        var _member_token: String = member.token!!
        if (member_token != null) {
            _member_token = member_token
        }
        val params = hashMapOf<String, String>(
            "device" to "app",
            "channel" to CHANNEL,
            "member_token" to _member_token,
        )
        //println(params)
//        val objectMapper = ObjectMapper()
//        val body: String = objectMapper.writeValueAsString(params)
//        println(body)

        val url: String = URL_MEMBER_COINRETURN
        //println(url)

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
    }

    fun likelist(context: Context, able_type: String, like_list: String="喜歡", page: Int=1, perPage: Int=20, complete: CompletionHandler) {

        val params = hashMapOf<String, String>(
            "device" to "app",
            "channel" to CHANNEL,
            "member_token" to member.token!!,
            "able_type" to able_type,
            "like_list" to like_list,
            "page" to page.toString(),
            "perpage" to perPage.toString()
        )
        //println(params)
//        val objectMapper = ObjectMapper()
//        val body: String = objectMapper.writeValueAsString(params)
//        println(body)

        val url: String = URL_MEMBER_LIKELIST
        //println(url)

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
    }

    fun levelKind(context: Context, member_token: String?, page: Int=1, perPage: Int=20, complete: CompletionHandler) {
        var _member_token: String = member.token!!
        if (member_token != null) {
            _member_token = member_token
        }
        val params = hashMapOf<String, String>(
            "device" to "app",
            "channel" to CHANNEL,
            "member_token" to _member_token,
            "page" to page.toString(),
            "perpage" to perPage.toString()
        )
        //println(params)
//        val objectMapper = ObjectMapper()
//        val body: String = objectMapper.writeValueAsString(params)
//        println(body)

        val url: String = URL_MEMBER_LEVEL_KIND
        //println(url)

        _simpleService(context, url, params, complete)
    }

    fun memberSignupCalendar(year: Int, month: Int, member_token: String?=null, able_type: String="course", page: Int, perPage: Int, complete: CompletionHandler) {
        val url: String = URL_MEMBER_SIGNUPLIST
        //println(url)

        val params: HashMap<String, String> = hashMapOf(
            "channel" to CHANNEL,
            "device" to "app",
            "y" to year.toString(),
            "m" to month.toString(),
            "able_type" to able_type,
            "member_token" to member.token!!,
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

//    override fun jsonToMember(json: JSONObject, context: Context) {
//
//        json.put(ISLOGGEDIN_KEY, true)
//        val _member: SuperModel = JSONParse.parse<Member>(json)!!
//        val _member1: Member = _member as Member
//        _member1.isLoggedIn = true
//        //_member1.memberPrint()
//
//        val session: SharedPreferences = context.getSharedPreferences(SESSION_FILENAME, 0)
//        Member::class.memberProperties.forEach {
//            val name = it.name
//            //val type = it.returnType
//            var value = it.getter.call(_member1)
//            if (name == "avatar") {
//                val a: String = value.toString()
//                if (a.length > 0) {
//                    value = BASE_URL + a
//                }
//            }
//            when (value) {
//                is Int ->
//                    session.edit().putInt(name, value).apply()
//                is String ->
//                    session.edit().putString(name, value).apply()
//                is Boolean ->
//                    session.edit().putBoolean(name, value).apply()
//            }
//        }
//        val city: Int = json.getInt("city_id")
//        session.edit().putInt("city", city).apply()
//
//        val area: Int = json.getInt("area_id")
//        session.edit().putInt("area", area).apply()
//        //session.dump()
//        member.setMemberData(session)
//    }
}


















