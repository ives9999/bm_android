package com.sportpassword.bm.Adapters.Form

import android.graphics.Color
import android.view.View
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.R
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_content.*
import org.jetbrains.anko.backgroundColor

class ContentAdapter(form: BaseForm, idx: Int, section: Int = 0, clearClick:(idx: Int)->Unit, promptClick:(idx: Int)->Unit): FormItemAdapter(form, idx, section, clearClick, promptClick) {

    override fun getLayout(): Int {

        return R.layout.formitem_content
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val formItem = form.formItems[position]

        if (formItem.title != null) {
            viewHolder.title.text = formItem.title
        }
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
//            if (formItem.show.length > 5) {
//                val con = ConstraintSet()
//                val l = viewHolder.container
//                con.clone(l)
//                con.connect(viewHolder.detail.id, ConstraintSet.START, viewHolder.title.id, ConstraintSet.END, 8)
//                con.applyTo(l)
//                viewHolder.detail.gravity = Gravity.LEFT
//            }
        }

        if (formItem.show != null) {
            viewHolder.detail.text = formItem.show
        } else {
            viewHolder.detail.text = ""
        }
        viewHolder.detail.backgroundColor = Color.TRANSPARENT
    }
}