package com.sportpassword.bm.Controllers

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.activity_time_table_vc.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

class TimeTableVC : BaseActivity() {

    var source: String = "coach"
//    protected lateinit var adapter: TimeTableAdapter
//    var rows: ArrayList<String> = arrayListOf()

    val startNum: Int = 6
    val endNum: Int = 23
    val columnNum: Int = 8
    val totalGridCount = (endNum-startNum)*columnNum

    var cellWidth: Float = 150f
    var cellHeight: Int = 150

    val gridViews: ArrayList<ConstraintLayout> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_vc)

        source = intent.getStringExtra("source")
        setMyTitle("時刻表")
        prev.text = "<"
        next.text = ">"

//        for (i in 1..totalGridCount) {
//            rows.add(i.toString())
//        }
        cellWidth = screenWidth.toFloat() / columnNum.toFloat()
        addGrid()

        markEvent(container)
        //adapter = TimeTableAdapter(this, rows, startNum, endNum, columnNum)
        //TimeTableView.adapter = adapter
    }

    fun addGrid() {
        for (i in 0..totalGridCount-1) {
            val startTime: Int = i / columnNum + startNum
            val weekday: Int = i % columnNum

            val grid = ConstraintLayout(this)
            grid.tag = i
            grid.id = View.generateViewId()
            val lp = ConstraintLayout.LayoutParams(cellWidth.toInt(), cellHeight)
            grid.layoutParams = lp
            grid.setBackgroundResource(R.drawable.timetable_item_border)
            grid.setOnClickListener {
                val tag: Int = it.tag as Int
                //println(it.tag)
            }

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
        val evantView = ConstraintLayout(this)
        evantView.id = View.generateViewId()
        val lp = LinearLayout.LayoutParams(cellWidth.toInt(), cellHeight*2)
        evantView.layoutParams = lp
        evantView.backgroundColor = Color.RED
        evantView.setOnClickListener {
            println(it.tag)
        }
        parent.addView(evantView)
        val c1 = ConstraintSet()
        c1.clone(container)
        c1.connect(evantView.id, ConstraintSet.TOP, gridViews[17].id, ConstraintSet.TOP, 0)
        c1.connect(evantView.id, ConstraintSet.LEFT, gridViews[17].id, ConstraintSet.LEFT, 0)
        c1.applyTo(container)
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














