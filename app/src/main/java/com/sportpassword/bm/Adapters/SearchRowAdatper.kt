package com.sportpassword.bm.Adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.sportpassword.bm.R

class SearchRowAdatper(val section: Int, val itemClick: (Int) -> Unit): RecyclerView.Adapter<SearchRowAdatper.ViewHolder>() {

    var rows: ArrayList<HashMap<String, String>> = arrayListOf()

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
        val keywordView = itemView.findViewById<EditText>(R.id.keywordTxt)
        val detailView = itemView.findViewById<TextView>(R.id.row_detail)
        val greaterView = itemView.findViewById<ImageView>(R.id.greater)

        fun bind(position: Int) {
            titleView.text = rows[position].get("title")
            detailView.text = rows[position].get("detail")
            if (section == 0 && position == 0) {
                keywordView.visibility = View.VISIBLE
                detailView.visibility = View.INVISIBLE
                greaterView.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener{itemClick(position)}
        }
    }
}