package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.Models.SuperCourses
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.xwray.groupie.GroupAdapter
import kotlinx.android.synthetic.main.course_calendar_item.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_course.*
import java.util.*
import kotlin.collections.ArrayList
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlin.collections.HashMap


class CourseCalendarVC: MyTableVC1() {

    var superCourses: SuperCourses? = null

    var y: Int = Date().getY()
    var m: Int = Date().getm()

    var calendarParams: HashMap<String, Any> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_course_calendar)

        val title = "會員報名課程列表"
        setMyTitle(title)

        dataService = CourseService
        calendarParams = makeCalendar()

        recyclerView = list_container
        refreshLayout = tab_refresh
        maskView = mask

        initAdapter()
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
    }

    override fun initAdapter(include_section: Boolean) {
        adapter = GroupAdapter()
        val items = generateItems()
        adapter.addAll(items)
        recyclerView.adapter = adapter
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
        for (i in 1..calendarParams["monthLastDay"] as Int) {

            val day: Int = i
            val date: String = "%4d-%02d-%02d".format(y, m, i)
//            println(date)
            var rows: ArrayList<SuperCourse> = arrayListOf()
            if (superCourses != null) {
                rows = superCourses!!.rows
            }
            val item = CourseCalendarItem(this@CourseCalendarVC, date, rows)
            items.add(item)
        }

        return items
    }

    fun makeCalendar(): HashMap<String, Any> {
        var res: HashMap<String, Any> = hashMapOf()
        //取得該月1號的星期幾，日曆才知道從星期幾開始顯示
        val weekday01 = "$y-$m-01"
        res["weekday01"] = weekday01
        var beginWeekday = weekday01.toDate()!!.dateToWeekday()
        if (beginWeekday == 0) { beginWeekday = 7 }
        res["beginWeekday"] = beginWeekday

        //取得該月最後一天的日期，30, 31或28，日曆才知道顯示到那一天
        val monthLastDay = Date().getMonthDays(y, m)
        res["monthLastDay"] = monthLastDay
        //dump($monthLastDay);
        res["monthLastDay_add_begin"] = monthLastDay + beginWeekday - 1;

        //取得該月最後一天的星期幾，計算月曆有幾列需要用到的數字
        var weekday31 = ""
        if (y == null && m == null) {
            y = Date().getY()
            m = Date().getm()
        }
        weekday31 = "$y-$m-$monthLastDay"
        res["weekday31"] = weekday31
        val endWeekday = weekday31.toDate()!!.dateToWeekday()
        res["endWeekday"] = endWeekday;

        //算出共需幾個日曆的格子
        val allMonthGrid = monthLastDay + (beginWeekday-1) + (7-endWeekday)

        //算出月曆列數，日曆才知道顯示幾列
        val monthRow = allMonthGrid/7;
        res["monthRow"] = monthRow;

        //建立下個月的連結
        var next_month = 0
        if (m == 12) { next_month = 1 } else { next_month = m+1 }
        var next_year = 0
        if (m == 12) { next_year = y+1 } else { next_year = y }
        res["next_year"] = next_year;
        res["next_month"] = next_month;

        return res
    }
}

class CourseCalendarItem(val context: Context, val date: String, val superCourses: ArrayList<SuperCourse>): Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val d: Date = date.toDate()!!
        val weekday_i = d.dateToWeekday()
        println(weekday_i)
        val weekday_c: String = d.dateToWeekdayForChinese()

        viewHolder.date.text = "%s(%s)".format(date, weekday_c)
    }

    override fun getLayout() = R.layout.course_calendar_item
}