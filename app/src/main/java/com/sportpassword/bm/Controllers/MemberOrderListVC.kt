package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.sportpassword.bm.Fragments.MyAdapter
import com.sportpassword.bm.Fragments.MyViewHolder
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.jsonToModels
import com.sportpassword.bm.member
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_member_order_list_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.order_list_cell.*
import kotlinx.android.synthetic.main.order_list_cell.view.*
import kotlin.collections.ArrayList

class MemberOrderListVC : MyTableVC() {

    var mysTable: OrdersTable? = null
    lateinit var tableAdapter: MemberOrderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_order_list_vc)

        setMyTitle("會員訂單")

        dataService = OrderService
        recyclerView = order_list
        refreshLayout = order_list_refresh
        maskView = mask
        setRefreshListener()
        setRecyclerViewScrollListener()

//        initAdapter()
        tableAdapter = MemberOrderAdapter(R.layout.order_list_cell, this)
        recyclerView.adapter = tableAdapter

        perPage = 10
        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        isSearchIconShow = false
        super.onCreateOptionsMenu(menu)

        return true
    }

    override fun refresh() {
//        page = 1
//        theFirstTime = true
//        getDataStart(page, perPage)

        page = 1
        theFirstTime = true
        //adapter.clear()
        items.clear()
        tableLists.clear()
        params.clear()
        getDataStart(page, perPage, member.token)
    }

    override fun genericTable() {
        mysTable = jsonToModels<OrdersTable>(dataService.jsonString)
        if (mysTable != null) {
            tables = mysTable
            getPage()
            tableLists += generateItems1(OrderTable::class, mysTable!!.rows)
            tableAdapter.setMyTableList(tableLists)
            runOnUiThread {
                tableAdapter.notifyDataSetChanged()
            }

        }
    }

    override fun generateItems(): ArrayList<Item> {
        if (mysTable != null) {
            for (row in mysTable!!.rows) {
                row.filterRow()
                val orderItem = OrderItem(this, row)
                //productItem.list1CellDelegate = this
                items.add(orderItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {

        val orderItem = item as OrderItem
        val table = orderItem.row
        toPayment(table.token)
    }
}

class MemberOrderAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<MemberOrderViewHolder>(resource, ::MemberOrderViewHolder, list1CellDelegate) {}

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
        viewHolder.gatewayProcessLbl.text = row.gateway!!.process_show
        viewHolder.shippingProcessLbl.text = row.shipping!!.process_show
        viewHolder.gatewayMethodLbl.text = row.gateway!!.method_show
        viewHolder.shippingMethodLbl.text = row.shipping!!.method_show
    }
}

class OrderItem(val context: Context, val row: OrderTable): Item() {

    //var list1CellDelegate: List1CellDelegate? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

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
        viewHolder.noLbl.text = (position+1).toString()

        viewHolder.orderAllProcessLbl.text = row.all_process_show
        viewHolder.gatewayProcessLbl.text = row.gateway!!.process_show
        viewHolder.shippingProcessLbl.text = row.shipping!!.process_show
        viewHolder.gatewayMethodLbl.text = row.gateway!!.method_show
        viewHolder.shippingMethodLbl.text = row.shipping!!.method_show
    }

    override fun getLayout() = R.layout.order_list_cell
}


















