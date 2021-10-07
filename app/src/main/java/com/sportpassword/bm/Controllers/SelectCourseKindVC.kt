package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Utilities.COURSE_KIND
import com.sportpassword.bm.Utilities.COURSE_KIND_KEY
import com.sportpassword.bm.Utilities.PRICE_UNIT_KEY

class SelectCourseKindVC : SingleSelectVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rows = COURSE_KIND.makeSelect()
        key = COURSE_KIND_KEY

        //notifyChanged()
    }
}