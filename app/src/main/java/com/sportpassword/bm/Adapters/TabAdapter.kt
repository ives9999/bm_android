package com.sportpassword.bm.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.sportpassword.bm.Fragments.*

/**
 * Created by ivessun on 2018/2/23.
 */

class TabAdapter(fm: FragmentManager, val tabs: Array<String>): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        //println("position: $position")
        when(position) {
            0 -> return TempPlayFragment.newInstance("tempPlay", "blah")
            1 -> return CoachFragment.newInstance("coach", "blah")
            2 -> return TeamFragment.newInstance("team", "blah")
            3 -> return MoreFragment.newInstance("more", "blah")
            else -> return TempPlayFragment.newInstance("tempPlay", "blah")
        }
    }

    override fun getCount(): Int {
        return tabs.size
    }

}