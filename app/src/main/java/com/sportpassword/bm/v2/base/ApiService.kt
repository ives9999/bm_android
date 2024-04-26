package com.sportpassword.bm.v2.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Models.MemberSubscriptionKindTable
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.bm_new.ui.util.canEditSignUp
import com.sportpassword.bm.v2.arena.read.ReadDao
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Type

class ApiService<T>() {
    private var client: OkHttpClient = OkHttpClient()
    private var jsonHeaders: Headers = Headers.Builder()
        .add("Accept", "application/json")
        .add("Content-Type", "application/json; charset=utf-8")
        .build()

    //fun get(url: String, params: Map<String, String>, typeToken: Type, callback: (T)->Unit) {
    fun get(url: String, params: Map<String, String>, typeToken: Type, callback: IRepository.IDaoCallback<T>) {
        val httpUrlBuilder = url.toHttpUrl().newBuilder()
        params.let {
            for ((key, value) in it) {
                httpUrlBuilder.addQueryParameter(key, value)
            }
        }
        println(httpUrlBuilder.toString())

        val request = Request.Builder()
            .url(httpUrlBuilder.build())
            .headers(jsonHeaders)
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                println(e.localizedMessage)
                e.localizedMessage?.let { callback.onFailure(500, it) }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val status = response.code
                    if (response.code != 200) {
                        callback.onFailure(response.code, response.message)
                    } else {
                        val jsonString = response.body!!.string()
                        val t = Gson().fromJson<T>(jsonString, typeToken) as T
                        //val t = Gson().fromJson<T>(jsonString, object: TypeToken<T>() {}.type) as T
                        println(t)
                        callback.onSuccess(t)
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                    e.localizedMessage?.let { callback.onFailure(501, it) }
                }
            }

        })
    }
}
















