package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.StoreTable
import com.sportpassword.bm.Models.Table
import kotlinx.android.synthetic.main.store_list_cell.view.*

class StoreAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<StoreViewHolder>(resource, ::StoreViewHolder, list1CellDelegate) {}

class StoreViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: StoreTable = _row as StoreTable
        viewHolder.cityBtn.text = row.city_show
        viewHolder.cityBtn.setOnClickListener {
            if (list1CellDelegate != null) {
                list1CellDelegate!!.cellCity(row)
            }
        }

        viewHolder.titleLbl.text = row.name

        viewHolder.business_timeTxt.text = "${row.open_time_show}~${row.close_time_show}"

        viewHolder.addressTxt.text = row.address

        if (row.tel_show.isNotEmpty()) {
            viewHolder.telLbl.text = row.tel_show
        } else {
            viewHolder.telLbl.text = "電話：未提供"
        }
    }
}