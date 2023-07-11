package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Adapters.OneItemAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Interface.MyTable2IF
import com.sportpassword.bm.Models.MemberCoinTable
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityMemberCoinListVcBinding
import com.sportpassword.bm.extensions.formattedWithSeparator
import com.sportpassword.bm.member
import java.lang.reflect.Type

class MemberCoinListVC: BaseActivity(), MyTable2IF {

    private lateinit var binding: ActivityMemberCoinListVcBinding
    private lateinit var view: ViewGroup

    //lateinit var tableAdapter: MemberCoinAdapter
    var coinResultTable: CoinResultTable? = null
    var coinReturnResultTable: CoinReturnResultTable? = null

    private val tableType: Type = object : TypeToken<Tables2<MemberCoinTable>>() {}.type
    lateinit var tableView: MyTable2VC<MemberCoinViewHolder, MemberCoinTable, MemberCoinListVC>

    var bottom_button_count: Int = 3
    val button_width: Int = 300

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = MemberService

        super.onCreate(savedInstanceState)

        binding = ActivityMemberCoinListVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        setMyTitle("解碼點數")

        init()
        refresh()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()

        findViewById<SwipeRefreshLayout>(R.id.refreshSR) ?. let {
            refreshLayout = it
        }
        val recyclerView: RecyclerView = findViewById(R.id.list)
        tableView = MyTable2VC(recyclerView, refreshLayout, R.layout.coin_list_cell, ::MemberCoinViewHolder, tableType, this::tableViewSetSelected, this::getDataFromServer, this)

        setBottomThreeView()

        setBottomButtonPadding()
    }

    fun didSelect(row: MemberCoinTable, idx: Int) {}

    fun tableViewSetSelected(row: MemberCoinTable): Boolean { return false }

    override fun refresh() {
        page = 1
        tableView.getDataFromServer(page)
    }

    private fun getDataFromServer(page: Int) {
        loadingAnimation.start()
        loading = true

        MemberService.coinlist(this, member.token!!, page, tableView.perPage) { success ->
            runOnUiThread {
                loadingAnimation.stop()

                //MyTable2IF
                val b: Boolean = showTableView(tableView, MemberService.jsonString)
                if (b) {
                    this.totalPage = tableView.totalPage
                    refreshLayout?.isRefreshing = false
                } else {
                    val rootView: ViewGroup = getRootView()
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
            //showTableView(myTable, MemberService.jsonString)
        }
    }

    fun getDataFromServer() {
        loadingAnimation.start()
        loading = true

        MemberService.coinlist(this, member.token!!, tableView.page, tableView.perPage) { success ->
            runOnUiThread {
                loadingAnimation.stop()

                //MyTable2IF
                val b: Boolean = showTableView(tableView, MemberService.jsonString)
                if (b) {
                    tableView.notifyDataSetChanged()
                } else {
                    val rootView: ViewGroup = getRootView()
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
            //showTableView(myTable, MemberService.jsonString)
        }
    }

//    override fun cellClick(row: MemberCoinTable) {
//        //購買點數，前往查看訂單
//
//        if (row.in_type != null && MEMBER_COIN_IN_TYPE.enumFromString(row.in_type) == MEMBER_COIN_IN_TYPE.buy && row.order_token.length > 0) {
//            toPayment(row.order_token, null, null, "member")
//        } else if (row.out_type != null && !row.in_out && MEMBER_COIN_OUT_TYPE.enumFromString(row.out_type) == MEMBER_COIN_OUT_TYPE.product && row.able_type == "order") {
//            //使用點數購買商品，前往查看訂單
//            toPayment(row.able_token, null, null, "member")
//        }
//    }

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

//    override fun cellClick(row: Table) {
//
//    }

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
            loadingAnimation.start()
            loading = true

            MemberService.coinReturn(this, member.token!!) { success ->
                loadingAnimation.stop()
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

class MemberCoinViewHolder(
    context: Context,
    view: View,
    delegate: MemberCoinListVC
): MyViewHolder2<MemberCoinTable, MemberCoinListVC>(context, view, delegate) {

    val noLbl: TextView = view.findViewById(R.id.noTV)
    val able_typeLbl: TextView = view.findViewById(R.id.able_typeLbl)
    val dateLbl: TextView = view.findViewById(R.id.dateLbl)
    val priceSignLbl: TextView = view.findViewById(R.id.priceSignLbl)
    val priceLbl: TextView = view.findViewById(R.id.priceLbl)
    val balanceSignLbl: TextView = view.findViewById(R.id.balanceSignLbl)
    val balanceLbl: TextView = view.findViewById(R.id.balanceLbl)
    val typeButton: Button = view.findViewById(R.id.typeButton)

    //_row is cartTable
    override fun bind(row: MemberCoinTable, idx: Int) {

        super.bind(row, idx)

        row.filterRow()
        val no: String = (idx + 1).toString() + "."

        noLbl.text = no
        balanceLbl.text = row.balance.formattedWithSeparator()

        if (row.able_type_show.isNotEmpty()) {
            able_typeLbl.text = row.able_type_show
            able_typeLbl.setTextLook(16F, R.color.TEXT_WHITE)
        }
        dateLbl.text = row.created_at.noSec()
        dateLbl.setTextLook(10F, R.color.MY_LIGHT_WHITE)

        priceSignLbl.setTextLook(10F, R.color.MY_WHITE)
        priceLbl.setTextLook(16F, R.color.MY_WHITE)

        balanceSignLbl.setTextLook(10F, R.color.MY_WHITE)
        balanceLbl.setTextLook(16F, R.color.MY_WHITE)

        typeButton.text =
            ((row.in_out) then { row.type_in_enum.chineseName }) ?: row.type_out_enum.chineseName

        if (row.in_out) {
            priceLbl.text = "+" + row.coin.formattedWithSeparator()
            if (row.type_in_enum == MEMBER_COIN_IN_TYPE.buy) {
                typeButton.setLook(R.color.MEMBER_COIN_BUY, R.color.MY_WHITE)
            } else if (row.type_in_enum == MEMBER_COIN_IN_TYPE.gift) {
                typeButton.setLook(R.color.MEMBER_COIN_GIFT, R.color.MY_WHITE)
            } else {
                typeButton.visibility = View.INVISIBLE
            }
        } else {
            priceLbl.text = "-" + row.coin.formattedWithSeparator()
            priceLbl.setTextLook(16F, R.color.MY_RED)
            if (row.type_out_enum == MEMBER_COIN_OUT_TYPE.product) {
                typeButton.setLook(R.color.MEMBER_COIN_PAY, R.color.MY_WHITE)
            } else if (row.type_out_enum == MEMBER_COIN_OUT_TYPE.course) {
                typeButton.setLook(R.color.MEMBER_COIN_PAY, R.color.MY_WHITE)
            } else {
                typeButton.visibility = View.INVISIBLE
            }
        }

        if (row.name.isNotEmpty()) {
            able_typeLbl.text = row.name
        }

        //viewHolder.setOnClickListener {
        //list2CellDelegate?.cellClick(row)
        //購買點數，前往查看訂單
//            if (MEMBER_COIN_IN_TYPE.enumFromString(_row.in_type) == MEMBER_COIN_IN_TYPE.buy && _row.order_token.length > 0 && list1CellDelegate != null) {
//                list1CellDelegate.cellClick(row)
//            }
//
//            //使用點數購買商品，前往查看訂單
//            if (!row.in_out && MEMBER_COIN_OUT_TYPE.enumFromString(row.out_type) == MEMBER_COIN_OUT_TYPE.product && row.able_type == "order" && list1CellDelegate != null) {
//                list1CellDelegate.cellClick(row)
//            }
        //}

//        if (list1CellDelegate != null) {
//            refreshIcon?.setOnClickListener {
//                list1CellDelegate.cellRefresh()
//            }
//
//            viewHolder.editIcon.setOnClickListener {
//                list1CellDelegate.cellEdit(row)
//            }
//
//            viewHolder.deleteIcon.setOnClickListener {
//                list1CellDelegate.cellDelete(row)
//            }
//        } else {
//            viewHolder.iconView.visibility = View.GONE
//        }
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