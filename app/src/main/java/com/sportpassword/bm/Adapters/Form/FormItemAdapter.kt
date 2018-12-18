package com.sportpassword.bm.Adapters.Form

import android.view.View
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.TT_TITLE
import com.sportpassword.bm.Utilities.TT_WEEKDAY
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.edit_item.*
import kotlinx.android.synthetic.main.formitem_textfield.*
import kotlinx.android.synthetic.main.formitem.title as title
import kotlinx.android.synthetic.main.formitem_textfield.title as textfield_title
import kotlinx.android.synthetic.main.formitem.clear as clear
import kotlinx.android.synthetic.main.formitem_textfield.clear as textfield_clear

open class FormItemAdapter(val form: BaseForm, val row: Int, val section: Int = 0): Item() {


    override fun getLayout(): Int {
        val formItem = form.formItems[row]

        return formItem.uiProperties.cellType!!.registerCell()
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val formItem = form.formItems[position]
        if (formItem.title != null) {
            viewHolder.title.text = formItem.title
        }
        if (formItem.value != null) {
            viewHolder.clear.visibility = View.VISIBLE
        } else {
            viewHolder.clear.visibility = View.INVISIBLE
        }
        if (formItem.uiProperties.cellType == FormItemCellType.textField) {
            if (formItem.placeholder != null) {
                viewHolder.textField.hint = formItem.placeholder
            }
            if (formItem.value != null) {
                viewHolder.textField.setText(formItem.value)
            }
        }
        if (formItem.uiProperties.cellType == FormItemCellType.weekday) {
            if (formItem.show != null) {
                viewHolder.detail.text = formItem.show
            }
        }
    }
}