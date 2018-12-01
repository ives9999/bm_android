package com.sportpassword.bm.Controllers

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.AppBarLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sportpassword.bm.Models.TimeTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.sportpassword.bm.Utilities.Loading
import kotlinx.android.synthetic.main.activity_time_table_vc.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk25.coroutines.onScrollChange
import org.jetbrains.anko.textColor

class TimeTableVC : BaseActivity() {

    var source: String = "coach"
    var token: String = "token"
//    protected lateinit var adapter: TimeTableAdapter
//    var rows: ArrayList<String> = arrayListOf()

    val startNum: Int = 6
    val endNum: Int = 23
    val columnNum: Int = 8
    val totalGridCount = (endNum-startNum)*columnNum

    var cellWidth: Float = 150f
    var cellHeight: Int = 150
    var cellBorderWidth: Int = 1

    val gridViews: ArrayList<ViewGroup> = arrayListOf()
    val eventViews: ArrayList<ViewGroup> = arrayListOf()

    lateinit var timeTable: TimeTable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_vc)

        source = intent.getStringExtra("source")
        token = intent.getStringExtra("token")
        dataService = CoachService

        setMyTitle("時刻表")
        prev.text = "<"
        next.text = ">"

        cellWidth = screenWidth.toFloat() / columnNum.toFloat()
        addGrid()

        refreshLayout = refresh
        setRefreshListener()
        refresh.isEnabled = false
        refresh()

        scroll.onScrollChange { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY <= 0) {
                refresh.isEnabled = true
            } else {
                refresh.isEnabled = false
            }
        }

        //adapter = TimeTableAdapter(this, rows, startNum, endNum, columnNum)
        //TimeTableView.adapter = adapter
    }


    override fun refresh() {
        super.refresh()
        Loading.show(mask)
        dataService.getTT(this, token, source) { success ->
            if (success) {
                timeTable = dataService.timeTable
                markEvent(container)
            } else {
                warning(dataService.msg)
            }
            closeRefresh()
            Loading.hide(mask)
        }
    }

    fun addGrid() {
        for (i in 0..totalGridCount-1) {
            val startTime: Int = i / columnNum + startNum
            val weekday: Int = i % columnNum

            val grid = generateView(cellWidth.toInt(), cellHeight, i)
            grid.setBackgroundResource(R.drawable.timetable_item_border)

            if (weekday == 0) {
                addTimeLabel(grid, startTime)
            }

            container.addView(grid)
            gridViews.add(grid)
            val c = ConstraintSet()
            c.clone(container)
            if (startTime == startNum) {
                c.connect(grid.id, ConstraintSet.TOP, container.id, ConstraintSet.TOP, 0)
                if (weekday == 0) {
                    c.connect(grid.id, ConstraintSet.LEFT, container.id, ConstraintSet.LEFT, 0)
                } else {
                    c.connect(grid.id, ConstraintSet.LEFT, gridViews[i-1].id, ConstraintSet.RIGHT, 0)
                }
            } else {
                c.connect(grid.id, ConstraintSet.TOP, gridViews[i-8].id, ConstraintSet.BOTTOM, 0)
                if (weekday == 0) {
                    c.connect(grid.id, ConstraintSet.LEFT, container.id, ConstraintSet.LEFT, 0)
                } else {
                    c.connect(grid.id, ConstraintSet.LEFT, gridViews[i-1].id, ConstraintSet.RIGHT, 0)
                }
            }
            c.applyTo(container)
        }
    }

    fun addTimeLabel(parent: ViewGroup, time: Int) {
        val t = TextView(this)
        t.id = View.generateViewId()
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        t.layoutParams = lp
        t.text = String.format("%d-%d", time, time+1)
        t.textColor = Color.WHITE
        t.gravity = Gravity.CENTER
        parent.addView(t)
    }

    fun markEvent(parent: ViewGroup) {
        for (i in 0..timeTable.rows.size-1) {
            val row = timeTable.rows[i]
            val hours = row._end - row._start
            val eventView = generateView(cellWidth.toInt()-2*cellBorderWidth, cellHeight*hours-2*cellBorderWidth, 100+i , row._color.toColor())
            parent.addView(eventView)
            eventViews.add(eventView)
            val c1 = ConstraintSet()
            c1.clone(container)
            val day = row.day
            val _start = row._start
            val idx = columnNum*(_start-startNum) + day
            c1.connect(eventView.id, ConstraintSet.TOP, gridViews[idx].id, ConstraintSet.TOP, cellBorderWidth)
            c1.connect(eventView.id, ConstraintSet.LEFT, gridViews[idx].id, ConstraintSet.LEFT, cellBorderWidth)
            c1.applyTo(container)
        }
    }

    protected fun generateView(width: Int, height: Int, tag: Any?=null, bColor: Int?=null): ViewGroup {
        val view = ConstraintLayout(this)
        if (tag != null) {
            view.tag = tag
        }
        view.id = View.generateViewId()
        val lp = LinearLayout.LayoutParams(width, height)
        view.layoutParams = lp
        if (bColor != null) {
            view.backgroundColor = bColor
        }
        view.setOnClickListener {
            clickEvent(it)
        }

        return view
    }

    protected fun clickEvent(view: View) {
        println(view.tag)
    }

//    fun generateItems(): ArrayList<Item> {
//        var items: ArrayList<Item> = arrayListOf()
//        for (i in 1..totalGridCount) {
//            rows.add(i.toString())
//            val row: Int = i / columnNum
//            val column: Int = i % columnNum
//            items.add(TimeTableItem(row, column, i.toString()))
//        }
//
//        return items
//    }
}

//class TimeTableItem(val row:Int,val column:Int,val text:String): Item() {
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {
//        viewHolder.text.text = text
//    }
//
//    override fun getLayout() = R.layout.timetable_item
//}














