package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Models.Team
import com.sportpassword.bm.Models.Data

/**
 * Created by ives on 2018/2/14.
 */

class MenuTeamListAdapter(val context: Context, val lists: ArrayList<Data>, val itemClick: (Data) -> Unit): RecyclerView.Adapter<MenuTeamListAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(lists[position])
//        holder?.swipeLayout.showMode = SwipeLayout.ShowMode.LayDown
//        holder?.swipeLayout.addOnLayoutListener {v: SwipeLayout? ->
//            println(v)
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.menu_team_list_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }


    inner class ViewHolder(itemView: View, val itemClick: (Data) -> Unit): RecyclerView.ViewHolder(itemView) {
        //val swipeLayout = itemView.findViewById<SwipeLayout>(R.id.menu_team_list_swipe)
        val nameView = itemView.findViewById<TextView>(R.id.menu_team_list_name)

        fun bind(team: Data) {
            nameView.text = team.title
            itemView.setOnClickListener{itemClick(team)}
        }
    }
}










