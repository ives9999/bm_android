package com.sportpassword.bm.Form.FormItem

import android.text.InputType
import com.sportpassword.bm.Form.FormItemCellType

class PasswordFormItem(
        name: String,
        title: String,
        placeholder: String? = null,
        value: String? = null,
        isRequired: Boolean = true,
        tooltip: String? = null,
        re: Boolean = false,
        passwordFormItem: PasswordFormItem? = null):
        TextFieldFormItem(name, title, placeholder, value, android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD, isRequired, tooltip) {

    var re: Boolean = false
    var passwordFormItem: PasswordFormItem? = null

    init {
        uiProperties.cellType = FormItemCellType.password
        this.re = re
        this.passwordFormItem = passwordFormItem
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

    override fun checkValidity() {
        this.isValid = true
        if (this.re) {
            if (this.isRequired) {
                if (value != null && value!!.isNotEmpty()) {
                    if (this.passwordFormItem != null) {
                        if (this.value != passwordFormItem!!.value) {
                            isValid = false
                            msg = "密碼不符合"
                        }
                    }
                } else {
                    isValid = false
                    msg = "($title)沒有填寫欄位值"
                }
            }
        } else {
            super.checkValidity()
        }
    }
}























