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
import com.sportpassword.bm.bm_new.ui.base.ViewEvent
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_AGE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_EMAIL
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_LINE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_NAME
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchSignUpActivity.Companion.PLAYER_PHONE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.CAPTAIN_EMAIL
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.CAPTAIN_LINE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.CAPTAIN_NAME
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.CAPTAIN_PHONE
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.TEAM_NAME
import com.sportpassword.bm.bm_new.ui.vo.PlayerInfo
import com.sportpassword.bm.member
import kotlinx.coroutines.launch
import timber.log.Timber

class MatchSignUpVM(
    private val state: SavedStateHandle,
    private val repo: MatchRepo,
) :
    BaseViewModel(state) {

    val matchSignUp = MutableLiveData<MatchSignUpDto>()
    val playerInfoList = mutableListOf<PlayerInfo>()
    private var matchGroupId: String? = null
    private var matchId: String? = null
    private var teamToken: String? = null

    sealed class SignUpEvent {
        class SignUp(val isSuccess: Boolean) : ViewEvent.ViewEventData<Boolean>()
        class Edit(val isSuccess: Boolean) : ViewEvent.ViewEventData<Boolean>()
    }

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
        playerInfoList.clear()
        //隊名與隊長
        playerInfoList.addAll(
            listOf(
                PlayerInfo(0, TEAM_NAME, R.string.match_sign_team_name, "", true),    //隊名
                PlayerInfo(0, CAPTAIN_NAME, R.string.match_sign_captain_name, "", true),    //隊長姓名
                PlayerInfo(0, CAPTAIN_PHONE, R.string.match_sign_captain_phone, "", true),    //隊長手機
                PlayerInfo(
                    0,
                    CAPTAIN_EMAIL,
                    R.string.match_sign_captain_email,
                    "",
                    true
                ),    //隊長email
                PlayerInfo(0, CAPTAIN_LINE, R.string.match_sign_captain_line, ""),    //隊長line
            )
        )
        //隊員
        for (i in 1..number) {
            playerInfoList.addAll(
                listOf(
                    PlayerInfo(i, PLAYER_NAME, R.string.match_sign_name, "", true),
                    PlayerInfo(i, PLAYER_PHONE, R.string.match_sign_phone, "", true),
                    PlayerInfo(i, PLAYER_EMAIL, R.string.match_sign_email, "", true),
                    PlayerInfo(i, PLAYER_LINE, R.string.match_sign_line, ""),
                    PlayerInfo(i, PLAYER_AGE, R.string.match_sign_age, "", true),
                )
            )
        }
    }

    fun setSignUpInfo(num: Int, type: String, name: String) {
        Timber.d("報名資料輸入 $type, $name")
        playerInfoList.find { it.playerNum == num && it.type == type }?.value = name
    }

    fun signUp() {
        viewModelScope.launch {
            getMatchTeamInsertInfo()?.let {
                repo.insertMatchTeam(it).setupBase().collect {
                    //新增報名或修改
                    if (teamToken == null) {
                        eventChannel.send(SignUpEvent.SignUp(it.success))
                    } else {
                        eventChannel.send(SignUpEvent.Edit(it.success))
                    }
                }
            }
        }
    }

    private fun getMatchTeamInsertInfo(): MatchTeamInsertDto? {
        return with(playerInfoList) {
            MatchTeamInsertDto(
                token = teamToken,
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
                players = players() ?: return null
            )
        }
    }

    private fun MutableList<PlayerInfo>.players(): List<MatchTeamInsertDto.Player>? {
        val players = mutableListOf<MatchTeamInsertDto.Player>()
        matchSignUp.value?.let { signedMatch ->
            //隊長除外
            for (i in 1..signedMatch.matchPlayers.size) {
                filter { it.playerNum == i }.also { playInfo ->
                    players.add(
                        MatchTeamInsertDto.Player(
                            name = playInfo.find { it.type == PLAYER_NAME }?.value
                                ?: return null,
                            mobile = playInfo.find { it.type == PLAYER_PHONE }?.value
                                ?: return null,
                            email = playInfo.find { it.type == PLAYER_EMAIL }?.value
                                ?: return null,
                            line = playInfo.find { it.type == PLAYER_LINE }?.value,
                            age = playInfo.find { it.type == PLAYER_AGE }?.value,
                            gift = MatchTeamInsertDto.Player.Gift(
                                attributes = "{name:顏色,alias:color,value:經典白}|{name:尺寸,alias:size,value:M}",
                                matchGiftId = "1"
                            )    //todo 贈品假資料,待刪除
                        )
                    )
                }
            }
        }
        return players
    }

    fun editSignedMatch(token: String) {
        teamToken = token

        viewModelScope.launch {
            repo.getMatchSignUp(PostMatchSignUpDto(token = token)).setupBase().collect {
                Timber.d("已報名資料 $it")
                matchSignUp.value = it
                matchId = it.matchId.toString()
                matchGroupId = it.matchGroupId.toString()
            }
        }
    }
}