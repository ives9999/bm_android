package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.MemberCoinTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.noSec
import kotlinx.android.synthetic.main.coin_list_cell.view.*

class MemberCoinAdapter(list1CellDelegate: List1CellDelegate?): MyAdapter<MemberCoinViewHolder>(
    R.layout.coin_list_cell, ::MemberCoinViewHolder, list1CellDelegate) {

    override fun onBindViewHolder(holder: MemberCoinViewHolder, position: Int) {

        val row: MemberCoinTable = tableList[position] as MemberCoinTable

        holder.bind(row, position)
    }
}

class MemberCoinViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    val noLbl: TextView = viewHolder.noLbl
    val priceLbl: TextView = viewHolder.priceLbl
    val able_typeLbl: TextView = viewHolder.able_typeLbl
    val dateLbl: TextView = viewHolder.dateLbl

    //_row is cartTable
    override fun bind(row: Table, idx: Int) {

        val _row: MemberCoinTable = row as MemberCoinTable
        _row.filterRow()
        val no: String = (idx + 1).toString() + "."

        noLbl.text = no
        priceLbl.text = _row.coin_show
        if (_row.able_type_show.isNotEmpty()) {
            able_typeLbl.text = _row.able_type_show
        }
        dateLbl.text = _row.created_at.noSec()


//        if (list1CellDelegate != null) {
//            refreshIcon?.setOnClickListener {
//                list1CellDelegate.cellRefresh()
//            }
//
//            viewHolder.editIcon.setOnClickListener {
//                list1CellDelegate.cellEdit(row)
//            }
//
//            viewHolder.deleteIcon.setOnClickListener {
//                list1CellDelegate.cellDelete(row)
//            }
//        } else {
//            viewHolder.iconView.visibility = View.GONE
//        }
    }
}