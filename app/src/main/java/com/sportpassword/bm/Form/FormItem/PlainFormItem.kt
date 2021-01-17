package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.PRIVACY_KEY

class PlainFormItem(name: String, title: String, unit: String?=null, thousand_mark: Boolean=false, delegate: BaseActivity? = null): FormItem(name, title, "", null, null, false, delegate) {
    var unit: String? = null
    var thousand_mark: Boolean = false

    init {
        uiProperties.cellType = FormItemCellType.plain
        reset()
        this.unit = unit
        this.thousand_mark = thousand_mark
    }

    override fun reset() {
        super.reset()
        make()
    }

    override fun make() {
        if (value != null) {
            show = value!!
        } else {
            show = ""
        }
    }
}