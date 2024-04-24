package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.truncate

class TeamAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<TeamViewHolder>(resource, ::TeamViewHolder, list1CellDelegate) {}

class TeamViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: TeamTable = _row as TeamTable

        viewHolder.findViewById<Button>(R.id.cityBtn) ?. let {
            if (row.arena?.name != null && row.arena!!.name.isNotEmpty()) {
                it.text = row.arena!!.city_show
                it.setOnClickListener {
                    list1CellDelegate?.cellCity(row)
                }

                var arena_name: String = row.arena!!.name
                if (arena_name.length > 5) {
                    arena_name = arena_name.truncate(5)
                }

                viewHolder.findViewById<Button>(R.id.arenaBtn) ?. let { it1 ->
                    it1.text = arena_name
                    it1.setOnClickListener {
                        list1CellDelegate?.cellArena(row)
                    }
                }
            } else {
                it.visibility = View.GONE
                viewHolder.findViewById<Button>(R.id.arenaBtn) ?. let { it1 ->
                    it1.visibility = View.GONE
                }
            }
        }

        viewHolder.findViewById<TextView>(R.id.weekdayLbl) ?. let {
            if (row.weekdays_show.isNotEmpty()) {
                it.text = row.weekdays_show
            } else {
                it.text = "臨打日期:未提供"
            }
        }

        viewHolder.findViewById<TextView>(R.id.intervalLbl) ?. let {
            if (row.interval_show.isNotEmpty()) {
                it.text = row.interval_show
            } else {
                it.text = "臨打時段:未提供"
            }
        }

        viewHolder.findViewById<RelativeLayout>(R.id.iconView) ?. let {
            var a = it.findViewById<ImageButton>(R.id.mapIcon)
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

            a = it.findViewById<ImageButton>(R.id.editIcon)
            a?.setOnClickListener {
                list1CellDelegate?.cellEdit(row)
            }

            a = it.findViewById<ImageButton>(R.id.deleteIcon)
            a?.setOnClickListener {
                list1CellDelegate?.cellDelete(row)
            }

            a = it.findViewById<ImageButton>(R.id.signupIcon)
            a?.setOnClickListener {
                list1CellDelegate?.cellSignup(row)
            }

            a = it.findViewById<ImageButton>(R.id.teamMemberIcon)
            a?.setOnClickListener {
                list1CellDelegate?.cellTeamMember(row)
            }
        }

        viewHolder.findViewById<TextView>(R.id.temp_quantityLbl) ?. let {
            it.text = row.people_limit_show
        }

        viewHolder.findViewById<TextView>(R.id.signup_countLbl) ?. let {
            it.text = row.temp_signup_count_show
        }
    }
}