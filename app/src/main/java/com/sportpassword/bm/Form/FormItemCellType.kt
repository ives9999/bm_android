package com.sportpassword.bm.Form

import com.sportpassword.bm.R

enum class FormItemCellType {
    textField,
    weekday,
    time,
    color,
    status,
    more;

    fun registerCell(): Int {
        when (this) {
            textField -> return R.layout.formitem_textfield
            weekday -> return R.layout.formitem
            time -> return R.layout.formitem
            color -> return R.layout.formitem
            status -> return R.layout.formitem
            more -> return R.layout.formitem

            else -> return R.layout.formitem
        }
    }
}