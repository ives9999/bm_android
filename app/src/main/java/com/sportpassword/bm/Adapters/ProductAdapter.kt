package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Controllers.ProductVC
import com.sportpassword.bm.Models.ProductTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Utilities.formattedWithSeparator
import kotlinx.android.synthetic.main.product_list_cell.view.*

class ProductAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<ProductViewHolder>(resource, ::ProductViewHolder, list1CellDelegate) {}

class ProductViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: ProductTable = _row as ProductTable

        viewHolder.buyBtn.setOnClickListener {
            val vc: ProductVC = context as ProductVC

            vc.buyButtonPressed(row)

//            val type: String = row.type
//            if (type == "coin") {
//                vc.toOrder(row.token)
//            } else {
//                vc.toAddCart(row.token)
//            }
        }

        if (row.prices.size > 0) {
            val tmp: String = (row.prices[0].price_member).formattedWithSeparator()
            val price: String = "NT$ ${tmp}"
            viewHolder.priceLbl.text = price
        } else {
            viewHolder.priceLbl.text = "未提供"
        }
    }
}
