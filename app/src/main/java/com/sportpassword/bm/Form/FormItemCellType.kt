package com.sportpassword.bm.Form

import com.sportpassword.bm.R

enum class FormItemCellType {
    textField,
    weekday,
    date,
    time,
    color,
    status,
    content,
    more,
    section;

    fun registerCell(): Int {
        when (this) {
            textField -> return R.layout.formitem_textfield
            weekday -> return R.layout.formitem_more
            date -> return R.layout.formitem_more
            time -> return R.layout.formitem_more
            color -> return R.layout.formitem_more
            status -> return R.layout.formitem_more
            more -> return R.layout.formitem_more
            content -> return R.layout.formitem_content

            else -> return R.layout.formitem_more
        }
    }
}