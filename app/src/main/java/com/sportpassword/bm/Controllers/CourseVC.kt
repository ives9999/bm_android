package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Adapters.CourseAdapter
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Data.SearchSection
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.CoursesTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_course_vc.*
import kotlinx.android.synthetic.main.activity_store_vc.list_container
import kotlinx.android.synthetic.main.bottom_view.*
import kotlinx.android.synthetic.main.course_list_cell.view.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.top_view.*
import org.jetbrains.anko.backgroundColor

class CourseVC : MyTableVC() {

    var mysTable: CoursesTable? = null
    lateinit var tableAdapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_course_vc)

        this.dataService = CourseService
        able_type = "course"

        courseTabLine.backgroundColor = myColorGreen
        topTitleLbl.setText("課程")
        //setMyTitle("課程")

        recyclerView = list_container
        refreshLayout = page_refresh
        maskView = mask
        setRefreshListener()

//        initAdapter()
        tableAdapter = CourseAdapter(R.layout.course_list_cell, this)
        recyclerView.adapter = tableAdapter

        init()
        refresh()
    }

    override fun init() {
        isSearchIconShow = true
        super.init()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        isSearchIconShow = true
//        super.onCreateOptionsMenu(menu)
//
//        return true
//    }

    override fun genericTable() {
        mysTable = jsonToModels<CoursesTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
            getPage()
            tableLists += generateItems1(CourseTable::class, mysTable!!.rows)
            tableAdapter.setMyTableList(tableLists)
            runOnUiThread {
                tableAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun makeSection0Row(isExpanded: Boolean): SearchSection {
        val rows: ArrayList<SearchRow> = arrayListOf()
        val r1: SearchRow = SearchRow("關鍵字", "", "", KEYWORD_KEY, "textField")
        rows.add(r1)
        val r2: SearchRow = SearchRow("縣市", "", "全部", CITY_KEY, "more")
        rows.add(r2)
        val r3: SearchRow = SearchRow("星期幾", "", "全部", WEEKDAY_KEY, "more")
        rows.add(r3)
        val r4: SearchRow = SearchRow("開始時間", "", "全部", START_TIME_KEY, "more")
        rows.add(r4)
        val r5: SearchRow = SearchRow("結束時間", "", "全部", END_TIME_KEY, "more")
        rows.add(r5)

        val s: SearchSection = SearchSection("一般", isExpanded)
        s.items.addAll(rows)
        return s
    }

    override fun cellCity(row: Table) {
        val key: String = CITY_KEY
        val row1: CourseTable = row as CourseTable
        val row2: SearchRow = getSearchRowFromKey(key)
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
//                val myItem = CourseItem(this, row)
//                myItem.list1CellDelegate = this
//                items.add(myItem)
//            }
//        }
//
//        return items
//    }
//
//    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {
//
//        val myItem = item as CourseItem
//        val table = myItem.row
//        toShowCourse(table.token)
//    }
}
