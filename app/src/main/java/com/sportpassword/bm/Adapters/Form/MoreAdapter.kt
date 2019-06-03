package com.sportpassword.bm.Adapters.Form

import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.ColorFormItem
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.formitem_more.*
import org.jetbrains.anko.backgroundColor

class MoreAdapter(form: BaseForm, idx: Int, indexPath: IndexPath, clearClick:(idx: Int)->Unit, promptClick:(idx: Int)->Unit, val rowClick:(idx: Int)->Unit): FormItemAdapter(form, idx, indexPath, clearClick, promptClick) {

    override fun getLayout(): Int {

        return R.layout.formitem_more
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val formItem = form.formItems[position]
        if (formItem.title != null) {
            viewHolder.title.text = formItem.title
        }
        if (formItem.value != null) {
            viewHolder.detail.visibility = View.VISIBLE
            viewHolder.detail.text = formItem.show
            viewHolder.clear.visibility = View.VISIBLE
            viewHolder.clear.setOnClickListener {
                formItem.reset()
                viewHolder.detail.text = ""
                clearClick(idx)
            }
        } else {
            viewHolder.clear.visibility = View.INVISIBLE
            viewHolder.detail.visibility = View.INVISIBLE
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

        viewHolder.container.setOnClickListener {
            rowClick(idx)
        }
    }
}