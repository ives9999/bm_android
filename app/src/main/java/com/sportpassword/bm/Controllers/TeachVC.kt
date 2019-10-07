package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.sportpassword.bm.Adapters.CollectionAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeachService
import kotlinx.android.synthetic.main.activity_teach_vc.*
import com.sportpassword.bm.Utilities.*

class TeachVC : MoreVC() {

    lateinit var collectionAdapter: CollectionAdapter
    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teach_vc)

        setMyTitle("教學")

        searchRows = _searchRows
        recyclerView = teach_list
        dataService = TeachService
        refreshLayout = teach_refresh
        initAdapter()

        refresh()
    }

    override fun refresh() {
        page = 1
        getDataStart(page, perPage)
        collectionAdapter.notifyDataSetChanged()
    }

    override fun initAdapter(include_section: Boolean) {
        collectionAdapter = CollectionAdapter(this, type!!, screenWidth) { data ->
            val intent = Intent(this, ShowActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("token", data.token)
            intent.putExtra("title", data.title)
            startActivity(intent)
        }
        recyclerView.adapter = collectionAdapter
        val layoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
    }

    override fun notifyDataSetChanged() {
        if (page == 1) {
            superDataLists = arrayListOf()
        }
        superDataLists.addAll(dataService.superDataLists)
//        for (data in superDataLists) {
//            data.print()
//            println("===================")
//        }
        collectionAdapter.lists = superDataLists
        collectionAdapter.notifyDataSetChanged()
    }

    override protected fun setRecyclerViewRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            this@TeachVC.page = 1
            this.getDataStart(this.page, this.perPage)
            this.collectionAdapter.notifyDataSetChanged()

            refreshLayout.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }
}
