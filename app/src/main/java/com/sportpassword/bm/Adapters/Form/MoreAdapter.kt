package com.sportpassword.bm.Adapters.Form

import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.ColorFormItem
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.IndexPath
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.formitem_more.*
import kotlinx.android.synthetic.main.formitem_more.clear
import kotlinx.android.synthetic.main.formitem_more.container
import kotlinx.android.synthetic.main.formitem_more.promptBtn
import kotlinx.android.synthetic.main.formitem_more.required
import kotlinx.android.synthetic.main.formitem_more.title
import kotlinx.android.synthetic.main.formitem_textfield.*
import org.jetbrains.anko.backgroundColor

class  MoreAdapter1(sectionKey: String, rowKey: String, title: String, value: String, keyboard: String = "default", delegate: BaseActivity?=null): FormItemAdapter1(sectionKey, rowKey, title, value,"", delegate) {

}


class MoreAdapter(formItem: FormItem, clearClick:(formItem: FormItem)->Unit, val rowClick:(formItem: FormItem)->Unit): FormItemAdapter(formItem, clearClick) {

    override fun getLayout(): Int {

        return R.layout.formitem_more
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.title.text = formItem.title
        if (formItem.value != null) {
            viewHolder.detail.visibility = View.VISIBLE
            viewHolder.detail.text = formItem.show
            viewHolder.clear.visibility = View.VISIBLE
            viewHolder.clear.setOnClickListener {
                formItem.reset()
                viewHolder.detail.text = ""
                clearClick(formItem)
            }
        } else {
            viewHolder.clear.visibility = View.INVISIBLE
            viewHolder.detail.visibility = View.INVISIBLE
        }
        if (formItem.tooltip != null) {
            viewHolder.promptBtn.visibility = View.VISIBLE
            viewHolder.promptBtn.setOnClickListener {
                promptClick(formItem)
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
        if (formItem.isRequired) {
            viewHolder.required.visibility = View.VISIBLE
        } else {
            viewHolder.required.visibility = View.INVISIBLE
        }

        viewHolder.detail.text = formItem.show
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
            rowClick(formItem)
        }
    }
}