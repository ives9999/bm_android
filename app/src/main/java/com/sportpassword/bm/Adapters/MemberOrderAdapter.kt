package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.OrderItemTable
import com.sportpassword.bm.Models.OrderTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R

class MemberOrderAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<MemberOrderViewHolder>(resource, ::MemberOrderViewHolder, list1CellDelegate) {

}

class MemberOrderViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: OrderTable = _row as OrderTable
        val items: ArrayList<OrderItemTable> = row.items
        var name: String = ""
        if (items.size > 0) {
            val productTable = items[0].product
            if (productTable != null) {
                name = productTable.name
            }
            if (items.size > 1) {
                name += "..."
            }
        } else {
            name = "無法取得商品名稱，請洽管理員"
        }

        viewHolder.findViewById<TextView>(R.id.nameLbl) ?. let {
            it.text = name
        }

        viewHolder.findViewById<TextView>(R.id.dateLbl) ?. let {
            it.text = row.created_at_show
        }

        viewHolder.findViewById<TextView>(R.id.priceLbl) ?. let {
            it.text = row.amount_show
            it.textSize = 20F
        }

        viewHolder.findViewById<TextView>(R.id.orderNoLbl) ?. let {
            it.text = row.order_no
        }

        viewHolder.findViewById<TextView>(R.id.noTV) ?. let {
            it.text = (idx+1).toString()
        }

        viewHolder.findViewById<TextView>(R.id.orderAllProcessLbl) ?. let {
            it.text = row.all_process_show
        }

        viewHolder.findViewById<TextView>(R.id.gatewayProcessLbl) ?. let {
            it.text = row.gateway?.process_show
        }

        viewHolder.findViewById<TextView>(R.id.shippingProcessLbl) ?. let {
            it.text = row.shipping?.process_show
        }

        viewHolder.findViewById<TextView>(R.id.gatewayMethodLbl) ?. let {
            it.text = row.gateway?.method_show
        }

        viewHolder.findViewById<TextView>(R.id.shippingMethodLbl) ?. let {
            it.text = row.shipping?.method_show
        }
    }
}