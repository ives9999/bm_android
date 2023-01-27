package com.sportpassword.bm.Controllers

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.ManagerSignupListAdapter
import com.sportpassword.bm.Adapters.TeamAdapter
import com.sportpassword.bm.Models.DateTable
import com.sportpassword.bm.Models.SignupNormalTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.setInfo
import com.sportpassword.bm.databinding.ActivityBlackListVcBinding
import com.sportpassword.bm.databinding.ActivityManagerSignupListVcBinding
import com.sportpassword.bm.databinding.MytablevcBinding
import com.sportpassword.bm.member

class ManagerSignupListVC : MyTableVC() {

    private lateinit var binding: ActivityManagerSignupListVcBinding
    private lateinit var view: ViewGroup

    var able_token: String = ""
    var able_title: String = ""

    var signupListResultTable: SignupListResultTable? = null
    var signupNormalTables: ArrayList<SignupNormalTable> = arrayListOf()
    var signupSections: ArrayList<SignupSection> = arrayListOf()

    lateinit var adapter: ManagerSignupListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityManagerSignupListVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
        }

        if (intent.hasExtra("able_token")) {
            able_token = intent.getStringExtra("able_token")!!
        }

        if (intent.hasExtra("able_title")) {
            able_title = intent.getStringExtra("able_title")!!
        }

        setMyTitle(able_title + "報名會員列表")
        if (able_type == "team") {
            dataService = TeamService
        } else if (able_type == "course") {
            dataService = CourseService
        }

        refreshLayout = binding.listRefresh
        setRefreshListener()

        recyclerView = binding.listContainer

        adapter = ManagerSignupListAdapter(this)
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
        params.put("manager_token", member.token!!)
        params.put("status", "all")
        tableLists.clear()
        getDataStart(page, perPage)
    }

    override fun getDataStart(_page: Int, _perPage: Int, token: String?) {
        Loading.show(view)
        loading = true

        dataService.managerSignupList(able_type, able_token, _page, _perPage) { success ->
            jsonString = dataService.jsonString
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
            Loading.hide(view)
            closeRefresh()
        }
        loading = false
        if (refreshLayout != null) {
            refreshLayout!!.isRefreshing = false
        }
    }

    override fun genericTable() {

        try {
            signupListResultTable = Gson().fromJson<SignupListResultTable>(dataService.jsonString, SignupListResultTable::class.java)
        } catch (e: JsonParseException) {

        }
        if (signupListResultTable != null && signupListResultTable!!.success) {
            if (signupListResultTable!!.rows.size > 0) {
                if (page == 1) {
                    getPage()
                }
                signupNormalTables = signupListResultTable!!.rows

                for (signupNormalTable in signupNormalTables) {

                    signupNormalTable.filterRow()
                    if (signupNormalTable.dateTable != null) {

                        val dateTable: DateTable = signupNormalTable.dateTable!!
                        val date: String = dateTable.date

                        var bExist: Boolean = false
                        for (signupSection in signupSections) {
                            if (signupSection.date == date) {
                                signupSection.rows.add(signupNormalTable)
                                bExist = true
                            }
                        }

                        if (!bExist) {
                            val signupSection: SignupSection = SignupSection()
                            signupSection.date = date
                            signupSection.rows.add(signupNormalTable)
                            signupSections.add(signupSection)
                        }
                    }
                }

                adapter.signupSections = signupSections
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
        if (signupListResultTable != null) {

            perPage = signupListResultTable!!.perPage
            totalCount = signupListResultTable!!.totalCount
            val _totalPage: Int = totalCount / perPage
            totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
        }
    }

    override fun handleOneSectionExpanded(idx: Int) {
        //println(idx)
        val signupSection = signupSections[idx]
        var isExpanded: Boolean = signupSection.isExpanded
        isExpanded = !isExpanded
        signupSections[idx].isExpanded = isExpanded
        adapter.signupSections = signupSections
        adapter.notifyItemChanged(idx)
    }
}

class SignupListResultTable: Table() {

    var success: Boolean = false
    var msg: String = ""
    var page: Int = -1
    var totalCount: Int = -1
    var perPage: Int = -1
    var rows: ArrayList<SignupNormalTable> = arrayListOf()

}

class SignupSection {

    var date: String = ""
    var isExpanded: Boolean = true
    var rows: ArrayList<SignupNormalTable> = arrayListOf()
}