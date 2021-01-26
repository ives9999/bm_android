package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.NUMBER_KEY

class NumberFormItem(name: String = NUMBER_KEY, title: String = "數量", isRequire: Boolean = false, min: Int = 1, max: Int = 5): FormItem(name, title, null, null, null, isRequire) {

    var min: Int = 1
    var max: Int = 5
    init {
        uiProperties.cellType = FormItemCellType.number
        this.min = min
        this.max = max
        reset()
    }

    override fun reset() {
        super.reset()
        this.value = "1"
        make()
    }
}