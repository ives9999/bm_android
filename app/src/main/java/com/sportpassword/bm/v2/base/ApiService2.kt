package com.sportpassword.bm.v2.base

import com.sportpassword.bm.v2.arena.read.ReadDao
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService2 {
    @GET("/arena/getRead")
    suspend fun getRead(
        @Query("page") page: Int,
        @Query("perpage") perpage: Int,
    ): ReadDao

}