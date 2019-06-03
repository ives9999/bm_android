package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.CYCLE_UNIT
import com.sportpassword.bm.Utilities.CYCLE_UNIT_KEY

class CycleUnitFormItem: FormItem {

    var oldCycleUnit: CYCLE_UNIT? = null
    var cycleUnit: CYCLE_UNIT? = null

    init {
        uiProperties.cellType = FormItemCellType.more
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String = CYCLE_UNIT_KEY, title: String = "週期", tooltip: String? = null): super(name, title, "", null, tooltip) {
        reset()
    }

    override fun reset() {
        super.reset()
        value = null
        cycleUnit = null
        make()
    }

    override fun make() {
        valueToAnother()
        if (cycleUnit != null) {
            show = cycleUnit!!.value
            value = cycleUnit.toString()
            val tmp: ArrayList<CYCLE_UNIT> = arrayListOf(cycleUnit!!)
            sender = tmp
        } else {
            value = null
            show = ""
            sender = null
        }
    }

    override fun valueToAnother() {
        if (value != null) {
            cycleUnit = CYCLE_UNIT.from(value!!)
        }
    }
}