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
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.noSec
import com.sportpassword.bm.Utilities.setInfo
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.subscriptionlog_cell.view.*
import java.lang.reflect.Type

class MemberSubscriptionLogVC : BaseActivity(), MyTable2IF {

    private val tableType: Type = object : TypeToken<Tables2<MemberSubscriptionLogTable>>() {}.type
    lateinit var tableView: MyTable2VC<MemberSubscriptionLogViewHolder, MemberSubscriptionLogTable, MemberSubscriptionLogVC>

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = MemberService
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_subscription_log_vc)

        setMyTitle("訂閱會員付款紀錄")

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
        tableView = MyTable2VC(recyclerView, refreshLayout, R.layout.subscriptionlog_cell,
            ::MemberSubscriptionLogViewHolder, tableType, this::tableViewSetSelected, this::getDataFromServer, this)
    }

    override fun refresh() {
        page = 1
        tableView.getDataFromServer(page)
    }

    private fun getDataFromServer(page: Int) {
        Loading.show(mask)
        loading = true

        MemberService.subscriptionLog(this, member.token!!, page, tableView.perPage) { success ->
            runOnUiThread {
                Loading.hide(mask)

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

    val noLbl: TextView = view.noTV
    val priceLbl: TextView = view.priceLbl
    val dateLbl: TextView = view.dateLbl

    override fun bind(row: MemberSubscriptionLogTable, idx: Int) {
        super.bind(row, idx)

        noLbl.text = row.no.toString()
        priceLbl.text = "NT$: " + row.amount.toString() + " 元"
        dateLbl.text = row.created_at.noSec()
    }
}
