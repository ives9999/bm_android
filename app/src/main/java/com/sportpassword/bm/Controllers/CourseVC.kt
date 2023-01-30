package com.sportpassword.bm.Controllers

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sportpassword.bm.Adapters.CourseAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.CoursesTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityCourseVcBinding
import com.sportpassword.bm.databinding.MytablevcBinding

class CourseVC : MyTableVC() {

    private lateinit var binding: ActivityCourseVcBinding
    //private lateinit var view: ViewGroup

    var mysTable: CoursesTable? = null
    lateinit var tableAdapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCourseVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        this.dataService = CourseService
        able_type = "course"

        if (intent.hasExtra("isPrevIconShow")) {
            isPrevIconShow = intent.getBooleanExtra("isPrevIconShow", false)
        }

        if (intent.hasExtra("isSearchIconShow")) {
            isSearchIconShow = intent.getBooleanExtra("isSearchIconShow", false)
        }

        setBottomTabFocus()
        binding.topViewRL.topTitleLbl.setText("課程")

        recyclerView = binding.listContainer
        refreshLayout = binding.pageRefresh

        view.findViewById<FrameLayout>(R.id.mask) ?. let { mask ->
            maskView = mask
        }
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
            if (mysTable!!.rows.count() > 0) {

                getPage()
                tableLists += generateItems1(CourseTable::class, mysTable!!.rows)
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
        val r3: OneRow = OneRow("星期幾", "", "全部", WEEKDAYS_KEY, "more")
        rows.add(r3)
        val r4: OneRow = OneRow("開始時間", "", "全部", START_TIME_KEY, "more")
        rows.add(r4)
        val r5: OneRow = OneRow("結束時間", "", "全部", END_TIME_KEY, "more")
        rows.add(r5)

        val s: OneSection = OneSection("一般", "general", isExpanded)
        s.items.addAll(rows)
        return s
    }

    override fun cellCity(row: Table) {
        val key: String = CITY_KEY
        val row1: CourseTable = row as CourseTable
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
