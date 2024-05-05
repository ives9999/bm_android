package com.sportpassword.bm.v2.arena.show

import com.sportpassword.bm.v2.base.ApiService2
import com.sportpassword.bm.v2.base.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository {

    private val apiService2: ApiService2 = AppConfig.ApiService2()

    suspend fun getOne(token: String): Flow<ShowDao> {
        return flow {
            val showDao = apiService2.getOne(token)
            emit(showDao)
        }.flowOn(Dispatchers.IO)
    }

//    override fun getOne(token: String, callback: IRepository.IDaoCallback<ShowDao>) {
//        val url = URL_ARENA_ONE
//        val params: MutableMap<String, String> = mutableMapOf(
//            "arena_token" to token,
//        )
//
//        val apiService: ApiService<ShowDao> = ApiService()
//        apiService.get(url, params, object: TypeToken<ShowDao>() {}.type, callback)
//    }
}