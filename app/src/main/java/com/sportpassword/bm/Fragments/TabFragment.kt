package com.sportpassword.bm.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Models.Data

import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService


/**
 * A simple [Fragment] subclass.
 * Use the [TabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
interface Factory<T> {
    fun newInstance(): T
}

open class TabFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    protected var page: Int = 1

    protected lateinit var recyclerView: RecyclerView
    protected var lastVisibleItemPosition: Int = 0
    protected lateinit var scrollerListenr: RecyclerView.OnScrollListener
    protected lateinit var listAdapter: ListAdapter

    protected lateinit var refreshListener: SwipeRefreshLayout.OnRefreshListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    protected fun setAdapter(view: View, dataLists: ArrayList<Data>) {
        listAdapter = ListAdapter(context!!, dataLists) { data ->

        }
        recyclerView = view.findViewById<RecyclerView>(R.id.list_container)
        recyclerView.adapter = listAdapter

        val layoutManager = GridLayoutManager(context, 1)
        lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
    }
    protected fun setRecyclerViewScrollListener() {
        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //println("dy: $dy")
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalCount = recyclerView!!.layoutManager.itemCount
                //val position = recyclerView!!.layoutManager.findL
                println("totalCount: $totalCount")
                println("position: $lastVisibleItemPosition")

                if (totalCount == lastVisibleItemPosition + 1) {
                    recyclerView!!.removeOnScrollListener(scrollerListenr)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }

    protected fun setRecyclerViewRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            Thread.sleep(200)
        }
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
