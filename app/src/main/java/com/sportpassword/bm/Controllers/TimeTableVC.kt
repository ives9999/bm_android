package com.sportpassword.bm.Controllers

import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import com.sportpassword.bm.Adapters.TimeTableAdapter
import com.sportpassword.bm.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_time_table_vc.*
import kotlinx.android.synthetic.main.timetable_item.*

class TimeTableVC : BaseActivity() {

    var source: String = "coach"
    protected lateinit var adapter: TimeTableAdapter
    var rows: ArrayList<String> = arrayListOf()

    val startNum: Int = 6
    val endNum: Int = 23
    val columnNum: Int = 8
    val totalGridCount = (endNum-startNum)*columnNum

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_vc)

        source = intent.getStringExtra("source")
        setMyTitle("時刻表")
        prev.text = "<"
        next.text = ">"

//        adapter = GroupAdapter()
//        adapter.setOnItemClickListener { item, view ->
//
//        }
        for (i in 1..totalGridCount) {
            rows.add(i.toString())
        }
//        val adapter = ArrayAdapter(this, R.layout.timetable_item, rows)

//        val items = generateItems()
//        adapter.addAll(items)
        adapter = TimeTableAdapter(this, rows)
        TimeTableView.adapter = adapter
//        TimeTableView.onItemClickListener = object: AdapterView.OnItemClickListener{
//            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                println(p2)
//            }
//        }
    }

    fun generateItems(): ArrayList<Item> {
        var items: ArrayList<Item> = arrayListOf()
        for (i in 1..totalGridCount) {
            rows.add(i.toString())
            val row: Int = i / columnNum
            val column: Int = i % columnNum
            items.add(TimeTableItem(row, column, i.toString()))
        }

        return items
    }
}

class TimeTableItem(val row:Int,val column:Int,val text:String): Item() {
    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {
        viewHolder.text.text = text
    }

    override fun getLayout() = R.layout.timetable_item
}














