package com.sportpassword.bm.bm_new.ui.match.sign_up

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.base.ViewEvent
import com.sportpassword.bm.bm_new.ui.match.sign_up.MatchTeamInformationFragment.Companion.CAPTAIN
import com.sportpassword.bm.databinding.ActivityMatchSignUpBinding
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MatchSignUpActivity : BaseActivity<ActivityMatchSignUpBinding>() {

    companion object {
        const val MATCH_GROUP_TOKEN = "matchGroupToken"
        const val MATCH_TEAM_TOKEN = "matchTeamToken"
        const val MATCH_GROUP_ID = "matchGroupId"
        const val MATCH_ID = "matchId"
        const val PLAYER_NAME = "player_name"
        const val PLAYER_PHONE = "player_phone"
        const val PLAYER_EMAIL = "player_email"
        const val PLAYER_LINE = "player_line"
        const val PLAYER_AGE = "player_age"
    }

    private var signUpPagerAdapter: MatchSignUpPagerAdapter? = null
    private val vm by stateViewModel<MatchSignUpVM>()

    override fun initViewBinding(): ActivityMatchSignUpBinding =
        ActivityMatchSignUpBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
        data.getString(MATCH_GROUP_TOKEN)?.let {
            vm.getMatchSignUp(
                data.getInt(MATCH_GROUP_ID),
                data.getInt(MATCH_ID),
                it
            )
        }

        //從編輯過來時
        data.getString(MATCH_TEAM_TOKEN)?.let {
            vm.editSignedMatch(it)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            btnCancel.setOnClickListener { finish() }

            vm.matchSignUp.observe(this@MatchSignUpActivity) {
                indicator.addIndicator(it.matchGroup.number)

                vp.run {
                    adapter = MatchSignUpPagerAdapter(
                        supportFragmentManager,
                        lifecycle,
                        it.matchGroup.number,
                    ).apply {
                        signUpPagerAdapter = this
                    }
                    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageScrolled(
                            position: Int,
                            positionOffset: Float,
                            positionOffsetPixels: Int
                        ) {
                            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                            if (positionOffsetPixels != 0) {
                                return
                            }
                        }

                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            indicator.setIndicatorSelected(position)
                        }
                    })

                    offscreenPageLimit = 2
                }

                vm.initSignUpInfo(it.matchGroup.number)

                btnSignUp.setOnClickListener {
                    val requiredBlankList =
                        (vm.playerInfoList.filter { it.value.isBlank() && it.isRequired })

                    when (requiredBlankList.isEmpty()) {
                        true -> {
                            vm.signUp()
                        }

                        false -> {      //有必填欄位空白時，show alert
                            val stringBuilder = StringBuilder()
                            requiredBlankList.forEach { signUpInfo ->
                                val title = getString(signUpInfo.titleStringRes)

                                when (signUpInfo.playerNum) {
                                    CAPTAIN -> {
                                        stringBuilder.append(title)
                                    }

                                    else -> {   //隊員
                                        stringBuilder.append(
                                            getString(
                                                R.string.match_sign_player_num,
                                                signUpInfo.playerNum
                                            )
                                        )
                                        stringBuilder.append(title)
                                    }
                                }

                                stringBuilder.append(getString(R.string.match_sign_cannot_blank))
                                stringBuilder.append("\n")
                            }
                            Alert.show(this@MatchSignUpActivity, "警告", "$stringBuilder")
                        }
                    }
                }
            }
        }
    }

    override fun getViewModel(): BaseViewModel = vm

    override fun handleViewEvent(event: ViewEvent) {
        super.handleViewEvent(event)
        when (event) {
            is MatchSignUpVM.SignUpEvent.SignUp -> {
                if (event.isSuccess) {
                    Toast.makeText(this, getString(R.string.match_sign_success), Toast.LENGTH_LONG)
                        .show()
                    finish()
                }
            }

            is MatchSignUpVM.SignUpEvent.Edit -> {
                if (event.isSuccess) {
                    Toast.makeText(
                        this,
                        getString(R.string.match_sign_edit_success),
                        Toast.LENGTH_LONG
                    ).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }

            else -> {}
        }
    }
}