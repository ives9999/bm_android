package com.sportpassword.bm.Form.FormItem

import android.text.InputType
import com.sportpassword.bm.Form.FormItemCellType

class TextFieldFormItem: FormItem {

    init {
        uiProperties.cellType = FormItemCellType.textField
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null, keyboardType: Int = InputType.TYPE_CLASS_TEXT, isRequired: Boolean = true): super(name, title, placeholder, value) {
        this.isRequired = isRequired
        uiProperties.keyboardType = keyboardType
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