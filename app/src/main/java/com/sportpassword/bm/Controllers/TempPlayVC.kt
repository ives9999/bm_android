package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.sportpassword.bm.Adapters.TempPlayListAdapter
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.DEGREE
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.TEAM_PLAY_START_KEY
import com.sportpassword.bm.Utilities.TEAM_TOKEN_KEY
import kotlinx.android.synthetic.main.activity_temp_play_vc.*
import kotlinx.android.synthetic.main.tab.*
import kotlinx.android.synthetic.main.mask.*

class TempPlayVC : MoreVC() {

    protected lateinit var tempPlayListAdapter: TempPlayListAdapter
    protected var dataLists: ArrayList<Map<String, Map<String, Any>>> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_play_vc)

        setMyTitle("臨打")

        citys = intent.getSerializableExtra("citys") as ArrayList<City>
        arenas = intent.getSerializableExtra("arenas") as ArrayList<Arena>
        degrees = intent.getSerializableExtra("degrees") as ArrayList<DEGREE>
        days = intent.getIntegerArrayListExtra("days")
        times = intent.getSerializableExtra("times") as HashMap<String, Any>
        keyword = intent.getStringExtra("keyword")

        prepareParams()

        recyclerView = tempplay_list
        dataService = TeamService
        refreshLayout = tempplay_refresh
        initAdapter()

        refresh()
    }

    override fun initAdapter() {
        tempPlayListAdapter = TempPlayListAdapter(this, { data ->
            show(data)
        }, { city_id ->
            cityBtnPressed(city_id)
        }, { arena_id ->
            arenaBtnPressed(arena_id)
        })
        recyclerView.adapter = tempPlayListAdapter

        recyclerView.setHasFixedSize(true)

        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
    }

    override fun refresh() {
        page = 1
        getDataStart(page, perPage)
        tempPlayListAdapter.notifyDataSetChanged()
    }

    private fun show(data: Map<String, Map<String, Any>>) {
        val position = data["position"]!!["value"] as Int
        val token = data[TEAM_TOKEN_KEY]!!["value"] as String
        //val intent = Intent(activity, TestActivity::class.java)
        val intent = Intent(this, ShowTempPlayActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra(TEAM_TOKEN_KEY, token)
        startActivity(intent)
    }

    private fun cityBtnPressed(city_id: Int) {
        resetParams()
        citys.add(City(city_id, ""))
        prepareParams("all")
        println(city_id)
        refresh()
    }

    private fun arenaBtnPressed(arena_id: Int) {
        resetParams()
        arenas.add(Arena(arena_id, ""))
        prepareParams()
        refresh()
    }

    override fun getDataStart(_page: Int, _perPage: Int) {
        Loading.show(mask)
        TeamService.tempPlay_list(this, params, _page, _perPage) { success ->
            getDataEnd(success)
        }
    }

    override fun notifyDataSetChanged() {
        if (page == 1) {
            dataLists = arrayListOf()
        }
        dataLists.addAll(TeamService.tempPlayLists)
//        var arr: ArrayList<String> = arrayListOf()
//        for (i in 0..dataLists.size-1) {
//            val row = dataLists[i]
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

            tempplay_refresh.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }
}
