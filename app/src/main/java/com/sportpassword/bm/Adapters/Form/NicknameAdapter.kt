package com.sportpassword.bm.Adapters.Form

import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.R

class NicknameAdapter(formItem: FormItem, clearClick:(formItem: FormItem)->Unit, promptClick:(formItem: FormItem)->Unit): TextFieldAdapter(formItem, clearClick, promptClick) {

    override fun getLayout(): Int {

        return R.layout.formitem_nickname
    }
}