package com.sportpassword.bm.Adapters.Form

import android.graphics.Color
import android.view.View
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.ColorFormItem
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_more.*
import org.jetbrains.anko.backgroundColor

class PlainAdapter(form: BaseForm, idx: Int, indexPath: IndexPath, clearClick:(idx: Int)->Unit, promptClick:(idx: Int)->Unit): FormItemAdapter(form, idx, indexPath, clearClick, promptClick) {

    override fun getLayout(): Int {

        return R.layout.formitem_plain
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val formItem = form.formItems[position]
        viewHolder.title.text = formItem.title
        viewHolder.detail.text = formItem.show
        viewHolder.detail.backgroundColor = Color.TRANSPARENT
    }
}