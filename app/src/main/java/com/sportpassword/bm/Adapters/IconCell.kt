package com.sportpassword.bm.Adapters

import android.content.Context

class IconCell(val context: Context, val icon: String, val title: String, val content: String, val isPressed: Boolean=false) {

//    var delegate: IconCellDelegate? = null
//
//    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        viewHolder.icon.setImage(icon)
//        viewHolder.title.text = title + "："
//        viewHolder.content.text = content
//        viewHolder.itemView.setOnClickListener {
//            if (delegate != null) {
//                delegate!!.didSelectRowAt(it, position)
//            }
//        }
//
//        if (isPressed) {
//            val color = ContextCompat.getColor(context, R.color.Primary_300)
//            viewHolder.content.textColor = color
//        }
//    }
//
//    override fun getLayout() = R.layout.iconcell
}

//interface IconCellDelegate {
//
//    fun didSelectRowAt(view: View, position: Int)
//}