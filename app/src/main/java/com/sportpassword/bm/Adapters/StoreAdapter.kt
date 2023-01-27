package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.StoreTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R

class StoreAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<StoreViewHolder>(resource, ::StoreViewHolder, list1CellDelegate) {}

class StoreViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: StoreTable = _row as StoreTable

        viewHolder.findViewById<Button>(R.id.cityBtn) ?. let {
            it.text = row.city_show
            it.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate.cellCity(row)
                }
            }
        }


        viewHolder.findViewById<TextView>(R.id.titleLbl) ?. let {
            it.text = row.name
        }

        viewHolder.findViewById<TextView>(R.id.business_timeTxt) ?. let {
            it.text = "${row.open_time_show}~${row.close_time_show}"
        }

        viewHolder.findViewById<TextView>(R.id.addressTxt) ?. let {
            it.text = row.address
        }

        viewHolder.findViewById<TextView>(R.id.telLbl) ?. let {
            if (row.tel_show.isNotEmpty()) {
                it.text = row.tel_show
            } else {
                it.text = "電話：未提供"
            }
        }
    }
}