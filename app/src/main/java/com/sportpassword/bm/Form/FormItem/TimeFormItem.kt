package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.SELECT_TIME_TYPE
import com.sportpassword.bm.Utilities.START_TIME_KEY
import com.sportpassword.bm.extensions.noSec

class TimeFormItem: FormItem {

    var timeType: SELECT_TIME_TYPE? = null

    init {
        uiProperties.cellType = FormItemCellType.time
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String = START_TIME_KEY, title: String = "時間", timeType: SELECT_TIME_TYPE, isRequired: Boolean = true): super(name, title) {
        this.isRequired = isRequired
        this.timeType = timeType
        reset()
    }

    override fun reset() {
        super.reset()
        make()
    }

    override fun make() {
        if (value != null) {
            show = value!!.noSec()
            sender = hashMapOf("type" to timeType!!, "time" to value!!)
        } else {
            sender = hashMapOf("type" to timeType!!, "time" to "")
        }
    }
}