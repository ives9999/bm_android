package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.OrderItemTable
import com.sportpassword.bm.Models.OrderTable
import com.sportpassword.bm.Models.Table
import kotlinx.android.synthetic.main.order_list_cell.view.*

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
        viewHolder.nameLbl.text = name
        viewHolder.dateLbl.text = row.created_at_show
        viewHolder.priceLbl.text = row.amount_show
        viewHolder.orderNoLbl.text = row.order_no
        viewHolder.noLbl.text = (idx+1).toString()

        viewHolder.orderAllProcessLbl.text = row.all_process_show
        viewHolder.gatewayProcessLbl.text = row.gateway?.process_show
        viewHolder.shippingProcessLbl.text = row.shipping?.process_show
        viewHolder.gatewayMethodLbl.text = row.gateway?.method_show
        viewHolder.shippingMethodLbl.text = row.shipping?.method_show
    }
}