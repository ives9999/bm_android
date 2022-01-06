package com.sportpassword.bm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Data.SignupRow
import com.sportpassword.bm.R

class SignupAdapter(val context: Context, val delegate: BaseActivity?=null): RecyclerView.Adapter<SignupViewHolder>() {

    var rows: ArrayList<SignupRow> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.olcell, parent, false)

        return SignupViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: SignupViewHolder, position: Int) {

        val row: SignupRow = rows[position]
        holder.number.text = row.number
        holder.name.text = row.name

        holder.viewHolder.setOnClickListener {

            if (delegate != null) {
                delegate.showSignupInfo(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return rows.size
    }

}

class SignupViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var number: TextView = viewHolder.findViewById(R.id.number)
    var name: TextView = viewHolder.findViewById(R.id.name)
}