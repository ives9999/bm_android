package com.sportpassword.bm.Form

import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.Utilities.*
import java.util.HashMap

class CourseForm: BaseForm {

    constructor(id: Int? = null, values: HashMap<String, String>? = null, title: String = ""): super(id, values, title, true) {
    }

    override fun configureItems() {

        val section1 = SectionFormItem("一般")
        val titleItem = TextFieldFormItem(TITLE_KEY, "標題", "請輸入標題")
        val youtubeItem = TextFieldFormItem(YOUTUBE_KEY, "youtube代碼", "請輸入youtube影片代碼")
        val section2 = SectionFormItem("收費")
        val priceItem = TextFieldFormItem(PRICE_KEY, "收費標準", "請輸入收費費用")
        val priceUnitItem = PriceUnitFormItem()
        val priceDescItem = TextFieldFormItem(PRICE_DESC_KEY, "補充說明", "收費標準的補充說明")
        val section3 = SectionFormItem("課程")
        val courseKindItem = CourseKindFormItem()
        val cycleUnitItem = CycleUnitFormItem()
        val weekdayFormItem = WeekdayFormItem()
        val startTimeFormItem = TimeFormItem(START_TIME_KEY, "開始時間", SELECT_TIME_TYPE.play_start, false)
        val endTimeFormItem = TimeFormItem(END_TIME_KEY, "結束時間", SELECT_TIME_TYPE.play_end, false)
        val contentFormItem = ContentFormItem(CONTENT_KEY, "詳細介紹", TEXT_INPUT_TYPE.content)

        formItems = arrayListOf(section1,titleItem,youtubeItem,section2,priceItem,priceUnitItem,priceDescItem,section3,courseKindItem,cycleUnitItem,weekdayFormItem,startTimeFormItem,endTimeFormItem,contentFormItem)

    }
}