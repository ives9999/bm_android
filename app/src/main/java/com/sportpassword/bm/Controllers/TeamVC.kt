package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Fragments.TeamItem
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.Models.TeamsTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.mask.*

class TeamVC : MyTableVC() {

    var mysTable: TeamsTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        this.dataService = TeamService
        able_type = "team"

        searchRows = arrayListOf(
            hashMapOf("title" to "關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to ""),
            hashMapOf("title" to "縣市","show" to "全部","key" to CITY_KEY,"value" to ""),
            hashMapOf("title" to "球館","show" to "全部","key" to ARENA_KEY,"value" to ""),
            hashMapOf("title" to "星期幾","show" to "全部","key" to WEEKDAY_KEY,"value" to ""),
            hashMapOf("title" to "時段","show" to "全部","key" to START_TIME_KEY,"value" to ""),
            hashMapOf("title" to "程度","show" to "全部","key" to DEGREE_KEY,"value" to "")
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)

        setMyTitle("球隊")

        if (intent.hasExtra("params")) {
            val t = intent.getSerializableExtra("params")
            if (t != null) {
                params = t as HashMap<String, Any>
            }
        }

        recyclerView = list_container
        refreshLayout = refresh
        maskView = mask

        initAdapter()
        refresh()
    }

    override fun genericTable() {
        mysTable = jsonToModels(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        if (mysTable != null) {
            for (row in mysTable!!.rows) {
                //row.print()
                row.filterRow()
                val myItem = TeamItem(this, row)
                myItem.list1CellDelegate = this
                items.add(myItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.ViewHolder>, view: View) {

        val myItem = item as TeamItem
        val table = myItem.row
        toShowTeam(table.token)
    }

    override fun cellArena(row: Table) {

        val myTable: TeamTable? = row as? TeamTable
        if (myTable != null) {
            val key: String = ARENA_KEY
            val arena_id: Int = myTable.arena_id
            val row1 = getDefinedRow(key)
            row1["value"] = arena_id.toString()
            replaceRows(key, row1)
            prepareParams()
            refresh()
        } else {
            warning("轉為TeamTable失敗，請洽管理員")
        }
    }
}