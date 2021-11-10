package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.CoachTable
import com.sportpassword.bm.Models.Table
import kotlinx.android.synthetic.main.coach_list_cell.view.*

class CoachAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<CoachViewHolder>(resource, ::CoachViewHolder, list1CellDelegate) {}

class CoachViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: CoachTable = _row as CoachTable
        if (row.city_show.isNotEmpty()) {
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

        if (row.mobile_show.isNotEmpty()) {
            viewHolder.mobileLbl.text = row.mobile_show
        }

        if (row.seniority >= 0) {
            viewHolder.seniorityLbl.text = "年資:${row.seniority_show}"
        } else {
            viewHolder.seniorityLbl.text = "年資:未提供"
        }

        if (row.line != null && row.line.isNotEmpty()) {
            viewHolder.line.text = "Line:${row.line}"
        } else {
            viewHolder.line.text = "Line:未提供"
        }
    }
}