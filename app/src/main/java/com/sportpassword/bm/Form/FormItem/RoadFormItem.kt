package com.sportpassword.bm.Form.FormItem

import android.text.InputType
import com.sportpassword.bm.Form.FormItemCellType

class RoadFormItem: TextFieldFormItem {

    init {
        uiProperties.cellType = FormItemCellType.road
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null, isRequired: Boolean = false, tooltip: String? = null): super(name, title, placeholder, value, isRequired, tooltip) {
        reset()
    }


    constructor(name: String, title: String, placeholder: String? = null, value: String? = null, keyboardType: Int = InputType.TYPE_CLASS_TEXT, isRequired: Boolean = true, tooltip: String? = null): super(name, title, placeholder, value, isRequired) {
        this.isRequired = isRequired
        uiProperties.keyboardType = keyboardType
        //this.inputType = inputType
        reset()
    }
}