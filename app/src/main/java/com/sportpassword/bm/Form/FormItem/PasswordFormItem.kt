package com.sportpassword.bm.Form.FormItem

import android.text.InputType
import com.sportpassword.bm.Form.FormItemCellType

class PasswordFormItem: TextFieldFormItem {

    init {
        uiProperties.cellType = FormItemCellType.password
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null):
            super(name, title, placeholder, value) {
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null, isRequired: Boolean = true, tooltip: String? = null):
            super(name, title, placeholder, value, InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD, isRequired, tooltip) {
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