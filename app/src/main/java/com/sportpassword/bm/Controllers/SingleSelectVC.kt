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
import kotlinx.android.synthetic.main.activity_single_select_vc.*

interface SingleSelectDelegate {
    fun singleSelected(key: String, selected: String)
}

open class SingleSelectVC : SelectVC() {

    var selected: String? = null
    lateinit var tableAdapter: SingleSelectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_select_vc)

        if (intent.hasExtra("selected")) {
            selected = intent.getStringExtra("selected")
        }

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
        }

        recyclerView = tableView
        tableAdapter = SingleSelectAdapter(selected, this)
        recyclerView.adapter = tableAdapter
//        recyclerView.adapter = adapter
//        initAdapter()
//
//        init()
    }

//    override fun generateItems(): ArrayList<Item> {
//
//        val items: ArrayList<Item> = arrayListOf()
//        val rowClick = { i: Int ->
//            submit(i)
//        }
//        for (row in rows) {
//            var title: String? = null
//            if (row.containsKey("title")) {
//                title = row.get("title")!!
//            }
//            var value: String? = null
//            if (row.containsKey("value")) {
//                value = row.get("value")!!
//            }
//            val isSelected = if(value == selected) true else false
//
//            if (title != null && value != null) {
//                val item = SingleSelectItem(title, value, isSelected, rowClick)
//                items.add(item)
//            }
//        }
//
//        return items
//    }

    override fun cellClick(idx: Int) {
        submit(idx)
    }

    open fun submit(idx: Int) {
        if (idx >= tableRows.size) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }
        val row = tableRows[idx]
        if (row.value.isEmpty()) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }

        var cancel: Boolean = false
        if (selected != null && selected!!.isNotEmpty()) {
            if (selected == row.value) {
                cancel = true
            }
        }

        //選擇其他的
        if (!cancel) {
            selected = row.value
            val intent = Intent()
            intent.putExtra("key", key)
            intent.putExtra("selected", selected)
            intent.putExtra("able_type", able_type)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else { //取消原來的選擇
            selected = ""
            tableAdapter.notifyDataSetChanged()
        }
    }

//    open fun submit(idx: Int) {
//
//        if (idx >= rows.size) {
//            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
//        }
//        val row = rows[idx]
//        if (!row.containsKey("value")) {
//            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
//        }
//
//        var cancel: Boolean = false
//        if (selected != null && selected!!.isNotEmpty()) {
//            if (selected == row["value"]) {
//                cancel = true
//            }
//        }
//
//        //選擇其他的
//        if (!cancel) {
//            selected = row["value"]
//            //dealSelected()
//            val intent = Intent()
//            intent.putExtra("key", key)
//            intent.putExtra("selected", selected)
//            intent.putExtra("able_type", able_type)
//            setResult(Activity.RESULT_OK, intent)
//            finish()
//        } else { //取消原來的選擇
//            selected = ""
////            generateItems()
////            notifyChanged()
//        }
//    }

    //selected 有時候是56.0要把它處理成56
    open fun dealSelected() {}
}

class SingleSelectAdapter(var selected: String?, val list1CellDelegate: List1CellDelegate?): RecyclerView.Adapter<SingleSelectViewHolder>() {

    var rows: ArrayList<SelectRow> = arrayListOf()

//    fun setSelected(selected: String) {
//        this.selected = selected
//    }

    override fun onBindViewHolder(holder: SingleSelectViewHolder, position: Int) {

        val row: SelectRow = rows[position]
        holder.title.text = row.title

        if (selected == row.value) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleSelectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.select_item, parent, false)

        return SingleSelectViewHolder(viewHolder)
    }
}

class SingleSelectViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    val title: TextView = viewHolder.findViewById(R.id.title)
    val selected: ImageView = viewHolder.findViewById(R.id.selected)
}

//class SingleSelectItem(val title: String, val value: String, val isSelected: Boolean, val rowClick:(idx: Int)->Unit): Item() {
//
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
//
//        viewHolder.title.text = title
//        viewHolder.container.setOnClickListener {
//            rowClick(position)
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