package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Utilities.Global
import kotlin.collections.ArrayList

class SelectTimeVC : SingleSelectVC1() {

    var start: String = "07:00"
    var end: String = "23:00"

    var interval: Int = 30
    var allTimes: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        allTimes = Global.makeTimes(start, end, interval)

        rowsBridge()
        notifyChanged()
    }

    fun rowsBridge() {

        if (rows.count() > 0) {
            rows.clear()
        } else {
            rows = arrayListOf()
        }
        for(time in allTimes) {
            rows.add(hashMapOf("title" to time, "value" to time))
        }
    }
}