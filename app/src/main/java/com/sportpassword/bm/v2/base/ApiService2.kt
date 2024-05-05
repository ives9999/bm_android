package com.sportpassword.bm.v2.base

import com.sportpassword.bm.v2.arena.read.ReadDao
import com.sportpassword.bm.v2.arena.show.ShowDao
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService2 {
    @GET("/arena/getRead")
    suspend fun getRead(
        @Query("page") page: Int,
        @Query("perpage") perpage: Int,
    ): ReadDao

    @GET("/arena/getOne")
    suspend fun getOne(
        @Query("arena_token") token: String
    ): ShowDao

}