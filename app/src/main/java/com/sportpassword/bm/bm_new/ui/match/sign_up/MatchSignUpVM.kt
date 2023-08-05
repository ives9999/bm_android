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

    fun initSignUpInfo(signUpDto: MatchSignUpDto) {
        playerInfoList.clear()
        //隊名與隊長
        playerInfoList.addAll(
            listOf(
                PlayerInfo(
                    0,
                    TEAM_NAME,
                    R.string.match_sign_team_name,
                    value = "",
                    isRequired = true
                ),    //隊名
                PlayerInfo(
                    0,
                    CAPTAIN_NAME,
                    R.string.match_sign_captain_name,
                    value = "",
                    isRequired = true
                ),    //隊長姓名
                PlayerInfo(
                    0,
                    CAPTAIN_PHONE,
                    R.string.match_sign_captain_phone,
                    value = "",
                    isRequired = true
                ),    //隊長手機
                PlayerInfo(
                    0,
                    CAPTAIN_EMAIL,
                    R.string.match_sign_captain_email,
                    value = "", isRequired = true
                ),    //隊長email
                PlayerInfo(
                    0,
                    CAPTAIN_LINE,
                    R.string.match_sign_captain_line,
                    value = ""
                ),    //隊長line
            )
        )
        //隊員
        for (i in 1..signUpDto.matchGroup.number) {
            playerInfoList.addAll(
                listOf(
                    PlayerInfo(
                        i,
                        PLAYER_NAME,
                        R.string.match_sign_name,
                        value = "",
                        isRequired = true
                    ),
                    PlayerInfo(
                        i,
                        PLAYER_PHONE,
                        R.string.match_sign_phone,
                        value = "",
                        isRequired = true
                    ),
                    PlayerInfo(
                        i,
                        PLAYER_EMAIL,
                        R.string.match_sign_email,
                        value = "",
                        isRequired = true
                    ),
                    PlayerInfo(i, PLAYER_LINE, R.string.match_sign_line, value = ""),
                    PlayerInfo(
                        i,
                        PLAYER_AGE,
                        R.string.match_sign_age,
                        value = "",
                        isRequired = true
                    ),
                )
            )
            //隊員-贈品
            signUpDto.matchGifts.getOrNull(0)?.let {
                it.product.productAttributes.forEach { gift ->
                    playerInfoList.add(
                        PlayerInfo(
                            i,
                            gift.alias,
                            giftTitle = gift.name,
                            value = "",
                            isRequired = true
                        )
                    )
                }
            }
        }
    }

    fun setSignUpInfo(num: Int, type: String, name: String) {
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
        matchSignUp.value?.let { matchSign ->
            //隊長除外
            for (i in 1..matchSign.matchGroup.number) {
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
                            age = playInfo.find { it.type == PLAYER_AGE }?.value ?: return null,
                            gift = if (matchSign.matchGifts.isEmpty()) null else
                                MatchTeamInsertDto.Player.Gift(
                                    attributes = setGifts(matchSign, playInfo),
                                    matchGiftId = "1"
                                )
                        )
                    )
                }
            }
            if (players.size != matchSign.matchGroup.number) return null
        }
        return players
    }

    private fun setGifts(
        signedMatch: MatchSignUpDto,
        playInfo: List<PlayerInfo>
    ): String {
        val gifts = mutableListOf<String>()
        signedMatch.matchGifts.getOrNull(0)?.let { matchGift ->
            matchGift.product.productAttributes.forEach { gift ->
                playInfo.find { it.type == gift.alias }?.value?.let {
                    gifts.add(it)
                }
            }
            Timber.d("giftsInfo ${gifts.joinToString("|")}")
        }
        //回傳格式爲"{name:顏色,alias:color,value:經典白}|{name:尺寸,alias:size,value:M}"
        return gifts.joinToString("|")
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