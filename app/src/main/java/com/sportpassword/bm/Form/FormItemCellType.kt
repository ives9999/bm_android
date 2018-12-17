package com.sportpassword.bm.Form

import com.sportpassword.bm.R

enum class FormItemCellType {
    textField,
    weekday;

    fun registerCell(): Int {
        when (this) {
            textField -> return R.layout.formitem_textfield
            weekday -> return R.layout.formitem

            else -> return R.layout.formitem
        }
    }
}