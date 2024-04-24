package com.sportpassword.bm.Controllers

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Interface.MyTable2IF
import com.sportpassword.bm.Models.MemberSubscriptionLogTable
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Views.IconText
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.Views.ShowTop2Delegate
import com.sportpassword.bm.databinding.ActivityMemberSubscriptionLogVcBinding
import com.sportpassword.bm.extensions.getMyColor
import com.sportpassword.bm.extensions.noSec
import com.sportpassword.bm.extensions.toTwoString
import com.sportpassword.bm.member
import org.jetbrains.anko.textColor
import tw.com.bluemobile.hbc.extensions.setInfo
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

    val noTV: TextView = view.findViewById(R.id.noTV)
    val startOrEndTV: TextView = view.findViewById(R.id.startOrEndTV)
    val datetimeIT: IconText = view.findViewById(R.id.datetimeIT)
    val priceIT: IconText = view.findViewById(R.id.priceIT)
    val invoiceIT: IconText = view.findViewById(R.id.invoiceIT)
    val orderTV: TextView = view.findViewById(R.id.orderTV)

    override fun bind(row: MemberSubscriptionLogTable, idx: Int) {
        super.bind(row, idx)

        noTV.text = row.no.toTwoString()
        datetimeIT.setText(row.created_at.noSec())

        priceIT.setText("NT$: ${row.amount} 元")
        invoiceIT.setText(row.invoice_no)

        //開始訂閱
        if (row.orderTable != null) {
            startOrEndTV.visibility = View.VISIBLE
            startOrEndTV.text = "訂閱開始"
            startOrEndTV.textColor = context.getMyColor(R.color.Primary_300)

            priceIT.visibility = View.VISIBLE
            invoiceIT.visibility = View.VISIBLE

            orderTV.text = "訂單編號：${row.orderTable!!.order_no}"
        } else {
            //繼續付款或取消訂閱
            if (row.type == "stop") {
                startOrEndTV.visibility = View.VISIBLE
                startOrEndTV.text = "取消訂閱"
                startOrEndTV.textColor = context.getMyColor(R.color.DANGER)

                priceIT.visibility = View.GONE
                invoiceIT.visibility = View.GONE
            } else {
                startOrEndTV.visibility = View.INVISIBLE
                priceIT.visibility = View.VISIBLE
                invoiceIT.visibility = View.VISIBLE
            }
            orderTV.visibility = View.GONE
        }
    }
}
