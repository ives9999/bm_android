package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.COURSE_KIND
import com.sportpassword.bm.Utilities.COURSE_KIND_KEY

class CourseKindFormItem: FormItem {

    var oldCourseKind: COURSE_KIND? = null
    var courseKind: COURSE_KIND? = null

    init {
        uiProperties.cellType = FormItemCellType.more
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String = COURSE_KIND_KEY, title: String = "課程週期", tooltip: String? = null): super(name, title, "", null, tooltip) {
        reset()
    }

    override fun reset() {
        super.reset()
        value = null
        courseKind = null
        make()
    }

    override fun make() {
        valueToAnother()
        if (courseKind != null) {
            show = courseKind!!.value
            value = courseKind.toString()
            sender = value
        } else {
            value = null
            show = ""
            sender = null
        }
    }

    override fun valueToAnother() {
        if (value != null) {
            courseKind = COURSE_KIND.from(value!!)
        }
    }
}