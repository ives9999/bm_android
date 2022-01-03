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
import com.sportpassword.bm.Utilities.setImage
import com.squareup.picasso.Picasso

class ManagerSignupListAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<ManagerSignupListViewHolder>(resource, ::ManagerSignupListViewHolder, list1CellDelegate) {}

class ManagerSignupListViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

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

            if (row.memberTable != null) {

                viewHolder.findViewById<ImageView>(R.id.listFeatured) ?.let {
                    if (row.memberTable!!.featured_path.isNotEmpty()) {
                        Picasso.with(context)
                            .load(row.memberTable!!.featured_path)
                            .placeholder(R.drawable.loading_square_120)
                            .error(R.drawable.loading_square_120)
                            .into(listFeatured)
                    }
                }

                viewHolder.findViewById<TextView>(R.id.nicknameLbl)?.let {
                    it.setText(row.memberTable!!.nickname)
                }

                viewHolder.findViewById<TextView>(R.id.nameLbl)?.let {
                    it.setText(row.memberTable!!.name)
                }

                viewHolder.findViewById<Button>(R.id.mobileBtn)?.let {
                    if (row.memberTable!!.mobile == null) {
                        it.visibility = View.GONE
                    } else {
                        it.visibility = View.VISIBLE
                        it.setText(row.memberTable!!.mobile_show)
                    }
                }

                viewHolder.findViewById<Button>(R.id.emailBtn)?.let {
                    if (row.memberTable!!.email == null) {
                        it.visibility = View.GONE
                    } else {
                        it.visibility = View.VISIBLE
                        it.setText(row.memberTable!!.email)
                    }
                }
            }
        }
    }
}
