package com.sportpassword.bm.bm_new.ui.match.sign_up

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.databinding.ActivityMatchSignUpBinding
import timber.log.Timber

class MatchSignUpActivity : BaseActivity<ActivityMatchSignUpBinding>() {

    private var signUpPagerAdapter: MatchSignUpPagerAdapter? = null

    override fun initViewBinding(): ActivityMatchSignUpBinding =
        ActivityMatchSignUpBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {}

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            btnClose.setOnClickListener { finish() }

            val signUpPages = listOf(0, 1, 2)
            indicator.addIndicator(signUpPages.size - 1)

            vp.run {
                adapter = MatchSignUpPagerAdapter(
                    supportFragmentManager,
                    lifecycle,
                    items = signUpPages,
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

    override fun getViewModel(): BaseViewModel? = null

}