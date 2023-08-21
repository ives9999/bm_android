package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Interface.MyTable2IF
import com.sportpassword.bm.Models.MemberSubscriptionLogTable
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.MEMBER_SUBSCRIPTION_KIND
import com.sportpassword.bm.Utilities.noSec
import com.sportpassword.bm.Utilities.setInfo
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.Views.ShowTop2Delegate
import com.sportpassword.bm.databinding.ActivityMemberSubscriptionLogVcBinding
import com.sportpassword.bm.member
import java.lang.reflect.Type

class MemberSubscriptionLogVC : BaseActivity(), MyTable2IF, ShowTop2Delegate {

    private lateinit var binding: ActivityMemberSubscriptionLogVcBinding

    var showTop2: ShowTop2? = null
    private val tableType: Type = object : TypeToken<Tables2<MemberSubscriptionLogTable>>() {}.type
    lateinit var tableView: MyTable2VC<MemberSubscriptionLogViewHolder, MemberSubscriptionLogTable, MemberSubscriptionLogVC>

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = MemberService
        super.onCreate(savedInstanceState)

        binding = ActivityMemberSubscriptionLogVcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTop()
        initRecyclerView()
        init()
    }

    private fun initTop() {
        isPrevIconShow = true
        binding.top.delegate = this
        binding.top.apply {
            setTitle("訂閱會員付款紀錄")
            showPrev(true)
        }
    }

    private fun initRecyclerView() {

        refreshLayout = binding.refreshSR
        tableView = MyTable2VC(binding.list, refreshLayout, R.layout.subscriptionlog_cell,
            ::MemberSubscriptionLogViewHolder, tableType, this::tableViewSetSelected, this::getDataFromServer, this)
    }

    override fun init() {
        super.init()
        refresh()
    }

    override fun refresh() {
        page = 1
        tableView.getDataFromServer(page)
    }

    private fun getDataFromServer(page: Int) {
        loadingAnimation.start()
        loading = true

        MemberService.subscriptionLog(this, member.token!!, page, tableView.perPage) { success ->
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

//    private fun getDataFromServer() {
//        Loading.show(mask)
//        loading = true
//
//        MemberService.subscriptionLog(this, member.token!!, tableView.page, tableView.perPage) { success ->
//            runOnUiThread {
//                Loading.hide(mask)
//
//                //MyTable2IF
//                val b: Boolean = showTableView(tableView, MemberService.jsonString)
//                if (b) {
//                    tableView.notifyDataSetChanged()
//                } else {
//                    val rootView: ViewGroup = getRootView()
//                    rootView.setInfo(this, "目前暫無資料")
//                }
//            }
//        }
//    }

    fun tableViewSetSelected(row: MemberSubscriptionLogTable): Boolean {

        return false
    }
}

class MemberSubscriptionLogViewHolder(
    context: Context,
    view: View,
    delegate: MemberSubscriptionLogVC
): MyViewHolder2<MemberSubscriptionLogTable, MemberSubscriptionLogVC>(context, view, delegate) {

    val noLbl: TextView = view.findViewById(R.id.noTV)
    val priceLbl: TextView = view.findViewById(R.id.priceLbl)
    val dateLbl: TextView = view.findViewById(R.id.dateLbl)

    override fun bind(row: MemberSubscriptionLogTable, idx: Int) {
        super.bind(row, idx)

        noLbl.text = row.no.toString()
        priceLbl.text = "NT$: " + row.amount.toString() + " 元"
        dateLbl.text = row.created_at.noSec()
    }
}
