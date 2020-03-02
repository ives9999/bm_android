package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.Models.SuperCourses
import com.sportpassword.bm.Models.SuperData
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.course_calendar_item.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_course.*
import java.util.*
import kotlin.collections.ArrayList

class CourseCalendarVC: MyTableVC1() {

    var superCourses: SuperCourses? = null

    var year: Int = Date().getY()
    var month: Int = Date().getm()
    var monthLastDay: Int = 31

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_course_calendar)

        val title = "會員報名課程列表"
        setMyTitle(title)

        dataService = CourseService
        monthLastDay = Global.getMonthLastDay(year, month)

        recyclerView = list_container
        refreshLayout = tab_refresh
        maskView = mask

        initAdapter(false)
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
    }

    override fun getDataStart(_page: Int, _perPage: Int) {
        Loading.show(maskView)
        loading = true
    }

    override fun getDataEnd(success: Boolean) {
        if (success) {
            if (theFirstTime) {
                page = dataService.page
                perPage = dataService.perPage
                totalCount = dataService.totalCount
                var _totalPage: Int = totalCount / perPage
                totalPage = if (totalCount % perPage > 0) _totalPage+1 else _totalPage
                theFirstTime = false
            }

            notifyDataSetChanged()
            page++
        }
//        mask?.let { mask?.dismiss() }
        Loading.hide(maskView)
        loading = false
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        for (i in 1..monthLastDay) {

            val day: Int = i
            val date: String = "%4d-%02d-%02d".format(year, month, i)
            //println(date)
            var rows: ArrayList<SuperCourse> = arrayListOf()
            if (superCourses != null) {
                rows = superCourses!!.rows
            }
            items.add(CalendarItem(this@CourseCalendarVC, date, rows))

        }

        return items
    }

    class CalendarItem(val context: Context, val date: String, val superCourses: ArrayList<SuperCourse>): Item() {

        override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

            val d: Date = date.toDate()!!
            val weekday_i = d.dateToWeekday()
            println(weekday_i)
            val weekday_c: String = d.dateToWeekdayForChinese()

            viewHolder.date.text = "%s(%s)".format(date, weekday_c)
        }

        override fun getLayout() = R.layout.course_calendar_item

    }


}