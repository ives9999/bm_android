package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType

class SectionFormItem: FormItem {

    init {
        uiProperties.cellType = FormItemCellType.section
    }

    constructor(title: String): super("section", title, null, null) {
    }
}