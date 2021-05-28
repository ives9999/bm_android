package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.selected
import com.sportpassword.bm.Utilities.unSelected
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_single_select_vc.*
import kotlinx.android.synthetic.main.select_item.*

open class SingleSelectVC1 : SelectVC1() {

    var selected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_select_vc)

        if (intent.hasExtra("selected")) {
            selected = intent.getStringExtra("selected")!!
        }

        recyclerView = tableView
        initAdapter()

        init()
    }

    override fun generateItems(): ArrayList<Item> {

        val items: ArrayList<Item> = arrayListOf()

        val rowClick = { i: Int ->
            submit(i)
        }
        for (row in rows) {
            var title: String? = null
            if (row.containsKey("title")) {
                title = row.get("title")!!
            }
            var value: String? = null
            if (row.containsKey("value")) {
                value = row.get("value")!!
            }
            val isSelected = if(value == selected) true else false

            if (title != null && value != null) {
                val item = SingleSelectItem(title, value, isSelected, rowClick)
                items.add(item)
            }
        }

        return items
    }

    fun submit(idx: Int) {

        if (idx >= rows.size) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }
        val row = rows[idx]
        if (!row.containsKey("value")) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }

        var cancel: Boolean = false
        if (selected != null && selected!!.isNotEmpty()) {
            if (selected == row["value"]) {
                cancel = true
            }
        }
        if (!cancel) {
            selected = row["value"]
            dealSelected()
            val intent = Intent()
            intent.putExtra("key", key)
            intent.putExtra("selected", selected)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    //selected 有時候是56.0要把它處理成56
    open fun dealSelected() {}
}

class SingleSelectItem(val title: String, val value: String, val isSelected: Boolean, val rowClick:(idx: Int)->Unit): Item() {

    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

        viewHolder.title.text = title
        viewHolder.container.setOnClickListener {
            rowClick(position)
        }

        if (isSelected) {
            viewHolder.selected.visibility = View.VISIBLE
            viewHolder.title.selected()
        } else {
            viewHolder.selected.visibility = View.INVISIBLE
            viewHolder.title.unSelected()
        }
    }

    override fun getLayout() = R.layout.select_item
}