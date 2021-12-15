package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.selected
import com.sportpassword.bm.Utilities.unSelected
import kotlinx.android.synthetic.main.activity_multi_select_vc.*
import kotlinx.android.synthetic.main.select_item.*

open class MultiSelectVC : SelectVC() {

    var selecteds: ArrayList<String> = arrayListOf()
    lateinit var tableAdapter: MultiSelectAdapter

    /*
    rows is [
        title: 星期一, value: "1"
        title: 星期二, value: "2"
        title: 星期三, value: "3"
        title: 星期四, value: "4"
        title: 星期五, value: "5"
        title: 星期六, value: "6"
        title: 星期日, value: "7"
    ]
     */

    /*
    selecteds is [
        "1", "2"
    ]
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_select_vc)

        if (intent.hasExtra("selecteds")) {
            selecteds = intent.getStringArrayListExtra("selecteds")!!
        }

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
        }


        recyclerView = tableView
        tableAdapter = MultiSelectAdapter(selecteds, this)
        recyclerView.adapter = tableAdapter
//        initAdapter()

        init()
    }

    override fun cellClick(idx: Int) {
        val row = tableRows[idx]
        var isExist = false
        var at = 0
        for ((i, str) in selecteds.withIndex()) {
            if (row.value == str) {
                isExist = true
                at = i
                break
            }
        }
        if (isExist) {
            selecteds.removeAt(at)
            row.isSelected = false
        } else {
            selecteds.add(row.value)
            row.isSelected = true
        }
        tableAdapter.rows = tableRows
        tableAdapter.selecteds = selecteds
        tableAdapter.notifyDataSetChanged()
    }

//    override fun generateItems(): ArrayList<Item> {
//
//        val items: ArrayList<Item> = arrayListOf()
//
//        for (row in rows) {
//            var title: String? = null
//            if (row.containsKey("title")) {
//                title = row.get("title")!!
//            }
//            var value: String? = null
//            if (row.containsKey("value")) {
//                value = row.get("value")!!
//            }
//            var isSelected = false
//            for (selected in selecteds) {
//                if (value == selected) {
//                    isSelected = true
//                    break
//                }
//            }
//
//            if (title != null && value != null) {
//                val item = MultiSelectItem(title, value, isSelected, this)
//                items.add(item)
//            }
//        }
//
//        return items
//    }



    open fun submit(view: View) {

        val intent = Intent()
        intent.putExtra("key", key)
        intent.putExtra("able_type", able_type)
        //println(selecteds);
        intent.putStringArrayListExtra("selecteds", selecteds)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

class MultiSelectAdapter(var selecteds: ArrayList<String>?, val list1CellDelegate: List1CellDelegate?): RecyclerView.Adapter<MulitSelectViewHolder>() {

    var rows: ArrayList<SelectRow> = arrayListOf()

    override fun onBindViewHolder(holder: MulitSelectViewHolder, position: Int) {

        val row: SelectRow = rows[position]
        holder.title.text = row.title

        if (row.isSelected) {
            holder.selected.visibility = View.VISIBLE
            holder.title.selected()
        } else {
            holder.selected.visibility = View.INVISIBLE
            holder.title.unSelected()
        }

        holder.viewHolder.setOnClickListener {
            list1CellDelegate?.cellClick(position)
        }
    }

    override fun getItemCount(): Int {
        return rows.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MulitSelectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.select_item, parent, false)

        return MulitSelectViewHolder(viewHolder)
    }
}

class MulitSelectViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    val title: TextView = viewHolder.findViewById(R.id.title)
    val selected: ImageView = viewHolder.findViewById(R.id.selected)
}

//class MultiSelectItem(val title: String, val value: String, val isSelected: Boolean, val delegate: MultiSelectVC): Item() {
//
//
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
//
//        viewHolder.title.text = title
//        viewHolder.container.setOnClickListener {
//            if (viewHolder.selected.visibility == View.VISIBLE) {
//                viewHolder.selected.visibility = View.INVISIBLE
//                viewHolder.title.unSelected()
//            } else {
//                viewHolder.selected.visibility = View.VISIBLE
//                viewHolder.title.selected()
//            }
//            delegate.rowClick(position)
//        }
//
//        if (isSelected) {
//            viewHolder.selected.visibility = View.VISIBLE
//            viewHolder.title.selected()
//        } else {
//            viewHolder.selected.visibility = View.INVISIBLE
//            viewHolder.title.unSelected()
//        }
//    }
//
//    override fun getLayout() = R.layout.select_item
//}
