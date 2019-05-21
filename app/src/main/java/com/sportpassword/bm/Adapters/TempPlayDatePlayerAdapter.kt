package com.sportpassword.bm.Adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.Models.TempPlayDatePlayer
import com.sportpassword.bm.R

class TempPlayDatePlayerAdapter(val context: Context, val lists: ArrayList<TempPlayDatePlayer>, val itemClick: (Int) -> Unit, val call:(String)->Unit): RecyclerView.Adapter<TempPlayDatePlayerAdapter.ViewHolder>() {

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

        val noView = itemView.findViewById<TextView>(R.id.no)
        val titleView = itemView.findViewById<TextView>(R.id.title)
        val subTitleView = itemView.findViewById<TextView>(R.id.subTitle)
        val lineView = itemView.findViewById<LinearLayout>(R.id.line)

        fun bind(position: Int) {
            if (position == lists.size-1) {
                lineView.visibility = View.INVISIBLE
            }
            noView.text = (position+1).toString() + "."
            var name = lists[position].name
            val status = lists[position].status
            if (status == "off") {
                name = name + "(取消)"
            }
            titleView.text = name
            val mobile: String = lists[position].mobile
            subTitleView.text = mobile
            subTitleView.setOnClickListener { call(mobile) }
            itemView.setOnClickListener { itemClick(position) }
        }
    }
}