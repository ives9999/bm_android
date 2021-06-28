package com.sportpassword.bm.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sportpassword.bm.Fragments.*

/**
 * Created by ivessun on 2018/2/23.
 */

class TabAdapter(fm: FragmentManager, private val tabs: Array<String>, val screenWidth: Int=0): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        //println("position: $position")
        when(position) {
            0 -> return TempPlayFragment.newInstance("team", screenWidth)
            1 -> return CourseFragment.newInstance("course", screenWidth)
            2 -> return MemberFragment.newInstance("member", screenWidth)
            3 -> return ArenaFragment.newInstance("arena", screenWidth)
            4 -> return MoreFragment.newInstance("more", screenWidth)
            else -> return TempPlayFragment.newInstance("team", screenWidth)
        }
    }

    override fun getCount(): Int {
        return tabs.size
    }

}