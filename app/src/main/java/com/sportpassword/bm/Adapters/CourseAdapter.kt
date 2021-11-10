package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.Table
import kotlinx.android.synthetic.main.course_list_cell.view.*

class CourseAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<CourseViewHolder>(resource, ::CourseViewHolder, list1CellDelegate) {}

class CourseViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: CourseTable = _row as CourseTable

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

        if (row.price_text_short != null && row.price_text_short.isNotEmpty()) {
            viewHolder.priceLbl.text = row.price_text_short
        } else {
            viewHolder.priceLbl.text = "價格:未提供"
        }

        if (row.weekdays_show.length > 0) {
            viewHolder.weekdayLbl.text = row.weekdays_show
        } else {
            viewHolder.weekdayLbl.text = "未提供"
        }

        if (row.interval_show.length > 0) {
            viewHolder.intervalLbl.text = row.interval_show
        } else {
            viewHolder.intervalLbl.text = "未提供"
        }

        viewHolder.people_limitLbl.text = row.people_limit_show

        if (row.signup_count_show.length > 0 && row.people_limit > 0) {
            viewHolder.signup_countLbl.text = "已報名:${row.signup_count_show}"
        } else {
            viewHolder.signup_countLbl.visibility = View.INVISIBLE
        }
    }
}