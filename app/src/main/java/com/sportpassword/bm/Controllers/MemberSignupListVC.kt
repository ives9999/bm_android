package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.MemberSignupListAdapter
import com.sportpassword.bm.Models.SignupNormalTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.setInfo
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_member_signup_list_vc.*
import kotlinx.android.synthetic.main.mask.*

class MemberSignupListVC : MyTableVC() {

    lateinit var adapter: MemberSignupListAdapter

    var signupResultTable: SignupResultTable? = null
    var signupNormalTables: ArrayList<SignupNormalTable> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_signup_list_vc)

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
        }

        if (able_type == "team") {
            setMyTitle("球隊報名列表")
            dataService = TeamService
        } else if (able_type == "course") {
            setMyTitle("課程報名列表")
            dataService = CourseService
        }

        refreshLayout = list_refresh
        setRefreshListener()

        recyclerView = list

        adapter = MemberSignupListAdapter(R.layout.member_signuplist_cell, this)
        recyclerView.adapter = adapter

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
//        adapter.clear()
//        items.clear()

        params.clear()
//        params.put("manager_token", manager_token!!)
//        params.put("status", "all")
        tableLists.clear()
        getDataStart(page, perPage)
    }

    override fun getDataStart(_page: Int, _perPage: Int, token: String?) {
        Loading.show(mask)
        loading = true

        MemberService.memberSignupCalendar(2021, 1, member.token!!, able_type, _page, _perPage) { success ->
            jsonString = MemberService.jsonString
            getDataEnd(success)
        }
    }

    override fun getDataEnd(success: Boolean) {
        if (success) {

            if (jsonString != null && jsonString!!.isNotEmpty()) {
                genericTable()
            } else {
                warning("沒有取得回傳的json字串，請洽管理員")
            }
            page++
        }
        runOnUiThread {
            Loading.hide(mask)
            closeRefresh()
        }
        loading = false
        if (refreshLayout != null) {
            refreshLayout!!.isRefreshing = false
        }
    }

    override fun genericTable() {

        try {
            signupResultTable = Gson().fromJson<SignupResultTable>(jsonString, SignupResultTable::class.java)
        } catch (e: JsonParseException) {
            println(e.localizedMessage)
        }
        if (signupResultTable != null && signupResultTable!!.success) {

            if (signupResultTable!!.rows.size > 0) {
                if (page == 1) {
                    getPage()
                }
                signupNormalTables = signupResultTable!!.rows
                tableLists += generateItems1(SignupNormalTable::class, signupNormalTables)
                adapter.setMyTableList(tableLists)
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            } else {
                val rootView: ViewGroup = getRootView()
                runOnUiThread {
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
        }
    }

    override fun getPage() {
        if (signupResultTable != null) {

            perPage = signupResultTable!!.perPage
            totalCount = signupResultTable!!.totalCount
            val _totalPage: Int = totalCount / perPage
            totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
        }
    }

    override fun cellClick(row: Table) {
        val row1: SignupNormalTable = row as SignupNormalTable
        if (able_type == "team") {
            row1.ableTable?.token?.let { toShowTeam(it) }
        } else if (able_type == "course") {
            row1.ableTable?.token?.let { toShowCourse(it) }
        }
    }
}

class SignupResultTable {

    var success: Boolean = false
    var page: Int = -1
    var totalCount: Int = -1
    var perPage: Int = -1
    var msg: String = ""
    var rows: ArrayList<SignupNormalTable> = arrayListOf()
}