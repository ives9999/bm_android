package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.Utilities.COURSE_KIND
import com.sportpassword.bm.Utilities.COURSE_KIND_KEY
import com.sportpassword.bm.Utilities.PRICE_UNIT_KEY

class SelectCourseKindVC : SingleSelectVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        key = COURSE_KIND_KEY
        val rows = COURSE_KIND.makeSelect()
        tableRows = rowsBridge(rows)
        tableAdapter.rows = tableRows
    }

    fun rowsBridge(rows: ArrayList<HashMap<String, String>>): ArrayList<SelectRow> {

        val selectRows: ArrayList<SelectRow> = arrayListOf()

        for(row in rows) {
            val title: String = row["title"]!!
            val id: String = row["value"]!!
            selectRows.add(SelectRow(title, id))
        }

        return selectRows
    }
}