package com.sportpassword.bm.bm_new.data.repo.match

import com.sportpassword.bm.bm_new.data.dto.MatchListDto
import com.sportpassword.bm.bm_new.data.dto.PostListDto
import kotlinx.coroutines.flow.Flow

interface MatchRepo {

    suspend fun getMatchList(data: PostListDto): Flow<MatchListDto>

}