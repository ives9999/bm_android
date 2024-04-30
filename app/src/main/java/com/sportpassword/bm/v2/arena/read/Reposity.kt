package com.sportpassword.bm.v2.arena.read

import androidx.paging.PagingConfig
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Utilities.URL_ARENA_LIST
import com.sportpassword.bm.v2.base.ApiService
import com.sportpassword.bm.v2.base.BaseRepository
import com.sportpassword.bm.v2.base.IRepository

class Repository : BaseRepository<ReadDao>() {
//    private lateinit var apiService: APIService
    init {
//        val client = NetworkModule.provideOkHttpClient()
//        val retrofit = NetworkModule.provideRetrofit(client)
//        apiService = retrofit.create(APIService::class.java)
    }

    override suspend fun getRead(page: Int, perpage: Int, otherParams: Map<String, String>?, callback: IRepository.IDaoCallback<ReadDao>) {
        val url: String = URL_ARENA_LIST

        val params: MutableMap<String, String> = mutableMapOf(
            "page" to page.toString(),
            "perpage" to perpage.toString()
        )
//        url += "?page=$page&perpage=$perpage"
        otherParams ?. let {
            for ((key, value) in it) {
                params[key] = value
            }
        }

        val config = PagingConfig(perpage)

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