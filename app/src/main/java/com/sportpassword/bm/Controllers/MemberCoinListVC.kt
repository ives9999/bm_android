package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.MemberCoinAdapter
import com.sportpassword.bm.Models.MemberCoinTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.setInfo
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_member_coin_list_vc.*
import kotlinx.android.synthetic.main.mask.*

class MemberCoinListVC: MyTableVC() {

    lateinit var tableAdapter: MemberCoinAdapter
    var coinResultTable: CoinResultTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = MemberService

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_coin_list_vc)

        setMyTitle("解碼點數")

        recyclerView = list
        refreshLayout = list_refresh

        tableAdapter = MemberCoinAdapter(this)
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
        getDataStart(page, perPage, member.token)
    }

    override fun getDataStart(_page: Int, _perPage: Int, token: String?) {
        Loading.show(mask)
        loading = true

        MemberService.coinlist(this, member.token!!, _page, _perPage) { success ->
            jsonString = MemberService.jsonString
            getDataEnd(success)
        }
    }

    override fun genericTable() {
        println(MemberService.jsonString)
        try {
            coinResultTable = Gson().fromJson<CoinResultTable>(MemberService.jsonString, CoinResultTable::class.java)
        } catch (e: JsonParseException) {
            runOnUiThread {
                warning(e.localizedMessage!!)
            }
        }
        if (coinResultTable != null) {
            if (coinResultTable!!.rows.size > 0) {
                getPage()
                tableLists += generateItems1(MemberCoinTable::class, coinResultTable!!.rows)
                tableAdapter.setMyTableList(tableLists)
                runOnUiThread {
                    tableAdapter.notifyDataSetChanged()
                }
            } else {
                val rootView: ViewGroup = getRootView()
                runOnUiThread {
                    rootView.setInfo(this, "無法解析伺服器傳回的參數")
                }
            }
        }
    }

    override fun getPage() {
        if (coinResultTable != null) {
            page = coinResultTable!!.page
            perPage = coinResultTable!!.perPage
            totalCount = coinResultTable!!.totalCount
            val _totalPage: Int = totalCount / perPage
            totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
        }
    }
}

class CoinResultTable {

    var success: Boolean = false
    var page: Int = -1
    var totalCount: Int = -1
    var perPage: Int = -1
    var msg: String = ""
    var rows: ArrayList<MemberCoinTable> = arrayListOf()
}