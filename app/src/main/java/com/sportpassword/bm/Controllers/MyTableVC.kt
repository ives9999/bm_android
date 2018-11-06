package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

open class MyTableVC : BaseActivity() {

    var rows: ArrayList<HashMap<String, String>> = arrayListOf()

    protected lateinit var adapter: GroupAdapter<ViewHolder>
    protected lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initAdapter() {
        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->

        }
        val items = generateItems()
        adapter.addAll(items)
        recyclerView.adapter = adapter
    }

    fun generateItems(): ArrayList<Item> {
        var items: ArrayList<Item> = arrayListOf()
        for (row in rows) {
            var text = ""
            if (row.containsKey("text")) {
                text = row.get("text")!!
            }
            var icon = ""
            if (row.containsKey("icon")) {
                icon = row.get("icon")!!
            }
            items.add(FunctionItem(icon, text))
        }

        return items
    }
}