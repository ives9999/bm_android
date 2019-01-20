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

        val eventStartDateItem = DateFormItem(TT_START_DATE, "開始日期", SELECT_DATE_TYPE.start)
        val eventEndDateItem = DateFormItem(TT_END_DATE, "結束日期", SELECT_DATE_TYPE.end)

        val eventStartTimeItem = TimeFormItem(TT_START_TIME, "開始時間", SELECT_TIME_TYPE.play_start)
        val eventEndTimeItem = TimeFormItem(TT_END_TIME, "結束時間", SELECT_TIME_TYPE.play_end)

        val eventChargeItem = TextFieldFormItem(TT_CHARGE, "費用", "", null, InputType.TYPE_CLASS_NUMBER)
        val eventLimitItem = TextFieldFormItem(TT_LIMIT, "限制人數", "無限制請填-1", null, InputType.TYPE_CLASS_NUMBER)

        val eventColorItem = ColorFormItem()

        val eventStatusItem = StatusFormItem()

        val eventContentItem = ContentFormItem(TT_CONTENT, "詳細內容", TEXT_INPUT_TYPE.timetable_coach)

        formItems = arrayListOf(eventTitleItem, eventWeekdayItem, eventStartDateItem, eventEndDateItem, eventStartTimeItem, eventEndTimeItem, eventChargeItem, eventLimitItem, eventColorItem, eventStatusItem, eventContentItem)
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
        var start_time: Date? = null
        var end_time: Date? = null
        for (formItem in formItems) {
            if (formItem.title == "開始時間") {
                start_time = formItem.value?.toDateTime("HH:mm")
            }
            if (formItem.title == "結束時間") {
                end_time = formItem.value?.toDateTime("HH:mm")
            }
        }
        if (start_time != null && end_time != null) {
            if (start_time!! > end_time!!) {
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