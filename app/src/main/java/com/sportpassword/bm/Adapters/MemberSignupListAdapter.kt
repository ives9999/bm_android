package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.SignupNormalTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.SIGNUP_STATUS
import com.sportpassword.bm.Utilities.setImage
import com.squareup.picasso.Picasso

class MemberSignupListAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<ArenaViewHolder>(resource, ::ArenaViewHolder, list1CellDelegate) {

//    var tableList: ArrayList<Table> = arrayListOf()
//
//    fun setMyTableList(tableList: ArrayList<Table>) {
//        this.tableList = tableList
//    }
//
//    override fun getItemCount(): Int {
//        return tableList.size
//    }
//
//    override fun onBindViewHolder(holder: MemberSignupListViewHolder, position: Int) {
//        val row: Table = tableList[position]
//
//        holder.bind(row, position)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberSignupListViewHolder {
//        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
//        val viewHolder: View = inflater.inflate(resource, parent, false)
//
//        return MemberSignupListViewHolder(parent.context, viewHolder, able_type, list1CellDelegate)
//    }
}

class MemberSignupListViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(row: Table, idx: Int) {
        super.bind(row, idx)

        val row1: SignupNormalTable? = row as? SignupNormalTable

        if (row1 != null) {
            viewHolder.findViewById<TextView>(R.id.signupTime)?.let {
                it.setText(row.created_at_show)
            }

            viewHolder.findViewById<ImageView>(R.id.statusIV)?.let {
                val status_enum: SIGNUP_STATUS = SIGNUP_STATUS.stringToSwift(row1.status)
                if (status_enum == SIGNUP_STATUS.normal) {
                    it.setImage("signup_normal")
                } else if (status_enum == SIGNUP_STATUS.cancel) {
                    it.setImage("cancel")
                } else if (status_enum == SIGNUP_STATUS.standby) {
                    it.setImage("signup_standby")
                }
            }

            if (row1.ableTable != null) {

                viewHolder.findViewById<ImageView>(R.id.listFeatured) ?.let {
                    if (row1.ableTable!!.featured_path.isNotEmpty()) {
                        Picasso.with(context)
                            .load(row.ableTable!!.featured_path)
                            .placeholder(R.drawable.loading_square_120)
                            .error(R.drawable.loading_square_120)
                            .into(listFeatured)
                    }
                }

                viewHolder.findViewById<TextView>(R.id.nameLbl)?.let {
                    if (row1.ableTable!!.name != null && row1.ableTable!!.name.isNotEmpty()) {
                        it.setText(row.ableTable!!.name)
                    }
                    if (row1.ableTable!!.title != null && row1.ableTable!!.title.isNotEmpty()) {
                        it.setText(row.ableTable!!.title)
                    }
                }

                viewHolder.findViewById<Button>(R.id.cityBtn)?.let {
                    if (row1.ableTable!!.city_id > 0) {
                        it.visibility = View.VISIBLE
                        it.setText(row.ableTable!!.city_show)
                    } else {
                        it.visibility = View.GONE
                    }
                }

                viewHolder.findViewById<Button>(R.id.arenaBtn)?.let {
                    if (row1.ableTable!!.arena_name != null && row1.ableTable!!.arena_name.isNotEmpty()) {
                        it.visibility = View.VISIBLE
                        it.setText(row.ableTable!!.arena_name)
                    } else {
                        it.visibility = View.GONE
                    }
                }

                viewHolder.findViewById<TextView>(R.id.weekendLbl)?.let {
                    it.setText(row1.ableTable!!.weekdays_show)
                }

                viewHolder.findViewById<TextView>(R.id.intervalLbl)?.let {
                    it.setText(row1.ableTable!!.interval_show)
                }

                viewHolder.setOnClickListener {
                    list1CellDelegate?.cellClick(row)
                }
            }
        }
    }
}
