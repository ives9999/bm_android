package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Adapters.MemberSubscriptionKindViewHolder
import com.sportpassword.bm.Interface.MyTable2IF
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.mask.*
import java.lang.reflect.Type

class MemberSubscriptionKindVC : BaseActivity(), MyTable2IF {

    private val tableType: Type = object : TypeToken<Tables2<MemberSubscriptionKindTable>>() {}.type
    lateinit var tableView: MyTable2VC<MemberSubscriptionKindViewHolder, MemberSubscriptionKindTable>
    //lateinit var tableAdapter: MyAdapter2<MemberLevelUpViewHolder<MemberLevelKindTable>, MemberLevelKindTable>
    //var rows: ArrayList<MemberLevelKindTable> = arrayListOf()

    var bottom_button_count: Int = 3
    val button_width: Int = 400

    //var mysTable: Tables2<MemberLevelKindTable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = MemberService
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_subscription_kind_vc)

        setMyTitle("訂閱會員")

        val recyclerView: RecyclerView = findViewById(R.id.list)
        tableView = MyTable2VC(recyclerView, R.layout.subscriptionkind_cell, ::MemberSubscriptionKindViewHolder, tableType, this::didSelect, this::tableViewSetSelected)

        setBottomThreeView()
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

        MemberService.subscriptionKind(this, member.token!!, tableView.page, tableView.perPage) { success ->
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

    fun didSelect(row: MemberSubscriptionKindTable, idx: Int) {
        toMemberSubscriptionPay(row.name, row.price, row.eng_name)
    }

    fun tableViewSetSelected(row: MemberSubscriptionKindTable): Boolean {

        return row.eng_name == member.subscription
    }

    fun setBottomThreeView() {
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