package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.iconcell.*
import org.jetbrains.anko.textColor

class IconCell(val context: Context, val icon: String, val title: String, val content: String, val isPressed: Boolean=false): Item() {

    var delegate: IconCellDelegate? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.icon.setImage(icon)
        viewHolder.title.text = title + "："
        viewHolder.content.text = content
        viewHolder.itemView.setOnClickListener {
            if (delegate != null) {
                delegate!!.didSelectRowAt(it, position)
            }
        }

        if (isPressed) {
            val color = ContextCompat.getColor(context, R.color.MY_GREEN)
            viewHolder.content.textColor = color
        }
    }

    override fun getLayout() = R.layout.iconcell
}

interface IconCellDelegate {

    fun didSelectRowAt(view: View, position: Int)
}