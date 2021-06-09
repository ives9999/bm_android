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
import kotlinx.android.synthetic.main.mask.*

class TeachVC : MyTableVC() {

    protected var type: String = "teach"
    protected var titleField: String = "title"

    lateinit var collectionAdapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        searchRows = arrayListOf(
            hashMapOf("title" to "標題關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to "")
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teach_vc)

        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")!!
        }
        setMyTitle("教學")

        dataService = TeachService
        recyclerView = teach_list
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
        setRefreshListener()
    }

    override fun getDataStart(_page: Int, _perPage: Int) {
        //println("page: $_page")
        Loading.show(mask)
        dataService.getList(this, type!!, titleField, params, _page, _perPage, null) { success ->
            getDataEnd(success)
        }
    }

    override fun getDataEnd(success: Boolean) {
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

    override fun setRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            this@TeachVC.page = 1
            this.getDataStart(this.page, this.perPage)
            this.collectionAdapter.notifyDataSetChanged()

            refreshLayout?.isRefreshing = false
        }
        refreshLayout?.setOnRefreshListener(refreshListener)
    }
}
