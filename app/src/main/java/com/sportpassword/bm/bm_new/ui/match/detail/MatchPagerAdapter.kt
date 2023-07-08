package com.sportpassword.bm.bm_new.ui.match.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sportpassword.bm.bm_new.ui.match.detail.brochure.MatchBrochureFragment
import com.sportpassword.bm.bm_new.ui.match.detail.content.MatchContentFragment
import com.sportpassword.bm.bm_new.ui.match.detail.groups.MatchGroupsFragment
import com.sportpassword.bm.bm_new.ui.vo.MatchTab

class MatchPagerAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle,
    private val tabList: List<MatchTab>,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (tabList[position]) {
            MatchTab.CONTENT -> MatchContentFragment.newInstance()
            MatchTab.BROCHURE -> MatchBrochureFragment.newInstance()
            MatchTab.SIGNUP -> MatchGroupsFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        return tabList.size
    }
}