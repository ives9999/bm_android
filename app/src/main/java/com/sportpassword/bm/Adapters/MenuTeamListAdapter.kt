package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.sportpassword.bm.R
import com.sportpassword.bm.Models.Data

/**
 * Created by ives on 2018/2/14.
 */

class MenuTeamListAdapter(val context: Context, val lists: ArrayList<Data>, val edit: (Data) -> Unit, val delete: (Data) -> Unit, val onoff: (Data) -> Unit): RecyclerView.Adapter<MenuTeamListAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.menu_team_list_item, parent, false)
        return ViewHolder(view, edit, delete, onoff)
    }

    override fun getItemCount(): Int {
        return lists.size
    }


    inner class ViewHolder(itemView: View, val edit: (Data) -> Unit, val delete: (Data) -> Unit, val onoff: (Data) -> Unit): RecyclerView.ViewHolder(itemView) {
        val frontLayout = itemView.findViewById<FrameLayout>(R.id.front_layout)
        val editView = itemView.findViewById<LinearLayout>(R.id.menu_team_list_edit)
        val deleteView = itemView.findViewById<LinearLayout>(R.id.menu_team_list_delete)
        val nameView = itemView.findViewById<TextView>(R.id.menu_team_list_name)
        val line2View = itemView.findViewById<LinearLayout>(R.id.line2)

        fun bind(position: Int) {
            if (position == lists.size-1) {
                line2View.visibility = View.INVISIBLE
            }
            val team = lists[position]
            nameView.text = team.title
            frontLayout.setOnClickListener{ onoff(team) }
            editView.setOnClickListener { edit(team) }
            deleteView.setOnClickListener { delete(team) }
        }
    }
}










