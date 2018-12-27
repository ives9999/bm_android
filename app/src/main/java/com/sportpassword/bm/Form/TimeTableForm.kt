package com.sportpassword.bm.Form

import android.text.InputType
import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.Utilities.*
import java.util.*

class TimeTableForm: BaseForm {

    constructor(id: Int? = null, values: HashMap<String, String>? = null, title: String = ""): super(id, values, title) {
    }

    override fun configureItems() {

        val eventTitleItem = TextFieldFormItem(TT_TITLE, "標題", "請輸入標題")

        val eventWeekdayItem = WeekdayFormItem("星期幾", TT_WEEKDAY)

        val eventStartTimeItem = TimeFormItem(TT_START, "開始時間", SELECT_TIME_TYPE.play_start)

        val eventEndTimeItem = TimeFormItem(TT_END, "結束時間", SELECT_TIME_TYPE.play_end)

        val eventLimitItem = TextFieldFormItem(TT_LIMIT, "限制人數", "無限制請填-1", null, InputType.TYPE_CLASS_NUMBER)

        val eventColorItem = ColorFormItem()

        val eventStatusItem = StatusFormItem()

        val eventContentItem = ContentFormItem(TT_CONTENT, "詳細內容", TEXT_INPUT_TYPE.timetable_coach)

        formItems = arrayListOf(eventTitleItem, eventWeekdayItem, eventStartTimeItem, eventEndTimeItem, eventLimitItem, eventColorItem, eventStatusItem, eventContentItem)
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

//        var isValid1 = false
//        for (forItem in formItems) {
//            if (forItem.oldValue != null) {
//                if (forItem.isRequired && forItem.value != null) {
//                    if (forItem.value != forItem.oldValue) {
//                        isValid1 = true
//                        break
//                    }
//                }
//            }
//        }
//        if (!isValid1) {
//            return (isValid1, "沒有修改任何值，不用提交")
//        }

        return Pair(true, null)
    }
}