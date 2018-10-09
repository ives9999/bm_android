package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage

interface inter {
    fun setP(section: Int, row: Int)
}

class SearchAdapter(): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var data: ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> = arrayListOf()
    lateinit var rowAdatper: SearchRowAdatper
    var collapse: Boolean = true

    var delegate: inter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.search_section_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = data[position]
        holder.titleView.text = section.keys.elementAt(0)

        holder.rv.layoutManager = LinearLayoutManager(holder.rv.context, LinearLayout.VERTICAL, false)
        rowAdatper = SearchRowAdatper(position) { row ->
            delegate!!.setP(position, row)
        }
        if (position == 0) {
            rowAdatper.rows = section.values.elementAt(0)
            holder.sectionView.visibility = View.GONE
        } else {
            rowAdatper.rows = arrayListOf()
        }
        holder.rv.adapter = rowAdatper

        holder.bind(position)
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var titleView = itemView.findViewById<TextView>(R.id.section_title)
        var collapseView = itemView.findViewById<ImageView>(R.id.collapse)
        var sectionView = itemView.findViewById<ConstraintLayout>(R.id.section_container)
        var rv = itemView.findViewById<RecyclerView>(R.id.row_container)

        fun bind(position: Int) {

            itemView.setOnClickListener{
                if (!collapse) {
                    rowAdatper.rows = arrayListOf()
                    collapseView.setImage("to_right")
                } else {
                    val section = data[position]
                    rowAdatper.rows = section.values.elementAt(0)
                    collapseView.setImage("to_down")
                }
//                rowAdatper.collapsed()
                rowAdatper.notifyDataSetChanged()
                collapse = !collapse
            }
        }
    }
}