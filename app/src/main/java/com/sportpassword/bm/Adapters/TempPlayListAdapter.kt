package com.sportpassword.bm.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*

/**
 * Created by ives on 2018/3/3.
 */
class TempPlayListAdapter(val context: Context, val itemClick: (Map<String, Map<String, Any>>) -> Unit): RecyclerView.Adapter<TempPlayListAdapter.ViewHolder>() {

    var lists: ArrayList<Map<String, Map<String, Any>>> = arrayListOf()
        get() = field
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tab_tempplay_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class ViewHolder(itemView: View, itemClick: (Map<String, Map<String, Any>>) -> Unit): RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.temp_play_name)
        val arenaBtn = itemView.findViewById<Button>(R.id.temp_play_arena_btn)
        val cityBtn = itemView.findViewById<Button>(R.id.temp_play_city_btn)
        val countView = itemView.findViewById<TextView>(R.id.temp_play_count)
        val signupView = itemView.findViewById<TextView>(R.id.temp_play_signup)
        val dateView = itemView.findViewById<TextView>(R.id.temp_play_date)
        val intervalView = itemView.findViewById<TextView>(R.id.temp_play_interval)

        fun bind(position: Int) {
            val _data: Map<String, Map<String, Any>> = lists[position]
            var data: MutableMap<String, Map<String, Any>> = _data.toMutableMap()
            data.put("position", mapOf("value" to position))
            val name = data[TEAM_NAME_KEY]!!["show"] as String
            nameView.text = name
            val count = data["count"]!!["quantity"] as Int
            countView.text = if (count>=0) count.toString() else "未提供"
            val signup = data["count"]!!["signup"] as Int
            signupView.text = signup.toString()
            val date = data[TEAM_NEAR_DATE_KEY]!!["show"] as String
            dateView.text = date
            val interval = "${data[TEAM_PLAY_START_KEY]!!["show"] as String}-${data[TEAM_PLAY_END_KEY]!!["show"] as String}"
            intervalView.text = interval
            val city = data[TEAM_CITY_KEY]!!["show"] as String
            cityBtn.text = city
            val arena = data[TEAM_ARENA_KEY]!!["show"] as String
            arenaBtn.text = arena
//            println("${data.title}: featured => ${data.featured_path}")
//            println("${data.title}: vimeo => ${data.vimeo}")
//            println("${data.title}: youbute => ${data.youtube}")
            itemView.setOnClickListener{itemClick(data)}
        }
    }












}