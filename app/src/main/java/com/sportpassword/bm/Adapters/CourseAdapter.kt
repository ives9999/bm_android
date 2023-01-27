package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R

class CourseAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<CourseViewHolder>(resource, ::CourseViewHolder, list1CellDelegate) {}

class CourseViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: CourseTable = _row as CourseTable

        viewHolder.findViewById<Button>(R.id.cityBtn) ?. let {
            if (row.city_show.length > 0) {
                it.text = row.city_show
                it.setOnClickListener {
                    if (list1CellDelegate != null) {
                        list1CellDelegate.cellCity(row)
                    }
                }
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.GONE
            }
        }

        viewHolder.findViewById<TextView>(R.id.priceLbl) ?. let {
            if (row.price_text_short != null && row.price_text_short.isNotEmpty()) {
                it.text = row.price_text_short
            } else {
                it.text = "價格:未提供"
            }
        }

        viewHolder.findViewById<TextView>(R.id.weekdayLbl) ?. let {
            if (row.weekdays_show.length > 0) {
                it.text = row.weekdays_show
            } else {
                it.text = "未提供"
            }
        }

        viewHolder.findViewById<TextView>(R.id.intervalLbl) ?. let {
            if (row.interval_show.length > 0) {
                it.text = row.interval_show
            } else {
                it.text = "未提供"
            }
        }

        viewHolder.findViewById<TextView>(R.id.people_limitLbl) ?. let {
            it.text = row.people_limit_show
        }

        viewHolder.findViewById<TextView>(R.id.signup_countLbl) ?. let {
            if (row.signup_count_show.length > 0 && row.people_limit > 0) {
                it.text = "已報名:${row.signup_count_show}"
            } else {
                it.visibility = View.INVISIBLE
            }
        }

        viewHolder.findViewById<RelativeLayout>(R.id.iconView) ?. let {
            viewHolder.findViewById<ImageButton>(R.id.editIcon) ?. let { it1 ->
                it1.setOnClickListener {
                    list1CellDelegate?.cellEdit(row)
                }
            }

            viewHolder.findViewById<ImageButton>(R.id.deleteIcon) ?. let { it1 ->
                it1.setOnClickListener {
                    list1CellDelegate?.cellDelete(row)
                }
            }

            viewHolder.findViewById<ImageButton>(R.id.signupIcon) ?. let { it1 ->
                it1.setOnClickListener {
                    list1CellDelegate?.cellSignup(row)
                }
            }
        }

//        val r = viewHolder.context.resources.getIdentifier("editIcon", "id", viewHolder.context.packageName)
//        if (r != null) {
//            val editIcon: ImageButton = viewHolder.findViewById(R.id.editIcon)
//            if (editIcon != null) {
//                editIcon.setOnClickListener {
//                    if (list1CellDelegate != null) {
//                        list1CellDelegate.cellEdit(row)
//                    }
//                }
//            }
//        }
    }
}