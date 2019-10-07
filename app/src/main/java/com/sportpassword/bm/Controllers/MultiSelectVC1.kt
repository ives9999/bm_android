package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.selected
import com.sportpassword.bm.Utilities.unSelected
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_multi_select_vc.*
import kotlinx.android.synthetic.main.select_item.*

class MultiSelectVC1 : SelectVC1() {

    var selecteds: ArrayList<String> = arrayListOf()

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
            selecteds = intent.getStringArrayListExtra("selecteds")
        }

        recyclerView = tableView
        initAdapter()

        init()
    }

    override fun generateItems(): ArrayList<Item> {

        val items: ArrayList<Item> = arrayListOf()

        val rowClick = { i: Int ->
            val row = rows[i]
            var isExist = false
            var at = 0
            for ((idx, str) in selecteds.withIndex()) {
                if (row["value"] == str) {
                    isExist = true
                    at = idx
                    break
                }
            }
            if (isExist) {
                selecteds.removeAt(at)
            } else {
                selecteds.add(row["value"]!!)
            }
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
            var isSelected = false
            for (selected in selecteds) {
                if (value == selected) {
                    isSelected = true
                    break
                }
            }

            if (title != null && value != null) {
                val item = MultiSelectItem(title, value, isSelected, rowClick)
                items.add(item)
            }
        }

        return items
    }



    fun submit(view: View) {

        val intent = Intent()
        intent.putExtra("key", key)
        //println(selecteds);
        intent.putStringArrayListExtra("selecteds", selecteds)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

class MultiSelectItem(val title: String, val value: String, val isSelected: Boolean, val rowClick:(idx: Int)->Any): Item() {


    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

        viewHolder.title.text = title
        viewHolder.container.setOnClickListener {
            if (viewHolder.selected.visibility == View.VISIBLE) {
                viewHolder.selected.visibility = View.INVISIBLE
                viewHolder.title.unSelected()
            } else {
                viewHolder.selected.visibility = View.VISIBLE
                viewHolder.title.selected()
            }
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
