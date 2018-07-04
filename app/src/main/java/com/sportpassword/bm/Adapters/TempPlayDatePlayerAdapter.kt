package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.Models.Row_TempPlayDatePlayer
import com.sportpassword.bm.Models.TempPlayDatePlayer
import com.sportpassword.bm.R

class TempPlayDatePlayerAdapter(val context: Context, val lists: ArrayList<Row_TempPlayDatePlayer>, val itemClick: (Int) -> Unit, val call:(String)->Unit): RecyclerView.Adapter<TempPlayDatePlayerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.title_subtitle_item, parent, false)
        return ViewHolder(view, itemClick, call)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View, val itemClick: (Int)->Unit, val call:(String)->Unit): RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.title)
        val subTitleView = itemView.findViewById<TextView>(R.id.subTitle)
        val lineView = itemView.findViewById<LinearLayout>(R.id.line)

        fun bind(position: Int) {
            if (position == lists.size-1) {
                lineView.visibility = View.INVISIBLE
            }
            titleView.text = lists[position].name
            val mobile: String = lists[position].mobile
            subTitleView.text = mobile
            subTitleView.setOnClickListener { call(mobile) }
            itemView.setOnClickListener { itemClick(position) }
        }
    }
}