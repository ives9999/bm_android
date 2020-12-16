package com.sportpassword.bm.Form.FormItem

import android.text.InputType
import com.sportpassword.bm.Form.FormItemCellType

open class TextFieldFormItem: FormItem {

    //var inputType: Int = InputType.TYPE_CLASS_TEXT

    init {
        uiProperties.cellType = FormItemCellType.textField
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null, isRequired: Boolean = false, tooltip: String? = null): super(name, title, placeholder, value, tooltip, isRequired) {
        reset()
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null, keyboardType: Int = InputType.TYPE_CLASS_TEXT, isRequired: Boolean = true, tooltip: String? = null): super(name, title, placeholder, value, tooltip) {
        this.isRequired = isRequired
        uiProperties.keyboardType = keyboardType
        //this.inputType = inputType
        reset()
    }

    override fun reset() {
        super.reset()
        make()
    }

    override fun make() {
        if (value != null) {
            show = value!!
        } else {
            show = ""
        }
    }
}