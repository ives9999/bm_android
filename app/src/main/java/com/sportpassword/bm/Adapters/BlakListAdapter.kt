package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.Models.BlackList
import com.sportpassword.bm.Models.BlackLists
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.noSec

class BlakListAdapter(val context: Context, val lists: ArrayList<BlackList>, val itemClick:(Int)->Unit, val call: (String)->Unit, val cancel:(Int)->Unit):RecyclerView.Adapter<BlakListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.blacklist_item, parent, false)
        return ViewHolder(view, itemClick, call, cancel)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View, itemClick: (Int) -> Unit, call: (String)->Unit, cancel:(Int)->Unit): RecyclerView.ViewHolder(itemView) {
        val memberNameView = itemView.findViewById<TextView>(R.id.memberName)
        val mobileView = itemView.findViewById<TextView>(R.id.mobile)
        val teamNameView = itemView.findViewById<TextView>(R.id.teamName)
        val dateView = itemView.findViewById<TextView>(R.id.date)
        val lineView = itemView.findViewById<LinearLayout>(R.id.line)
        val cancelButton = itemView.findViewById<Button>(R.id.cancel)

        fun bind(position: Int) {
            val row: BlackList = lists[position]
            row.print()
            val memberName = row.name
            val mobile = row.mobile
            val date = row.created_at.noSec()
            val team = row.team
            memberNameView.text = memberName
            mobileView.text = mobile
            dateView.text = date
            if (team.containsKey("name")) {
                teamNameView.text = team.get("name") as String
            }
            if (position == lists.size-1) {
                lineView.visibility = View.INVISIBLE
            }
            mobileView.setOnClickListener { call(mobile) }
            itemView.setOnClickListener { itemClick(position) }
            cancelButton.setOnClickListener { cancel(position) }
        }

    }
}