package com.sportpassword.bm.Adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.noSec

/**
 * Created by ives on 2018/3/9.
 */
class SignupsAdapter(val context: Context, val itemClick: (String, String, String, String)->Unit): RecyclerView.Adapter<SignupsAdapter.ViewHolder>() {

    var lists: ArrayList<Map<String, String>> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tempplay_signups_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View, val itemClick:(String, String, String, String)->Unit): RecyclerView.ViewHolder(itemView) {
        val nicknameView = itemView.findViewById<TextView>(R.id.text)
        val dateView = itemView.findViewById<TextView>(R.id.signup_date)

        fun bind(position: Int) {
            val data: Map<String, String> = lists[position]
            var status = "on"
            var status1 = ""
            if (data.containsKey("status")) {
                status = data.get("status")!!
                if (status == "off") {
                    status1 = "(取消)"
                }
            }
            var off_at = ""
            if (data.containsKey("off_at")) {
                off_at = data.get("off_at")!!
            }
            nicknameView.text = data.get("nickname") + status1
            var date = data.get("created_at")
            date = date!!.noSec()
            dateView.text = date

            val token: String = data.get("token").toString()
            itemView.setOnClickListener{itemClick(token, date, status, off_at)}
        }
    }
}