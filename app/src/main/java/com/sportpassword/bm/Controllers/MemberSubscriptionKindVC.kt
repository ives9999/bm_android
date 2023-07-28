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
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Interface.MyTable2IF
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityMemberSubscriptionKindVcBinding
import com.sportpassword.bm.databinding.MytablevcBinding
import com.sportpassword.bm.member
import java.lang.reflect.Type

class MemberSubscriptionKindVC : BaseActivity(), MyTable2IF, List1CellDelegate {

    private lateinit var binding: ActivityMemberSubscriptionKindVcBinding
    private lateinit var view: ViewGroup

    private val tableType: Type = object : TypeToken<Tables2<MemberSubscriptionKindTable>>() {}.type
    lateinit var tableView: MyTable2VC<MemberSubscriptionKindViewHolder, MemberSubscriptionKindTable, MemberSubscriptionKindVC>
    //lateinit var tableAdapter: MyAdapter2<MemberLevelUpViewHolder<MemberLevelKindTable>, MemberLevelKindTable>
    //var rows: ArrayList<MemberLevelKindTable> = arrayListOf()

    var bottom_button_count: Int = 3
    val button_width: Int = 300

    //var mysTable: Tables2<MemberLevelKindTable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = MemberService
        super.onCreate(savedInstanceState)

        binding = ActivityMemberSubscriptionKindVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        setMyTitle("訂閱會員")
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
        tableView = MyTable2VC(recyclerView, refreshLayout, R.layout.subscriptionkind_cell, ::MemberSubscriptionKindViewHolder, tableType, this::tableViewSetSelected, this::getDataFromServer, this)

        setBottomThreeView()
    }

    override fun refresh() {

        tableView.page = 1
        tableView.getDataFromServer(page)
    }

    private fun getDataFromServer(page: Int) {
        loadingAnimation.start()
        loading = true

        MemberService.subscriptionKind(this, member.token!!, page, tableView.perPage) { success ->
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
        }
    }

    //1.如果按下原本訂閱的選項，不動作
    //2.如果已經有訂閱需要先退訂
    //3.如果按下「基本」表示要退訂
    override fun cellClick(row: Table) {

        val _row: MemberSubscriptionKindTable? = row as? MemberSubscriptionKindTable?
        if (_row != null) {
            val kind: MEMBER_SUBSCRIPTION_KIND = MEMBER_SUBSCRIPTION_KIND.stringToEnum(_row.eng_name)

            //如果點選原來訂閱的選項，不做任何動作
            if (kind == MEMBER_SUBSCRIPTION_KIND.stringToEnum(member.subscription!!)) {
                return
            }

            //如果點選基本選項，執行退訂
            if (kind == MEMBER_SUBSCRIPTION_KIND.basic) {
                unScbscription()
                return
            }

            //如果點選其他選項，警告要先退訂
            if (MEMBER_SUBSCRIPTION_KIND.stringToEnum(member.subscription!!) != MEMBER_SUBSCRIPTION_KIND.basic) {
                warning("您已經有訂閱，如果要更改，請先執行「退訂」，再重新訂閱，謝謝")
                return
            }

            warning("確定要訂閱${kind.chineseName}嗎？", "取消", "確定") {
                subscription(kind)
                //toMemberSubscriptionPay(_row.name, _row.price, _row.eng_name)
            }
        }
    }

    private fun tableViewSetSelected(row: MemberSubscriptionKindTable): Boolean {
        return row.eng_name == member.subscription
    }

    private fun setBottomThreeView() {
        findViewById<Button>(R.id.submitBtn) ?. let {
            it.text = "查詢"
            it.setOnClickListener {
                toMemberSubscriptionLog()
            }
        }

        findViewById<Button>(R.id.threeBtn) ?. let {
            it.text = "退訂"
            it.setOnClickListener {
                unScbscription()
            }
        }

        findViewById<Button>(R.id.cancelBtn) ?. let {
            it.text = "回上一頁"
            it.setOnClickListener {
                prev()
            }
        }

        setBottomButtonPadding()
    }

    private fun subscription(kind: MEMBER_SUBSCRIPTION_KIND) {
        loadingAnimation.start()
        MemberService.subscription(this, kind.englishName) { success ->
            runOnUiThread {
                loadingAnimation.stop()
            }

            if (success) {
                if (MemberService.jsonString.isNotEmpty()) {
                    //println(MemberService.jsonString)
                    try {
                        val table: OrderUpdateResTable = Gson().fromJson(
                            MemberService.jsonString,
                            OrderUpdateResTable::class.java
                        )

                        if (!table.success) {
                            runOnUiThread {
                                warning(table.msg)
                            }
                        } else {
                            val orderTable: OrderTable? = table.model
                            if (orderTable != null) {
                                orderTable.filterRow()
                                val ecpay_token: String = orderTable.ecpay_token
                                val ecpay_token_ExpireDate: String =
                                    orderTable.ecpay_token_ExpireDate

                                runOnUiThread {
                                    info("訂閱已經成立，是否前往付款？", "關閉", "付款") {
                                        toPayment(
                                            orderTable.token,
                                            ecpay_token,
                                            ecpay_token_ExpireDate
                                        )
                                    }
                                }

                            } else {
                                runOnUiThread {
                                    warning("無法拿到伺服器傳回值")
                                }
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        runOnUiThread {
                            warning(e.localizedMessage!!)
                        }
                    }
                }
            } else {
                runOnUiThread {
                    warning(MemberService.msg)
                }
            }
        }
    }

    private fun unScbscription() {
        if (MEMBER_SUBSCRIPTION_KIND.stringToEnum(member.subscription!!) == MEMBER_SUBSCRIPTION_KIND.basic) {
            warning("基本會員無法退訂")
        } else {
            warning("是否真的要退訂？", "取消", "確定") {
                runOnUiThread {
                    loadingAnimation.start()
                }
                MemberService.unSubscription(this) { success ->
                    runOnUiThread {
                        loadingAnimation.stop()
                        val table: OrderUpdateResTable = Gson().fromJson(
                            MemberService.jsonString,
                            OrderUpdateResTable::class.java
                        )
                        if (!table.success) {
                            warning(table.msg)
                        } else {
                            info("已經完成退訂")
                        }
                    }
                }
            }
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
}

class MemberSubscriptionKindViewHolder(
    context: Context,
    view: View,
    delegate: MemberSubscriptionKindVC
): MyViewHolder2<MemberSubscriptionKindTable, MemberSubscriptionKindVC>(context, view, delegate) {

    val titleLbl: TextView = view.findViewById(R.id.titleLbl)
    val lotteryTV: TextView = view.findViewById(R.id.lotteryTV)
    val priceLbl: TextView = view.findViewById(R.id.priceLbl)

    override fun bind(row: MemberSubscriptionKindTable, idx: Int) {
        super.bind(row, idx)

        titleLbl.text = row.name
        lotteryTV.text = "每次開箱球拍券：${row.lottery}張"
        priceLbl.text = "NT$: " + row.price.toString() + " 元/月"

        view.setOnClickListener {
            delegate.cellClick(row)
        }
    }
}