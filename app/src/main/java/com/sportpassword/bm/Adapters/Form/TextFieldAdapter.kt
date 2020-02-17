package com.sportpassword.bm.Adapters.Form

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_textfield.*

class TextFieldAdapter(form: BaseForm, idx: Int, indexPath: IndexPath, clearClick:(idx: Int)->Unit, promptClick:(idx: Int)->Unit): FormItemAdapter(form, idx, indexPath, clearClick, promptClick) {

    override fun getLayout(): Int {

        return R.layout.formitem_textfield
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val formItem = form.formItems[position]
        if (formItem.title != null) {
            viewHolder.title.text = formItem.title
        }
        viewHolder.clear.setOnClickListener {
            //clearClick(row)
            viewHolder.textField.setText("")
        }

        if (formItem.tooltip != null) {
            viewHolder.promptBtn.visibility = View.VISIBLE
            viewHolder.promptBtn.setOnClickListener {
                promptClick(idx)
            }
        } else {
            viewHolder.promptBtn.visibility = View.INVISIBLE
        }

        val textField = viewHolder.textField
        if (formItem.placeholder != null) {
            textField.hint = formItem.placeholder
        }
        if (formItem.value != null) {
            textField.setText(formItem.value)
        }
        textField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val _indexPath: IndexPath = IndexPath(indexPath.section, position)
                delegate?.textFieldTextChanged(_indexPath, p0.toString())
            }
        })
    }
}