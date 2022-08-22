package com.sportpassword.bm.Adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sportpassword.bm.Controllers.didSelectClosure
import com.sportpassword.bm.Controllers.selectedClosure
import com.sportpassword.bm.Models.MemberLevelKindTable
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.levelup_cell.view.*
import org.jetbrains.anko.backgroundColor

class MemberLevelUpViewHolder(
    context: Context,
    viewHolder: View,
    didSelect: didSelectClosure<MemberLevelKindTable>,
    selected: selectedClosure<MemberLevelKindTable>
    ): MyViewHolder2<MemberLevelKindTable>(context, viewHolder, didSelect, selected) {

    val titleLbl: TextView = viewHolder.titleLbl
    val priceLbl: TextView = viewHolder.priceLbl

    override fun bind(row: MemberLevelKindTable, idx: Int) {
        super.bind(row, idx)

        titleLbl.text = row.name
        priceLbl.text = "NT$: " + row.price.toString() + " 元/月"
    }
}
