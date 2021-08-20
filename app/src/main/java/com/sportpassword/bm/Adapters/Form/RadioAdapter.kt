package com.sportpassword.bm.Adapters.Form

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.formitem_radio.*

class RadioAdapter(context: Context, sectionKey: String, rows: ArrayList<HashMap<String, String>>, delegate: BaseActivity?=null): Item() {

    var context: Context? = null
    var sectionKey: String = ""
    var rows: ArrayList<HashMap<String, String>> = arrayListOf()
    var baseActivityDelegate: BaseActivity? = null

    init {
        this.context = context
        this.sectionKey = sectionKey
        this.rows = rows
        baseActivityDelegate = delegate
    }

    override fun getLayout(): Int {

        return R.layout.formitem_radio
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        //viewHolder.title.text = title
        //viewHolder.radio.isChecked = checked

        val textColor: Int = ContextCompat.getColor(context!!, R.color.MY_WHITE)
        val checkedColor: Int = ContextCompat.getColor(context!!, R.color.MY_RED)

        val colorStateList: ColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled), //disabled
                intArrayOf(android.R.attr.state_enabled)   //enabled
            ), intArrayOf(
                textColor, //disabled
                textColor  //enabled
            )
        )

        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        lp.setMargins(18, 24, 0, 24)

        val group = RadioGroup(context)

        val radioButtons: ArrayList<RadioButton> = arrayListOf()
        for (row in rows) {

            val radioButton: RadioButton = RadioButton(context)
            var title = ""
            if (row.containsKey("title")) {
                title = row["title"]!!
            }
            radioButton.text = title
            radioButton.buttonTintList = colorStateList
            radioButton.setTextColor(textColor)
            radioButton.textSize = 18F
            radioButton.layoutParams = lp

            val isChecked: Boolean = row["value"].toBoolean() ?: run {
                false
            }

            radioButton.isChecked = false
            group.addView(radioButton)

            radioButtons.add(radioButton)
        }

        for ((idx, row) in rows.withIndex()) {
            if (row["value"].toBoolean()) {
                radioButtons[idx].isChecked = true
            }
        }

        viewHolder.radioContainer.addView(group)

        if (baseActivityDelegate != null) {
            group.setOnCheckedChangeListener { radioGroup, i ->
                baseActivityDelegate!!.radioDidChange(sectionKey, i-1)
            }
        }
    }
}





















