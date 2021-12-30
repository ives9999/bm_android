package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.SignupNormalTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.squareup.picasso.Picasso

class ManagerSignupListAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<ManagerSignupListViewHolder>(resource, ::ManagerSignupListViewHolder, list1CellDelegate) {}

class ManagerSignupListViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: SignupNormalTable = _row as SignupNormalTable

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
            
            viewHolder.findViewById<TextView>(R.id.titleLbl)?.let {
                it.setText(row.memberTable!!.nickname)
            }
        }
    }
}
