package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.SEX_KEY
import com.sportpassword.bm.Utilities.STATUS
import com.sportpassword.bm.Utilities.TT_STATUS

class SexFormItem: FormItem {

    var oldSelected: String? = null
    var selected: String = "M"

    init {
        uiProperties.cellType = FormItemCellType.sex
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String = SEX_KEY, value: String = "M", isRequired: Boolean = false): super(name, "性別", null, value, null, isRequired) {
        reset()
    }

    override fun reset() {
        super.reset()
        selected = "M"
        make()
    }

    override fun make() {
        value = selected
        sender = selected
    }
}