package com.sportpassword.bm.bm_new.ui.match.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sportpassword.bm.bm_new.data.dto.match.MatchDetailDto
import com.sportpassword.bm.bm_new.data.dto.match.PostMatchDetailDto
import com.sportpassword.bm.bm_new.data.repo.match.MatchRepo
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MatchDetailVM(private val state: SavedStateHandle, private val repo: MatchRepo) :
    BaseViewModel(state) {

    val matchDetail = MutableLiveData<MatchDetailDto>()

    fun getMatchDetail(token: String) {
        viewModelScope.launch {
            repo.getMatchDetail(PostMatchDetailDto(token = token)).setupBase().collect {
                matchDetail.value = it
            }
        }
    }
}