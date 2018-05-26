package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import org.jetbrains.anko.image

class MemberFunctionsAdapter(val context: Context, val lists: ArrayList<Map<String, String>>, val itemClick: (String) -> Unit): RecyclerView.Adapter<MemberFunctionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.adapter_member_functions, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(position)
    }

    inner class ViewHolder(itemView: View, val itemClick: (String) -> Unit): RecyclerView.ViewHolder(itemView) {
        val iconView = itemView.findViewById<ImageView>(R.id.icon)
        val textView = itemView.findViewById<TextView>(R.id.text)
        val line2View = itemView.findViewById<LinearLayout>(R.id.line2)

        fun bind(position: Int) {
            if (position == lists.size-1) {
                line2View.visibility = View.INVISIBLE
            }
            val row = lists[position]
            val iconID = context.resources.getIdentifier(row["icon"], "drawable", context.packageName)
            iconView.setImageResource(iconID)
            textView.text = row["text"]
            itemView.setOnClickListener{itemClick(row["type"]!!)}
        }
    }
}