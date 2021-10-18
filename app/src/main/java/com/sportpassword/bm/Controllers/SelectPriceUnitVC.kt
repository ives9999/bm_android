package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Utilities.PRICE_UNIT
import com.sportpassword.bm.Utilities.PRICE_UNIT_KEY

class SelectPriceUnitVC : SingleSelectVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //rows = PRICE_UNIT.makeSelect()
        key = PRICE_UNIT_KEY

        //notifyChanged()
    }
}