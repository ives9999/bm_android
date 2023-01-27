package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.CartItemTable
import com.sportpassword.bm.Models.ProductTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.squareup.picasso.Picasso

class MemberCartAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<MemberCartViewHolder>(resource, ::MemberCartViewHolder, list1CellDelegate) {

    override fun onBindViewHolder(holder: MemberCartViewHolder, position: Int) {

        val row: CartItemTable = tableList[position] as CartItemTable

        holder.bind(row, position)
    }
}

class MemberCartViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

//    val title: TextView = viewHolder.titleLbl
//    val featured_path: ImageView = viewHolder.listFeatured
//    val attribute: TextView = viewHolder.attributeLbl
//    val amount: TextView = viewHolder.amountLbl
//    val quantity: TextView = viewHolder.quantityLbl

    //_row is cartTable
    override fun bind(row: Table, idx: Int) {

        val _row: CartItemTable = row as CartItemTable
        _row.filterRow()
        //super.bind(_row, idx)
        val productTable: ProductTable = _row.product!!
        titleLbl?.text = productTable.name
        if (productTable.featured_path.isNotEmpty()) {
            Picasso.with(context)
                .load(productTable.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(listFeatured)
        }
        var attribute_text: String = ""
        if (_row.attributes.size > 0) {

            for ((idx, attribute) in _row.attributes.withIndex()) {
                attribute_text += attribute["name"]!! + ":" + attribute["value"]!!
                if (idx < _row.attributes.size - 1) {
                    attribute_text += " | "
                }
            }
        }

        viewHolder.findViewById<TextView>(R.id.attributeLbl) ?. let {
            it.text = attribute_text
        }

        viewHolder.findViewById<TextView>(R.id.amountLbl) ?. let {
            it.text = row.amount_show
        }

        viewHolder.findViewById<TextView>(R.id.quantityLbl) ?. let {
            it.text = "數量：${row.quantity}"
        }

        if (list1CellDelegate != null) {

            viewHolder.findViewById<ImageView>(R.id.refreshIcon) ?. let {
                it.setOnClickListener {
                    list1CellDelegate.cellRefresh()
                }
            }

            viewHolder.findViewById<ImageView>(R.id.editIcon) ?. let {
                it.setOnClickListener {
                    list1CellDelegate.cellEdit(row)
                }
            }

            viewHolder.findViewById<ImageView>(R.id.deleteIcon) ?. let {
                it.setOnClickListener {
                    list1CellDelegate.cellDelete(row)
                }
            }
        } else {
            viewHolder.findViewById<RelativeLayout>(R.id.iconView) ?. let {
                it.visibility = View.GONE
            }
        }
    }
}