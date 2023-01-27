package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Controllers.viewHolder
import com.sportpassword.bm.Models.CoachTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R

//import kotlinx.android.synthetic.main.coach_list_cell.view.*

class CoachAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<CoachViewHolder>(resource, ::CoachViewHolder, list1CellDelegate) {}

class CoachViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: CoachTable = _row as CoachTable

        viewHolder.findViewById<Button>(R.id.cityBtn) ?. let {
            if (row.city_show.isNotEmpty()) {
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

        if (row.mobile_show.isNotEmpty()) {
            viewHolder.findViewById<TextView>(R.id.mobile) ?. let {
                it.text = row.mobile_show
            }
        }

        viewHolder.findViewById<TextView>(R.id.seniorityLbl) ?. let {
            if (row.seniority >= 0) {
                it.text = "年資:${row.seniority_show}"
            } else {
                it.text = "年資:未提供"
            }
        }

        viewHolder.findViewById<TextView>(R.id.line) ?. let {
            if (row.line != null && row.line.isNotEmpty()) {
                it.text = "Line:${row.line}"
            } else {
                it.text = "Line:未提供"
            }
        }
    }
}