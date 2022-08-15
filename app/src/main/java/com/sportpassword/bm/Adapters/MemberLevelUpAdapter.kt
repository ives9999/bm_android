package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Controllers.List2CellDelegate
import com.sportpassword.bm.Models.MemberLevelKindTable
import kotlinx.android.synthetic.main.levelup_cell.view.*

class MemberLevelUpViewHolder(context: Context, viewHolder: View, list2CellDelegate: List2CellDelegate<MemberLevelKindTable>? = null):
    MyViewHolder2<MemberLevelKindTable>(context, viewHolder, list2CellDelegate) {

    val titleLbl: TextView = viewHolder.titleLbl
    val priceLbl: TextView = viewHolder.priceLbl

    override fun bind(item: MemberLevelKindTable, idx: Int) {
        super.bind(item, idx)
        titleLbl.text = item.name
        priceLbl.text = "NT$: " + item.price.toString() + " 元/月"
    }
}
