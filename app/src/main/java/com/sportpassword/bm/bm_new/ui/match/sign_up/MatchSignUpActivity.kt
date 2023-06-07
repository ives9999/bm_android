package com.sportpassword.bm.bm_new.ui.match.sign_up

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.databinding.ActivityMatchSignUpBinding
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

class MatchSignUpActivity : BaseActivity<ActivityMatchSignUpBinding>() {

    companion object {
        const val MATCH_GROUP_TOKEN = "matchGroupToken"
    }

    private var signUpPagerAdapter: MatchSignUpPagerAdapter? = null
    private val vm by stateViewModel<MatchSignUpVM>()

    override fun initViewBinding(): ActivityMatchSignUpBinding =
        ActivityMatchSignUpBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
        data.getString(MATCH_GROUP_TOKEN)?.let {
            vm.getMatchSignUp(it)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            btnClose.setOnClickListener { finish() }
            btnCancel.setOnClickListener { finish() }

            btnSignUp.setOnClickListener {
                //todo post報名動作
            }

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
            }
        }
    }

    override fun getViewModel(): BaseViewModel = vm

}