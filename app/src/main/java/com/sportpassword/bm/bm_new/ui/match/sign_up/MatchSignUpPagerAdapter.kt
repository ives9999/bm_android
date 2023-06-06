package com.sportpassword.bm.bm_new.ui.match.sign_up

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sportpassword.bm.bm_new.ui.error.BlankFragment

class MatchSignUpPagerAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle,
    private val items: List<Int>,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (items[position]) {
            0 -> BlankFragment.newInstance()
            1 -> BlankFragment.newInstance()
            else -> BlankFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}