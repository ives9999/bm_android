package com.sportpassword.bm.Adapters

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.timetable_item.view.*

class TimeTableAdapter: BaseAdapter {

    var rows = arrayListOf<String>()
    var context: Context? = null

    constructor(context: Context, rows: ArrayList<String>): super() {
        this.context = context
        this.rows = rows
    }

    override fun getCount(): Int {
        return rows.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.timetable_item, parent, false)
        //val lp = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //view.layoutParams = lp
        //view.setBackgroundColor(Color.parseColor("#000000"))
        val lp = view.layoutParams
//        lp.width = 50
//        view.layoutParams = lp

        val s = rows[position]
        view.text.text = s
        return view
    }

    override fun getItem(position: Int): Any {
        return rows[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
















