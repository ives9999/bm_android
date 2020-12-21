package com.sportpassword.bm.Adapters.Form

import android.view.View
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_privacy.*
import kotlinx.android.synthetic.main.formitem_sex.required
import kotlinx.android.synthetic.main.formitem_sex.title

class PrivacyAdapter(form: BaseForm, idx: Int, indexPath: IndexPath, val click: (checked: Boolean)->Unit, clearClick:(idx: Int)->Unit, promptClick:(idx: Int)->Unit): FormItemAdapter(form, idx, indexPath, clearClick, promptClick) {

    override fun getLayout(): Int {

        return R.layout.formitem_privacy
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val formItem = form.formItems[position]
        viewHolder.title.text = formItem.title

        if (formItem.isRequired) {
            viewHolder.required.visibility = View.VISIBLE
        } else {
            viewHolder.required.visibility = View.INVISIBLE
        }

        viewHolder.privacyBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            //println(isChecked)
            click(isChecked)
        }
    }
}