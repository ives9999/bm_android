package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.sportpassword.bm.Adapters.MemberOrderAdapter
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_member_order_list_vc.*
import kotlinx.android.synthetic.main.mask.*
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

        init()
        refresh()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    override fun refresh() {

        page = 1
        theFirstTime = true
        //adapter.clear()
        //items.clear()
        tableLists.clear()
        params.clear()
        getDataStart(page, perPage, member.token)
    }

    override fun genericTable() {
        //println(dataService.jsonString)
        mysTable = jsonToModels<OrdersTable>(dataService.jsonString)
        if (mysTable != null) {
            tables = mysTable
            if (mysTable!!.rows.count() > 0) {
                getPage()
                tableLists += generateItems1(OrderTable::class, mysTable!!.rows)
                tableAdapter.setMyTableList(tableLists)
                runOnUiThread {
                    tableAdapter.notifyDataSetChanged()
                }
            } else {
                val rootView: ViewGroup = getRootView()
                runOnUiThread {
                    rootView.setInfo(this, "目前無訂單")
                }
            }
        }
    }

    override fun cellClick(row: Table) {

//        val orderTable: OrderTable = row as OrderTable
//
//        val gateway_method: String = orderTable.gateway!!.method
//        if (gateway_method == GATEWAY.credit_card.englishName || gateway_method == GATEWAY.store_cvs.englishName) {
//            toPayment(row.token, null, null, "member")
//        } else if (gateway_method == GATEWAY.coin.englishName) {
//
//        } else if (gateway_method == GATEWAY.store_pay_711.englishName || gateway_method == GATEWAY.store_pay_family.englishName) {
//            toWebView(orderTable.token)
//        }

        toPayment(row.token, null, null, "member")
    }
}



















