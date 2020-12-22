package com.sportpassword.bm.Adapters.Form

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.ColorFormItem
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_more.*
import kotlinx.android.synthetic.main.formitem_textfield.*
import org.jetbrains.anko.backgroundColor
import kotlinx.android.synthetic.main.formitem_more.title as title
import kotlinx.android.synthetic.main.formitem_textfield.title as textfield_title
import kotlinx.android.synthetic.main.formitem_more.clear as clear
import kotlinx.android.synthetic.main.formitem_textfield.clear as textfield_clear
import kotlinx.android.synthetic.main.formitem_more.promptBtn as promptBtn
import kotlinx.android.synthetic.main.formitem_more.detail as detail
import kotlinx.android.synthetic.main.formitem_more.container as container

interface TextFieldChangeDelegate {
    fun textFieldTextChanged(indexPath: IndexPath, text: String)
}

interface SexChangeDelegate {
    fun sexChanged(sex: String) {}
}

interface PrivacyChangeDelegate {
    fun privateChanged(checked: Boolean) {}
}

open class FormItemAdapter(val form: BaseForm, val idx: Int, val indexPath: IndexPath, val clearClick:(idx: Int)->Unit, val promptClick:(idx: Int)->Unit): Item() {

    var textFieldDelegate: TextFieldChangeDelegate? = null
    var sexDelegate: SexChangeDelegate? = null
    var privacyDelegate: PrivacyChangeDelegate? = null

    override fun getLayout(): Int {
        val formItem = form.formItems[idx]

        return formItem.uiProperties.cellType!!.registerCell()
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val formItem = form.formItems[position]
        viewHolder.title.text = formItem.title
        if (formItem.value != null) {
            viewHolder.clear.visibility = View.VISIBLE
            viewHolder.clear.setOnClickListener {
                clearClick(idx)
            }
        } else {
            viewHolder.clear.visibility = View.INVISIBLE
        }
        if (formItem.tooltip != null) {
            viewHolder.promptBtn.visibility = View.VISIBLE
            viewHolder.promptBtn.setOnClickListener {
                promptClick(idx)
            }
        } else {
            viewHolder.promptBtn.visibility = View.INVISIBLE
//            val con =  ConstraintSet()
//            val l = viewHolder.containerView
//            con.clone(viewHolder.container)
//            con.connect(viewHolder.detail.id, ConstraintSet.LEFT, viewHolder.title.id, ConstraintSet.RIGHT, 8)
//            con.applyTo(viewHolder.container)
        }
        if (formItem.uiProperties.cellType == FormItemCellType.textField) {
            val textField = viewHolder.textField
            textField.hint = formItem.placeholder
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
                    textFieldDelegate?.textFieldTextChanged(_indexPath, p0.toString())
                }
            })
        }
        if (formItem.uiProperties.cellType == FormItemCellType.weekday || formItem.uiProperties.cellType == FormItemCellType.time || formItem.uiProperties.cellType == FormItemCellType.status || formItem.uiProperties.cellType == FormItemCellType.more || formItem.uiProperties.cellType == FormItemCellType.date) {
            viewHolder.detail.text = formItem.show
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