package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.select_date_vc.*
import java.util.*
import android.widget.DatePicker

import android.widget.DatePicker.OnDateChangedListener




class SelectDateVC : BaseActivity() {

    lateinit var key: String
    var type: SELECT_DATE_TYPE = SELECT_DATE_TYPE.start
    var selected: String? = null
    var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_date_vc)

        if (intent.hasExtra("key")) {
            key = intent.getStringExtra("key")!!
        }
        if (key == null) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }
        //type = intent.getSerializableExtra("type") as SELECT_DATE_TYPE
        if (intent.hasExtra("selected")) {
            selected = intent.getStringExtra("selected")!!
        }
        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")!!
        } else {
            title = "日期"
        }

        setMyTitle(title!!)
        var date = Global.today().toDateTime("yyyy-MM-dd")
        if (selected != null && selected!!.isDate()) {
            date = selected!!.toDateTime("yyyy-MM-dd")
        }
        val yyyy: Int = date!!.getY()
        val MM: Int = date.getm() - 1
        val dd: Int = date.getd()
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.YEAR, yyyy);
//        calendar.set(Calendar.MONTH, MM);
//        calendar.set(Calendar.DAY_OF_MONTH, dd);
//        val milliTime = calendar.timeInMillis

        //datePicker.updateDate(yyyy, MM, dd)

//        datePicker.setDate(milliTime, true, true)
        if (selected == null || selected?.length == 0) {
            selected = "" + yyyy + "-" + (MM+1) + "-" + dd
        }

        val onDateChangeListener = MyOnDateChangeListener(this)
        datePicker.init(yyyy, MM, dd, onDateChangeListener)

//        datePicker.setOnDateChangedListener { _, yyyy1, MM1, dd1 ->
//            selected = "" + yyyy1 + "-" + (MM1+1) + "-" + dd1
//        }
//        datePicker.setOnDateChangeListener { calendarView, yyyy1, MM1, dd1 ->
//
//        }

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun dateOnChangListener(year: Int, month: Int, day: Int) {
        selected = "$year-$month-$day"
    }

    fun submitBtnPressed(v: View) {
        val intent = Intent()
        intent.putExtra("key", key)
        intent.putExtra("type", type)
        intent.putExtra("selected", selected)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

class MyOnDateChangeListener(val delegate: SelectDateVC) : OnDateChangedListener {
    override fun onDateChanged(view: DatePicker, year: Int, month: Int, day: Int) {
        val mon = month + 1
        //expirationDate.setText("$day/$mon/$year")

        delegate.dateOnChangListener(year, month + 1, day)
    }
}






















