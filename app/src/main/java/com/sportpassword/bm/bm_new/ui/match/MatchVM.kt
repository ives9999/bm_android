package com.sportpassword.bm.bm_new.ui.match

import android.nfc.tech.MifareUltralight
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sportpassword.bm.bm_new.data.dto.MatchListDto
import com.sportpassword.bm.bm_new.data.dto.PostListDto
import com.sportpassword.bm.bm_new.data.repo.match.MatchPagingSource
import com.sportpassword.bm.bm_new.data.repo.match.MatchRepo
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow

class MatchVM(private val state: SavedStateHandle, private val repo: MatchRepo) :
    BaseViewModel(state),
    MatchPagingSource.Listener {

    fun getMatchList(): Flow<PagingData<MatchListDto.Row>> {
        return Pager(
            config = PagingConfig(
                pageSize = MifareUltralight.PAGE_SIZE,
                prefetchDistance = 60 / 3
            )
        ) {
            MatchPagingSource(perPage = 60).apply {
                setListener(this@MatchVM)
            }
        }.flow.cachedIn(viewModelScope)
    }

    override suspend fun getTeamList(
        nextPageNumber: Int,
        perPage: Int,
        callback: (MatchListDto) -> Unit
    ) {
        val post = PostListDto()
        post.perPage = perPage.toString()
        post.page = nextPageNumber.toString()

        repo.getMatchList(post).setupBase().collect {
            callback.invoke(it)
        }
    }
}