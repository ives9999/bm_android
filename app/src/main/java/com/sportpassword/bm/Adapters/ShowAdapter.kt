package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.R

class ShowAdapter(val context: Context): RecyclerView.Adapter<ShowViewHolder>() {

    var rows: ArrayList<ShowRow> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.iconcell, parent, false)

        return ShowViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {

        val row: ShowRow = rows[position]
        var iconID: Int = 0
        iconID = context.resources.getIdentifier(row.icon, "drawable", context.packageName)
        holder.icon.setImageResource(iconID)
        holder.title.text = row.title
        holder.show.text = row.show
    }

    override fun getItemCount(): Int {
        return rows.size
    }

}

class ShowViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var icon: ImageView = viewHolder.findViewById(R.id.icon)
    var title: TextView = viewHolder.findViewById(R.id.title)
    var show: TextView = viewHolder.findViewById(R.id.content)
}