package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Controllers.List2CellDelegate
import com.sportpassword.bm.Models.Table

open class MyAdapter2<T: MyViewHolder2<U>, U: Table>(private val resource: Int, private val viewHolderConstructor: (Context, View, List2CellDelegate<U>?)-> T, private val list2CellDelegate: List2CellDelegate<U>?=null): RecyclerView.Adapter<T>() {

    var items: ArrayList<U> = arrayListOf()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        val row: U = items[position]

        holder.bind(row, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val viewHolder: View = inflater.inflate(resource, parent, false)

        //return T(parent.context, viewHolder, list1CellDelegate)
        return viewHolderConstructor(parent.context, viewHolder, list2CellDelegate)
    }
}

open class MyViewHolder2<U: Table>(val context: Context, val viewHolder: View, private val list2CellDelegate: List2CellDelegate<U>? = null): RecyclerView.ViewHolder(viewHolder) {

    open fun bind(row: U, idx: Int) {

        viewHolder.setOnClickListener {
            list2CellDelegate?.cellClick(row)
        }
    }
}
