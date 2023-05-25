package com.sportpassword.bm.bm_new.data.repo.match

import com.sportpassword.bm.bm_new.data.remote_source.util.dataToJsonObject
import com.sportpassword.bm.bm_new.data.dto.MatchListDto
import com.sportpassword.bm.bm_new.data.dto.PostListDto
import com.sportpassword.bm.bm_new.data.remote_source.api.MatchApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MatchRepoImpl(private val source: MatchApi) : MatchRepo {
    override suspend fun getMatchList(data: PostListDto): Flow<MatchListDto> {
        return flow { emit(source.getMatchList(dataToJsonObject(data))) }
    }
}