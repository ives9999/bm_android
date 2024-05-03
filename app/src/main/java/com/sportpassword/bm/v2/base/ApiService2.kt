package com.sportpassword.bm.v2.base

import com.sportpassword.bm.v2.arena.read.ReadDao
import retrofit2.http.GET


interface ApiService2 {
    @GET("/arena/getRead")
    suspend fun getRead(): ReadDao

}