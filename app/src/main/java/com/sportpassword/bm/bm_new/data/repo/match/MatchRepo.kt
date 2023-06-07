package com.sportpassword.bm.bm_new.data.repo.match

import com.sportpassword.bm.bm_new.data.dto.PostListDto
import com.sportpassword.bm.bm_new.data.dto.match.*
import kotlinx.coroutines.flow.Flow

interface MatchRepo {

    suspend fun getMatchList(data: PostListDto): Flow<MatchListDto>
    suspend fun getMatchDetail(data: PostMatchDetailDto): Flow<MatchDetailDto>
    suspend fun getMatchSignUp(data: PostMatchSignUpDto): Flow<MatchSignUpDto>

}