package com.sportpassword.bm.Form

import com.sportpassword.bm.Form.FormItem.PriceCycleUnitFormItem
import com.sportpassword.bm.Form.FormItem.SectionFormItem
import com.sportpassword.bm.Form.FormItem.TextFieldFormItem
import com.sportpassword.bm.Utilities.PRICE_CYCLE_UNIT_KEY
import com.sportpassword.bm.Utilities.PRICE_KEY
import com.sportpassword.bm.Utilities.TITLE_KEY
import com.sportpassword.bm.Utilities.YOUTUBE_KEY
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
        val priceCycleUnitItem = PriceCycleUnitFormItem()

        formItems = arrayListOf(section1,titleItem,youtubeItem,section2,priceItem,priceCycleUnitItem)

    }
}