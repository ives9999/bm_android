package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Controllers.viewHolder
import com.sportpassword.bm.Models.ArenaTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R

//import kotlinx.android.synthetic.main.arena_list_cell.view.*

class ArenaAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<ArenaViewHolder>(resource, ::ArenaViewHolder, list1CellDelegate) {}

class ArenaViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: ArenaTable = _row as ArenaTable

        viewHolder.findViewById<Button>(R.id.cityBtn) ?. let {
            if (row.city_show.isNotEmpty()) {
                it.text = row.city_show
                it.setOnClickListener {
                    list1CellDelegate?.cellCity(row)
                }
            } else {
                it.visibility = View.GONE
            }
        }

        viewHolder.findViewById<Button>(R.id.areaBtn) ?. let {
            if (row.area_show.isNotEmpty()) {
                it.text = row.area_show
                it.setOnClickListener {
                    list1CellDelegate?.cellArea(row)
                }
            } else {
                it.visibility = View.GONE
            }
        }

        viewHolder.findViewById<TextView>(R.id.telLbl) ?. let {
            if (row.tel_show.isNotEmpty()) {
                it.text = row.tel_show
            } else {
                it.text = "電話：未提供"
            }
        }


        viewHolder.findViewById<TextView>(R.id.parkingLbl) ?. let {
            it.text = "停車場:${row.parking_show}"
        }

        viewHolder.findViewById<TextView>(R.id.intervalLbl) ?. let {
            if (row.interval_show.isNotEmpty()) {
                it.text = row.interval_show
            } else {
                it.text = "未提供"
            }
        }

        viewHolder.findViewById<TextView>(R.id.air_conditionLbl) ?. let {
            it.text = "空調:${row.air_condition_show}"
        }
    }
}