package com.sportpassword.bm.Adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R

class TempPlayDateAdapter(val context: Context, val lists: ArrayList<String>, val itemClick: (Int) -> Unit): RecyclerView.Adapter<TempPlayDateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.title_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View, val itemClick: (Int)->Unit): RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.title)
        val lineView = itemView.findViewById<LinearLayout>(R.id.line)

        fun bind(position: Int) {
            if (position == lists.size-1) {
                lineView.visibility = View.INVISIBLE
            }
            val date = lists[position]
            titleView.text = date
            itemView.setOnClickListener { itemClick(position) }
        }
    }
}