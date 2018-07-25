package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Models.SuperData

/**
 * Created by ives on 2018/2/14.
 */

class ManagerTeamAdapter(val context: Context, val lists: ArrayList<SuperData>, val itemClick: (String, String) -> Unit): RecyclerView.Adapter<ManagerTeamAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.manager_team_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    inner class ViewHolder(itemView: View, val itemClick: (String, String) -> Unit): RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.manager_team_name)
        val line2View = itemView.findViewById<LinearLayout>(R.id.line2)

        fun bind(position: Int) {
            if (position == lists.size-1) {
                line2View.visibility = View.INVISIBLE
            }
            val team = lists[position]
            nameView.text = team.title
            itemView.setOnClickListener{itemClick(team.title, team.token)}
        }
    }
}










