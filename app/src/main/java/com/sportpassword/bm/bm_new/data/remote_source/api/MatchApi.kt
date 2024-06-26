package com.sportpassword.bm.bm_new.data.remote_source.api

import com.google.gson.JsonObject
import com.sportpassword.bm.bm_new.data.dto.match.MatchDetailDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchListDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchSignUpDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamInsertResDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamListDto
import com.sportpassword.bm.bm_new.data.dto.match.SuccessResDto
import retrofit2.http.Body
import retrofit2.http.POST

interface MatchApi {

    //賽事列表
    @POST("/app/match/list")
    suspend fun getMatchList(
        @Body data: JsonObject
    ): MatchListDto

    //match get one, 賽事內容
    @POST("/app/match/one")
    suspend fun getMatchDetail(
        @Body data: JsonObject
    ): MatchDetailDto

    //match team get one, 賽事內容
    @POST("/app/match_team/one")
    suspend fun getMatchSignUp(
        @Body data: JsonObject
    ): MatchSignUpDto

    //match team list 取得報名隊伍列表
    @POST("/app/match_team/list")
    suspend fun getMatchTeamList(
        @Body data: JsonObject
    ): MatchTeamListDto

    //match team insert 新增賽事隊伍
    @POST("/app/match_team/update")
    suspend fun insertMatchTeam(
        @Body data: JsonObject
    ): MatchTeamInsertResDto

    //match team delete 刪除參賽隊伍
    @POST("/app/match_team/delete")
    suspend fun delMatchTeam(
        @Body data: JsonObject
    ): SuccessResDto
}