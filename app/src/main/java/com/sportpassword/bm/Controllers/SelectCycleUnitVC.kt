package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Utilities.COURSE_KIND_KEY
import com.sportpassword.bm.Utilities.CYCLE_UNIT
import com.sportpassword.bm.Utilities.CYCLE_UNIT_KEY

class SelectCycleUnitVC : SingleSelectVC() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //rows = CYCLE_UNIT.makeSelect()
        key = CYCLE_UNIT_KEY

        //notifyChanged()
    }
}