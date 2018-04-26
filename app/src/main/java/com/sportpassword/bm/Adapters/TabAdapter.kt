package com.sportpassword.bm.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.sportpassword.bm.Fragments.*

/**
 * Created by ivessun on 2018/2/23.
 */

class TabAdapter(fm: FragmentManager, val tabs: Array<String>, val screenWidth: Int=0): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        //println("position: $position")
        when(position) {
            0 -> return TempPlayFragment.newInstance("tempPlay", screenWidth)
            1 -> return CoachFragment.newInstance("coach", screenWidth)
            2 -> return TeamFragment.newInstance("team", screenWidth)
            3 -> return MoreFragment.newInstance("more", screenWidth)
            else -> return TempPlayFragment.newInstance("tempPlay", screenWidth)
        }
    }

    override fun getCount(): Int {
        return tabs.size
    }

}