package com.sportpassword.bm.bm_new.data.repo.match

import com.sportpassword.bm.bm_new.data.dto.PostListDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchDetailDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchListDto
import com.sportpassword.bm.bm_new.data.dto.match.PostMatchDetailDto
import com.sportpassword.bm.bm_new.data.remote_source.api.MatchApi
import com.sportpassword.bm.bm_new.data.remote_source.util.dataToJsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MatchRepoImpl(private val source: MatchApi) : MatchRepo {
    override suspend fun getMatchList(data: PostListDto): Flow<MatchListDto> {
        return flow { emit(source.getMatchList(dataToJsonObject(data))) }
    }

    override suspend fun getMatchDetail(data: PostMatchDetailDto): Flow<MatchDetailDto> {
        return flow { emit(source.getMatchDetail(dataToJsonObject(data))) }
    }
}