package com.sportpassword.bm.bm_new.ui.match.detail

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.vo.MatchTab
import com.sportpassword.bm.databinding.ActivityMatchDetailBinding

class MatchDetailActivity : BaseActivity<ActivityMatchDetailBinding>() {

    companion object {
        const val MATCH_TITLE = "match_title"
    }

    private var title: String? = null
    private var matchPagerAdapter: MatchPagerAdapter? = null
    private val tabs = MatchTab.values().toList()

    override fun initViewBinding(): ActivityMatchDetailBinding =
        ActivityMatchDetailBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
        data.getString(MATCH_TITLE)?.let {
            title = it
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            tvName.text = title

            vp.run {
                adapter = MatchPagerAdapter(
                    supportFragmentManager,
                    lifecycle,
                    tabList = tabs,
                ).apply {
                    matchPagerAdapter = this
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

    override fun getViewModel(): BaseViewModel? = null
}