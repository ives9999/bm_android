package com.sportpassword.bm.Services

import android.content.Context
import org.json.JSONObject
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.sportpassword.bm.Controllers.MainActivity
import com.sportpassword.bm.Models.*
import okhttp3.Call
import okhttp3.Callback
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

//    fun register(context: Context, email: String, password: String, repassword: String, complete: CompletionHandler) {
//        val url = URL_REGISTER
//        //println(url)
//
//
//        val body = JSONObject()
//        body.put("email", email)
//        body.put("password", password)
//        body.put("repassword", repassword)
//        body.put("source", "app")
//        val requestBody = body.toString()
//        //println(requestBody)
//
//        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            //println(response)
//            try {
//                success = json.getBoolean("success")
//            } catch (e: JSONException) {
//                success = false
//                msg = "無法註冊，沒有傳回成功值 " + e.localizedMessage
//            }
//            if (success) {
//                //jsonToMember(json)
//            } else {
//                //makeErrorMsg(json)
//            }
//            complete(true)
//        }, Response.ErrorListener { error ->
//            //Log.d("ERROR", "Could not register user: $error")
//            msg = "註冊失敗，網站或網路錯誤"
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
//
//        Volley.newRequestQueue(context).add(request)
//    }

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

//        MyHttpClient.instance.post(context, url, body) { success ->
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//
//                        this.jsonString = response.toString()
////                        println(jsonString)
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

//        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            //println(json)
//            try {
//                success = json.getBoolean("success")
//                //println(success)
//            } catch (e: JSONException) {
//                success = false
//                msg = "無法登入，沒有傳回成功值 " + e.localizedMessage
//            }
//            if (success) {
//                //print(json)
//                jsonToMember(json, context)
//                //member.memberPrint()
//                if (json.has("msg")) {
//                    Alert.show(context, "警告", json.getString("msg"), {
//                        complete(true)
//                    })
//                } else {
//                    complete(true)
//                }
//            } else {
//                makeErrorMsg(json)
//                complete(true)
//            }
//        }, Response.ErrorListener { error ->
//            println(error.localizedMessage)
//            msg = "登入失敗，網站或網路錯誤"
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
        //Volley.newRequestQueue(context).add(request)
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


//        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            try {
//                success = json.getBoolean("success")
//                //println(json)
//            } catch (e: JSONException) {
//                success = false
//                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
//            }
//            if (success) {
//                msg = json.getString("msg")
//            } else {
//                //makeErrorMsg(json)
//            }
//            complete(true)
//        }, Response.ErrorListener { error ->
//            println(error.localizedMessage)
//            msg = "失敗，網站或網路錯誤"
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

//        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            //println(json)
//            try {
//                success = json.getBoolean("success")
//            } catch (e: JSONException) {
//                success = false
//                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
//            }
//            if (success) {
//                msg = "修改密碼成功，之後請用新密碼登入"
//            } else {
//                //makeErrorMsg(json)
//            }
//            complete(true)
//        }, Response.ErrorListener { error ->
//            println(error.localizedMessage)
//            msg = "失敗，網站或網路錯誤"
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
    }

//    fun update(context: Context, id: Int, field: String, _value: String, complete: CompletionHandler) {
//        var value = _value
//        if (field == EMAIL_KEY) {
//            value = value.toLowerCase()
//        }
//        val url = URL_MEMBER_UPDATE
//        //println(url)
//
//        val body = JSONObject()
//        body.put(field, value)
//        body.put(ID_KEY, id)
//        body.put("source", "app")
//        val requestBody = body.toString()
//        //println(requestBody)
//
//        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            //println(json)
//            try {
//                success = json.getBoolean("success")
//            } catch (e: JSONException) {
//                success = false
//                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
//            }
//            if (success) {
//                jsonToMember(json, context)
//            } else {
//                makeErrorMsg(json)
//            }
//            complete(true)
//        }, Response.ErrorListener { error ->
//            println(error.localizedMessage)
//            msg = "失敗，網站或網路錯誤"
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

//        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            //println(json)
//            try {
//                success = json.getBoolean("success")
//            } catch (e: JSONException) {
//                success = false
//                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
//            }
//            if (success) {
//                complete(true)
//            } else {
//                if (json.has("msg")) {
//                    msg = json.getString("msg")
//                }
//                complete(false)
//            }
//        }, Response.ErrorListener { error ->
//            println(error.localizedMessage)
//            msg = "失敗，網站或網路錯誤"
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

//        val request = object : JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { json ->
//            //println(json)
//            try {
//                success = json.getBoolean("success")
//            } catch (e: JSONException) {
//                success = false
//                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
//            }
//            if (success) {
//                complete(true)
//            } else {
//                if (json.has("msg")) {
//                    msg = json.getString("msg")
//                }
//                complete(false)
//            }
//        }, Response.ErrorListener { error ->
//            println(error.localizedMessage)
//            msg = "失敗，網站或網路錯誤"
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
    }

//    fun getOne(context: Context, token: String, complete: CompletionHandler) {
//        val body = JSONObject()
//        body.put(TOKEN_KEY, token)
//        body.put("source", "app")
//        val requestBody = body.toString()
//        //println(URL_MEMBER_GETONE)
//        //println(body)
//
//        val request = object : JsonObjectRequest(Request.Method.POST, URL_MEMBER_GETONE, null, Response.Listener { json ->
//            //println(json)
//            try {
//                one = json
//                success = json.getBoolean("success")
//            } catch (e: JSONException) {
//                success = false
//                msg = "無法執行，沒有傳回成功值 " + e.localizedMessage
//            }
//            if (success) {
//                jsonToMember(json, context)
//                complete(true)
//            } else {
//                if (json.has("msg")) {
//                    msg = json.getString("msg")
//                }
//                complete(false)
//            }
//        }, Response.ErrorListener { error ->
//            println(error.localizedMessage)
//            msg = "失敗，網站或網路錯誤"
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

    /*
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
                jsonToMember(json, context)
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
*/
    fun logout(mainActivity: MainActivity) {
        //FBLogout()
        mainActivity.session.resetMember()
        member.reset()
    }
//    fun FBLogout() {
//        LoginManager.getInstance().logOut()
//    }

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
//        println(params)
//        val objectMapper = ObjectMapper()
//        val body: String = objectMapper.writeValueAsString(params)
//        println(body)

        val url: String = URL_MEMBER_LIKELIST
//        println(url)

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
//            if (success) {
//                val response = MyHttpClient.instance.response
//                if (response != null) {
//                    try {
//                        jsonString = response.toString()
////                        println(jsonString)
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

    fun memberSignupCalendar(year: Int, month: Int, member_token: String?=null, source: String="course", complete: CompletionHandler): Pair<Boolean, String> {
        var res = true
        if (member_token == null) {
            res = false
            return Pair(res, "沒有傳輸會員碼錯誤，請洽管理員")
        }

        return Pair(res, "")
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


















