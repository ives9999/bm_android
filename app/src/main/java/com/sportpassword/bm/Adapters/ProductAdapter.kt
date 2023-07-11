package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Controllers.ProductVC
import com.sportpassword.bm.Models.ProductTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.formattedWithSeparator

class ProductAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<ProductViewHolder>(resource, ::ProductViewHolder, list1CellDelegate) {}

class ProductViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: ProductTable = _row as ProductTable

        viewHolder.findViewById<Button>(R.id.buyBtn) ?. let {
            it.setOnClickListener {
                val vc: ProductVC = context as ProductVC

                vc.buyButtonPressed(row)
            }
        }

        viewHolder.findViewById<TextView>(R.id.priceLbl) ?. let {
            if (row.prices.size > 0) {
                val tmp: String = (row.prices[0].price_member).formattedWithSeparator()
                val price: String = "NT$ ${tmp}"
                it.text = price
            } else {
                it.text = "未提供"
            }
        }
    }
}
