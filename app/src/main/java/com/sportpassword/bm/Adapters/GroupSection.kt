package com.sportpassword.bm.Adapters

import android.view.View
import com.sportpassword.bm.R
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.search_section_item.*

class GroupSection(val title: String, val show: Boolean=true): Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun getLayout() = R.layout.search_section_item

    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
        viewHolder.section_title.text = title
        viewHolder.collapse.setImageResource(getExpandIcon())

        viewHolder.section_container.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewHolder.collapse.setImageResource(getExpandIcon())
        }
        if (!show) {
            viewHolder.section_container.visibility = View.GONE
        } else {
            viewHolder.section_container.visibility = View.VISIBLE
        }
//        println(position)
//        if (position == 0) {
//            viewHolder.section_container.visibility = View.GONE
//        } else {
//            viewHolder.section_container.visibility = View.VISIBLE
//        }
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    private fun getExpandIcon() =
            if (expandableGroup.isExpanded)
                R.drawable.to_down
            else
                R.drawable.to_right
}