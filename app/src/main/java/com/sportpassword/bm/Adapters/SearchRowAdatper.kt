package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sportpassword.bm.R

class SearchRowAdatper(val itemClick: (Int) -> Unit): RecyclerView.Adapter<SearchRowAdatper.ViewHolder>() {

    var rows: ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.search_row_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return rows.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

//    fun collapsed() {
//        notifyItemRangeRemoved(0, rows.size)
//    }


    inner class ViewHolder(itemView: View, val itemClick: (Int)->Unit): RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.row_title)

        fun bind(position: Int) {
            titleView.text = rows[position]

            itemView.setOnClickListener{itemClick(position)}
        }
    }
}