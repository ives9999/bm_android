package com.sportpassword.bm.Form

import com.sportpassword.bm.R

enum class FormItemCellType {
    textField,
    title,
    nickname,
    email,
    tel,
    mobile,
    road,
    fb,
    line,
    weekday,
    date,
    time,
    color,
    status,
    content,
    more,
    password,
    sex,
    city,
    area,
    privacy,
    plain,
    tag,
    clothesSize,
    number,
    weight,
    section;

    fun registerCell(): Int {
        when (this) {
            textField -> return R.layout.formitem_textfield
            title -> return R.layout.formitem_title
            nickname -> return R.layout.formitem_nickname
            email -> return R.layout.formitem_email
            tel -> return R.layout.formitem_tel
            mobile -> return R.layout.formitem_mobile
            road -> return R.layout.formitem_road
            fb -> return R.layout.formitem_fb
            line -> return R.layout.formitem_line
            weekday -> return R.layout.formitem_more
            date -> return R.layout.formitem_more
            time -> return R.layout.formitem_more
            color -> return R.layout.formitem_more
            status -> return R.layout.formitem_more
            more -> return R.layout.formitem_more
            content -> return R.layout.formitem_content
            password -> return R.layout.formitem_password
            sex -> return  R.layout.formitem_sex
            city -> return R.layout.formitem_more
            area -> return R.layout.formitem_more
            privacy -> return R.layout.formitem_privacy
            plain -> return R.layout.formitem_plain
            tag -> return R.layout.formitem_tag
            clothesSize -> return R.layout.formitem_tag
            weight -> return R.layout.formitem_tag
            number -> return R.layout.formitem_number

            else -> return R.layout.formitem_more
        }
    }
}