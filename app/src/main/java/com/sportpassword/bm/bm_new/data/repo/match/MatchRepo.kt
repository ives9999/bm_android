package com.sportpassword.bm.bm_new.data.repo.match

import com.sportpassword.bm.bm_new.data.dto.match.MatchDetailDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchListDto
import com.sportpassword.bm.bm_new.data.dto.PostListDto
import com.sportpassword.bm.bm_new.data.dto.match.PostMatchDetailDto
import kotlinx.coroutines.flow.Flow

interface MatchRepo {

    suspend fun getMatchList(data: PostListDto): Flow<MatchListDto>
    suspend fun getMatchDetail(data: PostMatchDetailDto): Flow<MatchDetailDto>

}