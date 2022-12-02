package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
    lateinit var tableView: MyTable2VC<MemberSubscriptionLogViewHolder, MemberSubscriptionLogTable>

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = MemberService
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_subscription_log_vc)

        setMyTitle("訂閱會員付款紀錄")

        val recyclerView: RecyclerView = findViewById(R.id.list)
        tableView = MyTable2VC(recyclerView, R.layout.subscriptionlog_cell,
            ::MemberSubscriptionLogViewHolder, tableType, this::didSelect, this::tableViewSetSelected)

        init()

        refresh()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    override fun refresh() {

        tableView.page = 1
        getDataFromServer()
    }

    private fun getDataFromServer() {
        Loading.show(mask)
        loading = true

        MemberService.subscriptionLog(this, member.token!!, tableView.page, tableView.perPage) { success ->
            runOnUiThread {
                Loading.hide(mask)

                //MyTable2IF
                val b: Boolean = showTableView(tableView, MemberService.jsonString)
                if (b) {
                    tableView.notifyDataSetChanged()
                } else {
                    val rootView: ViewGroup = getRootView()
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
        }
    }

    fun didSelect(row: MemberSubscriptionLogTable, idx: Int) {}

    fun tableViewSetSelected(row: MemberSubscriptionLogTable): Boolean {

        return false
    }
}

class MemberSubscriptionLogViewHolder(
    context: Context,
    viewHolder: View,
    didSelect: didSelectClosure<MemberSubscriptionLogTable>,
    selected: selectedClosure<MemberSubscriptionLogTable>
): MyViewHolder2<MemberSubscriptionLogTable>(context, viewHolder, didSelect, selected) {

    val noLbl: TextView = viewHolder.noTV
    val priceLbl: TextView = viewHolder.priceLbl
    val dateLbl: TextView = viewHolder.dateLbl

    override fun bind(row: MemberSubscriptionLogTable, idx: Int) {
        super.bind(row, idx)

        noLbl.text = row.no.toString()
        priceLbl.text = "NT$: " + row.amount.toString() + " 元"
        dateLbl.text = row.created_at.noSec()
    }
}
