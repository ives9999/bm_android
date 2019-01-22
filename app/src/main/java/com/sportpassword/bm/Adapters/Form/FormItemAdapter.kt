package com.sportpassword.bm.Adapters.Form

import android.graphics.Color
import android.view.View
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.ColorFormItem
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.MYCOLOR
import com.sportpassword.bm.Utilities.TT_TITLE
import com.sportpassword.bm.Utilities.TT_WEEKDAY
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.edit_item.*
import kotlinx.android.synthetic.main.formitem_textfield.*
import org.jetbrains.anko.backgroundColor
import kotlinx.android.synthetic.main.formitem.title as title
import kotlinx.android.synthetic.main.formitem_textfield.title as textfield_title
import kotlinx.android.synthetic.main.formitem.clear as clear
import kotlinx.android.synthetic.main.formitem_textfield.clear as textfield_clear

open class FormItemAdapter(val form: BaseForm, val row: Int, val section: Int = 0, val clearClick:(idx: Int)->Unit): Item() {


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
            viewHolder.clear.setOnClickListener {
                clearClick(row)
            }
        } else {
            viewHolder.clear.visibility = View.INVISIBLE
        }
        if (formItem.tooltip != null) {
            viewHolder.promptBtn.visibility = View.VISIBLE
        } else {
            viewHolder.promptBtn.visibility = View.INVISIBLE
        }
        if (formItem.uiProperties.cellType == FormItemCellType.textField) {
            if (formItem.placeholder != null) {
                viewHolder.textField.hint = formItem.placeholder
            }
            if (formItem.value != null) {
                viewHolder.textField.setText(formItem.value)
            }
        }
        if (formItem.uiProperties.cellType == FormItemCellType.weekday || formItem.uiProperties.cellType == FormItemCellType.time || formItem.uiProperties.cellType == FormItemCellType.status || formItem.uiProperties.cellType == FormItemCellType.more || formItem.uiProperties.cellType == FormItemCellType.date) {
            if (formItem.show != null) {
                viewHolder.detail.text = formItem.show
            } else {
                viewHolder.detail.text = ""
            }
            viewHolder.detail.backgroundColor = Color.TRANSPARENT
        }

        if (formItem.uiProperties.cellType == FormItemCellType.color) {
            val _formItem = formItem as ColorFormItem
            if (_formItem.color != null) {
                viewHolder.detail.setBackgroundColor(_formItem.color!!.toColor())
                viewHolder.detail.text = "          "
            } else {
                viewHolder.detail.text = ""
                viewHolder.detail.backgroundColor = Color.TRANSPARENT
            }
        }

    }
}