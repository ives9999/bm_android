package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.Utilities.DEGREE
import com.sportpassword.bm.Utilities.DEGREE_KEY
import com.sportpassword.bm.Utilities.WEEKDAY
import com.sportpassword.bm.Utilities.WEEKDAY_KEY

class SelectWeekdaysVC : MultiSelectVC() {

    var selecteds1: Int = 0
    var selected_weekdays: ArrayList<WEEKDAY> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        key = WEEKDAY_KEY

        super.onCreate(savedInstanceState)

        setMyTitle("選擇星期幾")

        if (intent.hasExtra("selected1")) {
            selecteds1 = intent.getIntExtra("selected1", 0)
        }

        if (selecteds1 > 0) {
            var i: Int = 1
            while (i <= 7) {
                val n = (Math.pow(2.0, i.toDouble())).toInt()
                if ((selecteds1 and n) > 0) {
                    val w: WEEKDAY = WEEKDAY.from(i)
                    selected_weekdays.add(w)
                }
                i++
            }
        }

        val weekdays: ArrayList<WEEKDAY> = arrayListOf(
            WEEKDAY.mon, WEEKDAY.tue, WEEKDAY.wed, WEEKDAY.thu, WEEKDAY.fri, WEEKDAY.sat, WEEKDAY.sun
        )
        tableRows = rowsBridge(weekdays)
        tableAdapter.rows = tableRows
    }

    fun rowsBridge(weekdays: ArrayList<WEEKDAY>): ArrayList<SelectRow> {

        val selectRows: ArrayList<SelectRow> = arrayListOf()

        for(weekday in weekdays) {
            val title = WEEKDAY.intToString(weekday.value)
            val id = weekday.value

            var isSelected: Boolean = false
            for (selected in selected_weekdays) {
                if (selected == weekday) {
                    isSelected = true
                    break
                }
            }
            selectRows.add(SelectRow(title, id.toString(), isSelected))
        }

        return selectRows
    }

    override fun submit(view: View) {

        var value: Int = 0
        for (selected in selecteds) {
            value = value or (Math.pow(2.0, selected.toDouble())).toInt()
        }


        var show: String = ""
        val tmps: ArrayList<String> = arrayListOf()
        for (selected in selecteds) {
            tmps.add(WEEKDAY.from(selected.toInt()).enumToShortString())
        }
        if (tmps.size > 0) {
            show = tmps.joinToString(",")
        }

        val intent = Intent()
        intent.putExtra("key", key)
        intent.putExtra("able_type", able_type)
        //println(selecteds);
        intent.putExtra("selecteds", value)
        intent.putExtra("show", show)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}