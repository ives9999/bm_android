package com.sportpassword.bm.bm_new.ui.match.sign_up

import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.base.ViewEvent
import com.sportpassword.bm.databinding.ActivityMatchSignUpBinding
import com.sportpassword.bm.member
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

class MatchSignUpActivity : BaseActivity<ActivityMatchSignUpBinding>() {

    companion object {
        const val MATCH_GROUP_TOKEN = "matchGroupToken"
        const val MATCH_GROUP_ID = "matchGroupId"
        const val MATCH_ID = "matchId"
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
                            Timber.d("page selected position, $position")
                            indicator.setIndicatorSelected(position)
                        }
                    })
                }

                vm.initSignUpInfo(it.matchGroup.number)

                btnSignUp.setOnClickListener {
                    Timber.d("member token, ${member.token}")
                    val requiredBlankList =
                        (vm.signUpList.filter { it.value.isBlank() && it.isRequired })

                    when (requiredBlankList.isEmpty()) {
                        true -> {
                            vm.signUp()
                        }

                        false -> {      //有必填欄位空白時，show alert
                            val stringBuilder = StringBuilder()
                            requiredBlankList.forEach { signUpInfo ->
                                val title = getString(signUpInfo.titleStringRes)
                                when {
                                    signUpInfo.type.contains("player_one") -> {
                                        stringBuilder.append(
                                            "${getString(R.string.match_sign_player_one)} $title"
                                        )
                                    }

                                    signUpInfo.type.contains("player_two") -> {
                                        stringBuilder.append(
                                            "${getString(R.string.match_sign_player_two)} $title"
                                        )
                                    }

                                    else -> {
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

            else -> {}
        }
    }

}