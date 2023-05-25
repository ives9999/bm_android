package com.sportpassword.bm.bm_new.data.remote_source.api

import com.google.gson.JsonObject
import com.sportpassword.bm.bm_new.data.dto.MatchListDto
import retrofit2.http.Body
import retrofit2.http.POST

interface MatchApi {

    //賽事列表
    @POST("/app/match/list")
    suspend fun getMatchList(
        @Body data: JsonObject
    ): MatchListDto
}