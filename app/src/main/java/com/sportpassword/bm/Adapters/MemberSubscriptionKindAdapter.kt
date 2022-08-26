package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.TextView
import com.sportpassword.bm.Controllers.didSelectClosure
import com.sportpassword.bm.Controllers.selectedClosure
import com.sportpassword.bm.Models.MemberSubscriptionKindTable
import kotlinx.android.synthetic.main.subscriptionkind_cell.view.*

class MemberSubscriptionKindViewHolder(
    context: Context,
    viewHolder: View,
    didSelect: didSelectClosure<MemberSubscriptionKindTable>,
    selected: selectedClosure<MemberSubscriptionKindTable>
    ): MyViewHolder2<MemberSubscriptionKindTable>(context, viewHolder, didSelect, selected) {

    val titleLbl: TextView = viewHolder.titleLbl
    val priceLbl: TextView = viewHolder.priceLbl

    override fun bind(row: MemberSubscriptionKindTable, idx: Int) {
        super.bind(row, idx)

        titleLbl.text = row.name
        priceLbl.text = "NT$: " + row.price.toString() + " 元/月"
    }
}
