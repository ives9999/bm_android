package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.PRICE_UNIT
import com.sportpassword.bm.Utilities.PRICE_UNIT_KEY

class PriceUnitFormItem: FormItem {

    var oldPriceUnit: PRICE_UNIT? = null
    var priceUnit: PRICE_UNIT? = null

    init {
        uiProperties.cellType = FormItemCellType.more
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String = PRICE_UNIT_KEY, title: String = "收費週期", tooltip: String? = null): super(name, title, "", null, tooltip) {
        reset()
    }

    override fun reset() {
        super.reset()
        value = null
        priceUnit = null
        make()
    }

    override fun make() {
        valueToAnother()
        if (priceUnit != null) {
            show = priceUnit!!.value
            value = priceUnit.toString()
            sender = value
        } else {
            value = null
            show = ""
            sender = null
        }
    }

    override fun valueToAnother() {
        if (value != null) {
            priceUnit = PRICE_UNIT.from(value!!)
        }
    }
}