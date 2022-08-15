package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.MemberLevelKindTable
import kotlinx.android.synthetic.main.levelup_cell.view.*

class MemberLevelUpViewHolder<T>(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null):
    MyViewHolder2<MemberLevelKindTable>(context, viewHolder, list1CellDelegate) {

    val titleLbl: TextView = viewHolder.titleLbl
    val priceLbl: TextView = viewHolder.priceLbl

    override fun bind(item: MemberLevelKindTable, idx: Int) {
        titleLbl.text = item.name
        priceLbl.text = "NT$: " + item.price.toString() + " 元/月"
    }
}
