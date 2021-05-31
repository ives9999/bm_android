package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Utilities.WEEKDAY

class SelectWeekdayVC : SingleSelectVC1() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weekdays: ArrayList<WEEKDAY> = arrayListOf(
            WEEKDAY.mon, WEEKDAY.tue, WEEKDAY.wed, WEEKDAY.thu, WEEKDAY.fri, WEEKDAY.sat, WEEKDAY.sun
        )

        rowsBridge(weekdays)
        notifyChanged()
    }

    fun rowsBridge(weekdays: ArrayList<WEEKDAY>) {

        if (rows.count() > 0) {
            rows.clear()
        } else {
            rows = arrayListOf()
        }
        for(weekday in weekdays) {
            val name = WEEKDAY.intToString(weekday.value)
            val id = weekday.value
            rows.add(hashMapOf("title" to name, "value" to id.toString()))
        }
    }
}