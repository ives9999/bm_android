package com.sportpassword.bm.Controllers

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.selected
import com.sportpassword.bm.Utilities.unSelected
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_single_select_vc.*
import kotlinx.android.synthetic.main.color_select_item.view.*
import kotlinx.android.synthetic.main.select_item.*

class SingleSelectVC : MyTableVC() {

    var title: String = "選擇"
    var key: String? = null
    var selected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_select_vc)

        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")
        }
        setMyTitle(title)

        if (intent.hasExtra("rows")) {
            rows = intent.getSerializableExtra("rows") as ArrayList<HashMap<String, String>>
        }

        if (intent.hasExtra("selected")) {
            selected = intent.getStringExtra("selected")
        }

        recyclerView = tableView
        initAdapter()

        if (intent.hasExtra("key")) {
            key = intent.getStringExtra("key")
        }
        if (key == null) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }
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
        val selected = row.get("value")!!
        val intent = Intent()
        intent.putExtra("key", key)
        intent.putExtra("selected", selected)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
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