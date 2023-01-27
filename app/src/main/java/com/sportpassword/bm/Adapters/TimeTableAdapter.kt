package com.sportpassword.bm.Adapters

import android.content.Context
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.sportpassword.bm.R

class TimeTableAdapter: BaseAdapter {

    var rows = arrayListOf<String>()
    var context: Context? = null
    var startNum: Int = 6
    var endNum: Int = 23
    var columnNum: Int = 8

    constructor(context:Context,rows:ArrayList<String>,startNum:Int,endNum:Int,columnNum:Int): super() {
        this.context = context
        this.rows = rows
        this.startNum = startNum
        this.endNum = endNum
        this.columnNum = columnNum
    }

    override fun getCount(): Int {
        return rows.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.timetable_item, parent, false)

        val rowNum: Int = position / columnNum
        if (position % columnNum == 0) {
            val first = startNum + rowNum
            val second = first + 1
            //view.text.text = "%d-%d".format(first, second)
        } else {
            //view.text.text = ""
        }

        val startTime: Int = position / columnNum + startNum
        val weekday: Int = position % columnNum
        view.setOnClickListener {
            println("%d - %d".format(weekday, startTime))
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return rows[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
















