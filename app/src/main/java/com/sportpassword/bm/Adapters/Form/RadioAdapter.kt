package com.sportpassword.bm.Adapters.Form

import android.view.View
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.formitem_radio.*

class RadioAdapter(sectionKey: String, rowKey: String, title: String, checked: Boolean=false, delegate: BaseActivity?=null): Item() {

    var sectionKey: String = ""
    var rowKey: String = ""
    var title: String = ""
    var checked: Boolean = false
    var baseActivityDelegate: BaseActivity? = null

    init {
        this.sectionKey = sectionKey
        this.rowKey = rowKey
        this.title = title
        this.checked = checked
        baseActivityDelegate = delegate
    }

    override fun getLayout(): Int {

        return R.layout.formitem_radio
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.title.text = title
        viewHolder.radio.isChecked = checked

        if (baseActivityDelegate != null) {
            viewHolder.radio.setOnCheckedChangeListener { buttonView, isChecked ->
                //println(isChecked)
                baseActivityDelegate!!.radioDidChange(sectionKey, rowKey, isChecked)
            }
        }
    }
}





















