package com.sportpassword.bm.Controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Fragments.CourseFragment
import com.sportpassword.bm.Models.SuperCourses
import com.sportpassword.bm.Models.SuperData
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.CITY_KEY
import com.sportpassword.bm.Utilities.KEYWORD_KEY
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.PERPAGE
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_coach_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_course.*

class StoreVC : BaseActivity() {

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
            hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY)
    )
    protected lateinit var maskView: View
    protected lateinit var recyclerView: RecyclerView

    protected var theFirstTime: Boolean = true
    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0

    protected lateinit var adapter: GroupAdapter<ViewHolder>
    var sections: ArrayList<String> = arrayListOf()
    protected val adapterSections: ArrayList<Section> = arrayListOf()

    protected var isCourseShow: Boolean = false
    protected var isTeamShow: Boolean = false

    protected var loading: Boolean = false

    var superCourses: SuperCourses? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)

        dataService = CourseService
        //setHasOptionsMenu(true)


        setMyTitle("體育用品店")
//        searchRows = _searchRows
//        recyclerView = coach_list
//        dataService = CoachService
//        refreshLayout = coach_refresh
//        initAdapter()
//
//        refresh()

    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_store_vc, container, false)

        return rootView
    }

    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = list_container
        refreshLayout = tab_refresh
        maskView = mask

        initAdapter(false)
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
        refresh()
    }

    override fun refresh() {
        page = 1
        theFirstTime = true
        getDataStart(page, perPage)
    }

    fun initAdapter(include_section: Boolean=false) {
        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->
            //rowClick(item, view)
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

    fun getDataStart(_page: Int, _perPage: Int) {
        if (isCourseShow || isTeamShow) {
            Loading.show(maskView)
        }
        loading = true
        Loading.show(maskView)
        //println("page: $_page")
        //println(mainActivity!!.params)
        dataService.getList(this, null, params, _page, _perPage) { success ->
            getDataEnd(success)
        }
    }

    fun getDataEnd(success: Boolean) {
        if (success) {
            if (theFirstTime) {

                superCourses = dataService.superModel as SuperCourses
                if (superCourses != null) {
                    page = superCourses!!.page
                    perPage = superCourses!!.perPage
                    totalCount = superCourses!!.totalCount
                    var _totalPage: Int = totalCount / perPage
                    totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
                    theFirstTime = false

                    val items = generateItems()
                    adapter.update(items)
                    adapter.notifyDataSetChanged()
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

    fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (superCourses != null) {
            for (row in superCourses!!.rows) {
                items.add(CourseFragment.CourseItem(this, row))
            }
        }

        return items
    }
    fun generateItems(section: Int): ArrayList<Item> {
        return arrayListOf()
    }


    protected fun setRecyclerViewScrollListener() {

        var pos: Int = 0

        scrollerListenr = object: RecyclerView.OnScrollListener() {

        }

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView!!.layoutManager as GridLayoutManager
                if (superDataLists.size < totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (superDataLists.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && superDataLists.size < totalCount && !loading) {
                    getDataStart(page, perPage)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }

    protected fun setRecyclerViewRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            refresh()

            refreshLayout.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }
}