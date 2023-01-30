package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sportpassword.bm.Adapters.TeamAdapter
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.Models.TeamsTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityTeamVcBinding

class TeamVC : MyTableVC() {

    private lateinit var binding: ActivityTeamVcBinding
    //private lateinit var view: ViewGroup

    var mysTable: TeamsTable? = null
    lateinit var tableAdapter: TeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        this.dataService = TeamService
        able_type = "team"

//        searchRows = arrayListOf(
//            hashMapOf("title" to "關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to ""),
//            hashMapOf("title" to "縣市","show" to "全部","key" to CITY_KEY,"value" to ""),
//            hashMapOf("title" to "球館","show" to "全部","key" to ARENA_KEY,"value" to ""),
//            hashMapOf("title" to "星期幾","show" to "全部","key" to WEEKDAY_KEY,"value" to ""),
//            hashMapOf("title" to "時段","show" to "全部","key" to START_TIME_KEY,"value" to ""),
//            hashMapOf("title" to "程度","show" to "全部","key" to DEGREE_KEY,"value" to "")
//        )

        super.onCreate(savedInstanceState)

        binding = ActivityTeamVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        setMyTitle("球隊")

        if (intent.hasExtra("params")) {
            val t = intent.getSerializableExtra("params")
            if (t != null) {
                params = t as HashMap<String, String>
            }
        }

        if (intent.hasExtra("isPrevIconShow")) {
            isPrevIconShow = intent.getBooleanExtra("isPrevIconShow", false)
        }

        if (intent.hasExtra("isSearchIconShow")) {
            isSearchIconShow = intent.getBooleanExtra("isSearchIconShow", false)
        }

        recyclerView = binding.listContainer
        refreshLayout = binding.refresh

        //initAdapter()
        tableAdapter = TeamAdapter(R.layout.team_list_cell, this)
        recyclerView.adapter = tableAdapter

        init()
        refresh()
    }

    override fun init() {
        super.init()
    }

    override fun genericTable() {
        mysTable = jsonToModels(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
            if (mysTable!!.rows.count() > 0) {
                getPage()
                tableLists += generateItems1(TeamTable::class, mysTable!!.rows)
                tableAdapter.setMyTableList(tableLists)
                runOnUiThread {
                    tableAdapter.notifyDataSetChanged()
                }
            } else {
                val rootView: ViewGroup = getRootView()
                runOnUiThread {
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
        }
    }

    override fun cellArena(row: Table) {

//        val myTable: TeamTable? = row as? TeamTable
//        if (myTable != null) {
//            val key: String = ARENA_KEY
//            val arena_id: Int = myTable.arena_id
//            val row1 = getDefinedRow(key)
//            row1["value"] = arena_id.toString()
//            replaceRows(key, row1)
//            prepareParams()
//            refresh()
//        } else {
//            warning("轉為TeamTable失敗，請洽管理員")
//        }
    }
}