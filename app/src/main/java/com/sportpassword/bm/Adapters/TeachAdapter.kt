package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeachTable
import kotlinx.android.synthetic.main.teach_list_cell.view.*

class TeachAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<TeachViewHolder>(resource, ::TeachViewHolder, list1CellDelegate) {}

class TeachViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: TeachTable = _row as TeachTable
        viewHolder.pvLbl.text = row.pv.toString()
        viewHolder.dateLbl.text = row.created_at_show
    }
}
