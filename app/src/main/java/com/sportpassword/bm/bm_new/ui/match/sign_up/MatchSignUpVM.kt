package com.sportpassword.bm.bm_new.ui.match.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sportpassword.bm.R
import com.sportpassword.bm.bm_new.data.dto.match.MatchSignUpDto
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamInsertDto
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
import com.sportpassword.bm.member
import kotlinx.coroutines.launch
import timber.log.Timber

class MatchSignUpVM(
    private val state: SavedStateHandle,
    private val repo: MatchRepo,
) :
    BaseViewModel(state) {

    val matchSignUp = MutableLiveData<MatchSignUpDto>()
    val signUpList = mutableListOf<SignUpInfo>()
    private var matchGroupId: String? = null
    private var matchId: String? = null

    fun getMatchSignUp(matchGroupId: Int, matchId: Int, token: String) {
        viewModelScope.launch {
            repo.getMatchSignUp(PostMatchSignUpDto(matchGroupToken = token)).setupBase().collect {
                matchSignUp.value = it
                this@MatchSignUpVM.matchGroupId = matchGroupId.toString()
                this@MatchSignUpVM.matchId = matchId.toString()
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
                    SignUpInfo(PLAYER_ONE_AGE, R.string.match_sign_age, "", true),
                    SignUpInfo(PLAYER_TWO_NAME, R.string.match_sign_name, "", true),
                    SignUpInfo(PLAYER_TWO_PHONE, R.string.match_sign_phone, "", true),
                    SignUpInfo(PLAYER_TWO_EMAIL, R.string.match_sign_email, "", true),
                    SignUpInfo(PLAYER_TWO_LINE, R.string.match_sign_line, ""),
                    SignUpInfo(PLAYER_TWO_AGE, R.string.match_sign_age, "", true),
                )
            )
        }

    }

    fun setSignUpInfo(type: String, name: String) {
        signUpList.find { it.type == type }?.value = name
        Timber.d("signUpInfo, $signUpList")
    }

    fun signUp() {
        viewModelScope.launch {
            getMatchTeamInsertInfo()?.let {
                repo.insertMatchTeam(it).setupBase().collect {
                    Timber.d("報名完成 $it")
                }
            }

        }
    }

    private fun getMatchTeamInsertInfo(): MatchTeamInsertDto? {
        return with(signUpList) {
            MatchTeamInsertDto(
                doX = "update",
                name = find { it.type == TEAM_NAME }?.value ?: return null,
                managerToken = member.token ?: return null,
                memberToken = member.token ?: return null,
                managerName = find { it.type == CAPTAIN_NAME }?.value ?: return null,
                managerMobile = find { it.type == CAPTAIN_PHONE }?.value ?: return null,
                managerEmail = find { it.type == CAPTAIN_EMAIL }?.value ?: return null,
                managerLine = find { it.type == CAPTAIN_LINE }?.value,
                matchGroupId = matchGroupId ?: return null,
                matchId = matchId ?: return null,
                players = listOf(
                    //第一個隊員
                    MatchTeamInsertDto.Player(
                        name = find { it.type == PLAYER_ONE_NAME }?.value ?: return null,
                        mobile = find { it.type == PLAYER_ONE_PHONE }?.value ?: return null,
                        email = find { it.type == PLAYER_ONE_EMAIL }?.value ?: return null,
                        line = find { it.type == PLAYER_ONE_LINE }?.value,
                        age = find { it.type == PLAYER_ONE_AGE }?.value,
                        gift = MatchTeamInsertDto.Player.Gift(
                            attributes = "{name:顏色,alias:color,value:經典白}|{name:尺寸,alias:size,value:M}",
                            matchGiftId = "1"
                        )    //todo 贈品假資料,待刪除
                    ),
                    //第二個隊員
                    MatchTeamInsertDto.Player(
                        name = find { it.type == PLAYER_TWO_NAME }?.value ?: return null,
                        mobile = find { it.type == PLAYER_TWO_PHONE }?.value ?: return null,
                        email = find { it.type == PLAYER_TWO_EMAIL }?.value ?: return null,
                        line = find { it.type == PLAYER_TWO_LINE }?.value,
                        age = find { it.type == PLAYER_TWO_AGE }?.value,
                        gift = MatchTeamInsertDto.Player.Gift(
                            attributes = "{name:顏色,alias:color,value:湖水綠}|{name:尺寸,alias:size,value:XS}",
                            matchGiftId = "1"
                        )    //todo 贈品假資料,待刪除
                    )
                )
            )
        }
    }
}