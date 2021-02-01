package com.sportpassword.bm.Adapters.Form

import android.view.View
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_privacy.*
import kotlinx.android.synthetic.main.formitem_sex.required
import kotlinx.android.synthetic.main.formitem_sex.title

class PrivacyAdapter(formItem: FormItem, clearClick:(formItem: FormItem)->Unit, promptClick:(formItem: FormItem)->Unit): FormItemAdapter(formItem, clearClick, promptClick) {

    override fun getLayout(): Int {

        return R.layout.formitem_privacy
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.title.text = formItem.title

        if (formItem.isRequired) {
            viewHolder.required.visibility = View.VISIBLE
        } else {
            viewHolder.required.visibility = View.INVISIBLE
        }

        viewHolder.privacyBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            //println(isChecked)
            if (valueChangedDelegate != null) {
                valueChangedDelegate!!.privateChanged(isChecked)
            }
        }
    }
}