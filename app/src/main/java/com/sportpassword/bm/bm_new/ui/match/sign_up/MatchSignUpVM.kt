package com.sportpassword.bm.bm_new.ui.match.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sportpassword.bm.R
import com.sportpassword.bm.bm_new.data.dto.match.MatchSignUpDto
import com.sportpassword.bm.bm_new.data.dto.match.PostMatchSignUpDto
import com.sportpassword.bm.bm_new.data.repo.match.MatchRepo
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.CAPTAIN_EMAIL
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.CAPTAIN_LINE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.CAPTAIN_NAME
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.CAPTAIN_PHONE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.TEAM_NAME
import com.sportpassword.bm.bm_new.ui.vo.SignUpInfo
import kotlinx.coroutines.launch
import timber.log.Timber

class MatchSignUpVM(
    private val state: SavedStateHandle,
    private val repo: MatchRepo,
) :
    BaseViewModel(state) {

    val matchSignUp = MutableLiveData<MatchSignUpDto>()
    val signUpList = mutableListOf<SignUpInfo>()

    fun getMatchSignUp(token: String) {
        viewModelScope.launch {
            repo.getMatchSignUp(PostMatchSignUpDto(matchGroupToken = token)).setupBase().collect {
                matchSignUp.value = it
            }
        }
    }

    fun initSignUpInfo(number: Int) {
        signUpList.clear()

        signUpList.addAll(
            mutableListOf(
                SignUpInfo(TEAM_NAME, R.string.match_sign_team_name, "", true),    //隊名
                SignUpInfo(CAPTAIN_NAME, R.string.match_sign_captain_name, "", true),    //隊長姓名
                SignUpInfo(CAPTAIN_PHONE, R.string.match_sign_captain_phone, "", true),    //隊長手機
                SignUpInfo(CAPTAIN_EMAIL, R.string.match_sign_captain_email, "", true),    //隊長email
                SignUpInfo(CAPTAIN_LINE, R.string.match_sign_captain_line, ""),    //隊長line
            )
        )

    }

    fun setSignUpInfo(type: String, name: String) {
        signUpList.find { it.type == type }?.value = name
        Timber.d("signUpInfo, $signUpList")
    }
}