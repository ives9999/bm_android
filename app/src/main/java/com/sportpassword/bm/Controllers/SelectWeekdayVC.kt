package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Utilities.WEEKDAY
import com.sportpassword.bm.Utilities.WEEKDAY_KEY

class SelectWeekdayVC : SingleSelectVC() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weekdays: ArrayList<WEEKDAY> = arrayListOf(
            WEEKDAY.mon, WEEKDAY.tue, WEEKDAY.wed, WEEKDAY.thu, WEEKDAY.fri, WEEKDAY.sat, WEEKDAY.sun
        )

        key = WEEKDAY_KEY

        tableRows = rowsBridge(weekdays)
        tableAdapter.rows = tableRows

//        notifyChanged()
    }

    fun rowsBridge(weekdays: ArrayList<WEEKDAY>): ArrayList<SelectRow> {

        val selectRows: ArrayList<SelectRow> = arrayListOf()

        for(weekday in weekdays) {
            val title = WEEKDAY.intToString(weekday.value)
            val id = weekday.value
            selectRows.add(SelectRow(title, id.toString()))
        }

        return selectRows
    }

//    fun rowsBridge(weekdays: ArrayList<WEEKDAY>) {
//
//        if (rows.count() > 0) {
//            rows.clear()
//        } else {
//            rows = arrayListOf()
//        }
//        for(weekday in weekdays) {
//            val name = WEEKDAY.intToString(weekday.value)
//            val id = weekday.value
//            rows.add(hashMapOf("title" to name, "value" to id.toString()))
//        }
//    }
}