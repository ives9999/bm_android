package com.sportpassword.bm.Adapters.Form

import android.view.View
import android.widget.TextView
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_number.*
import java.lang.NumberFormatException

class  NumberAdapter1(sectionKey: String, rowKey: String, title: String, value: String, min: Int, max: Int, delegate: BaseActivity?=null): FormItemAdapter1(sectionKey, rowKey, title, value,"", delegate) {

    var number: Int = 1
    var min: Int = 1
    var max: Int = 5

    init {
        this.min = min
        this.max = max
        try {
            number = value.toInt()
        } catch (e: NumberFormatException) {}
    }

    override fun getLayout() = R.layout.formitem_number

    override fun bind(viewHolder: ViewHolder, position: Int) {

        super.bind(viewHolder, position)
        viewHolder.plus.setOnClickListener {
            if (!viewHolder.minus.isEnabled) {
                viewHolder.minus.isEnabled = true
            }
            number += 1
            if (number > max) {
                number = max
                it.isEnabled = false
            }
            updateNumber(viewHolder.numberLbl)
        }

        viewHolder.minus.setOnClickListener {
            if (!viewHolder.plus.isEnabled) {
                viewHolder.plus.isEnabled = true
            }
            number -= 1
            if (number < min) {
                number = min
                it.isEnabled = false
            }
            updateNumber(viewHolder.numberLbl)
        }
    }

    fun updateNumber(view: TextView) {
        view.text = number.toString()
        stepperValueChanged()
    }

    fun stepperValueChanged() {
        if (baseActivityDelegate != null) {
            baseActivityDelegate!!.stepperValueChanged(sectionKey, rowKey, number)
        }

    }
}

class NumberAdapter(formItem: FormItem, number: Int, min: Int, max: Int): FormItemAdapter(formItem) {

    var number: Int = 1
    var min: Int = 1
    var max: Int = 5

    init {
        this.number = number
        this.min = min
        this.max = max
    }

    override fun getLayout(): Int {
        return R.layout.formitem_number
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = formItem.title

        if (formItem.isRequired) {
            viewHolder.required.visibility = View.VISIBLE
        } else {
            viewHolder.required.visibility = View.INVISIBLE
        }

        viewHolder.plus.setOnClickListener {
            if (!viewHolder.minus.isEnabled) {
                viewHolder.minus.isEnabled = true
            }
            number += 1
            if (number > max) {
                number = max
                it.isEnabled = false
            }
            updateNumber(viewHolder.numberLbl)
        }

        viewHolder.minus.setOnClickListener {
            if (!viewHolder.plus.isEnabled) {
                viewHolder.plus.isEnabled = true
            }
            number -= 1
            if (number < min) {
                number = min
                it.isEnabled = false
            }
            updateNumber(viewHolder.numberLbl)
        }
    }

    fun updateNumber(view: TextView) {
        view.text = number.toString()
        stepperValueChanged()
    }

    fun stepperValueChanged() {
        if (valueChangedDelegate != null) {
            valueChangedDelegate!!.stepperValueChanged(number, formItem.name!!)
        }

    }
}