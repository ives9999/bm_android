package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.didSelectClosure
import com.sportpassword.bm.Controllers.selectedClosure
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import org.jetbrains.anko.backgroundColor

open class MyAdapter2<T: MyViewHolder2<U>, U: Table> (
    private val resource: Int,
    private val viewHolderConstructor: (Context, View, didSelectClosure<U>, selectedClosure<U>)-> T,
    private val didSelect: didSelectClosure<U>,
    private val selected: selectedClosure<U> /* = ((U) -> kotlin.Boolean)? */
    ) : RecyclerView.Adapter<T>() {

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
        return viewHolderConstructor(parent.context, viewHolder, didSelect, selected)
    }
}

open class MyViewHolder2<U: Table>(
    val context: Context,
    val viewHolder: View,
    val didSelect: didSelectClosure<U>,
    val selected: selectedClosure<U>
    ) : RecyclerView.ViewHolder(viewHolder) {

    open fun bind(row: U, idx: Int) {

        viewHolder.setOnClickListener {
            didSelect?.let { it1 -> it1(row, idx) }
            //list2CellDelegate?.cellClick(row)
        }

        val isSelected = selected?.let { it(row) } == true
        if (isSelected) {
            val color: Int = ContextCompat.getColor(context, R.color.CELL_SELECTED)
            viewHolder.backgroundColor = color
        }
    }
}
