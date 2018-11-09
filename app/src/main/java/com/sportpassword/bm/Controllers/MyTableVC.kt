package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sportpassword.bm.Adapters.GroupSection
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

open class MyTableVC : BaseActivity() {

    var sections: ArrayList<String> = arrayListOf()
    var rows: ArrayList<HashMap<String, String>> = arrayListOf()

    protected lateinit var adapter: GroupAdapter<ViewHolder>
    protected val adapterSections: ArrayList<Section> = arrayListOf()
    protected lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initAdapter(include_section: Boolean=false) {
        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->
            rowClick(item, view)
        }
        if (include_section) {
            for (section in sections) {
                adapterSections.add(Section())
            }
            for ((idx, title) in sections.withIndex()) {
                val expandableGroup = ExpandableGroup(GroupSection(title), true)
                val items = generateItems(idx)
                adapterSections[idx].addAll(items)
                expandableGroup.add(adapterSections[idx])
                adapter.add(expandableGroup)
            }
        } else {
            val items = generateItems()
            adapter.addAll(items)
        }
        recyclerView.adapter = adapter
    }

    open fun notifyChanged(include_section: Boolean=false) {
        if (include_section) {
            for ((idx, title) in sections.withIndex()) {
                val items = generateItems(idx)
                adapterSections[idx].update(items)
            }
        } else {
            val items = generateItems()
            adapter.update(items)
        }
        adapter.notifyDataSetChanged()
    }

    open fun generateItems(): ArrayList<Item> {
        return arrayListOf()
    }

    open fun generateItems(section: Int): ArrayList<Item> {
        return arrayListOf()
    }

    open fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {

    }
}