package com.sportpassword.bm.v2.arena.read

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Utilities.PERPAGE
import com.sportpassword.bm.Utilities.URL_ARENA_LIST
import com.sportpassword.bm.v2.base.ApiService
import com.sportpassword.bm.v2.base.ApiService2
import com.sportpassword.bm.v2.base.AppConfig
import com.sportpassword.bm.v2.base.BaseRepository
import com.sportpassword.bm.v2.base.IRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository {
//    private lateinit var apiService: APIService
    init {
//        val client = NetworkModule.provideOkHttpClient()
//        val retrofit = NetworkModule.provideRetrofit(client)
//        apiService = retrofit.create(APIService::class.java)
    }
    private val apiService2: ApiService2 = AppConfig.ApiService2()

    // prefetchDistance = 15 表示滑到第15個時，就開始呼叫api
    private val defaultPagingConfig:PagingConfig = PagingConfig(pageSize = PERPAGE, prefetchDistance = 15)

    fun getRead2(pagingConfig: PagingConfig = defaultPagingConfig): Flow<PagingData<PageArena>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { ArenaPagingSource(apiService2) }
        ).flow
    }

//    suspend fun getRead2(page: Int = 1, perpage: Int = PERPAGE, otherParams: Map<String, String>?): Flow<ReadDao> {
//        return flow {
//            val readDao = apiService2.getRead(page, perpage)
//            emit(readDao)
//        }.flowOn(Dispatchers.IO)
//    }

//    override fun getRead(page: Int, perpage: Int, otherParams: Map<String, String>?, callback: IRepository.IDaoCallback<ReadDao>) {
//        val url: String = URL_ARENA_LIST
//
//        val params: MutableMap<String, String> = mutableMapOf(
//            "page" to page.toString(),
//            "perpage" to perpage.toString()
//        )
////        url += "?page=$page&perpage=$perpage"
//        otherParams ?. let {
//            for ((key, value) in it) {
//                params[key] = value
//            }
//        }
//
//        val config = PagingConfig(perpage)
//
//        val apiService: ApiService<ReadDao> = ApiService()
//        apiService.get(url, params, object: TypeToken<ReadDao>() {}.type, callback)
//
//
////        runBlocking {
////            val d = apiService.getRead()
////            callback.onReadResult(d)
////        }
//
////        val request: okhttp3.Request = okhttp3.Request.Builder()
////            .url(url)
////            .addHeader("Accept", "application/json")
////            .addHeader("Content-Type", "application/json; charset=utf-8")
////            .build()
////
////        val okHttpClient = OkHttpClient()
////        okHttpClient.newCall(request).enqueue(object : Callback {
////            override fun onFailure(call: Call, e: IOException) {
////                val msg = e.localizedMessage
////            }
////
////            override fun onResponse(call: Call, response: okhttp3.Response) {
////                try {
////                    val jsonString = response.body!!.string()
////                    val t = Gson().fromJson<ReadDao>(jsonString, ReadDao::class.java)
////                    println(t)
////                    callback.onSuccess(t)
////                } catch (e: Exception) {
////                    println(e.localizedMessage)
////                }
////            }
////        })
//    }
}

//interface APIService {
//    @GET("arena/getRead")
//    suspend fun getRead(): ReadDao
//}