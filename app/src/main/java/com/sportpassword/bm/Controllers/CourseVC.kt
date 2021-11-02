package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Data.SearchSection
import com.sportpassword.bm.Fragments.MyAdapter
import com.sportpassword.bm.Fragments.MyViewHolder
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

class CourseAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<CourseViewHolder>(resource, ::CourseViewHolder, list1CellDelegate) {}

class CourseViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: CourseTable = _row as CourseTable

        if (row.city_show.length > 0) {
            viewHolder.cityBtn.text = row.city_show
            viewHolder.cityBtn.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate.cellCity(row)
                }
            }
            viewHolder.cityBtn.visibility = View.VISIBLE
        } else {
            viewHolder.cityBtn.visibility = View.GONE
        }

        if (row.price_text_short != null && row.price_text_short.isNotEmpty()) {
            viewHolder.priceLbl.text = row.price_text_short
        } else {
            viewHolder.priceLbl.text = "價格:未提供"
        }

        if (row.weekdays_show.length > 0) {
            viewHolder.weekdayLbl.text = row.weekdays_show
        } else {
            viewHolder.weekdayLbl.text = "未提供"
        }

        if (row.interval_show.length > 0) {
            viewHolder.intervalLbl.text = row.interval_show
        } else {
            viewHolder.intervalLbl.text = "未提供"
        }

        viewHolder.people_limitLbl.text = row.people_limit_show

        if (row.signup_count_show.length > 0 && row.people_limit > 0) {
            viewHolder.signup_countLbl.text = "已報名:${row.signup_count_show}"
        } else {
            viewHolder.signup_countLbl.visibility = View.INVISIBLE
        }
    }
}