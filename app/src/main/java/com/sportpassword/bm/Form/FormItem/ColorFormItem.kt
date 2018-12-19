package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.MYCOLOR
import com.sportpassword.bm.Utilities.TT_COLOR

class ColorFormItem: FormItem {

    var oldColor: MYCOLOR? = null
    var color: MYCOLOR? = null

    init {
        uiProperties.cellType = FormItemCellType.color
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String = TT_COLOR, title: String = "顏色"): super(name, title) {
        reset()
    }

    override fun reset() {
        super.reset()
        color = null
        make()
    }

    override fun make() {
        if (color != null) {
            value = color?.toString()
            val tmp: ArrayList<MYCOLOR> = arrayListOf(color!!)
            sender = tmp
        } else {
            value = null
            show = ""
            sender = null
        }
    }

    override fun valueToAnother() {
        if (value != null) {
            color = MYCOLOR.from(value!!)
        }
    }
}