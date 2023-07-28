package com.sportpassword.bm.bm_new.ui.match.detail

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.sportpassword.bm.bm_new.ui.base.BaseActivity
import com.sportpassword.bm.bm_new.ui.base.BaseViewModel
import com.sportpassword.bm.bm_new.ui.vo.MatchTab
import com.sportpassword.bm.databinding.ActivityMatchDetailBinding
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MatchDetailActivity : BaseActivity<ActivityMatchDetailBinding>() {

    companion object {
        const val MATCH_TITLE = "match_title"
        const val MATCH_TOKEN = "match_token"
        const val SIGN_UP = "sign_up"
    }

    private var title: String? = null
    private var directToSignUp = false
    private var matchPagerAdapter: MatchPagerAdapter? = null
    private val tabs = MatchTab.values().toList()
    private val vm by stateViewModel<MatchDetailVM>()

    override fun initViewBinding(): ActivityMatchDetailBinding =
        ActivityMatchDetailBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
        title = data.getString(MATCH_TITLE)
        data.getString(MATCH_TOKEN)?.let {
            vm.getMatchDetail(it)
        }

        directToSignUp = data.getBoolean(SIGN_UP)
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

                if (directToSignUp) {
                    setCurrentItem(2, false)
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