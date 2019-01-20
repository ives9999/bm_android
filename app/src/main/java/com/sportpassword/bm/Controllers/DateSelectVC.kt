package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.date_select_vc.*
import java.util.*



class DateSelectVC : BaseActivity() {

    lateinit var key: String
    var type: SELECT_DATE_TYPE = SELECT_DATE_TYPE.start
    var selected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.date_select_vc)
        setMyTitle("日期")

        key = intent.getStringExtra("key")
        type = intent.getSerializableExtra("type") as SELECT_DATE_TYPE
        selected = intent.getStringExtra("selected")

        var date = Global.today().toDateTime("yyyy-MM-dd")
        if (selected!!.isDate()) {
            date = selected!!.toDateTime("yyyy-MM-dd")
        }
        val yyyy = date.gety()
        val MM = date.getM()
        val dd = date.getd()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, yyyy);
        calendar.set(Calendar.MONTH, MM);
        calendar.set(Calendar.DAY_OF_MONTH, dd);
        val milliTime = calendar.timeInMillis
        datePicker.setDate(milliTime, true, true)

        datePicker.setOnDateChangeListener { calendarView, yyyy, MM, dd ->
            selected = "" + yyyy + "-" + (MM+1) + "-" + dd
        }
    }

    fun submit(v: View) {
        val intent = Intent()
        intent.putExtra("key", key)
        intent.putExtra("type", type)
        intent.putExtra("selected", selected)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}






















