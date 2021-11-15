package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Utilities.PRICE_UNIT
import com.sportpassword.bm.Utilities.PRICE_UNIT_KEY

class SelectPriceUnitVC : SingleSelectVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        key = PRICE_UNIT_KEY
        val rows: ArrayList<HashMap<String, String>> = PRICE_UNIT.makeSelect()
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