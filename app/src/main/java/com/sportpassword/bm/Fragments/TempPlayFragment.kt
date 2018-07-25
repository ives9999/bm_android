package com.sportpassword.bm.Fragments


import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sportpassword.bm.Adapters.TempPlayListAdapter
import com.sportpassword.bm.Controllers.ShowTempPlayActivity

import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.TEAM_TOKEN_KEY
import kotlinx.android.synthetic.main.tab.*


/**
 * A simple [Fragment] subclass.
 * Use the [TempPlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TempPlayFragment : TabFragment() {

    protected lateinit var tempPlayListAdapter: TempPlayListAdapter
    protected var dataLists1: ArrayList<Map<String, Map<String, Any>>> = arrayListOf()
    protected lateinit var that1: TempPlayFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dataService = TeamService
        that1 = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return view
    }
    override fun initAdapter() {
        tempPlayListAdapter = TempPlayListAdapter(context!!) { data ->
            //println(data)
            val position = data["position"]!!["value"] as Int
            val token = data[TEAM_TOKEN_KEY]!!["value"] as String
            //val intent = Intent(activity, TestActivity::class.java)
            val intent = Intent(activity, ShowTempPlayActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra(TEAM_TOKEN_KEY, token)
            startActivity(intent)

        }
        recyclerView.adapter = tempPlayListAdapter
    }
    override fun getDataStart(_page: Int, _perPage: Int) {
        TeamService.tempPlay_list(context!!, _page, _perPage) { success ->
            getDataEnd(success)
        }
    }
    override fun notifyDataSetChanged() {
        if (page == 1) {
            dataLists1 = arrayListOf()
        }
        dataLists1.addAll(TeamService.tempPlayLists)
        tempPlayListAdapter.lists = dataLists1
        tempPlayListAdapter.notifyDataSetChanged()
    }

    override fun setRecyclerViewScrollListener() {

        var pos: Int = 0

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView!!.layoutManager as GridLayoutManager
                if (that1.dataLists1.size < that1.totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (that1.superDataLists.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && that1.superDataLists.size < that1.totalCount) {
                    that1.getDataStart(that1.page, that1.perPage)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }

    override fun setRecyclerViewRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            that1.page = 1
            this.getDataStart(this.page, this.perPage)
            this.tempPlayListAdapter.notifyDataSetChanged()

            tab_refresh.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "TYPE"
        private val ARG_PARAM2 = "SCREEN_WIDTH"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TabFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: Int): TabFragment {
            val fragment = TempPlayFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
