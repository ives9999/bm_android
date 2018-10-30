package com.sportpassword.bm.Adapters

import android.view.View
import com.sportpassword.bm.R
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.search_row_item.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange

class SearchItem(val title: String, val detail: String, val switch: Boolean, val section: Int, val row: Int, val inputK:(view: View, b: Boolean)->Unit, val switched:(idx: Int, b: Boolean)->Unit): Item() {

    override fun getLayout() = R.layout.search_row_item

    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

        viewHolder.row_title.text = title
        viewHolder.row_detail.text = detail

        if ((section == 0 && position == 1) || (section == -1 && position == 0)) {
            val keywordView = viewHolder.keyword
            viewHolder.row_detail.visibility = View.INVISIBLE
            viewHolder.greater.visibility = View.INVISIBLE
            keywordView.visibility = View.VISIBLE
            keywordView.setOnFocusChangeListener { view, b ->
                inputK(view, b)
            }
        } else {
            viewHolder.row_detail.visibility = View.VISIBLE
            viewHolder.greater.visibility = View.VISIBLE
            viewHolder.keyword.visibility = View.INVISIBLE
        }

        val switchView = viewHolder.search_switch
        if (switch) {
            switchView.visibility = View.VISIBLE
            viewHolder.row_detail.visibility = View.INVISIBLE
            viewHolder.greater.visibility = View.INVISIBLE
            viewHolder.keyword.visibility = View.INVISIBLE
            switchView.onCheckedChange{ view, b ->
                switched(position, b)
            }
        } else {
            switchView.visibility = View.INVISIBLE
        }
    }

}