package com.sportpassword.bm.v2.arena.read

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Utilities.URL_ARENA_LIST
import com.sportpassword.bm.bm_new.data.remote_source.NetworkModule
import com.sportpassword.bm.v2.base.ApiService
import com.sportpassword.bm.v2.base.IRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.http.GET
import java.io.IOException
import java.lang.Exception

class Reposity : IRepository<ReadDao> {
//    private lateinit var apiService: APIService
    init {
//        val client = NetworkModule.provideOkHttpClient()
//        val retrofit = NetworkModule.provideRetrofit(client)
//        apiService = retrofit.create(APIService::class.java)
    }

    override fun getRead(page: Int, perpage: Int, _params: Map<String, String>?, callback: IRepository.IDaoCallback<ReadDao>) {
        val url: String = URL_ARENA_LIST

        val params: MutableMap<String, String> = mutableMapOf(
            "pge" to page.toString(),
            "perpage" to perpage.toString()
        )
//        url += "?page=$page&perpage=$perpage"
        _params ?. let {
            for ((key, value) in it) {
                params[key] = value
            }
        }

        val apiService: ApiService<ReadDao> = ApiService()
        apiService.get(url, params, object: TypeToken<ReadDao>() {}.type, callback)


//        runBlocking {
//            val d = apiService.getRead()
//            callback.onReadResult(d)
//        }

//        val request: okhttp3.Request = okhttp3.Request.Builder()
//            .url(url)
//            .addHeader("Accept", "application/json")
//            .addHeader("Content-Type", "application/json; charset=utf-8")
//            .build()
//
//        val okHttpClient = OkHttpClient()
//        okHttpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                val msg = e.localizedMessage
//            }
//
//            override fun onResponse(call: Call, response: okhttp3.Response) {
//                try {
//                    val jsonString = response.body!!.string()
//                    val t = Gson().fromJson<ReadDao>(jsonString, ReadDao::class.java)
//                    println(t)
//                    callback.onSuccess(t)
//                } catch (e: Exception) {
//                    println(e.localizedMessage)
//                }
//            }
//        })
    }
}

//interface APIService {
//    @GET("arena/getRead")
//    suspend fun getRead(): ReadDao
//}