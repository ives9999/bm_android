package com.sportpassword.bm.Adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textColor

/**
 * Created by ives on 2018/3/21.
 */
class EditTeamItemAdapter(val context: Context, val lists: List<Map<String, String>>, val itemClick: (Int) -> Unit): RecyclerView.Adapter<EditTeamItemAdapter.ViewHolder>() {

    val checkedColor = ContextCompat.getColor(context, R.color.MY_GREEN)
    val uncheckedColor = ContextCompat.getColor(context, R.color.WHITE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_edit_team_item_adapter, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class ViewHolder(itemView: View, val itemClick: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {

        val item1View = itemView.findViewById<TextView>(R.id.item1)
        val mark1View = itemView.findViewById<ImageView>(R.id.mark1)
        val line1View = itemView.findViewById<LinearLayout>(R.id.line1)

        fun bind(position: Int) {

            val row = lists[position]
            item1View.text = row["text"]!!
            var checked: Boolean = row["checked"]!!.toBoolean()
            item1View.setTextColor(if(checked) checkedColor else uncheckedColor)

            if (position == lists.size-1) {
                line1View.visibility = View.INVISIBLE
            }

            itemView.onClick {view ->
                //println(view)
                checked = !checked
                item1View.setTextColor(if(checked) checkedColor else uncheckedColor)
                itemClick(position)
            }
        }
    }


}