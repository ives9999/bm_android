package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.sportpassword.bm.Adapters.TempPlayListAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.TEAM_TOKEN_KEY
import kotlinx.android.synthetic.main.tab.*

class TempPlayVC : MoreVC() {

    protected lateinit var tempPlayListAdapter: TempPlayListAdapter
    protected var dataLists: ArrayList<Map<String, Map<String, Any>>> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_play_vc)

        setMyTitle("教學")
        recyclerView = list_container
        dataService = TeamService
        refreshLayout = tab_refresh
        initAdapter()

        refresh()
    }

    override fun initAdapter() {
        tempPlayListAdapter = TempPlayListAdapter(this) { data ->
            val position = data["position"]!!["value"] as Int
            val token = data[TEAM_TOKEN_KEY]!!["value"] as String
            //val intent = Intent(activity, TestActivity::class.java)
            val intent = Intent(this, ShowTempPlayActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra(TEAM_TOKEN_KEY, token)
            startActivity(intent)
        }
        recyclerView.adapter = tempPlayListAdapter

        val layoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
    }

    override fun getDataStart(_page: Int, _perPage: Int) {
//        mask = Loading.show(context!!)
        Loading.show(mask)
        loading = true
        TeamService.tempPlay_list(this, _page, _perPage) { success ->
            getDataEnd(success)
        }
    }

    override fun notifyDataSetChanged() {
        if (page == 1) {
            dataLists = arrayListOf()
        }
        dataLists.addAll(TeamService.tempPlayLists)
//        var arr: ArrayList<String> = arrayListOf()
//        for (i in 0..dataLists1.size-1) {
//            val row = dataLists1[i]
//            val id = row.get("id")!!["show"] as String
//            arr.add(id)
//        }
//        println(arr)
        tempPlayListAdapter.lists = dataLists
        tempPlayListAdapter.notifyDataSetChanged()
    }

    override fun setRecyclerViewScrollListener() {

        var pos: Int = 0

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView!!.layoutManager as GridLayoutManager
                if (this@TempPlayVC.dataLists.size < this@TempPlayVC.totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (this@TempPlayVC.dataLists.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && this@TempPlayVC.dataLists.size < this@TempPlayVC.totalCount && !this@TempPlayVC.loading) {
                    this@TempPlayVC.getDataStart(this@TempPlayVC.page, this@TempPlayVC.perPage)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }

    override fun setRecyclerViewRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            this.page = 1
            this.getDataStart(this.page, this.perPage)
            this.tempPlayListAdapter.notifyDataSetChanged()

            tab_refresh.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }
}
