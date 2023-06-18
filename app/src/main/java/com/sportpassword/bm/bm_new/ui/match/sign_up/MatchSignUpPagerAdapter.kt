package com.sportpassword.bm.bm_new.ui.match.sign_up

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sportpassword.bm.bm_new.ui.error.BlankFragment

class MatchSignUpPagerAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle,
    private val number: Int,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MatchTeamInformationFragment.newInstance()
            1 -> BlankFragment.newInstance()
            else -> BlankFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        return number + 1
        //number爲1時，應有隊名/隊員1，共2頁
        //number爲2時，應有隊名/隊員1/隊員2，共3頁
    }
}