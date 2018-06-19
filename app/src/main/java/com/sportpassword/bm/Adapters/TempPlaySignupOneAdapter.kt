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
import com.sportpassword.bm.Utilities.MOBILE_KEY
import com.sportpassword.bm.Utilities.setImage
import com.sportpassword.bm.Utilities.truncate

class TempPlaySignupOneAdapter(val context: Context, val itemClick:(String, String)->Unit): RecyclerView.Adapter<TempPlaySignupOneAdapter.ViewHolder>() {

    var lists: ArrayList<HashMap<String, HashMap<String, Any>>> = arrayListOf()
    var keys: ArrayList<String> = arrayListOf()
    var mobile: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tempplay_signup_one_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class ViewHolder(itemView: View, val itemClick: (String, String) -> Unit): RecyclerView.ViewHolder(itemView) {
        val icon = itemView.findViewById<ImageView>(R.id.iconView)
        val textView = itemView.findViewById<TextView>(R.id.text)
        val valueView = itemView.findViewById<TextView>(R.id.value)
        val greater = itemView.findViewById<ImageView>(R.id.greaterView)
        val line = itemView.findViewById<LinearLayout>(R.id.line)

        fun bind(position: Int) {
            val key: String = keys[position]
            val data: HashMap<String, Any> = lists[position][key]!!
            if (!data.containsKey("icon")) {
                icon.visibility = View.INVISIBLE
            } else {
                icon.setImage(data["icon"].toString())
            }
            if (data.containsKey("title")) {
                textView.text = data.get("title").toString()
            }
            if (data.containsKey("value")) {
                var value: String = data.get("value").toString()
                if (key == MOBILE_KEY) {
                    mobile = value
                    value = value.truncate(6, "****")
                }
                valueView.text = value
            }
            var isMore: Boolean = false
            if (data.containsKey("more")) {
                isMore = data.get("more") as Boolean
            }
            if (!isMore) {
                greater.visibility = View.INVISIBLE
            } else {
                itemView.setOnClickListener { itemClick(key, mobile) }
            }
            if (position == lists.size-1) {
                line.visibility = View.INVISIBLE
            }
        }
    }
}