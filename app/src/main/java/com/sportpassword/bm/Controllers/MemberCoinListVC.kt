package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Adapters.MemberCoinViewHolder
import com.sportpassword.bm.Adapters.OneItemAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Models.MemberCoinTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_member_coin_list_vc.*
import kotlinx.android.synthetic.main.mask.*
import java.lang.reflect.Type

class MemberCoinListVC: MyTableVC() {

    //lateinit var tableAdapter: MemberCoinAdapter
    var coinResultTable: CoinResultTable? = null
    var coinReturnResultTable: CoinReturnResultTable? = null

    private val tableType: Type = object : TypeToken<Tables2<MemberCoinTable>>() {}.type
    lateinit var myTable: MyTable2VC<MemberCoinViewHolder<MemberCoinTable>, MemberCoinTable>

    var bottom_button_count: Int = 3
    val button_width: Int = 400

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = MemberService

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_coin_list_vc)

        setMyTitle("解碼點數")

        val recyclerView: RecyclerView = findViewById(R.id.list)
        myTable = MyTable2VC(recyclerView, R.layout.coin_list_cell, ::MemberCoinViewHolder, tableType, this)

        //recyclerView = list
        refreshLayout = list_refresh

        //tableAdapter = MemberCoinAdapter(this)
        //recyclerView.adapter = tableAdapter

        setBottomThreeView()

        setBottomButtonPadding()

        init()
        refresh()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    override fun refresh() {

        page = 1
        getDataFromServer()
//        theFirstTime = true
//        tableLists.clear()
//        getDataStart(page, perPage, member.token)
    }

    fun getDataFromServer() {
        Loading.show(mask)
        loading = true

        MemberService.coinlist(this, member.token!!, myTable.page, myTable.perPage) { success ->
            runOnUiThread {
                Loading.hide(mask)
            }
            showTableView(myTable, MemberService.jsonString)
        }
    }

//    override fun getDataStart(_page: Int, _perPage: Int, token: String?) {
//        Loading.show(mask)
//        loading = true
//
//        MemberService.coinlist(this, member.token!!, _page, _perPage) { success ->
//            jsonString = MemberService.jsonString
//            getDataEnd(success)
//        }
//    }
//
//    override fun genericTable() {
//        //println(MemberService.jsonString)
//        try {
//            coinResultTable = Gson().fromJson<CoinResultTable>(MemberService.jsonString, CoinResultTable::class.java)
//        } catch (e: JsonParseException) {
//            runOnUiThread {
//                warning(e.localizedMessage!!)
//            }
//        }
//        if (coinResultTable != null) {
//            if (coinResultTable!!.rows.size > 0) {
//                getPage()
//                tableLists += generateItems1(MemberCoinTable::class, coinResultTable!!.rows)
//                tableAdapter.items = tableLists
//                runOnUiThread {
//                    tableAdapter.notifyDataSetChanged()
//                }
//            } else {
//                val rootView: ViewGroup = getRootView()
//                runOnUiThread {
//                    rootView.setInfo(this, "目前暫無資料")
//                }
//            }
//        }
//    }
//
//    override fun getPage() {
//        if (coinResultTable != null) {
//            page = coinResultTable!!.page
//            perPage = coinResultTable!!.perPage
//            totalCount = coinResultTable!!.totalCount
//            val _totalPage: Int = totalCount / perPage
//            totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
//        }
//    }

    override fun cellClick(row: Table) {
        //購買點數，前往查看訂單
        val _row: MemberCoinTable = row as MemberCoinTable

        if (row.in_type != null && MEMBER_COIN_IN_TYPE.enumFromString(row.in_type) == MEMBER_COIN_IN_TYPE.buy && _row.order_token.length > 0) {
            toPayment(row.order_token, null, null, "member")
        } else if (row.out_type != null && !row.in_out && MEMBER_COIN_OUT_TYPE.enumFromString(row.out_type) == MEMBER_COIN_OUT_TYPE.product && _row.able_type == "order") {
            //使用點數購買商品，前往查看訂單
            toPayment(row.able_token, null, null, "member")
        }
    }

    fun setBottomButtonPadding() {

        val padding: Int = (screenWidth - bottom_button_count * button_width) / (bottom_button_count + 1)
        //val leading: Int = bottom_button_count * padding + (bottom_button_count - 1) * button_width

        findViewById<Button>(R.id.submitBtn) ?. let {
            if (it.visibility == View.VISIBLE) {
                val params: ViewGroup.MarginLayoutParams =
                    it.layoutParams as ViewGroup.MarginLayoutParams
                params.width = button_width
                params.marginStart = padding
                it.layoutParams = params
            }
        }

        findViewById<Button>(R.id.threeBtn) ?. let {
            if (it.visibility == View.VISIBLE) {
                val params: ViewGroup.MarginLayoutParams =
                    it.layoutParams as ViewGroup.MarginLayoutParams
                params.width = button_width
                params.marginStart = padding
                it.layoutParams = params
            }
        }

        findViewById<Button>(R.id.cancelBtn) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }
    }

    fun setBottomThreeView() {
        findViewById<Button>(R.id.submitBtn) ?. let {
            it.text = "購買點數"
            it.setOnClickListener {
                toProduct()
            }
        }

        findViewById<Button>(R.id.threeBtn) ?. let {
            it.text = "退款"
            it.setOnClickListener {
                coinReturn()
            }
        }

        findViewById<Button>(R.id.cancelBtn) ?. let {
            it.text = "回上一頁"
            it.setOnClickListener {
                prev()
            }
        }
    }

    fun coinReturn() {
        msg = ""
        if (member.bank!!.length == 0 || member.branch!!.length == 0 || member.bank_code!! == 0 || member.account!!.length == 0) {
            msg += "請先填寫完整的銀行匯款資料才能進行退款，是否前往填寫"
            warning(msg, true, "是") {
                toMemberBank()
            }
        }

        if (msg.isEmpty()) {
            Loading.show(mask)
            loading = true

            MemberService.coinReturn(this, member.token!!) { success ->
                Loading.hide(mask)
                jsonString = MemberService.jsonString
                if (success) {
                    if (jsonString != null && jsonString!!.isNotEmpty()) {

                        try {
                            coinReturnResultTable = Gson().fromJson<CoinReturnResultTable>(MemberService.jsonString, CoinReturnResultTable::class.java)
                            val i = 6
                        } catch (e: JsonParseException) {
                            runOnUiThread {
                                warning(e.localizedMessage!!)
                            }
                        }

                        if (coinReturnResultTable != null) {
                            val rows: ArrayList<OneRow> = arrayListOf()
                            var row: OneRow = OneRow()
                            row = OneRow("購買：", coinReturnResultTable!!.grand_price.toString(), coinReturnResultTable!!.grand_price.formattedWithSeparator() + " 點", "grand_price", "text")
                            rows.add(row)
                            row = OneRow("贈送：", coinReturnResultTable!!.grand_give.toString(), coinReturnResultTable!!.grand_give.formattedWithSeparator() + " 點", "grand_give", "text")
                            rows.add(row)
                            row = OneRow("支出：", coinReturnResultTable!!.grand_spend.toString(), coinReturnResultTable!!.grand_spend.formattedWithSeparator() + " 點", "grand_spend", "text")
                            rows.add(row)
                            row = OneRow("手續費：", coinReturnResultTable!!.handle_fee.toString(), coinReturnResultTable!!.handle_fee.formattedWithSeparator() + " 點", "handle_fee", "text")
                            rows.add(row)
                            row = OneRow("轉帳費：", coinReturnResultTable!!.transfer_fee.toString(), coinReturnResultTable!!.transfer_fee.formattedWithSeparator() + " 點", "transfer_fee", "text")
                            rows.add(row)
                            row = OneRow("退款金額：", coinReturnResultTable!!.return_coin.toString(), coinReturnResultTable!!.return_coin.formattedWithSeparator() + " 點", "return_coin", "text")
                            row.titleColor = R.color.MY_RED
                            row.showColor = R.color.MY_RED
                            rows.add(row)

                            val section: OneSection = OneSection("退款", "coin", true, rows)
                            val tableViewHeight: Int = rowHeight * rows.size + 200

                            runOnUiThread {
                                showTableLayer(tableViewHeight)
                                val panelAdapter = OneItemAdapter(this, 0, section, this, hashMapOf())

                                layerTableView!!.adapter = panelAdapter
                            }
                        }
                    }
                }
            }
        }
    }

    override fun setButtonLayoutHeight(): Int {
        val buttonViewHeight: Int = 180

        return buttonViewHeight * 2
    }

    override fun addPanelBtn() {

        layerSubmitBtn = layerButtonLayout.submitButton(this, 120) {
            info("即將推出")
        }

        super.addPanelBtn()
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

class CoinReturnResultTable {

    var success: Boolean = false
    var grand_price: Int = 0
    var grand_give: Int = 0
    var grand_spend: Int = 0
    var handle_fee: Int = 0
    var transfer_fee: Int = 0
    var return_coin: Int = 0
}