package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

    override fun cellClick(row: Table) {
        val row1: MemberSubscriptionKindTable? = row as? MemberSubscriptionKindTable?
        if (row1 != null) {
            toMemberSubscriptionPay(row.name, row.price, row.eng_name)
        }
    }


//    fun didSelect(row: MemberSubscriptionKindTable, idx: Int) {
//        toMemberSubscriptionPay(row.name, row.price, row.eng_name)
//    }

    private fun tableViewSetSelected(row: MemberSubscriptionKindTable): Boolean {
        return row.eng_name == member.subscription
    }

    private fun setBottomThreeView() {
        findViewById<Button>(R.id.submitBtn) ?. let {
            it.text = "查詢"
            it.setOnClickListener {
                toMemberSuriptionLog()
            }
        }

        findViewById<Button>(R.id.threeBtn) ?. let {
            it.text = "退訂"
            it.setOnClickListener {
                //toProduct()
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
    val priceLbl: TextView = view.findViewById(R.id.priceLbl)

    override fun bind(row: MemberSubscriptionKindTable, idx: Int) {
        super.bind(row, idx)

        titleLbl.text = row.name
        priceLbl.text = "NT$: " + row.price.toString() + " 元/月"
    }
}