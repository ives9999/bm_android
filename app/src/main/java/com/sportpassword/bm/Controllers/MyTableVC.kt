package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Models.SuperModel
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.PERPAGE
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.mask.*

open class MyTableVC: BaseActivity() {

    protected lateinit var recyclerView: RecyclerView
    protected lateinit var adapter: GroupAdapter<ViewHolder>
    protected val adapterSections: ArrayList<Section> = arrayListOf()

    var sections: ArrayList<String> = arrayListOf()
    var rows: ArrayList<SuperModel> = arrayListOf()

    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0

    var scrollerPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun initAdapter(include_section: Boolean=false) {
        adapter = GroupAdapter()
        recyclerView.adapter = adapter
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

        recyclerView.setHasFixedSize(true)
        setRefreshListener()
        setScrollListener()
    }

    override fun refresh() {
        page = 1
        getDataStart(page, perPage)
    }

    open protected fun getDataStart(_page: Int, _perPage: Int) {
        //println("page: $_page")
        Loading.show(mask)
    }

    open protected fun getDataEnd(success: Boolean) {

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
    }

    open fun generateItems(): ArrayList<Item> {
        var items: ArrayList<Item> = arrayListOf()

        return items
    }

    open fun generateItems(section: Int): ArrayList<Item> {
        return arrayListOf()
    }

    open fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {

    }
    open protected fun setScrollListener() {

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
                if (rows.size < totalCount) {
                    scrollerPos = layoutManager.findLastVisibleItemPosition()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (rows.size == scrollerPos && newState == RecyclerView.SCROLL_STATE_IDLE && rows.size < totalCount) {
                    getDataStart(page, perPage)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }
}