package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.Global
import com.sportpassword.bm.Utilities.TT_WEEKDAY

class WeekdayFormItem: FormItem {

    var oldWeekdays: ArrayList<Int> = arrayListOf()
    var weekdays: ArrayList<Int> = arrayListOf()

    init {
        uiProperties.cellType = FormItemCellType.weekday
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(title: String = "星期幾", name: String = TT_WEEKDAY): super(name, title) {
        reset()
    }

    override fun reset() {
        super.reset()
        weekdays = arrayListOf()
        sender = arrayListOf<Int>()
    }

    override fun make() {
        var texts: ArrayList<String> = arrayListOf()
        var values: ArrayList<String> = arrayListOf()
        if (weekdays.size > 0) {
            for (weekday in weekdays) {
                for (gweekday in Global.weekdays) {
                    if (weekday == gweekday["value"] as Int) {
                        val text = gweekday["simple_text"] as String
                        texts.add(text)
                        break
                    }
                }
                values.add(weekday.toString())
            }
            show = texts.joinToString(",")
            value = values.joinToString(",")
            sender = weekdays
        } else {
            show = ""
        }
    }

    override fun valueToAnother() {
        if (value != null) {
            val values: ArrayList<String> = ArrayList(value!!.split(","))
            weekdays.clear()
            for (v in values) {
                weekdays.add(v.toInt())
            }
        }
    }
}