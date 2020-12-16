package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.SELECT_DATE_TYPE
import com.sportpassword.bm.Utilities.START_DATE_KEY
import com.sportpassword.bm.Utilities.START_TIME_KEY

class DateFormItem: FormItem {
    var dateType: SELECT_DATE_TYPE? = null

    init {
        uiProperties.cellType = FormItemCellType.date
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
        this.dateType = SELECT_DATE_TYPE.start
    }

    constructor(name: String = START_DATE_KEY, title: String = "日期", dateType: SELECT_DATE_TYPE, tooltip: String? = null, isRequired: Boolean = true): super(name, title, "", null, tooltip) {
        this.isRequired = isRequired
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