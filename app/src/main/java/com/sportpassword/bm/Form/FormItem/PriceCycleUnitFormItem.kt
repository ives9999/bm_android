package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.PRICE_CYCLE_UNIT
import com.sportpassword.bm.Utilities.PRICE_CYCLE_UNIT_KEY

class PriceCycleUnitFormItem: FormItem {

    var oldPriceCycleUnit: PRICE_CYCLE_UNIT? = null
    var priceCycleUnit: PRICE_CYCLE_UNIT? = null

    init {
        uiProperties.cellType = FormItemCellType.more
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String = PRICE_CYCLE_UNIT_KEY, title: String = "收費週期", tooltip: String? = null): super(name, title, "", null, tooltip) {
        reset()
    }

    override fun reset() {
        super.reset()
        value = null
        priceCycleUnit = null
        make()
    }

    override fun make() {
        valueToAnother()
        if (priceCycleUnit != null) {
            show = priceCycleUnit!!.value
            value = priceCycleUnit.toString()
            val tmp: ArrayList<PRICE_CYCLE_UNIT> = arrayListOf(priceCycleUnit!!)
            sender = tmp
        } else {
            value = null
            show = ""
            sender = null
        }
    }

    override fun valueToAnother() {
        if (value != null) {
            priceCycleUnit = PRICE_CYCLE_UNIT.from(value!!)
        }
    }
}