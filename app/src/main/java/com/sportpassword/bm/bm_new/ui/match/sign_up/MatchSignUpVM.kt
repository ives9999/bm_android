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
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamPlayerFragment.Companion.PLAYER_ONE_AGE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamPlayerFragment.Companion.PLAYER_ONE_EMAIL
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamPlayerFragment.Companion.PLAYER_ONE_LINE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamPlayerFragment.Companion.PLAYER_ONE_NAME
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamPlayerFragment.Companion.PLAYER_ONE_PHONE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamPlayerFragment.Companion.PLAYER_TWO_AGE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamPlayerFragment.Companion.PLAYER_TWO_EMAIL
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamPlayerFragment.Companion.PLAYER_TWO_LINE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamPlayerFragment.Companion.PLAYER_TWO_NAME
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamPlayerFragment.Companion.PLAYER_TWO_PHONE
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
            listOf(
                SignUpInfo(TEAM_NAME, R.string.match_sign_team_name, "", true),    //隊名
                SignUpInfo(CAPTAIN_NAME, R.string.match_sign_captain_name, "", true),    //隊長姓名
                SignUpInfo(CAPTAIN_PHONE, R.string.match_sign_captain_phone, "", true),    //隊長手機
                SignUpInfo(CAPTAIN_EMAIL, R.string.match_sign_captain_email, "", true),    //隊長email
                SignUpInfo(CAPTAIN_LINE, R.string.match_sign_captain_line, ""),    //隊長line
            )
        )
        if (number == 2) {
            signUpList.addAll(
                listOf(
                    SignUpInfo(PLAYER_ONE_NAME, R.string.match_sign_name, "", true),
                    SignUpInfo(PLAYER_ONE_PHONE, R.string.match_sign_phone, "", true),
                    SignUpInfo(PLAYER_ONE_EMAIL, R.string.match_sign_email, "", true),
                    SignUpInfo(PLAYER_ONE_LINE, R.string.match_sign_line, ""),
                    SignUpInfo(PLAYER_ONE_AGE, R.string.match_sign_age, ""),
                    SignUpInfo(PLAYER_TWO_NAME, R.string.match_sign_name, "", true),
                    SignUpInfo(PLAYER_TWO_PHONE, R.string.match_sign_phone, "", true),
                    SignUpInfo(PLAYER_TWO_EMAIL, R.string.match_sign_email, "", true),
                    SignUpInfo(PLAYER_TWO_LINE, R.string.match_sign_line, ""),
                    SignUpInfo(PLAYER_TWO_AGE, R.string.match_sign_age, ""),
                )
            )
        }

    }

    fun setSignUpInfo(type: String, name: String) {
        signUpList.find { it.type == type }?.value = name
        Timber.d("signUpInfo, $signUpList")
    }
}