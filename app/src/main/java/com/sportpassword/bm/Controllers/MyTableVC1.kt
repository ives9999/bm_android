package com.sportpassword.bm.Controllers

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Utilities.PERPAGE
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

abstract class MyTableVC1 : BaseActivity() {

    var sections: ArrayList<String> = arrayListOf()
    var rows: ArrayList<HashMap<String, String>> = arrayListOf()

    protected lateinit var adapter: GroupAdapter<ViewHolder>
    protected val adapterSections: ArrayList<Section> = arrayListOf()
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var listAdapter: ListAdapter

    protected var loading: Boolean = false
    protected lateinit var maskView: View

    protected var theFirstTime: Boolean = true
    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun initAdapter(include_section: Boolean=false) {
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
            println(items.size)
            adapter.addAll(items)
        }
        recyclerView.adapter = adapter
    }

    open protected fun getDataStart(_page: Int, _perPage: Int) {}

    open protected fun getDataEnd(success: Boolean) {}

    open protected fun notifyDataSetChanged() {
        if (page == 1) {
            superDataLists = arrayListOf()
        }
        superDataLists.addAll(dataService.superDataLists)
//        for (data in superDataLists) {
//            data.print()
//            println("===================")
//        }
        listAdapter.lists = superDataLists
        listAdapter.notifyDataSetChanged()
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

    open protected fun setRecyclerViewScrollListener() {

        var pos: Int = 0

        scrollerListenr = object: RecyclerView.OnScrollListener() {

        }

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView!!.layoutManager as GridLayoutManager
                if (superDataLists.size < totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (superDataLists.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && superDataLists.size < totalCount && !loading) {
                    getDataStart(page, perPage)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }

    open protected fun setRecyclerViewRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            refresh()

            refreshLayout.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }
}