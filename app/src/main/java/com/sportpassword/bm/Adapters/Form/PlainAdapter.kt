package com.sportpassword.bm.Adapters.Form

import android.graphics.Color
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.formitem_more.*
import kotlinx.android.synthetic.main.formitem_plain.*
import kotlinx.android.synthetic.main.formitem_plain.detail
import kotlinx.android.synthetic.main.formitem_plain.title
import org.jetbrains.anko.backgroundColor

class PlainAdapter1(title: String, show: String): FormItemAdapter1("", "", title, "", show) {

    override fun getLayout(): Int {
        return R.layout.formitem_plain
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        super.bind(viewHolder, position)
        viewHolder.detail.text = show
    }
}

class PlainAdapter(formItem: FormItem): FormItemAdapter(formItem) {

    override fun getLayout(): Int {

        return R.layout.formitem_plain
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.title.text = formItem.title
        viewHolder.detail.text = formItem.show
        viewHolder.detail.backgroundColor = Color.TRANSPARENT
    }
}