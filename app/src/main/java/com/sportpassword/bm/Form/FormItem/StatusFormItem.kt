package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.STATUS
import com.sportpassword.bm.Utilities.TT_STATUS

class StatusFormItem: FormItem {

    var oldStatus: STATUS? = null
    var status: STATUS? = null

    init {
        uiProperties.cellType = FormItemCellType.status
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String = TT_STATUS, title: String = "狀態"): super(name, title) {
        reset()
    }

    override fun reset() {
        super.reset()
        status = STATUS.online
        make()
    }

    override fun make() {
        if (status != null) {
            show = status!!.value
            value = status!!.toString()
            sender = status
        } else {
            show = ""
            value = null
            sender = null
        }
    }

    override fun valueToAnother() {
        if (value != null) {
            status = STATUS.from(value!!)
        }
    }
}