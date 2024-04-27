package com.sportpassword.bm.v2.arena.show

import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Utilities.URL_ARENA_ONE
import com.sportpassword.bm.v2.base.ApiService
import com.sportpassword.bm.v2.base.BaseRepository
import com.sportpassword.bm.v2.base.IRepository

class Repository: BaseRepository<ShowDao>() {

    override fun getOne(token: String, callback: IRepository.IDaoCallback<ShowDao>) {
        val url = URL_ARENA_ONE
        val params: MutableMap<String, String> = mutableMapOf(
            "arena_token" to token,
        )

        val apiService: ApiService<ShowDao> = ApiService()
        apiService.get(url, params, object: TypeToken<ShowDao>() {}.type, callback)
    }
}