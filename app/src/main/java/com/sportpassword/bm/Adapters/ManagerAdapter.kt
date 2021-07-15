package com.sportpassword.bm.Adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Models.Table
import com.squareup.picasso.Picasso

/**
 * Created by ives on 2018/2/14.
 */

class ManagerAdapter(val context: Context, val lists: ArrayList<Table>, val itemClick: (String, String) -> Unit): RecyclerView.Adapter<ManagerAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.manager_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    inner class ViewHolder(itemView: View, val itemClick: (String, String) -> Unit): RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.manager_name)
        val line2View = itemView.findViewById<LinearLayout>(R.id.line2)
        val featuredView = itemView.findViewById<ImageView>(R.id.featured)

        fun bind(position: Int) {
            if (position == lists.size-1) {
                line2View.visibility = View.INVISIBLE
            }
            val data = lists[position]
            nameView.text = data.title
            if (data.featured_path.isNotEmpty()) {
                Picasso.with(context)
                        .load(data.featured_path)
                        .placeholder(R.drawable.loading_square)
                        .error(R.drawable.load_failed_square)
                        .into(featuredView)
            } else {
                featuredView.setImageResource(R.drawable.loading_square)
            }

            itemView.setOnClickListener{itemClick(data.title, data.token)}
        }
    }
}










