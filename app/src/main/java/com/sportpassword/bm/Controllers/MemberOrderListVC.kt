package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sportpassword.bm.Adapters.MemberOrderAdapter
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityMemberOrderListVcBinding
import com.sportpassword.bm.databinding.MytablevcBinding
import com.sportpassword.bm.member
import tw.com.bluemobile.hbc.extensions.setInfo
import com.sportpassword.bm.functions.jsonToModels

class MemberOrderListVC : MyTableVC() {

    private lateinit var binding: ActivityMemberOrderListVcBinding
    //private lateinit var view: ViewGroup

    var mysTable: OrdersTable? = null
    lateinit var tableAdapter: MemberOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemberOrderListVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        var source = ""
        if (intent.hasExtra("source")) {
            source = intent.getStringExtra("source")!!
        }

        setMyTitle("會員訂單")

        dataService = OrderService
        recyclerView = binding.orderList
        refreshLayout = binding.orderListRefresh

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



















