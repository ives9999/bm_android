package com.sportpassword.bm.Adapters.Form

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.ColorFormItem
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Form.ValueChangedDelegate
import com.sportpassword.bm.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder

import kotlinx.android.synthetic.main.formitem_more.*
import kotlinx.android.synthetic.main.formitem_plain.*
import kotlinx.android.synthetic.main.formitem_textfield.*
import org.jetbrains.anko.backgroundColor
import kotlinx.android.synthetic.main.formitem_more.title as title
import kotlinx.android.synthetic.main.formitem_textfield.title as textfield_title
import kotlinx.android.synthetic.main.formitem_more.clear as clear
import kotlinx.android.synthetic.main.formitem_textfield.clear as textfield_clear
import kotlinx.android.synthetic.main.formitem_more.promptBtn as promptBtn
import kotlinx.android.synthetic.main.formitem_more.detail as detail
import kotlinx.android.synthetic.main.formitem_more.container as container

//interface TextFieldChangeDelegate {
//    fun textFieldTextChanged(indexPath: IndexPath, text: String)
//}
//
//interface SexChangeDelegate {
//    fun sexChanged(sex: String) {}
//}
//
//interface PrivacyChangeDelegate {
//    fun privateChanged(checked: Boolean) {}
//}

open class FormItemAdapter1(sectionKey: String, rowKey: String, title: String, value: String, show: String, delegate: BaseActivity?=null): Item() {

    var sectionKey: String = ""
    var rowKey: String = ""
    var title: String = ""
    var value: String = ""
    var show: String = ""
    var baseActivityDelegate: BaseActivity? = null

    init {
        this.sectionKey = sectionKey
        this.rowKey = rowKey
        this.title = title
        this.value = value
        this.show = show
        this.baseActivityDelegate = delegate
    }

    override fun getLayout(): Int {
        return R.layout.formitem_plain
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.title.text = title
    }
}

open class FormItemAdapter(formItem: FormItem, val clearClick:(formItem: FormItem)->Unit = {}, val promptClick:(formItem: FormItem)->Unit = {}): Item() {

    var valueChangedDelegate: ValueChangedDelegate? = null
//    var textFieldDelegate: TextFieldChangeDelegate? = null
//    var sexDelegate: SexChangeDelegate? = null
//    var privacyDelegate: PrivacyChangeDelegate? = null
    var formItem: FormItem = FormItem("", "")

    init {
        this.formItem = formItem
    }

    override fun getLayout(): Int {
        return formItem.uiProperties.cellType!!.registerCell()
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.title.text = formItem.title
        if (formItem.value != null) {
            viewHolder.clear.visibility = View.VISIBLE
            viewHolder.clear.setOnClickListener {
                clearClick(formItem)
            }
        } else {
            viewHolder.clear.visibility = View.INVISIBLE
        }
        if (formItem.tooltip != null) {
            viewHolder.promptBtn.visibility = View.VISIBLE
            viewHolder.promptBtn.setOnClickListener {
                promptClick(formItem)
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
                    valueChangedDelegate?.textFieldTextChanged(formItem, p0.toString())
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