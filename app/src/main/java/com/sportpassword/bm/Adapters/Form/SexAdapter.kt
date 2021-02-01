package com.sportpassword.bm.Adapters.Form

import android.view.View
import android.widget.RadioButton
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_sex.*

class SexAdapter(formItem: FormItem): FormItemAdapter(formItem) {

    var sex: String = "M"
    init {
        if (formItem.value != null) {
            sex = formItem.value!!
        }
    }
    override fun getLayout(): Int {

        return R.layout.formitem_sex
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.title.text = formItem.title

        if (formItem.isRequired) {
            viewHolder.required.visibility = View.VISIBLE
        } else {
            viewHolder.required.visibility = View.INVISIBLE
        }

        if (sex == "M") {
            viewHolder.male.isChecked = true
        } else {
            viewHolder.female.isChecked = true
        }

        viewHolder.sex.setOnCheckedChangeListener{ _, i ->
            if (valueChangedDelegate != null) {
                val radio = viewHolder.sex.findViewById<RadioButton>(i)
                sex = radio.tag.toString()
                formItem.value = sex
                formItem.make()
                valueChangedDelegate!!.sexChanged(sex)
            }
        }
    }
}








