package com.sportpassword.bm.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Models.Data

import com.sportpassword.bm.R
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Utilities.PERPAGE
import kotlinx.android.synthetic.main.tab.*


/**
 * A simple [Fragment] subclass.
 * Use the [TabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

open class TabFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    protected var theFirstTime: Boolean = true
    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0

    protected var dataLists: ArrayList<Data> = arrayListOf()

    protected lateinit var recyclerView: RecyclerView
    protected lateinit var refreshLayout: SwipeRefreshLayout
    protected lateinit var scrollerListenr: RecyclerView.OnScrollListener
    protected lateinit var listAdapter: ListAdapter
    protected lateinit var dataService: DataService
    protected lateinit var that: TabFragment

    protected lateinit var refreshListener: SwipeRefreshLayout.OnRefreshListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
        that = this
        //println("TabFragment onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab, container, false)
        listAdapter = ListAdapter(context!!) { data ->

        }
        recyclerView = view.findViewById<RecyclerView>(R.id.list_container)
        recyclerView.adapter = listAdapter

        val layoutManager = GridLayoutManager(context, 1)
        //lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)

        refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.tab_refresh)

        getDataStart(page, perPage)

        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()

        return view
    }

    open protected fun getDataStart(_page: Int, _perPage: Int) {}

    open protected fun getDataEnd(success: Boolean) {
        if (success) {
            if (page == 1) {
                dataLists = arrayListOf()
            }
            dataLists.addAll(dataService.dataLists)
            if (theFirstTime) {
                page = dataService.page
                perPage = dataService.perPage
                totalCount = dataService.totalCount
                var _totalPage: Int = totalCount / perPage
                totalPage = if (totalCount % perPage > 0) _totalPage+1 else _totalPage
                theFirstTime = false
            }

            listAdapter.lists = dataLists
            listAdapter.notifyDataSetChanged()
            page++
        }
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    protected fun setRecyclerViewScrollListener() {

        var pos: Int = 0

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView!!.layoutManager as GridLayoutManager
                if (that.dataLists.size < that.totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (that.dataLists.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && that.dataLists.size < that.totalCount) {
                    that.getDataStart(that.page, that.perPage)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }

    protected fun setRecyclerViewRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            that.page = 1
            this.getDataStart(this.page, this.perPage)
            this.listAdapter.notifyDataSetChanged()

            tab_refresh.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TabFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): TabFragment {
            val fragment = TabFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
