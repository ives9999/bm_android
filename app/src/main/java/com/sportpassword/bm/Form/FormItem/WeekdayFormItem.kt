package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.Global
import com.sportpassword.bm.Utilities.WEEKDAY
import com.sportpassword.bm.Utilities.WEEKDAY_KEY

class WeekdayFormItem: FormItem {

    var oldWeekdays: ArrayList<Int> = arrayListOf()
    var weekdays: ArrayList<Int> = arrayListOf()

    init {
        uiProperties.cellType = FormItemCellType.weekday
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(title: String = "星期幾", name: String = WEEKDAY_KEY): super(name, title) {
        reset()
    }

    override fun reset() {
        super.reset()
        weekdays = arrayListOf()
        sender = arrayListOf<Int>()
    }

    override fun make() {
        //value is "18"
        if (value != null) {

            valueToAnother()
            var texts: ArrayList<String> = arrayListOf()
            var senders: ArrayList<String> = arrayListOf()
            if (weekdays.size > 0) {
                for (weekday in weekdays) {
//                for (gweekday in Global.weekdays) {
//                    if (weekday == gweekday["value"] as Int) {
//                        val text = gweekday["simple_text"] as String
//                        texts.add(text)
//                        break
//                    }
//                }
//                values.add(weekday.toString())
                    val text = WEEKDAY.intToString(weekday)
                    texts.add(text)
                    senders.add(weekday.toString())
                }
                show = texts.joinToString(",")
//                value = weekdays.joinToString(",")
                sender = senders //["1","2"]
            } else {
                show = ""
            }
        }
    }

    override fun valueToAnother() {
        if (value != null) {
            weekdays = Global.weekdaysParse(value!!.toInt())
        }
    }
}