package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Controllers.SignupSection
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Models.SignupNormalTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.SIGNUP_STATUS
import com.sportpassword.bm.Utilities.setImage
import com.squareup.picasso.Picasso

class ManagerSignupListAdapter(var delegate: List1CellDelegate): RecyclerView.Adapter<ManagerSignupListViewHolder>() {

    var signupSections: ArrayList<SignupSection> = arrayListOf()
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerSignupListViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val viewHolder = inflater.inflate(R.layout.cell_manager_signup_list_section, parent, false)

        return ManagerSignupListViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ManagerSignupListViewHolder, position: Int) {

        val section: SignupSection = signupSections[position]
        holder.dateLbl.text = section.date

        val adapter =
            ManagerSignupListItemAdapter(context, position, signupSections[position])
//            holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.recyclerView.adapter = adapter

        var iconID: Int = 0
        if (section.isExpanded) {
            iconID = context.resources.getIdentifier("to_down", "drawable", context.packageName)
        } else {
            iconID = context.resources.getIdentifier("to_right", "drawable", context.packageName)
        }
        holder.greater.setImageResource(iconID)
        holder.greater.setOnClickListener {
            delegate.handleOneSectionExpanded(position)
        }
    }

    override fun getItemCount(): Int {
        return signupSections.size
    }
}

class ManagerSignupListViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var dateLbl: TextView = viewHolder.findViewById(R.id.dateLbl)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var recyclerView: RecyclerView = viewHolder.findViewById(R.id.recyclerView)
}

class ManagerSignupListItemAdapter(val context: Context, private val sectionIdx: Int, private val signupSection: SignupSection): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var rows: ArrayList<SignupNormalTable> = signupSection.rows
    var rowIdx: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.manager_signuplist_cell, parent, false)

        return ManagerSignupListItemViewHolder(context, viewHolder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder: ManagerSignupListItemViewHolder = holder as ManagerSignupListItemViewHolder
        myHolder.bind(rows[position], position)
    }

    override fun getItemCount(): Int {
        if (signupSection.isExpanded) {
            return rows.size
        } else {
            return 0
        }
    }
}

class ManagerSignupListItemViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

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
                    if (row.memberTable!!.featured_path != null && row.memberTable!!.featured_path.isNotEmpty()) {
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
