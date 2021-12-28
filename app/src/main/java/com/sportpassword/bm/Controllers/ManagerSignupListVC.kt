package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.TeamAdapter
import com.sportpassword.bm.Models.SignupNormalTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import kotlinx.android.synthetic.main.mask.*

class ManagerSignupListVC : MyTableVC() {

    var able_token: String = ""
    var signupNormalTables: ArrayList<SignupNormalTable> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_signup_list_vc)

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
        }

        if (intent.hasExtra("able_token")) {
            able_token = intent.getStringExtra("able_token")!!
        }

        if (able_type == "team") {
            setMyTitle("管理球隊報名列表")
            dataService = TeamService
        } else if (able_type == "course") {
            setMyTitle("管理課程報名列表")
            dataService = CourseService
        }

        tableAdapter = TeamAdapter(R.layout.manager_team_item, this)
        recyclerView.adapter = tableAdapter

        init()
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
        params.put("manager_token", manager_token!!)
        params.put("status", "all")
        tableLists.clear()
        getDataStart(page, perPage)
    }

    override fun getDataStart(_page: Int, _perPage: Int, token: String?) {
        Loading.show(mask)
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
            Loading.hide(mask)
        }
        loading = false
        if (refreshLayout != null) {
            refreshLayout!!.isRefreshing = false
        }
    }

    override fun genericTable() {

        var signupListResultTable: SignupListResultTable? = null
        try {
            signupListResultTable = Gson().fromJson<SignupListResultTable>(dataService.jsonString, SignupListResultTable::class.java)
        } catch (e: JsonParseException) {

        }
        if (signupListResultTable != null && signupListResultTable.success) {
            getPage()
            signupNormalTables = signupListResultTable.rows
            tableLists += generateItems1(SignupNormalTable::class, signupNormalTables)
            tableAdapter.setMyTableList(tableLists)
            runOnUiThread {
                tableAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun getPage() {
        page = tables!!.page
        perPage = tables!!.perPage
        totalCount = tables!!.totalCount
        val _totalPage: Int = totalCount / perPage
        totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
        theFirstTime = false
    }
}

class SignupListResultTable: Table() {

    var success: Boolean = false
    var msg: String = ""
    var rows: ArrayList<SignupNormalTable> = arrayListOf()

}