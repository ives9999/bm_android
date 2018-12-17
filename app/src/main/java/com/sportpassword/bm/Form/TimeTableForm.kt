package com.sportpassword.bm.Form

import com.sportpassword.bm.Form.FormItem.TextFieldFormItem
import com.sportpassword.bm.Form.FormItem.WeekdayFormItem
import com.sportpassword.bm.Utilities.TT_TITLE
import com.sportpassword.bm.Utilities.TT_WEEKDAY
import com.sportpassword.bm.Utilities.toDateTime
import java.util.*

class TimeTableForm: BaseForm {

    constructor(id: Int? = null, values: HashMap<String, String>? = null, title: String = ""): super(id, values, title) {
    }

    override fun configureItems() {

        val eventTitleItem = TextFieldFormItem(TT_TITLE, "標題", "請輸入標題")

        val eventWeekdayItem = WeekdayFormItem("星期幾", TT_WEEKDAY)
//
//        val eventStartTimeItem = TimeFormItem(name: TT_START, title: "開始時間", timeType: SELECT_TIME_TYPE.play_start)
//
//        val eventEndTimeItem = TimeFormItem(name: TT_END, title: "結束時間", timeType: SELECT_TIME_TYPE.play_end)
//
//        val eventLimitItem = TextFieldFormItemAdapter(name: TT_LIMIT, title: "限制人數", placeholder: "無限制請填-1", value: null, keyboardType: .numberPad)
//
//        val eventColorItem = ColorFormItem()
//
//        val eventStatusItem = StatusFormItem()
//
//        val eventContentItem = ContentFormItem(name: TT_CONTENT, title: "詳細內容", type: TEXT_INPUT_TYPE.timetable_coach)
//
//        formItems = [eventTitleItem, eventWeekdayItem, eventStartTimeItem, eventEndTimeItem, eventLimitItem, eventColorItem, eventStatusItem, eventContentItem]
        formItems = arrayListOf(eventTitleItem, eventWeekdayItem)
    }

    override fun fillValue() {
        super.fillValue()
    }

    override fun isValid(): Pair<Boolean, String?> {

        //check if empty
        val (isValid, msg) = super.isValid()
        if (!isValid) {
            return Pair(isValid, msg)
        }

        //check end early to start
        var start: Date? = null
        var end: Date? = null
        for (formItem in formItems) {
            if (formItem.title == "開始時間") {
                start = formItem.value?.toDateTime("HH:mm")
            }
            if (formItem.title == "結束時間") {
                end = formItem.value?.toDateTime("HH:mm")
            }
        }
        if (start != null && end != null) {
            if (start!! > end!!) {
                return Pair(isValid, "結束時間不能早於開始時間")
            }
        }

        var isValid1 = false
        for (forItem in formItems) {
            if (forItem.oldValue != null) {
                if (forItem.isRequired && forItem.value != null) {
                    if (forItem.value != forItem.oldValue) {
                        isValid1 = true
                        break
                    }
                }
            }
        }
        if (!isValid1) {
            //return (isValid1, "沒有修改任何值，不用提交")
        }

        return Pair(true, null)
    }
}