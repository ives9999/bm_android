 package com.sportpassword.bm.Services

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import org.apache.http.HttpEntity
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MIME
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.util.CharsetUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException

/**
 * Created by ives on 2018/2/15.
 */
open class BaseService {
    var msg: String = ""

    fun toJsonString(json: JSONObject, filter: Array<Array<Any>>) : String {
        var str = "{"

        val arr = json.names()
        for (i in 0..arr.length()-1) {
            val key = arr[i].toString()
            str += """ "$key":"${json.get(key)}" """
            if (i < arr.length()-1) {
                str += ","
            }
        }
        if (filter.isNotEmpty()) {
            str += """ ,"where": ["""
            for (i in filter.indices) {
                str += "["
                var j = 0
                for (item in filter[i]) {
                    str += """ "$item" """
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
            str += "]"
        }
        str += "}"

        return str
    }

    protected fun makeErrorMsg(json: JSONObject) {
        try {
            val errors = json.getJSONArray("msg")
            for (i in 0..errors.length() - 1) {
                val error = errors[i].toString()
                msg += error
            }
        } catch (e: JSONException) {

        }
        try {
            msg = json.getString("msg")
        } catch (e: JSONException) {

        }
    }
}

class MultipartRequest(url: String?,
                       val mFilePart: HashMap<String, File>?,
                       val mStringPart: Map<String, String>?,
                       val header: Map<String, String>?,
                       val mListener: Response.Listener<String>,
                       errorListener: Response.ErrorListener?
                             ) :
        Request<String>(Method.POST, url, errorListener) {
    var entity: MultipartEntityBuilder = MultipartEntityBuilder.create()
    var httpentity: HttpEntity

    init {
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
        try {
            entity.setCharset(CharsetUtils.get("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            println(e.localizedMessage)
        }
        bulidMultipartEntity()
        httpentity = entity.build()
    }

    private fun bulidMultipartEntity() {
        if (mFilePart != null) {
            for (entry: Map.Entry<String, File> in mFilePart.entries) {
                entity.addPart(entry.key, FileBody(entry.value, ContentType.create("image/jpg"), entry.value.name))
            }
        }
        if (mStringPart != null) {
            for (entry: Map.Entry<String, String> in mStringPart.entries) {
                var key = entry.key
                val re = "([^=]*)=(.*)".toRegex()
                val matches = re.find(key)
                if (matches != null && matches.groupValues.size > 2) {
                    val groups = matches.groupValues
                    key = groups[1]+"[]"
                }
                entity.addTextBody(key, entry.value, ContentType.create("text/plain", MIME.UTF8_CHARSET))
            }
        }
    }

    override fun getBodyContentType(): String {
        return httpentity.contentType.value
    }

    override fun getBody(): ByteArray {
        val bos: ByteArrayOutputStream = ByteArrayOutputStream()
        try {
            httpentity.writeTo(bos)
        } catch (e: IOException) {
            println(e.localizedMessage)
        }
        return bos.toByteArray()
    }

//    override fun getHeaders(): MutableMap<String, String> {
//        if (header == null) {
//            return super.getHeaders()
//        } else {
//            return header
//        }
//    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
        try {
            //println("Network Response " + String(response!!.data, CharsetUtils.get("UTF-8")))
            return Response.success(String(response!!.data, CharsetUtils.get("UTF-8")), cacheEntry)
        } catch (e: UnsupportedEncodingException) {
            println(e.localizedMessage)
            return Response.success(String(response!!.data, CharsetUtils.get("UTF-8")), cacheEntry)
        }
    }

    override fun deliverResponse(response: String?) {
        mListener.onResponse(response)
    }


}