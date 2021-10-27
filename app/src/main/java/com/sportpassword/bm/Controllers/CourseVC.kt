package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.content.ContextCompat
import com.sportpassword.bm.Fragments.CourseAdapter
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.CoursesTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.jsonToModels
import kotlinx.android.synthetic.main.activity_course_vc.*
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.activity_store_vc.list_container
import kotlinx.android.synthetic.main.bottom_view.*
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

        refresh()
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