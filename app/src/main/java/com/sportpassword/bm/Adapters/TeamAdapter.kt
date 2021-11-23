package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.ImageButton
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.team_list_cell.view.*

class TeamAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<TeamViewHolder>(resource, ::TeamViewHolder, list1CellDelegate) {}

class TeamViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: TeamTable = _row as TeamTable

        if (row.arena?.name != null && row.arena!!.name.length > 0) {
            viewHolder.cityBtn.text = row.arena!!.city_show
            viewHolder.cityBtn.setOnClickListener {
                list1CellDelegate?.cellCity(row)
            }

            viewHolder.arenaBtn.text = row.arena!!.name
            viewHolder.arenaBtn.setOnClickListener {
                list1CellDelegate?.cellArena(row)
            }
        } else {
            viewHolder.cityBtn.visibility = View.GONE
            viewHolder.arenaBtn.visibility = View.GONE
        }

        if (row.weekdays_show.isNotEmpty()) {
            viewHolder.weekdayLbl.text = row.weekdays_show
        } else {
            viewHolder.weekdayLbl.text = "臨打日期:未提供"
        }

        if (row.interval_show.isNotEmpty()) {
            viewHolder.intervalLbl.text = row.interval_show
        } else {
            viewHolder.weekdayLbl.text = "臨打時段:未提供"
        }

        val v = viewHolder.iconView
        val a = v.findViewById<ImageButton>(R.id.mapIcon)
        if (a != null && row.arena != null) {

            if (row.arena!!.address == null || row.arena!!.address.isEmpty()) {
                viewHolder.mapIcon.visibility = View.GONE
            } else {
                viewHolder.mapIcon.visibility = View.VISIBLE
                viewHolder.mapIcon.setOnClickListener {
                    list1CellDelegate?.cellShowMap(row)
                }
            }
        }

        viewHolder.temp_quantityLbl.text = row.temp_quantity_show
        viewHolder.signup_countLbl.text = row.temp_signup_count_show
    }
}