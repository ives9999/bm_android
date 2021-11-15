package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.MemberVC
import com.sportpassword.bm.Data.MemberRow
import com.sportpassword.bm.Data.MemberSection
import com.sportpassword.bm.R

class MemberSectionAdapter(val context: Context, private val resource: Int, var delegate: MemberVC): RecyclerView.Adapter<MemberSectionViewHolder>() {

    private var memberSections: ArrayList<MemberSection> = arrayListOf()

    fun setMyTableSection(tableSections: ArrayList<MemberSection>) {
        this.memberSections = tableSections
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberSectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(resource, parent, false)

        return MemberSectionViewHolder(viewHolder)

    }

    override fun onBindViewHolder(holder: MemberSectionViewHolder, position: Int) {
        val section: MemberSection = memberSections[position]
        holder.titleLbl.text = section.title

        val tableSection: MemberSection = memberSections[position]
        var iconID: Int = 0
        if (tableSection.isExpanded) {
            iconID = context.resources.getIdentifier("to_down", "drawable", context.packageName)
        } else {
            iconID = context.resources.getIdentifier("to_right", "drawable", context.packageName)
        }
        holder.greater.setImageResource(iconID)

        val adapter: MemberItemAdapter = MemberItemAdapter(context, position, memberSections[position], delegate)
//            holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.recyclerView.adapter = adapter

        holder.greater.setOnClickListener {
            delegate.handleMemberSectionExpanded(position)
        }
    }

    override fun getItemCount(): Int {
        return memberSections.size
    }
}

class MemberSectionViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var titleLbl: TextView = viewHolder.findViewById(R.id.titleLbl)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var recyclerView: RecyclerView = viewHolder.findViewById(R.id.recyclerView)
}

class MemberItemAdapter(val context: Context, private val sectionIdx: Int, private val memberSection: MemberSection, var delegate: MemberVC): RecyclerView.Adapter<MemberItemViewHolder>() {

    var memberRows: ArrayList<MemberRow> = memberSection.items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.adapter_member_functions, parent, false)

        return MemberItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: MemberItemViewHolder, position: Int) {
        val row: MemberRow = memberRows[position]
        holder.titleLbl.text = row.title

        val icon: String = row.icon
        val iconID = context.resources.getIdentifier(icon, "drawable", context.packageName)
        holder.iconView.setImageResource(iconID)

        holder.viewHolder.setOnClickListener {
            delegate.cellClick1(sectionIdx, position)
        }
    }

    override fun getItemCount(): Int {
        if (memberSection.isExpanded) {
            return memberRows.size
        } else {
            return 0
        }
    }

}

class MemberItemViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var iconView: ImageView = viewHolder.findViewById(R.id.icon)
    var titleLbl: TextView = viewHolder.findViewById(R.id.text)
}