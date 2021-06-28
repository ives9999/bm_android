package com.sportpassword.bm.Adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.TEAM_PLAY_END_KEY
import com.sportpassword.bm.Utilities.TEAM_PLAY_START_KEY
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor

/**
 * Created by ives on 2018/3/21.
 */
class EditTeamItemAdapter(val context: Context, val key: String, val lists: List<Map<String, String>>, val itemClick: (Int, Boolean) -> Unit): RecyclerView.Adapter<EditTeamItemAdapter.ViewHolder>() {

    val checkedColor = ContextCompat.getColor(context, R.color.MY_GREEN)
    val uncheckedColor = ContextCompat.getColor(context, R.color.MY_WHITE)

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


    inner class ViewHolder(itemView: View, val itemClick: (Int, Boolean) -> Unit): RecyclerView.ViewHolder(itemView) {

        val item1View = itemView.findViewById<TextView>(R.id.item1)
        val mark1View = itemView.findViewById<ImageView>(R.id.mark1)
        val line1View = itemView.findViewById<LinearLayout>(R.id.line1)

        fun bind(position: Int) {

            val row = lists[position]
            item1View.text = row["text"]!!
            var checked: Boolean = row["checked"]!!.toBoolean()
            toggleClick(checked)

            if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
                mark1View.visibility = View.INVISIBLE
            }

            if (position == lists.size-1) {
                line1View.visibility = View.INVISIBLE
            } else {
                line1View.visibility = View.VISIBLE
            }

            itemView.onClick {view ->
                //println(view)
                checked = !checked
                toggleClick(checked)
                itemClick(position, checked)
            }
        }

        fun toggleClick(checked: Boolean) {
            if (checked) {
                item1View.setTextColor(checkedColor)
                mark1View.visibility = View.VISIBLE
                mark1View.setColorFilter(checkedColor)
            } else {
                item1View.setTextColor(uncheckedColor)
                mark1View.visibility = View.INVISIBLE
            }
        }
    }


}