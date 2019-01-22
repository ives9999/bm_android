package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.SELECT_DATE_TYPE
import com.sportpassword.bm.Utilities.TT_START_TIME

class DateFormItem: FormItem {
    var dateType: SELECT_DATE_TYPE? = null

    init {
        uiProperties.cellType = FormItemCellType.date
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String = TT_START_TIME, title: String = "時間", dateType: SELECT_DATE_TYPE, tooltip: String? = null): super(name, title, "", null, tooltip) {
        this.dateType = dateType
        reset()
    }

    override fun reset() {
        super.reset()
        make()
    }

    override fun make() {
        if (value != null) {
            show = value!!
            sender = hashMapOf("type" to dateType!!, "date" to value!!)
        } else {
            sender = hashMapOf("type" to dateType!!, "date" to "")
        }
    }
}