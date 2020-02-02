package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.olcell.*
import org.jetbrains.anko.textColor

class OlCell(val context: Context, val number: String, val name: String="", val isPressed: Boolean=false): Item() {

    var delegate: IconCellDelegate? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.number.text = number + "."
        viewHolder.name.text = name
        viewHolder.itemView.setOnClickListener {
            if (delegate != null) {
                delegate!!.didSelectRowAt(it, position)
            }
        }

        if (isPressed) {
            val color = ContextCompat.getColor(context, R.color.MY_GREEN)
            viewHolder.name.textColor = color
        }
    }

    override fun getLayout() = R.layout.olcell
}

interface OlCellDelegate {

    fun didSelectRowAt(view: View, position: Int)
}