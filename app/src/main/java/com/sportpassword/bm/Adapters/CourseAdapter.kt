package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.ImageButton
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.course_list_cell.view.*
import kotlinx.android.synthetic.main.course_list_cell.view.cityBtn
import kotlinx.android.synthetic.main.course_list_cell.view.intervalLbl
import kotlinx.android.synthetic.main.course_list_cell.view.signup_countLbl
import kotlinx.android.synthetic.main.course_list_cell.view.weekdayLbl

class CourseAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<CourseViewHolder>(resource, ::CourseViewHolder, list1CellDelegate) {}

class CourseViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: CourseTable = _row as CourseTable

        if (viewHolder.cityBtn != null) {
            if (row.city_show.length > 0) {
                viewHolder.cityBtn.text = row.city_show
                viewHolder.cityBtn.setOnClickListener {
                    if (list1CellDelegate != null) {
                        list1CellDelegate.cellCity(row)
                    }
                }
                viewHolder.cityBtn.visibility = View.VISIBLE
            } else {
                viewHolder.cityBtn.visibility = View.GONE
            }
        }

        if (viewHolder.priceLbl != null) {
            if (row.price_text_short != null && row.price_text_short.isNotEmpty()) {
                viewHolder.priceLbl.text = row.price_text_short
            } else {
                viewHolder.priceLbl.text = "價格:未提供"
            }
        }

        if (viewHolder.weekdayLbl != null) {
            if (row.weekdays_show.length > 0) {
                viewHolder.weekdayLbl.text = row.weekdays_show
            } else {
                viewHolder.weekdayLbl.text = "未提供"
            }
        }

        if (viewHolder.intervalLbl != null) {
            if (row.interval_show.length > 0) {
                viewHolder.intervalLbl.text = row.interval_show
            } else {
                viewHolder.intervalLbl.text = "未提供"
            }
        }

        if (viewHolder.people_limitLbl != null) {
            viewHolder.people_limitLbl.text = row.people_limit_show
        }

        if (viewHolder.signup_countLbl != null) {
            if (row.signup_count_show.length > 0 && row.people_limit > 0) {
                viewHolder.signup_countLbl.text = "已報名:${row.signup_count_show}"
            } else {
                viewHolder.signup_countLbl.visibility = View.INVISIBLE
            }
        }

        if (viewHolder.iconView != null) {
            val v = viewHolder.iconView
            var a = v.findViewById<ImageButton>(R.id.editIcon)
            a?.setOnClickListener {
                list1CellDelegate?.cellEdit(row)
            }

            a = v.findViewById<ImageButton>(R.id.deleteIcon)
            a?.setOnClickListener {
                list1CellDelegate?.cellDelete(row)
            }

            a = v.findViewById<ImageButton>(R.id.signupIcon)
            a?.setOnClickListener {
                list1CellDelegate?.cellSignup(row)
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