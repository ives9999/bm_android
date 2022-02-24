package com.sportpassword.bm.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Data.MoreRow
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage

class MoreAdapter(val list1CellDelegate: List1CellDelegate?): RecyclerView.Adapter<MoreViewHolder1>() {

    var moreRow: ArrayList<MoreRow> = arrayListOf()

    override fun onBindViewHolder(holder: MoreViewHolder1, position: Int) {
        val row: MoreRow = moreRow[position]
        holder.title.text = row.title
        holder.icon.setImage(row.icon)
        holder.title.setTextColor(ContextCompat.getColor(holder.title.context, row.color))

        if (row.content.isNotEmpty()) {
            holder.content.text = row.content
        }

        holder.viewHolder.setOnClickListener {
            list1CellDelegate?.cellClick(position)
        }
    }

    override fun getItemCount(): Int {
        return moreRow.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreViewHolder1 {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.function_item, parent, false)

        return MoreViewHolder1(viewHolder)
    }
}

class MoreViewHolder1(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var title: TextView = viewHolder.findViewById(R.id.text)
    var icon: ImageView = viewHolder.findViewById(R.id.icon)
    var content: TextView = viewHolder.findViewById(R.id.content)
}