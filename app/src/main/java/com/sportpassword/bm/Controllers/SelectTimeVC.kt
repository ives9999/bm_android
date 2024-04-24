package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.extensions.noSec
import com.sportpassword.bm.functions.makeTimes
import kotlin.collections.ArrayList

class SelectTimeVC : SingleSelectVC() {

    var start: String = "07:00"
    var end: String = "23:00"

    var interval: Int = 30
    var allTimes: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        allTimes = makeTimes(start, end, interval)
        if (selected != null && selected!!.isNotEmpty()) {
            selected = selected!!.noSec()
            tableAdapter.selected = selected
        }

        tableRows = rowsBridge()
        tableAdapter.rows = tableRows

//        rowsBridge()
//        notifyChanged()
    }

    fun rowsBridge(): ArrayList<SelectRow> {

        val selectRows: ArrayList<SelectRow> = arrayListOf()

        for(time in allTimes) {
            selectRows.add(SelectRow(time, time))
        }

        return selectRows
    }
}