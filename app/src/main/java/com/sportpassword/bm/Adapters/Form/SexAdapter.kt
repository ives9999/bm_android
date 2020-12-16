package com.sportpassword.bm.Adapters.Form

import android.view.View
import android.widget.RadioButton
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_sex.*

class SexAdapter(form: BaseForm, idx: Int, indexPath: IndexPath, val click: (selected: String)->Unit, clearClick:(idx: Int)->Unit, promptClick:(idx: Int)->Unit): FormItemAdapter(form, idx, indexPath, clearClick, promptClick) {

    override fun getLayout(): Int {

        return R.layout.formitem_sex
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val formItem = form.formItems[position]
        viewHolder.title.text = formItem.title

        if (formItem.isRequired) {
            viewHolder.required.visibility = View.VISIBLE
        } else {
            viewHolder.required.visibility = View.INVISIBLE
        }

        viewHolder.sex.setOnCheckedChangeListener{ _, i ->
            val radio = viewHolder.sex.findViewById<RadioButton>(i)
            click(radio.tag.toString())
        }
    }
}








