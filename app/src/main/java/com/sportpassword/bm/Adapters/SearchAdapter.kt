package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R

interface inter {
    fun setP(section: Int, row: Int)
}

class SearchAdapter(val data: ArrayList<HashMap<String, ArrayList<String>>>, val itemClick: (Int) -> Unit): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

//    var sections: ArrayList<String> = arrayListOf()
    lateinit var rowAdatper: SearchRowAdatper
    var collapse: Boolean = true

    var delegate: inter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.search_section_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = data[position]
        holder.titleView.text = section.keys.elementAt(0)

        holder.rv.layoutManager = LinearLayoutManager(holder.rv.context, LinearLayout.VERTICAL, false)
        rowAdatper = SearchRowAdatper() { row ->
            delegate!!.setP(position, row)
        }
        if (position == 0) {
            rowAdatper.rows = section.values.elementAt(0)
            holder.titleView.height = 0
            holder.titleView.visibility = View.INVISIBLE
        } else {
            rowAdatper.rows = arrayListOf()
        }
        holder.rv.adapter = rowAdatper

        holder.bind(position)
    }


    inner class ViewHolder(itemView: View, val itemClick: (Int)->Unit): RecyclerView.ViewHolder(itemView) {

        val titleView = itemView.findViewById<TextView>(R.id.section_title)
        val rv = itemView.findViewById<RecyclerView>(R.id.row_container)

        fun bind(position: Int) {

            itemView.setOnClickListener{
                if (!collapse) {
                    rowAdatper.rows = arrayListOf()
                } else {
                    val section = data[position]
                    rowAdatper.rows = section.values.elementAt(0)
                }
//                rowAdatper.collapsed()
                rowAdatper.notifyDataSetChanged()
                collapse = !collapse
            }
        }
    }
}