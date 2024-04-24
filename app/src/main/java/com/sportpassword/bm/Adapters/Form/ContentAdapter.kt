package com.sportpassword.bm.Adapters.Form

import com.sportpassword.bm.Form.FormItem.FormItem

class ContentAdapter(formItem: FormItem, clearClick:(formItem: FormItem)->Unit, val rowClick:(formItem: FormItem)->Unit): FormItemAdapter(formItem, clearClick, {}) {

//    override fun getLayout(): Int {
//
//        return R.layout.formitem_content
//    }

//    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//
//        viewHolder.title.text = formItem.title
//        if (formItem.value != null) {
//            viewHolder.clear.visibility = View.VISIBLE
//            viewHolder.clear.setOnClickListener {
//                viewHolder.detail.text = ""
//                clearClick(formItem)
//            }
//        } else {
//            viewHolder.clear.visibility = View.INVISIBLE
//        }
//        if (formItem.tooltip != null) {
//            viewHolder.promptBtn.visibility = View.VISIBLE
//            viewHolder.promptBtn.setOnClickListener {
//                promptClick(formItem)
//            }
//        } else {
//            viewHolder.promptBtn.visibility = View.INVISIBLE
////            if (formItem.show.length > 5) {
////                val con = ConstraintSet()
////                val l = viewHolder.container
////                con.clone(l)
////                con.connect(viewHolder.detail.id, ConstraintSet.START, viewHolder.title.id, ConstraintSet.END, 8)
////                con.applyTo(l)
////                viewHolder.detail.gravity = Gravity.LEFT
////            }
//        }
//
//        viewHolder.detail.text = formItem.show
//        viewHolder.detail.backgroundColor = Color.TRANSPARENT
//
//        viewHolder.container.setOnClickListener {
//            rowClick(formItem)
//        }
//    }
}