package com.sportpassword.bm.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.selected
import com.sportpassword.bm.extensions.unSelected

class SingleSelectAdapter(var selected: String?, val list1CellDelegate: List1CellDelegate?): RecyclerView.Adapter<SingleSelectViewHolder>() {

    var rows: ArrayList<SelectRow> = arrayListOf()

    override fun onBindViewHolder(holder: SingleSelectViewHolder, position: Int) {

        val row: SelectRow = rows[position]
        holder.title.text = row.title

        if (selected == row.value) {
            holder.selected.visibility = View.VISIBLE
            holder.title.selected()
        } else {
            holder.selected.visibility = View.INVISIBLE
            holder.title.unSelected()
        }

        holder.viewHolder.setOnClickListener {
            list1CellDelegate?.cellClick(position)
        }
    }

    override fun getItemCount(): Int {
        return rows.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleSelectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.select_item, parent, false)

        return SingleSelectViewHolder(viewHolder)
    }
}

class SingleSelectViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    val title: TextView = viewHolder.findViewById(R.id.title)
    val selected: ImageView = viewHolder.findViewById(R.id.selected)
}