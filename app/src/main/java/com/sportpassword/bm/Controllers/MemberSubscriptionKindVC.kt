package com.sportpassword.bm.Controllers

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Interface.MyTable2IF
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.*
import com.sportpassword.bm.databinding.ActivityMemberSubscriptionKindVcBinding
import com.sportpassword.bm.databinding.MytablevcBinding
import com.sportpassword.bm.member
import org.jetbrains.anko.backgroundColor
import java.lang.reflect.Type

class MemberSubscriptionKindVC : BaseActivity(), MyTable2IF, List1CellDelegate, ShowTop2Delegate {

    private lateinit var binding: ActivityMemberSubscriptionKindVcBinding
    private lateinit var view: ViewGroup

    private val tableType: Type = object : TypeToken<Tables2<MemberSubscriptionKindTable>>() {}.type
    //lateinit var tableView: MyTable2VC<MemberSubscriptionKindViewHolder, MemberSubscriptionKindTable, MemberSubscriptionKindVC>
    //lateinit var tableAdapter: MyAdapter2<MemberLevelUpViewHolder<MemberLevelKindTable>, MemberLevelKindTable>
    var items: ArrayList<MemberSubscriptionKindTable> = arrayListOf()

    var showTop2: ShowTop2? = null
    var showBottom2: Bottom2? = null
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MemberSubscriptionKindAdapter

    var bottom_button_count: Int = 3
    val button_width: Int = 300

    //var mysTable: Tables2<MemberLevelKindTable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = MemberService
        super.onCreate(savedInstanceState)

        binding = ActivityMemberSubscriptionKindVcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTop()
        initRecyclerView()
        initBottom()

        init()
    }

    fun initTop() {
        isPrevIconShow = true
        binding.top.delegate = this
        binding.top.apply {
            setTitle("${MEMBER_SUBSCRIPTION_KIND.stringToEnum(member.subscription!!).toChineseString()}會員")
            showPrev(true)
            showLog(20)
        }
    }

    fun initRecyclerView() {
        recyclerView = binding.list
        adapter = MemberSubscriptionKindAdapter(this, this)
        recyclerView.adapter = adapter

        findViewById<SwipeRefreshLayout>(R.id.refreshSR) ?. let {
            refreshLayout = it
        }
    }

    fun initBottom() {
        bottom_button_count = 2
        findViewById<Bottom2>(R.id.bottom2) ?. let {
            showBottom2 = it
            it.showButton(true, false, true)
            it.setSubmitBtnTitle("取消訂閱")
            it.setOnSubmitClickListener(submit)
            it.setOnCancelClickListener(cancel)
        }
        //setBottomThreeView()
    }

    override fun init() {
        super.init()
//        val recyclerView: RecyclerView = findViewById(R.id.list)
//        tableView = MyTable2VC(recyclerView, refreshLayout, R.layout.subscriptionkind_cell, ::MemberSubscriptionKindViewHolder, tableType, this::tableViewSetSelected, this::getDataFromServer, this)
        refresh()
    }

    override fun refresh() {

        //tableView.page = 1
        //tableView.getDataFromServer(page)
        page = 1
        items.clear()
        getDataFromServer(page)
    }

    private fun getDataFromServer(page: Int) {
        loading = true
        //loadingAnimation.start()

        MemberService.subscriptionKind(this, member.token!!, page, 20) { success ->
            runOnUiThread {
                //loadingAnimation.stop()

                //MyTable2IF
                this.items = parseJSON(MemberService.jsonString)
                //val b: Boolean = showTableView(tableView, MemberService.jsonString)
                if (this.items.size > 0) {
                    adapter.items = this.items
                    //refreshLayout?.isRefreshing = false
                    adapter.notifyDataSetChanged()

                    //this.totalPage = tableView.totalPage
                    refreshLayout?.isRefreshing = false
                } else {
                    val rootView: ViewGroup = getRootView()
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
        }
    }

    fun parseJSON(jsonString: String): ArrayList<MemberSubscriptionKindTable> {
        var res: Boolean = true
        val rows: ArrayList<MemberSubscriptionKindTable> = arrayListOf()
        val tables2: Tables2<MemberSubscriptionKindTable>? = jsonToModels2<Tables2<MemberSubscriptionKindTable>, MemberSubscriptionKindTable>(jsonString, tableType)

        if (tables2 == null) {
            msg = "無法從伺服器取得正確的json資料，請洽管理員"
        } else {
            if (tables2.success) {
                if (tables2.rows.size > 0) {

                    for ((idx, row) in tables2.rows.withIndex()) {
                        row.filterRow()
                        row.selected = tableViewSetSelected(row)
                    }

                    if (page == 1) {
                        page = tables2.page
                        perPage = tables2.perPage
                        totalCount = tables2.totalCount
                        val _totalPage: Int = totalCount / perPage
                        totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage

//                        if (totalPage > 1) {
//                            val layoutManager = LinearLayoutManager(delegate)
//                            recyclerView.layoutManager = layoutManager
//                            scrollListener = ScrollListener(layoutManager)
//                            recyclerView.addOnScrollListener(scrollListener!!)
//                        }
                    }

                    rows.addAll(tables2.rows)
                }
            } else {
                msg = "解析JSON字串時，沒有成功，系統傳回值錯誤，請洽管理員"
            }
        }

        return rows
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

    override fun showTop2Log() {
        toMemberSubscriptionLog()
    }

    val cancel: ()-> Unit = {
        prev()
    }

    val submit: () -> Unit = {
        unScbscription()
    }

//    private fun setBottomThreeView() {
//        findViewById<Button>(R.id.submitBtn) ?. let {
//            it.text = "查詢"
//            it.setOnClickListener {
//                toMemberSubscriptionLog()
//            }
//        }
//
//        findViewById<Button>(R.id.threeBtn) ?. let {
//            it.text = "退訂"
//            it.setOnClickListener {
//                unScbscription()
//            }
//        }
//
//        findViewById<Button>(R.id.cancelBtn) ?. let {
//            it.text = "回上一頁"
//            it.setOnClickListener {
//                prev()
//            }
//        }
//
//        setBottomButtonPadding()
//    }

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

class MemberSubscriptionKindAdapter(val context: Context, val delegate: MemberSubscriptionKindVC): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: ArrayList<MemberSubscriptionKindTable> = arrayListOf()

    companion object {
        const val DESC: Int = 1
        const val LIST: Int = 2
    }

    override fun getItemViewType(position: Int): Int {
        //println("position: ${position}")
        val i = if (position == 0) DESC else LIST
        return i
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == DESC) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.desc_cell, parent, false)
            return DescViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.subscriptionkind_cell, parent, false)
            return MemberSubscriptionKindViewHolder(context, view, delegate)
        }
    }

    override fun getItemCount(): Int {
        val n = items.size
        if (items.size > 0) {
            return items.size + 1
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DescViewHolder) {
            holder.bind("", "")
        } else if (holder is MemberSubscriptionKindViewHolder) {
            if (items.size >= position) {
                holder.bind(items[position-1])
            }
        }
    }

}

class DescViewHolder(view: View): ViewHolder(view) {

    var titleTV: TextView? = null
    var descTV: TextView? = null

    init {
        view.findViewById<TextView>(R.id.titleTV) ?. let {
            titleTV = it
            it.text = "訂閱會員介紹"
        }

        view.findViewById<TextView>(R.id.descTV) ?. let {
            descTV = it
            val text: String = "訂閱會員將享有羽球密碼的各種優惠\n" +
                    "1.鑽石會員享有12張開箱球拍券。\n" +
                    "2.白金會員享有7張開箱球拍券。\n" +
                    "3.金牌會員享有3張開箱球拍券。\n" +
                    "4.銀牌會員享有2張開箱球拍券。\n" +
                    "5.銅牌會員享有1張開箱球拍券。\n" +
                    "6.鐵盤會員沒有開箱球拍券。\n" +
                    "7.基本會員沒有開箱球拍券。\n"
            it.text = text
        }
    }

    fun bind(title: String, desc: String) {
//        titleTV?.text = title
//        descTV?.text = desc
    }
}

class MemberSubscriptionKindViewHolder(
    context: Context,
    view: View,
    delegate: MemberSubscriptionKindVC
): MyViewHolder2<MemberSubscriptionKindTable, MemberSubscriptionKindVC>(context, view, delegate) {

    val containerRL: RelativeLayout = view.findViewById(R.id.containerRL)
    val iconBG: IconWithBGCircle = view.findViewById(R.id.iconBG)
    val titleLbl: TextView = view.findViewById(R.id.titleLbl)
    val lotteryTV: TextView = view.findViewById(R.id.lotteryTV)
    val priceLbl: TextView = view.findViewById(R.id.priceLbl)

    fun bind(item: MemberSubscriptionKindTable) {

        iconBG.setIcon("ic_subscription_${item.eng_name}")
        titleLbl.text = item.name
        lotteryTV.text = "每次開箱球拍券：${item.lottery}張"
        priceLbl.text = if (item.price == 0) "免費" else "${item.price.toString()}/月"

        if (item.selected) {
            ContextCompat.getDrawable(context, R.drawable.cell_selected) ?. let {
                containerRL.background = it
            }
        }

        view.setOnClickListener {
            delegate.cellClick(item)
        }
    }
}