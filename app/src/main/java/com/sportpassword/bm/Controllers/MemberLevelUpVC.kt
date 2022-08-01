package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Adapters.MemberLevelUpViewHolder
import com.sportpassword.bm.Adapters.MyAdapter2
import com.sportpassword.bm.Adapters.MyViewHolder2
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_member_level_up_vc.*
import kotlinx.android.synthetic.main.mask.*
import java.lang.reflect.Type

class MemberLevelUpVC : MyTableVC() {

    //lateinit var tableAdapter: MyAdapter2<MemberLevelUpViewHolder<MemberLevelKindTable>, MemberLevelKindTable>
    lateinit var myTable: MyTable2VC<MemberLevelUpViewHolder<MemberLevelKindTable>, MemberLevelKindTable>
    var rows: ArrayList<MemberLevelKindTable> = arrayListOf()

    var bottom_button_count: Int = 3
    val button_width: Int = 400

    var mysTable: Tables2<MemberLevelKindTable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_level_up_vc)

        setMyTitle("進階會員")

        val recyclerView: RecyclerView = findViewById(R.id.list)
        val type: Type = object : TypeToken<MemberLevelKindTable>() {}.type
        //val n: Type = MemberLevelKindTable::class.java
        myTable = MyTable2VC(recyclerView, R.layout.levelup_cell, ::MemberLevelUpViewHolder, type, this)
        myTable.setItems(rows)

        //recyclerView = list
        //tableAdapter = MyAdapter2(R.layout.levelup_cell, ::MemberLevelUpViewHolder, this)
        //recyclerView.adapter = tableAdapter
        //tableAdapter.items = rows
        //tableAdapter.setMyTableList(rows)

        setBottomThreeView()
        init()

        refresh()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    override fun refresh() {

        page = 1
        theFirstTime = true

        Loading.show(mask)
        loading = true

        MemberService.levelKind(this, member.token!!, page, perPage) { success ->
            runOnUiThread {
                Loading.hide(mask)
            }
            jsonString = MemberService.jsonString
            genericTable()

            //genericTable()

            if (jsonString != null) {
                val b: Boolean = myTable.parseJSON(jsonString!!)
                if (!b && myTable.msg.isEmpty()) {
                    val rootView: ViewGroup = getRootView()
                    runOnUiThread {
                        rootView.setInfo(this, "目前暫無資料")
                    }
                } else {
                    runOnUiThread {
                        myTable.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun genericTable() {
        //println(MemberService.jsonString)
        val type = object : TypeToken<Tables2<MemberLevelKindTable>>() {}.type
        mysTable = jsonToModels2<Tables2<MemberLevelKindTable>, MemberLevelKindTable>(jsonString!!, type)

        if (mysTable != null) {
            if (mysTable!!.rows.count() > 0) {
                //getPage()
                page = mysTable!!.page
                perPage = mysTable!!.perPage
                totalCount = mysTable!!.totalCount
                val _totalPage: Int = totalCount / perPage
                totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage

                rows += generateItems1(MemberLevelKindTable::class, mysTable!!.rows)
                myTable.setItems(rows)
                //tableAdapter.items = rows
                runOnUiThread {
                    myTable.notifyDataSetChanged()
                    //tableAdapter.notifyDataSetChanged()
                }
            } else {
                val rootView: ViewGroup = getRootView()
                runOnUiThread {
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
        }
    }

//    fun initRows() {
//        var row: OneRow = OneRow()
//        row.title = "金牌會員"
//        rows.add(row)
//        row = OneRow()
//        row.title = "銀牌會員"
//        rows.add(row)
//        row = OneRow()
//        row.title = "銅牌會員"
//        rows.add(row)
//        row = OneRow()
//        row.title = "鐵牌會員"
//        rows.add(row)
//    }

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
                //coinReturn()
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