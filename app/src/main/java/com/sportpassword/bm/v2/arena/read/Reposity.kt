package com.sportpassword.bm.v2.arena.read

import com.google.gson.Gson
import com.sportpassword.bm.Utilities.URL_ARENA_LIST
import com.sportpassword.bm.v2.base.IRepository
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import java.io.IOException
import java.lang.Exception

class Reposity : IRepository {

    override fun getRead(page: Int, perpage: Int, params: HashMap<String, String>?, callback: IRepository.IDaoCallback) {
        var url: String = URL_ARENA_LIST
        url += "?page=$page&perpage=$perpage"
        params ?. let {
            for ((key, value) in it) {
                url += "&$key=$value"
            }
        }
        println(url)

        val request: okhttp3.Request = okhttp3.Request.Builder()
            .url(url)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .build()

        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val msg = e.localizedMessage
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                try {
                    val jsonString = response.body!!.string()
                    val t = Gson().fromJson<ReadDao>(jsonString, ReadDao::class.java)
                    println(t)
                    callback.onReadResult(t)
                } catch (e: Exception) {
                    println(e.localizedMessage)
                }
            }
        })
//        ArenaService.getList(null, null, page, 20) { success ->
//            val jsonString = ArenaService.jsonString
////            println(jsonString)
//            try {
//                val t = Gson().fromJson<ReadDao>(jsonString, ReadDao::class.java)
//                println(t)
//                callback.onReadResult(t)
//            } catch (e: java.lang.Exception) {
//                println(e.localizedMessage)
//            }
//        }
    }
}