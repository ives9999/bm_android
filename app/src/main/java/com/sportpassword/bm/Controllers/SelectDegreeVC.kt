package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Utilities.DEGREE
import com.sportpassword.bm.Utilities.DEGREE_KEY
import com.sportpassword.bm.Utilities.Global

class SelectDegreeVC : MultiSelectVC1() {

    var rows1: ArrayList<DEGREE> = arrayListOf(DEGREE.high, DEGREE.soso, DEGREE.new)

    override fun onCreate(savedInstanceState: Bundle?) {

        key = DEGREE_KEY
        rowsBridge()
        setMyTitle("選擇程度")

        super.onCreate(savedInstanceState)
    }

    fun rowsBridge() {

        if (rows.count() > 0) {
            rows.clear()
        } else {
            rows = arrayListOf()
        }
        for(degree in rows1) {
            rows.add(hashMapOf("title" to degree.value, "value" to degree.toString()))
        }
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