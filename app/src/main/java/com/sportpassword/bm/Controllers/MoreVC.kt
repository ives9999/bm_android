package com.sportpassword.bm.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Utilities.CITY_KEY
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.PERPAGE
import kotlinx.android.synthetic.main.mask.*
import java.lang.Exception

open class MoreVC : BaseActivity() {

    protected var type: String = "arena"
    protected var titleField: String = "name"

    protected var theFirstTime: Boolean = true
    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0

    protected var loading: Boolean = false

    protected lateinit var recyclerView: RecyclerView
    open protected lateinit var listAdapter: ListAdapter

    protected lateinit var scrollerListenr: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = intent.getStringExtra("type")
        titleField = intent.getStringExtra("titleField")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val memuView = menu!!.findItem(R.id.menu_search).actionView
        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        searchBtn.tag = type
        return true
    }

    override fun refresh() {
        page = 1
        getDataStart(page, perPage)
        listAdapter.notifyDataSetChanged()
    }

    open protected fun getDataStart(_page: Int, _perPage: Int) {
        //println("page: $_page")
        Loading.show(mask)
        dataService.getList(this, type!!, titleField, params, _page, _perPage, null) { success ->
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
        Loading.hide(mask)
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
        listAdapter = ListAdapter(this, type!!, screenWidth, { data ->
            val intent = Intent(this, ShowActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("token", data.token)
            intent.putExtra("title", data.title)
            startActivity(intent)
        }, {data ->
            try {
                val city_id = data.data[CITY_KEY]!!["value"] as Int
                citys.clear()
                citys.add(City(city_id, ""))
                prepareParams("all")
                refresh()
            } catch (e: Exception) {
                warning("沒有縣市值，無法搜尋")
            }
        }, { data, address ->
            val intent = Intent(this, MyMapVC::class.java)
            intent.putExtra("title", data.title)
            intent.putExtra("address", address)
            startActivity(intent)
        })
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
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView!!.layoutManager as GridLayoutManager
                if (this@MoreVC.superDataLists.size < this@MoreVC.totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
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
            refresh()
            refreshLayout.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }
}
