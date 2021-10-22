package com.sportpassword.bm.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sportpassword.bm.Fragments.*

/**
 * Created by ivessun on 2018/2/23.
 */

class TabAdapter(activity: FragmentActivity, private val tabs: Array<String>, val screenWidth: Int=0): FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        //println("position: $position")
        return when(position) {
            0 -> TempPlayFragment()
            1 -> CourseFragment()
            2 -> MemberFragment()
            3 -> ArenaFragment()
            4 -> MoreFragment()
            else -> TempPlayFragment()
        }
    }

    override fun getItemCount(): Int {
        return tabs.size
    }

}