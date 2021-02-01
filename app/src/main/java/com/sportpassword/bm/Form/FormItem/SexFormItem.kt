package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.SEX_KEY
import com.sportpassword.bm.Utilities.STATUS
import com.sportpassword.bm.Utilities.TT_STATUS

class SexFormItem(value: String = "M", isRequired: Boolean = false) : FormItem(SEX_KEY, "性別", null, value, null, isRequired) {

    //var oldSelected: String? = null
    //var selected: String = "M"

    init {
        uiProperties.cellType = FormItemCellType.sex
    }

    init {
        reset()
    }

    override fun reset() {
        super.reset()
        //selected = "M"
        make()
    }

    override fun make() {
        //value = selected
        sender = value
    }
}