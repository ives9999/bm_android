package com.sportpassword.bm.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import com.sportpassword.bm.Adapters.CollectionAdapter
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import kotlinx.android.synthetic.main.activity_course_vc.*
import org.jetbrains.anko.contentView

class CourseVC : MoreVC() {

    lateinit var collectionAdapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_vc)

        setMyTitle("教學")
        recyclerView = course_list
        dataService = CourseService
        refreshLayout = course_refresh
        initAdapter()

        refresh()
    }

    override fun initAdapter() {
        collectionAdapter = CollectionAdapter(this, type!!, screenWidth) { data ->
            val intent = Intent(this, ShowActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("token", data.token)
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
            this@CourseVC.page = 1
            this.getDataStart(this.page, this.perPage)
            this.collectionAdapter.notifyDataSetChanged()

            refreshLayout.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }

}