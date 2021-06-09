package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import com.sportpassword.bm.Fragments.ListItem
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Utilities.jsonToModel
//import com.sportpassword.bm.Services.StoreService
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.coach_list_cell.*
import kotlinx.android.synthetic.main.coach_list_cell.cityBtn
import kotlinx.android.synthetic.main.team_list_cell.*

class CoachVC : MyTableVC() {

    var mysTable: CoachesTable? = null
    //var storesTable: StoresTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        searchRows = arrayListOf(
            hashMapOf("title" to "關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to ""),
            hashMapOf("title" to "縣市","show" to "全部","key" to CITY_KEY,"value" to "")
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)
        //setContentView(R.layout.activity_coach_vc)

        setMyTitle("教練")
        able_type = "coach"

        dataService = CoachService
        recyclerView = list_container
        refreshLayout = refresh
        initAdapter()

        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_manager, menu)
        val memuView = menu!!.findItem(R.id.menu_search_manager).actionView

        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        //val ManagerBtn = memuView.findViewById<ImageButton>(R.id.manager)

        searchBtn.tag = "coach"
//        ManagerBtn.tag = type

        // searchBtn onClick action is showSearchPanel, defined in menu_search_manager.xml layount

        return true
    }

    override fun genericTable() {
        //storesTable = jsonToModel<StoresTable>(dataService.jsonString)
        //println(dataService.jsonString)
        mysTable = jsonToModels<CoachesTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (mysTable != null) {
            for (row in mysTable!!.rows) {
                //row.print()
                row.filterRow()
                val coachItem = CoachItem(this, row)
                coachItem.list1CellDelegate = this
                items.add(coachItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.ViewHolder>, view: View) {

        val coachItem = item as CoachItem
        val myTable = coachItem.row
        toShowCoach(myTable.token)
    }

    override fun remove(indexPath: IndexPath) {
        val row = searchRows[indexPath.row]
        val key = row["key"]!!
        when (key) {
            CITY_KEY -> {
                row["show"] = "全部"
                citys.clear()
            }
        }

        row["value"] = ""
    }

    override fun prepare(idx: Int) {

        val row = searchRows.get(idx)
        var key: String = ""
        if (row.containsKey("key")) {
            key = row["key"]!!
        }

        var value: String = ""
        if (row.containsKey("value") && row["value"]!!.isNotEmpty()) {
            value = row["value"]!!
        }


        when (key) {
            CITY_KEY -> {
                toSelectCity(value, this)
            }
        }
    }
}

class CoachItem(override var context: Context, var _row: CoachTable): ListItem<Table>(context, _row) {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        super.bind(viewHolder, position)

        val row: CoachTable = _row

        if (row.city_show.isNotEmpty()) {
            viewHolder.cityBtn.text = row.city_show
            viewHolder.cityBtn.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellCity(row)
                }
            }

        } else {
            viewHolder.cityBtn.visibility = View.GONE
        }

        if (row.mobile_show.isNotEmpty()) {
            viewHolder.mobileLbl.text = row.mobile_show
        }

        if (row.seniority >= 0) {
            viewHolder.seniorityLbl.text = "年資:${row.seniority_show}"
        } else {
            viewHolder.seniorityLbl.text = "年資:未提供"
        }

        if (row.line != null && row.line.isNotEmpty()) {
            viewHolder.line.text = "Line:${row.line}"
        } else {
            viewHolder.line.text = "Line:未提供"
        }
    }

    override fun getLayout() = R.layout.coach_list_cell
}
