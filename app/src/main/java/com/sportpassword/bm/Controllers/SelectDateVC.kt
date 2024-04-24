package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.sportpassword.bm.Utilities.*
import android.widget.DatePicker

import android.widget.DatePicker.OnDateChangedListener
import com.sportpassword.bm.databinding.SelectDateVcBinding
import com.sportpassword.bm.extensions.getY
import com.sportpassword.bm.extensions.getd
import com.sportpassword.bm.extensions.getm
import com.sportpassword.bm.extensions.isDate
import com.sportpassword.bm.extensions.toDateTime
import com.sportpassword.bm.functions.today


class SelectDateVC : BaseActivity() {

    private lateinit var binding: SelectDateVcBinding
    private lateinit var view: ViewGroup

    lateinit var key: String
    var type: SELECT_DATE_TYPE = SELECT_DATE_TYPE.start
    var selected: String? = null
    var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SelectDateVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

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
        var date = today().toDateTime("yyyy-MM-dd")
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
        binding.datePicker.init(yyyy, MM, dd, onDateChangeListener)

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






















