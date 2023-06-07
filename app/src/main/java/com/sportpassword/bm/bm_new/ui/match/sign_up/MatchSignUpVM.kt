package com.sportpassword.bm.bm_new.ui.match.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sportpassword.bm.bm_new.data.dto.match.MatchSignUpDto
import com.sportpassword.bm.bm_new.data.dto.match.PostMatchSignUpDto
import com.sportpassword.bm.bm_new.data.repo.match.MatchRepo
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MatchSignUpVM(private val state: SavedStateHandle, private val repo: MatchRepo) :
    BaseViewModel(state) {

    val matchSignUp = MutableLiveData<MatchSignUpDto>()

    fun getMatchSignUp(token: String) {
        viewModelScope.launch {
            repo.getMatchSignUp(PostMatchSignUpDto(matchGroupToken = token)).setupBase().collect {
                matchSignUp.value = it
            }
        }
    }
}