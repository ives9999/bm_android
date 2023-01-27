package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeachTable
import com.sportpassword.bm.R

class TeachAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<TeachViewHolder>(resource, ::TeachViewHolder, list1CellDelegate) {}

class TeachViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: TeachTable = _row as TeachTable

        viewHolder.findViewById<TextView>(R.id.pvLbl) ?. let {
            it.text = row.pv.toString()
        }

        viewHolder.findViewById<TextView>(R.id.dateLbl) ?. let {
            it.text = row.created_at_show
        }
    }
}
