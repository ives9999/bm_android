package com.sportpassword.bm.bm_new.ui.match.detail

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.sportpassword.bm.bm_new.data.dto.match.MatchListDto
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.vo.MatchTab
import com.sportpassword.bm.databinding.ActivityMatchDetailBinding
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MatchDetailActivity : BaseActivity<ActivityMatchDetailBinding>() {

    companion object {
        const val MATCH_ROW = "match_row"
        const val SIGN_UP = "sign_up"
    }

    private var title: String? = null
    private var isSignUp = false
    private var matchPagerAdapter: MatchPagerAdapter? = null
    private val tabs = MatchTab.values().toList()
    private val vm by stateViewModel<MatchDetailVM>()

    override fun initViewBinding(): ActivityMatchDetailBinding =
        ActivityMatchDetailBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
        data.getParcelable<MatchListDto.Row>(MATCH_ROW)?.let {
            title = it.name
            vm.getMatchDetail(it.token)
        }

        isSignUp = data.getBoolean(SIGN_UP)
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {

            btnClose.setOnClickListener { finish() }

            tvName.text = title

            vp.run {
                adapter = MatchPagerAdapter(
                    supportFragmentManager,
                    lifecycle,
                    tabList = tabs,
                ).apply {
                    matchPagerAdapter = this
                }

                isUserInputEnabled = false

                if (isSignUp) {
                    vp.currentItem = 2
                }
            }

            TabLayoutMediator(tblMatch, vp) { tab, position ->
                with(tabs[position]) {
                    tab.text = getString(titleResId)
                    tab.tag = this
                }
            }.attach()
        }
    }

    override fun getViewModel(): BaseViewModel = vm
}