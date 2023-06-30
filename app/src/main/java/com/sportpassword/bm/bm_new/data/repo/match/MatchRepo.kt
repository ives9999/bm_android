package com.sportpassword.bm.bm_new.data.repo.match

import com.sportpassword.bm.bm_new.data.dto.PostListDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchDetailDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchListDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchSignUpDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamInsertDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamInsertResDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamListDto
import com.sportpassword.bm.bm_new.data.dto.match.PostMatchDetailDto
import com.sportpassword.bm.bm_new.data.dto.match.PostMatchSignUpDto
import com.sportpassword.bm.bm_new.data.dto.match.PostMatchTeamListDto
import kotlinx.coroutines.flow.Flow

interface MatchRepo {

    suspend fun getMatchList(data: PostListDto): Flow<MatchListDto>
    suspend fun getMatchTeamList(data: PostMatchTeamListDto): Flow<MatchTeamListDto>
    suspend fun getMatchDetail(data: PostMatchDetailDto): Flow<MatchDetailDto>
    suspend fun getMatchSignUp(data: PostMatchSignUpDto): Flow<MatchSignUpDto>
    suspend fun insertMatchTeam(data: MatchTeamInsertDto): Flow<MatchTeamInsertResDto>
}