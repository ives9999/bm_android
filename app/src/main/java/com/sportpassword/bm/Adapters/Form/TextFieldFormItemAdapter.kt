package com.sportpassword.bm.Adapters.Form

import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.R
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class TextFieldFormItemAdapter(form: BaseForm, indexPath: HashMap<String, Int>): FormItemAdapter(form, indexPath) {

    override fun getLayout() = R.layout.formitem_textfield

    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)
    }
}