package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sportpassword.bm.Models.Data
import com.sportpassword.bm.R

/**
 * Created by ives on 2018/2/23.
 */
class ListAdapter(val context: Context, val lists: ArrayList<Data>, val itemClick: (Data) -> Unit): RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder?, position: Int) {
        holder?.bind(lists[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tab_list_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    inner class ViewHolder(itemView: View, val itemClick: (Data) -> Unit): RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.listTitleTxt)

        fun bind(data: Data) {
            nameView.text = data.title
            itemView.setOnClickListener{itemClick(data)}
        }
    }

}