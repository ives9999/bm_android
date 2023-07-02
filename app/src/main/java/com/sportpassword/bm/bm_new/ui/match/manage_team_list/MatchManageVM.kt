package com.sportpassword.bm.bm_new.ui.match.manage_team_list

import android.nfc.tech.MifareUltralight
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sportpassword.bm.bm_new.data.dto.match.DelMatchTeamDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamListDto
import com.sportpassword.bm.bm_new.data.dto.match.PostMatchTeamListDto
import com.sportpassword.bm.bm_new.data.repo.match.MatchRepo
import com.sportpassword.bm.bm_new.data.repo.match.MatchTeamListPagingSource
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.base.ViewEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

class MatchManageVM(private val state: SavedStateHandle, private val repo: MatchRepo) :
    BaseViewModel(state),
    MatchTeamListPagingSource.Listener {

    sealed class MatchManageEvent {
        data class Delete(val isSuccess: Boolean) : ViewEvent.ViewEventData<Boolean>()
    }

    fun getMatchTeamList(): Flow<PagingData<MatchTeamListDto.Row>> {
        return Pager(
            config = PagingConfig(
                pageSize = MifareUltralight.PAGE_SIZE,
                prefetchDistance = 60 / 3
            )
        ) {
            MatchTeamListPagingSource(perPage = 60).apply {
                setListener(this@MatchManageVM)
            }
        }.flow.cachedIn(viewModelScope)
    }

    override suspend fun getMatchTeamList(
        nextPageNumber: Int,
        perPage: Int,
        callback: (MatchTeamListDto) -> Unit
    ) {
        val post = PostMatchTeamListDto()
        post.perPage = perPage.toString()
        post.page = nextPageNumber.toString()

        repo.getMatchTeamList(post).setupBase().collect {
            callback.invoke(it)
        }
    }

    fun delMatchTeamList(token: String) {
        viewModelScope.launch {
            repo.delMatchTeam(DelMatchTeamDto(token = token)).setupBase().collect {
                Timber.d("del match team $it")
                eventChannel.send(MatchManageEvent.Delete(it.success))
            }
        }
    }
}