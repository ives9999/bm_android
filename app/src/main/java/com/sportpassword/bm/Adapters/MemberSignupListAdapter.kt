package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.SignupNormalTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.SIGNUP_STATUS
import com.sportpassword.bm.extensions.setImage
import com.squareup.picasso.Picasso

class MemberSignupListAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<MemberSignupListViewHolder>(resource, ::MemberSignupListViewHolder, list1CellDelegate) {}

class MemberSignupListViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: SignupNormalTable? = _row as? SignupNormalTable

        if (row != null) {
            viewHolder.findViewById<TextView>(R.id.signupTime)?.let {
                it.setText(row.created_at_show)
            }

            viewHolder.findViewById<ImageView>(R.id.statusIV)?.let {
                val status_enum: SIGNUP_STATUS = SIGNUP_STATUS.stringToSwift(row.status)
                if (status_enum == SIGNUP_STATUS.normal) {
                    it.setImage("signup_normal")
                } else if (status_enum == SIGNUP_STATUS.cancel) {
                    it.setImage("cancel")
                } else if (status_enum == SIGNUP_STATUS.standby) {
                    it.setImage("signup_standby")
                }
            }

            if (row.ableTable != null) {

                viewHolder.findViewById<ImageView>(R.id.listFeatured) ?.let {
                    if (row.ableTable!!.featured_path.isNotEmpty()) {
                        Picasso.get()
                            .load(row.ableTable!!.featured_path)
                            .placeholder(R.drawable.loading_square_120)
                            .error(R.drawable.loading_square_120)
                            .into(listFeatured)
                    }
                }

                viewHolder.findViewById<TextView>(R.id.nameLbl)?.let {
                    if (row.ableTable!!.name != null && row.ableTable!!.name.isNotEmpty()) {
                        it.setText(row.ableTable!!.name)
                    }
                    if (row.ableTable!!.title != null && row.ableTable!!.title.isNotEmpty()) {
                        it.setText(row.ableTable!!.title)
                    }
                }

                viewHolder.findViewById<Button>(R.id.cityBtn)?.let {
                    if (row.ableTable!!.city_id > 0) {
                        it.visibility = View.VISIBLE
                        it.setText(row.ableTable!!.city_show)
                    } else {
                        it.visibility = View.GONE
                    }
                }

                viewHolder.findViewById<Button>(R.id.arenaBtn)?.let {
                    if (row.ableTable!!.arena_name != null && row.ableTable!!.arena_name.isNotEmpty()) {
                        it.visibility = View.VISIBLE
                        it.setText(row.ableTable!!.arena_name)
                    } else {
                        it.visibility = View.GONE
                    }
                }

                viewHolder.findViewById<TextView>(R.id.weekendLbl)?.let {
                    it.setText(row.ableTable!!.weekdays_show)
                }

                viewHolder.findViewById<TextView>(R.id.intervalLbl)?.let {
                    it.setText(row.ableTable!!.interval_show)
                }

                viewHolder.setOnClickListener {
                    list1CellDelegate?.cellClick(row)
                }
            }
        }
    }
}
