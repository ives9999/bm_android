package com.sportpassword.bm.Services

import android.content.Context
import android.os.Handler
import androidx.annotation.Keep
import android.util.Log
import com.sportpassword.bm.Utilities.CompletionHandler
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.internal.tls.OkHostnameVerifier
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLPeerUnverifiedException
import javax.net.ssl.SSLSession

class MyHttpClient private constructor() {
    private val TAG = "MyHttpClient"

    private val mOKHttpClient: OkHttpClient
    private object Holder { val INSTANCE = MyHttpClient()}
    var response: MyResponse? = null
    var success: Boolean = false

    companion object {
        private var CONNECTION_TIME_OUT = 30 * 1000
        private var READ_TIME_OUT = 30 * 1000
        private var WRITE_TIME_OUT = 30 * 1000
        val instance: MyHttpClient by lazy { Holder.INSTANCE }
    }

    init {
        mOKHttpClient = OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .hostnameVerifier(MyHostnameVerifier())
                .build()
    }

    /***
    @Throws(IOException::class)
    fun readFile(name: String): String {...}
    will be translated to

    String readFile(String name) throws IOException {...}
     ***/

    @Throws(Exception::class)
    /***
     * operator 操作符重載，get 屬於暨有操作符，所以須加operator 來定義重載
     */
    operator fun get(context: Context, url: String, requestParams: List<Pair<String, String>>, requestHeaders: List<Pair<String, String>>, completion: CompletionHandler) {
        return execute(context, Type.GET, url, requestParams, requestHeaders, completion)
    }

    @Throws(Exception::class)
    fun post(context: Context, url: String, requestParams: List<Pair<String, String>>?, requestHeaders: List<Pair<String, String>>?, completion: CompletionHandler) {
        return execute(context, Type.POST, url, requestParams, requestHeaders, completion)
    }

    @Throws(Exception::class)
    fun post(context: Context, url: String, postBody: String, completion: CompletionHandler) {
        return execute(context, url, postBody, completion)
    }

    @Throws(Exception::class)
    fun uploadFile(context: Context, url: String, postString: String?, filePaths: ArrayList<String>?, requestParams: List<Pair<String, String>>?, requestHeaders: List<Pair<String, String>>?, completion: CompletionHandler) {
        return execute(context, url, postString, filePaths, requestParams, requestHeaders, completion)
    }

    @Throws(Exception::class)
    fun downloadFile(context: Context, url: String, requestParams: List<Pair<String, String>>, requestHeaders: List<Pair<String, String>>, completion: CompletionHandler) {
        return execute(context, Type.POST, url, requestParams, requestHeaders, completion)
    }

    fun toJsonString(json: JSONObject, filter: Array<Array<Any>>?) : String {
        var str = """
            {
            """

        val arr = json.names()
        for (i in 0..arr.length()-1) {
            val key = arr[i].toString()
            str += """
                "${key}":"${json.get(key)}""""
            if (i < arr.length()-1) {
                str += ","
            }
        }
        if (filter != null) {
            str += ","
            str += """
                "where": ["""
            for (i in filter.indices) {
                str += """
                    ["""
                var j = 0
                for (item in filter[i]) {
                    str += """"$item""""
                    if (j < filter[i].size-1) {
                        str += ","
                    }
                    j++
                }
                str += "]"
                if (i < filter.size-1) {
                    str += ","
                }
            }
            str += """
                ]
                """
        }
        str += """
            }
            """
        str = str.trimIndent()

        return str
    }


    @Throws(Exception::class)
    private fun execute(context: Context, type: Type, url: String, requestParams: List<Pair<String, String>>?, requestHeaders: List<Pair<String, String>>?, completion: CompletionHandler) {
        try {
            performRequest(context, OkRequest(type, url, requestParams, requestHeaders), completion)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    @Throws(Exception::class)
    private fun execute(context: Context, url: String, requestParams: List<Pair<String, String>>, requestHeaders: List<Pair<String, String>>, filePaths: ArrayList<String>, completion: CompletionHandler) {
        try {
            val okResponse = performRequest(context, OkRequest(Type.POST, url, requestParams, requestHeaders, filePaths), completion)
        } catch (e: Exception) {
            throw e
        }
    }

    @Throws(Exception::class)
    private fun execute(context: Context, url: String, postString: String, completion: CompletionHandler) {
        try {
            val okResponse = performRequest(context, OkRequest(url, postString), completion)
        } catch (e: Exception) {
            throw e
        }
    }

    @Throws(Exception::class)
    private fun execute(context: Context, url: String, postString: String?, filePaths: ArrayList<String>?, requestParams: List<Pair<String, String>>?, requestHeaders: List<Pair<String, String>>?, completion: CompletionHandler) {
        try {
            val okResponse = performRequest(context, OkRequest(url, postString, filePaths, requestParams, requestHeaders), completion)
        } catch (e: Exception) {
            throw e
        }
    }

    @Throws(IOException::class)
    private fun performRequest(context: Context, request: OkRequest, completion: CompletionHandler) {
        val call = mOKHttpClient.newCall(request.getDataRequest())
        call.enqueue(object : Callback {
            val mainHandler = Handler(context.mainLooper)

            override fun onResponse(call: Call, response: Response) {
                if (response != null) {
                    val okResponse = OkResponse(response)
                    //val result = MyResponse(okResponse.body!!, okResponse.responseCode)
                    val result = MyResponse(okResponse.responseCode, okResponse.body, okResponse.headers)
                    MyHttpClient.instance.response = result
                    MyHttpClient.instance.success = true

                    mainHandler.post(object : Runnable {
                        override fun run() {
                            completion(true)
                            return
                        }
                    })
                } else {
                    MyHttpClient.instance.response = null
                    MyHttpClient.instance.success = false

                    mainHandler.post(object : Runnable {
                        override fun run() {
                            completion(false)
                            return
                        }
                    })
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                MyHttpClient.instance.response = null
                MyHttpClient.instance.success = false

                mainHandler.post(object : Runnable {
                    override fun run() {
                        completion(false)
                        return
                    }
                })
            }
        })
    }

    private inner class OkRequest {
        private var mType: Type? = null
        var mUrl: String? = null
            private set
        private var mRequestParams: List<Pair<String, String>>? = null
        private var mRequestHeaders: List<Pair<String, String>>? = null
        private var mFilePaths: ArrayList<String>? = null
        private var mPostString: String? = null

        constructor(type: Type, url: String, requestParams: List<Pair<String, String>>?, requestHeaders: List<Pair<String, String>>?) {
            mType = type
            mUrl = url
            mRequestParams = requestParams
            mRequestHeaders = requestHeaders
        }
        constructor(type: Type, url: String, requestParams: List<Pair<String, String>>, requestHeaders: List<Pair<String, String>>, filePaths: ArrayList<String>) {
            mType = type
            mUrl = url
            mRequestParams = requestParams
            mRequestHeaders = requestHeaders
            mFilePaths = filePaths
        }
        constructor(url: String, postBody: String) {
            mType = Type.POST
            mUrl = url
            mPostString = postBody
        }
        constructor(url: String, postString: String?, filePaths: ArrayList<String>?, requestParams: List<Pair<String, String>>?, requestHeaders: List<Pair<String, String>>?) {
            mType = Type.POST
            mUrl = url
            mPostString = postString
            mFilePaths = filePaths
            mRequestParams = requestParams
            mRequestHeaders = requestHeaders
        }

        fun getDataRequest(): Request {
            val request: Request
            val builder = Request.Builder()
            mUrl?.let { builder.url(it) }
            builder.header("Connection", "Close")
            if (mRequestHeaders?.isEmpty() ?: false) {
                for (header in mRequestHeaders!!) {
                    builder.addHeader(header.first, header.second)
                }
            }
            if (mType == Type.GET) {
                builder.get()
            } else {
                if (mFilePaths?.isNotEmpty() ?: false) {
                    val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                    mFilePaths?.let {
                        for (filePath in it) {
                            val file = File(filePath)
                            if (file.exists()) {
                                bodyBuilder.addFormDataPart("file", file.name, RequestBody.create(null, file))
                            }
                        }
                    }

                    if (mRequestParams != null) {
                        mRequestParams?.let {
                            for (param in it) {
                                bodyBuilder.addFormDataPart(param.first, param.second)
                            }
                        }
                    }

                    if (mPostString != null) {
                        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), mPostString!!)
                        bodyBuilder.addPart(body)
                    }
                    val a = bodyBuilder.build()
                    //a.

                    builder.post(bodyBuilder.build())
                } else {
                    if (mRequestParams != null) {
                        mRequestParams?.let {
                            //val formBuilder = FormBody.Builder()
                            val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                            for (param in it) {
                                bodyBuilder.addFormDataPart(param.first, param.second)
                                //formBuilder.add(param.first, param.second)
                            }
                            builder.post(bodyBuilder.build())
                            //builder.post(formBuilder.build())
                        }
                    }
                    if (mPostString != null) {
                        //println(mPostString)
                        val json = """
//{
//    "source":"app",
//    "channel":"bm",
//    "page":"1",
//    "perPage":"8"
//}
//""".trimIndent()
//                        println(json)
                        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), mPostString!!)
                        builder.post(body)
                    }
                }
            }
            request = builder.build()
            return request
        }
    }

    inner class OkResponse(private val response: Response?) {
        val responseCode: Int
            get() = response!!.code

        val body: ByteArray?
            get() {
//                var result: String? = null
                var result: ByteArray? = null
                if (response != null && response.body != null) {
                    try {
                        result = response.body!!.bytes()
                        //result = response.body()!!.string()
//                        println(result)
                    } catch (e: IOException) {
                        Log.e(TAG, e.message!!)
                        println(e.localizedMessage)
                    }
                    headers
                }
                return result
            }
        val headers: MutableMap<String, MutableList<String>>?
            get() {
                var headers: MutableMap<String, MutableList<String>>? = null
                if (response != null && response.headers != null) {
                    try {
                        val tmp: Map<String, List<String>> = response.headers.toMultimap()
                        headers = mutableMapOf()
                        for ((k, v) in tmp) {
                            headers[k] = v.toMutableList()
                        }
                    } catch (e: Exception) {

                    }
                }
                return headers
            }
    }

    private inner class MyHostnameVerifier: HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            val result = true
//            try {
//                val certs = session.peerCertificates as Array<X509Certificate>
//                if (certs.isNotEmpty()) {
//                    for (i in certs.indices) {
//                        OkHostnameVerifier.verify(hostname, certs[i])
//                        //result = OkHostnameVerifier.INSTANCE.verify(hostname, certs[i])
//                        if (result) {
//                            break
//                        }
//                    }
//                } else {
//                    result = true
//                }
//            } catch (e: SSLPeerUnverifiedException) {
//                e.printStackTrace()
//            }

            return result
        }
    }

    internal enum class Type {
        GET,
        POST
    }
}

@Keep
open class MyResponse {

    var statusCode: Int = 0
        private set
    private var responseAsString: String? = null
    private var responseAsBytes: ByteArray? = null
    private var headers: MutableMap<String, MutableList<String>>? = null

    @Throws(IOException::class)
    constructor(statusCode: Int, datas: ByteArray?, headers: MutableMap<String, MutableList<String>>?) {
        this.statusCode = statusCode
        this.responseAsBytes = datas
        this.headers = headers
    }

    constructor(content: String, responseCode: Int) {
        responseAsString = content
        statusCode = responseCode
    }

    fun getResponseAsString(): String? {
        responseAsString = responseAsBytes?.toString(Charset.forName("utf-8"))
        return responseAsString
    }

    fun getHeadersByName(name: String): MutableList<String>? {
        if (headers != null) {
            return headers?.get(name)
        }
        return null
    }

    override fun toString(): String {
        if (null != getResponseAsString()) {
            return responseAsString?: ""
        }
        return "Response{" +
                "statusCode=" + statusCode +
                ", responseString='" + responseAsString +
                '}'
    }
}






















