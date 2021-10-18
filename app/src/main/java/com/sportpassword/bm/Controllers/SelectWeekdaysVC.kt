package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Utilities.DEGREE
import com.sportpassword.bm.Utilities.DEGREE_KEY
import com.sportpassword.bm.Utilities.WEEKDAY

class SelectWeekdaysVC : MultiSelectVC() {

    override fun onCreate(savedInstanceState: Bundle?) {

        key = DEGREE_KEY

        super.onCreate(savedInstanceState)

        val weekdays: ArrayList<WEEKDAY> = arrayListOf(
            WEEKDAY.mon, WEEKDAY.tue, WEEKDAY.wed, WEEKDAY.thu, WEEKDAY.fri, WEEKDAY.sat, WEEKDAY.sun
        )
        rowsBridge(weekdays)
        //notifyChanged()
    }

    fun rowsBridge(weekdays: ArrayList<WEEKDAY>) {

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
    }

    override fun submit(view: View) {

        var show: String = ""
        val tmps: ArrayList<String> = arrayListOf()
        for (value in selecteds) {
            val tmp: String = DEGREE.fromEnglish(value).value
            tmps.add(tmp)
        }
        if (tmps.size > 0) {
            show = tmps.joinToString(",")
        }
        val intent = Intent()
        intent.putExtra("key", key)
        intent.putExtra("able_type", able_type)
        //println(selecteds);
        intent.putStringArrayListExtra("selecteds", selecteds)
        intent.putExtra("show", show)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}