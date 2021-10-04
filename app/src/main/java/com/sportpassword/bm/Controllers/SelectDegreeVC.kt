package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.Utilities.DEGREE
import com.sportpassword.bm.Utilities.DEGREE_KEY
import com.sportpassword.bm.Utilities.Global

class SelectDegreeVC : MultiSelectVC() {

    var rows1: ArrayList<DEGREE> = arrayListOf(DEGREE.high, DEGREE.soso, DEGREE.new)

    override fun onCreate(savedInstanceState: Bundle?) {

        key = DEGREE_KEY
        super.onCreate(savedInstanceState)

        setMyTitle("選擇程度")

        tableRows = rowsBridge()
        tableAdapter.rows = tableRows
//        notifyChanged()
    }

    fun rowsBridge(): ArrayList<SelectRow> {

        val selectRows: ArrayList<SelectRow> = arrayListOf()

        for(degree in rows1) {
            selectRows.add(SelectRow(degree.value, degree.toString()))
        }

        return selectRows
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