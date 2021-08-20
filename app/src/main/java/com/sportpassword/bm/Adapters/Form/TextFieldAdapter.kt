package com.sportpassword.bm.Adapters.Form

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.KEYBOARD
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.formitem_textfield.*
import com.xwray.groupie.kotlinandroidextensions.Item


class  TextFieldAdapter1(sectionKey: String, rowKey: String, title: String, value: String, keyboard: String = "default", delegate: BaseActivity?=null): FormItemAdapter1(sectionKey, rowKey, title, value,"", delegate) {

    var bFocus: Boolean = false
    var keyboard: String = "default"

    init {
        this.keyboard = keyboard
    }
    override fun getLayout(): Int {

        return R.layout.formitem_textfield
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.title.text = title

        val textField = viewHolder.textField
        textField.setText(value)
        textField.inputType = KEYBOARD.stringToSwift(keyboard)

        viewHolder.promptBtn.visibility = View.INVISIBLE

        textField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                bFocus = true
            }
        }

        textField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //bFocus = false
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //println("before:${formItem.name}: ${p0.toString()}")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                println("hasFocus:${bFocus}")
//                println("string:${p0.toString()}")
                if (baseActivityDelegate != null && bFocus) {
                    //println(p0.toString())
                    //formItem.value
                    //val _indexPath: IndexPath = IndexPath(indexPath.section, position)
                    //println("change:${formItem.name}: ${p0.toString()}")
                    baseActivityDelegate!!.textFieldTextChanged(sectionKey, rowKey, p0.toString())
                }
            }
        })

        viewHolder.clear.setOnClickListener {
            //clearClick(row)
            textField.setText("")
            if (baseActivityDelegate != null) {
                baseActivityDelegate!!.textFieldTextChanged(sectionKey, rowKey, "")
            }
        }
    }

}

open class TextFieldAdapter(formItem: FormItem, clearClick:(formItem: FormItem)->Unit, promptClick:(formItem: FormItem)->Unit): FormItemAdapter(formItem, clearClick, promptClick) {

    var bFocus: Boolean = false
    override fun getLayout(): Int {

        return R.layout.formitem_textfield
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        //val formItem: TextFieldFormItem = form.formItems[position] as TextFieldFormItem
//        if (formItem.name == MOBILE_KEY) {
//            println("adapt:${position}:${formItem.name}:${formItem.value}")
//        }
        viewHolder.title.text = formItem.title
        viewHolder.clear.setOnClickListener {
            //clearClick(row)
            viewHolder.textField.setText("")
            clearClick(formItem)
        }

        if (formItem.tooltip != null) {
            viewHolder.promptBtn.visibility = View.VISIBLE
            viewHolder.promptBtn.setOnClickListener {
                promptClick(formItem)
            }
        } else {
            viewHolder.promptBtn.visibility = View.INVISIBLE
        }
        val textField = viewHolder.textField
        textField.hint = formItem.placeholder
        if (formItem.value != null) {
            textField.setText(formItem.value)
        }

        if (formItem.isRequired) {
            viewHolder.required.visibility = View.VISIBLE
        } else {
            viewHolder.required.visibility = View.INVISIBLE
        }

        textField.inputType = formItem.uiProperties.keyboardType

        textField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                bFocus = true
            }
        }

        textField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //bFocus = false
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //println("before:${formItem.name}: ${p0.toString()}")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                println("hasFocus:${bFocus}")
//                println("string:${p0.toString()}")
                if (valueChangedDelegate != null && bFocus) {
                    //println(p0.toString())
                    //formItem.value
                    //val _indexPath: IndexPath = IndexPath(indexPath.section, position)
                    //println("change:${formItem.name}: ${p0.toString()}")
                    formItem.value = p0.toString()
                    formItem.make()
                    valueChangedDelegate?.textFieldTextChanged(formItem, p0.toString())
                }
            }
        })
    }
}