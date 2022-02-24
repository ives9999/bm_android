package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.ImageButton
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.truncate
import kotlinx.android.synthetic.main.team_list_cell.view.*
import kotlinx.android.synthetic.main.team_list_cell.view.cityBtn
import kotlinx.android.synthetic.main.team_list_cell.view.iconView
import kotlinx.android.synthetic.main.team_list_cell.view.intervalLbl
import kotlinx.android.synthetic.main.team_list_cell.view.signup_countLbl
import kotlinx.android.synthetic.main.team_list_cell.view.weekdayLbl

class TeamAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<TeamViewHolder>(resource, ::TeamViewHolder, list1CellDelegate) {}

class TeamViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: TeamTable = _row as TeamTable

        if (viewHolder.cityBtn != null) {
            if (row.arena?.name != null && row.arena!!.name.isNotEmpty()) {
                viewHolder.cityBtn.text = row.arena!!.city_show
                viewHolder.cityBtn.setOnClickListener {
                    list1CellDelegate?.cellCity(row)
                }

                var arena_name: String = row.arena!!.name
                if (arena_name.length > 5) {
                   arena_name = arena_name.truncate(5)
                }
                viewHolder.arenaBtn.text = arena_name
                viewHolder.arenaBtn.setOnClickListener {
                    list1CellDelegate?.cellArena(row)
                }
            } else {
                viewHolder.cityBtn.visibility = View.GONE
                viewHolder.arenaBtn.visibility = View.GONE
            }
        }

        if (viewHolder.weekdayLbl != null) {
            if (row.weekdays_show.isNotEmpty()) {
                viewHolder.weekdayLbl.text = row.weekdays_show
            } else {
                viewHolder.weekdayLbl.text = "臨打日期:未提供"
            }
        }

        if (viewHolder.intervalLbl != null) {
            if (row.interval_show.isNotEmpty()) {
                viewHolder.intervalLbl.text = row.interval_show
            } else {
                viewHolder.weekdayLbl.text = "臨打時段:未提供"
            }
        }

        if (viewHolder.iconView != null) {
            val v = viewHolder.iconView
            var a = v.findViewById<ImageButton>(R.id.mapIcon)
            if (a != null && row.arena != null) {

                if (row.arena!!.address == null || row.arena!!.address.isEmpty()) {
                    a.visibility = View.GONE
                } else {
                    a.visibility = View.VISIBLE
                    a.setOnClickListener {
                        list1CellDelegate?.cellShowMap(row)
                    }
                }
            }

            a = v.findViewById<ImageButton>(R.id.editIcon)
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

        if (viewHolder.temp_quantityLbl != null) {
            viewHolder.temp_quantityLbl.text = row.people_limit_show
        }

        if (viewHolder.signup_countLbl != null) {
            viewHolder.signup_countLbl.text = row.temp_signup_count_show
        }
    }
}