package com.sportpassword.bm.Fragments


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Adapters.SearchItemDelegate
import com.sportpassword.bm.Controllers.*
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Models.CoursesTable
import com.sportpassword.bm.Models.TeamsTable
import com.sportpassword.bm.Models.SuperData
import com.sportpassword.bm.Models.Tables

import com.sportpassword.bm.R
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Utilities.*
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item


/**
 * A simple [Fragment] subclass.
 * Use the [TabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

open class TabFragment : Fragment(), SearchItemDelegate, List1CellDelegate {

    // TODO: Rename and change types of parameters
    protected var type: String? = null
    protected var screenWidth: Int = 0

    protected var theFirstTime: Boolean = true
    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0

    protected var loading: Boolean = false
    protected lateinit var maskView: View

    protected var superDataLists: ArrayList<SuperData> = arrayListOf()

    protected lateinit var recyclerView: RecyclerView
    protected lateinit var refreshLayout: SwipeRefreshLayout
    //protected lateinit var listAdapter: ListAdapter
    protected lateinit var adapter: GroupAdapter<ViewHolder>
    var sections: ArrayList<String> = arrayListOf()
    protected val adapterSections: ArrayList<Section> = arrayListOf()

    protected lateinit var dataService: DataService
    protected lateinit var that: TabFragment

    protected lateinit var refreshListener: SwipeRefreshLayout.OnRefreshListener
    protected lateinit var scrollerListenr: RecyclerView.OnScrollListener
    //    var vimeoClient: VimeoClient? = null
    var mainActivity: MainActivity? = null

    protected var isCourseShow: Boolean = false
    protected var isTeamShow: Boolean = false

    var params: HashMap<String, Any> = hashMapOf()
    var tables: Tables? = null

    open val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            type = arguments!!.getString(ARG_PARAM1)
            screenWidth = arguments!!.getInt(ARG_PARAM2)
        }
        that = this
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab, container, false)

        return view
    }

//    open fun init() {
//        initAdapter()
//
//        recyclerView.setHasFixedSize(true)
//        setRecyclerViewScrollListener()
//        setRecyclerViewRefreshListener()
//        refresh()
//
//    }

    open fun initAdapter(include_section: Boolean=false) {
        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->
            rowClick(item, view)
        }
        if (include_section) {
            for (section in sections) {
                adapterSections.add(Section())
            }
            for ((idx, title) in sections.withIndex()) {
                val expandableGroup = ExpandableGroup(GroupSection(title), true)
                val items = generateItems(idx)
                adapterSections[idx].addAll(items)
                expandableGroup.add(adapterSections[idx])
                adapter.add(expandableGroup)
            }
        } else {
            val items = generateItems()
            adapter.addAll(items)
        }
        recyclerView.adapter = adapter
    }

//    open protected fun initAdapter() {
//        listAdapter = ListAdapter(context!!, type!!, screenWidth, { data ->
//            var intent: Intent? = null
//            if (type == "course") {
//                intent = Intent(activity, ShowCoachVC::class.java)
//            } else {
//                intent = Intent(activity, ShowActivity::class.java)
//            }
//            intent.putExtra("type", type)
//            intent.putExtra("token", data.token)
//            intent.putExtra("title", data.title)
//            startActivity(intent)
//        }, { data ->
//            var key = CITY_KEY
//            if (type == "course") {
//                key = CITYS_KEY
//            }
//            if (data.data.containsKey(key)) {
//                if (data.data[key]!!.containsKey("value")) {
//                    val city_id = data.data[CITY_KEY]!!["value"] as Int
//                    mainActivity!!.cityBtnPressed(city_id, type!!)
//                }
//            }
//
//        }, { data, address ->
//
//        })
//        recyclerView.adapter = listAdapter
//
//    }

    open fun refresh() {
        page = 1
        getDataStart(page, perPage)
        adapter.notifyDataSetChanged()
    }

    open fun getDataStart1(_page: Int, _perPage: Int) {
        Loading.show(maskView)
        //println("page: $_page")
        //println(mainActivity!!.params)

        dataService.getList1(context!!, null, params, _page, _perPage) { success ->
            getDataEnd1(success)
        }
    }

    fun getDataEnd1(success: Boolean) {
        if (success) {
            if (theFirstTime) {

                if (dataService.jsonString.isNotEmpty()) {
                    //println(dataService.jsonString)
                    genericTable()

                    //superCourses = dataService.superModel as SuperCourses
                    if (tables != null) {
                        page = tables!!.page
                        perPage = tables!!.perPage
                        totalCount = tables!!.totalCount
                        val _totalPage: Int = totalCount / perPage
                        totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
                        theFirstTime = false

                        val items = generateItems()
                        adapter.update(items)
                        adapter.notifyDataSetChanged()
                    } else {
                        mainActivity!!.warning(Global.message)
                        Global.message = ""
                    }
                } else {
                    mainActivity!!.warning("沒有取得回傳的json字串，請洽管理員")
                }

            }

            //notifyDataSetChanged()
            page++
        }
//        mask?.let { mask?.dismiss() }
        Loading.hide(maskView)
        loading = false
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    open fun genericTable() {}

    open fun getDataStart(_page: Int, _perPage: Int) {
        if (isCourseShow || isTeamShow) {
            Loading.show(maskView)
        }
        loading = true
    }

    open fun getDataEnd(success: Boolean) {
        if (success) {
            if (theFirstTime) {
                page = dataService.page
                perPage = dataService.perPage
                totalCount = dataService.totalCount
                var _totalPage: Int = totalCount / perPage
                totalPage = if (totalCount % perPage > 0) _totalPage+1 else _totalPage
                theFirstTime = false
            }

            adapter.notifyDataSetChanged()
            //notifyDataSetChanged()
            page++
        }
//        mask?.let { mask?.dismiss() }
        Loading.hide(maskView)
        loading = false
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    open fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()

        return items
    }

    open fun generateItems(section: Int): ArrayList<Item> {
        return arrayListOf()
    }

    open fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {}

//    open protected fun notifyDataSetChanged() {
//        if (page == 1) {
//            superDataLists = arrayListOf()
//        }
//        superDataLists.addAll(dataService.superDataLists)
////        for (data in superDataLists) {
////            data.print()
////            println("===================")
////        }
//        listAdapter.lists = superDataLists
//        listAdapter.notifyDataSetChanged()
//    }

    open protected fun setRecyclerViewScrollListener() {

        var pos: Int = 0

        scrollerListenr = object: RecyclerView.OnScrollListener() {

        }

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (that.superDataLists.size < that.totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (that.superDataLists.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && that.superDataLists.size < that.totalCount && !that.loading) {
                    that.getDataStart(that.page, that.perPage)
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

    override fun remove(indexPath: IndexPath) {
        val row = _searchRows[indexPath.row]
        val key = row["key"]!!
        when (key) {
            CITY_KEY -> mainActivity!!.citys.clear()
            ARENA_KEY -> mainActivity!!.arenas.clear()
            TEAM_WEEKDAYS_KEY -> mainActivity!!.weekdays.clear()
            TEAM_PLAY_START_KEY -> mainActivity!!.times.clear()
            TEAM_DEGREE_KEY -> mainActivity!!.degrees.clear()
        }
        _searchRows[indexPath.row]["detail"] = "全部"
        val rows = mainActivity!!.generateSearchItems(type!!)
        mainActivity!!.searchAdapter.update(rows)
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
            val fragment = TabFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
