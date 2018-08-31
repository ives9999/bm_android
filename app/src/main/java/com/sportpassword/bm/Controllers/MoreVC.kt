package com.sportpassword.bm.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Utilities.PERPAGE

open class MoreVC : BaseActivity() {

    protected var type: String = "arena"
    protected var titleField: String = "name"

    protected var theFirstTime: Boolean = true
    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0

    protected lateinit var recyclerView: RecyclerView
    open protected lateinit var listAdapter: ListAdapter
    protected lateinit var dataService: DataService

    protected lateinit var scrollerListenr: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = intent.getStringExtra("type")
        titleField = intent.getStringExtra("titleField")
    }

    override fun refresh() {
        super.refresh()
        getDataStart(page, perPage)
    }

    open protected fun getDataStart(_page: Int, _perPage: Int) {
        //println("page: $_page")
        dataService.getList(this, type!!, titleField, _page, _perPage, null) { success ->
            getDataEnd(success)
        }
    }

    open protected fun getDataEnd(success: Boolean) {
        if (success) {
            if (theFirstTime) {
                page = dataService.page
                perPage = dataService.perPage
                totalCount = dataService.totalCount
                var _totalPage: Int = totalCount / perPage
                totalPage = if (totalCount % perPage > 0) _totalPage+1 else _totalPage
                theFirstTime = false
                closeRefresh()
            }

            notifyDataSetChanged()
            page++
        }
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }
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
    open protected fun initAdapter() {
        listAdapter = ListAdapter(this, type!!, screenWidth) { data ->
            val intent = Intent(this, ShowActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("token", data.token)
            startActivity(intent)
        }
        recyclerView.adapter = listAdapter
        val layoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
    }
    open protected fun setRecyclerViewScrollListener() {

        var pos: Int = 0

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView!!.layoutManager as GridLayoutManager
                if (this@MoreVC.superDataLists.size < this@MoreVC.totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (this@MoreVC.superDataLists.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && this@MoreVC.superDataLists.size < this@MoreVC.totalCount) {
                    this@MoreVC.getDataStart(this@MoreVC.page, this@MoreVC.perPage)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }

    open protected fun setRecyclerViewRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            this@MoreVC.page = 1
            this.getDataStart(this.page, this.perPage)
            this.listAdapter.notifyDataSetChanged()

            refreshLayout.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }
}