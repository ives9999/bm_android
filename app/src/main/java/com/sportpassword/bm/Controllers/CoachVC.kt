package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.ViewGroup
import com.sportpassword.bm.Adapters.CoachAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Data.SearchSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_store_vc.*

class CoachVC : MyTableVC() {

    var mysTable: CoachesTable? = null
    lateinit var tableAdapter: CoachAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

//        searchRows = arrayListOf(
//            hashMapOf("title" to "關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to ""),
//            hashMapOf("title" to "縣市","show" to "全部","key" to CITY_KEY,"value" to "")
//        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)
        //setContentView(R.layout.activity_coach_vc)

        setMyTitle("教練")
        able_type = "coach"

        dataService = CoachService
        recyclerView = list_container
        refreshLayout = refresh
        //initAdapter()
        tableAdapter = CoachAdapter(R.layout.coach_list_cell, this)
        recyclerView.adapter = tableAdapter

        init()
        refresh()
    }

    override fun init() {
        isSearchIconShow = true
        isPrevIconShow = true
        super.init()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        isSearchIconShow = true
//        super.onCreateOptionsMenu(menu)
//
//        return true
//    }

    override fun genericTable() {
        //storesTable = jsonToModel<StoresTable>(dataService.jsonString)
        //println(dataService.jsonString)
        mysTable = jsonToModels<CoachesTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
            if (mysTable!!.rows.count() > 0) {
                getPage()
                tableLists += generateItems1(CoachTable::class, mysTable!!.rows)
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

    override fun makeSection0Row(isExpanded: Boolean): OneSection {
        val rows: ArrayList<OneRow> = arrayListOf()
        val r1: OneRow = OneRow("關鍵字", "", "", KEYWORD_KEY, "textField")
        rows.add(r1)
        val r2: OneRow = OneRow("縣市", "", "全部", CITY_KEY, "more")
        rows.add(r2)

        val s: OneSection = OneSection("一般", "general", isExpanded)
        s.items.addAll(rows)
        return s
    }

    override fun cellCity(row: Table) {
        val key: String = CITY_KEY
        val row1: CoachTable = row as CoachTable
        val row2: OneRow = getOneRowFromKey(key)
        row2.value = row1.city_id.toString()
        row2.show = row1.city_show
        prepareParams()
        page = 1
        tableLists.clear()
        getDataStart(page, perPage)
    }

//    override fun generateItems(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        if (mysTable != null) {
//            for (row in mysTable!!.rows) {
//                //row.print()
//                row.filterRow()
//                val coachItem = CoachItem(this, row)
//                coachItem.list1CellDelegate = this
//                items.add(coachItem)
//            }
//        }
//
//        return items
//    }
//
//    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {
//
//        val coachItem = item as CoachItem
//        val myTable = coachItem.row
//        toShowCoach(myTable.token)
//    }

//    fun remove(indexPath: IndexPath) {
//        val row = searchRows[indexPath.row]
//        val key = row["key"]!!
//        when (key) {
//            CITY_KEY -> {
//                row["show"] = "全部"
//                citys.clear()
//            }
//        }
//
//        row["value"] = ""
//    }
//
//    override fun prepare(idx: Int) {
//
//        val row = searchRows.get(idx)
//        var key: String = ""
//        if (row.containsKey("key")) {
//            key = row["key"]!!
//        }
//
//        var value: String = ""
//        if (row.containsKey("value") && row["value"]!!.isNotEmpty()) {
//            value = row["value"]!!
//        }
//
//
//        when (key) {
//            CITY_KEY -> {
//                toSelectCity(value, this)
//            }
//        }
//    }
}

//class CoachItem(override var context: Context, var _row: CoachTable): ListItem<Table>(context, _row) {
//
//    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//
//        super.bind(viewHolder, position)
//
//        val row: CoachTable = _row
//
//        if (row.city_show.isNotEmpty()) {
//            viewHolder.cityBtn.text = row.city_show
//            viewHolder.cityBtn.setOnClickListener {
//                if (list1CellDelegate != null) {
//                    list1CellDelegate!!.cellCity(row)
//                }
//            }
//
//        } else {
//            viewHolder.cityBtn.visibility = View.GONE
//        }
//
//        if (row.mobile_show.isNotEmpty()) {
//            viewHolder.mobileLbl.text = row.mobile_show
//        }
//
//        if (row.seniority >= 0) {
//            viewHolder.seniorityLbl.text = "年資:${row.seniority_show}"
//        } else {
//            viewHolder.seniorityLbl.text = "年資:未提供"
//        }
//
//        if (row.line != null && row.line.isNotEmpty()) {
//            viewHolder.line.text = "Line:${row.line}"
//        } else {
//            viewHolder.line.text = "Line:未提供"
//        }
//    }
//
//    override fun getLayout() = R.layout.coach_list_cell
//}
