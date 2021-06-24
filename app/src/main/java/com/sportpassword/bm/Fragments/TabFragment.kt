package com.sportpassword.bm.Fragments


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Adapters.SearchItemDelegate
import com.sportpassword.bm.Controllers.*
import com.sportpassword.bm.Models.*

import com.sportpassword.bm.R
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.course_list_cell.*
import kotlinx.android.synthetic.main.course_list_cell.likeIcon
import kotlinx.android.synthetic.main.course_list_cell.listFeatured
import kotlinx.android.synthetic.main.course_list_cell.refreshIcon
import kotlinx.android.synthetic.main.course_list_cell.telIcon
import kotlinx.android.synthetic.main.course_list_cell.titleLbl
import kotlinx.android.synthetic.main.list1_cell.*
import kotlinx.android.synthetic.main.list1_cell.mapIcon
import kotlinx.android.synthetic.main.team_list_cell.*
import java.io.Serializable


/**
 * A simple [Fragment] subclass.
 * Use the [TabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

open class TabFragment : Fragment(), SearchItemDelegate, List1CellDelegate, Serializable {

    // TODO: Rename and change types of parameters
    protected var type: String? = null
    protected var screenWidth: Int = 0

    protected var theFirstTime: Boolean = true
    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0
    var items: ArrayList<Item> = arrayListOf()

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
    protected var jsonString: String = ""
    protected lateinit var that: TabFragment

    protected lateinit var refreshListener: SwipeRefreshLayout.OnRefreshListener
    protected lateinit var scrollerListenr: RecyclerView.OnScrollListener
    //    var vimeoClient: VimeoClient? = null
    var mainActivity: MainActivity? = null

    protected var isCourseShow: Boolean = false
    protected var isTeamShow: Boolean = false

    var params: HashMap<String, Any> = hashMapOf()
    var tables: Tables? = null
    var able_type: String = "course"
    var member_like: Boolean = false

    var searchRows: ArrayList<HashMap<String, String>> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            type = requireArguments().getString(ARG_PARAM1)
            screenWidth = requireArguments().getInt(ARG_PARAM2)
        }
        that = this
        mainActivity = activity as MainActivity
        mainActivity!!.able_type = able_type

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
        theFirstTime = true
        adapter.clear()
        items.clear()
        //println(perPage)
        getDataStart1(page, perPage)
    }

    open fun getDataStart1(_page: Int, _perPage: Int) {
        Loading.show(maskView)
        //println("page: $_page")
        //println(mainActivity!!.params)
        loading = true

        if (member_like) {
            //var _able_type: String = able_type
            //if (able_type == "temp_play") _able_type = "team"
            MemberService.likelist(requireContext(), able_type, "喜歡", _page, _perPage) { success ->
                jsonString = MemberService.jsonString
                getDataEnd1(success)
            }
        } else {
            dataService.getList1(requireContext(), null, params, _page, _perPage) { success ->
                jsonString = dataService.jsonString
                getDataEnd1(success)
            }
        }
    }

    fun getDataEnd1(success: Boolean) {
        if (success) {
            //if (theFirstTime) {

                if (jsonString.isNotEmpty()) {
                    //println(jsonString)
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

            //}

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
                val _totalPage: Int = totalCount / perPage
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

    open fun prepare(idx: Int) {}

    open fun prepareParams() {
        params.clear()

        for (searchRow in searchRows) {
//            var value_type: String? = null
//            if (searchRow.containsKey("value_type")) {
//                value_type = searchRow.get("value_type")
//            }

            var key: String? = null
            if (searchRow.containsKey("key")) {
                key = searchRow.get("key")!!
            }

            if (key == null) {
                 continue
            }

            var value: String = ""
            if (searchRow.containsKey("value")) {
                value = searchRow.get("value")!!
            }
            if (value.isEmpty()) {
                continue
            }

            params[key] = value
        }
        println(params)
    }

    fun getDefinedRow(key: String): HashMap<String, String> {

        for (row in searchRows) {
            if (row["key"] == key) {
                return row
            }
        }

        return hashMapOf()
    }

    fun replaceRows(key: String, row: HashMap<String, String>) {
        for ((idx, _row) in searchRows.withIndex()) {
            if (_row["key"] == key) {
                searchRows[idx] = row
                break
            }
        }
    }

    open fun singleSelected(key: String, selected: String) {

        val row = getDefinedRow(key)
        var show = ""
        if (key == START_TIME_KEY || key == END_TIME_KEY) {
            row["value"] = selected
            show = selected.noSec()
        } else if (key == CITY_KEY || key == AREA_KEY) {
            row["value"] = selected
            show = Global.zoneIDToName(selected.toInt())
        } else if (key == WEEKDAY_KEY) {
            row["value"] = selected
            show = WEEKDAY.intToString(selected.toInt())
        }

        row["show"] = show
        replaceRows(key, row)

        val rows = mainActivity!!.searchPanel.generateSearchItems()
        mainActivity!!.searchPanel.searchAdapter.update(rows)
    }

    open fun arenaSelected(selected: String, show: String) {

        val key: String = ARENA_KEY
        val row = getDefinedRow(key)
        row["value"] = selected
        row["show"] = show
        replaceRows(key, row)
        updateAdapter()
    }

    open fun degreeSelected(selected: String, show: String) {
        val key: String = DEGREE_KEY
        val row = getDefinedRow(key)
        row["value"] = selected
        row["show"] = show
        replaceRows(key, row)
        updateAdapter()
    }

    protected fun updateAdapter() {
        val rows =  mainActivity!!.generateSearchItems(able_type)
        mainActivity!!.searchPanel.searchAdapter.update(rows)
    }

    override fun cellRefresh() {
        params.clear()
        refresh()
    }

    override fun cellShowMap(row: Table) {
        println(row.address)
    }

    override fun cellMobile(row: Table) {
        if (row.tel_show.isNotEmpty()) {
            println(row.tel)
        } else if (row.mobile_show.isNotEmpty()) {
            println(row.mobile)
        }
    }

    override fun cellLike(row: Table) {
        if (!member.isLoggedIn) {
            mainActivity!!.toLogin()
        } else {
            dataService.like(mainActivity!!, row.token, row.id)
        }
    }

    override fun cellCity(row: Table) {
        val key: String = CITY_KEY
        val city_id: Int = row.city_id
        val row = getDefinedRow(key)
        row["value"] = city_id.toString()
        replaceRows(key, row)
        prepareParams()
        refresh()
    }

    open protected fun setRecyclerViewScrollListener() {

        var pos: Int = 0

        scrollerListenr = object: RecyclerView.OnScrollListener() {

        }

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (items.size < that.totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                    //println("pos:${pos}")
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //println("items.size:${items.size}")
                if (items.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && items.size < totalCount && !loading) {
                    that.getDataStart1(page, perPage)
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
        val row = searchRows[indexPath.row]
        val key = row["key"]!!
        when (key) {
            CITY_KEY -> {
                mainActivity!!.citys.clear()
                row["show"] = "全部"
            }
            ARENA_KEY -> {
                mainActivity!!.arenas.clear()
                row["show"] = "全部"
            }
            WEEKDAY_KEY -> {
                mainActivity!!.weekdays.clear()
                row["show"] = "全部"
            }
            START_TIME_KEY -> {
                mainActivity!!.times.clear()
                row["show"] = "全部"
            }
            END_TIME_KEY -> {
                mainActivity!!.times.clear()
                row["show"] = "全部"
            }
            DEGREE_KEY -> {
                mainActivity!!.degrees.clear()
                row["show"] = "全部"
            }
        }
        row["value"] = ""
    }
    override fun textChanged(str: String) {
        val key: String = KEYWORD_KEY
        val row = getDefinedRow(key)
        row["value"] = str
    }

    override fun switchChanged(pos: Int, b: Boolean) {}


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

open class ListItem<T: Table>(open var context: Context, open var row: T): Item() {

    var list1CellDelegate: List1CellDelegate? = null
    var isLike: Boolean = false

    override fun bind(
        viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder,
        position: Int
    ) {
        val v = viewHolder.containerView

        if (row.title.isNotEmpty()) {
            viewHolder.titleLbl.text = row.title
        } else {
            viewHolder.titleLbl.text = row.name
        }

        Picasso.with(context)
            .load(row.featured_path)
            .placeholder(R.drawable.loading_square_120)
            .error(R.drawable.loading_square_120)
            .into(viewHolder.listFeatured)

        viewHolder.refreshIcon.setOnClickListener {
            if (list1CellDelegate != null) {
                list1CellDelegate!!.cellRefresh()
            }
        }

        var a = v.findViewById<ImageButton>(R.id.telIcon)
        if (a != null) {
            if (row.mobile_show.isNotEmpty()) {
                if (list1CellDelegate != null) {
                    viewHolder.telIcon.setOnClickListener {
                        list1CellDelegate!!.cellMobile(row)
                    }
                    viewHolder.telIcon.visibility = View.VISIBLE
                }
            } else if (row.tel_show.isNotEmpty()) {
                if (list1CellDelegate != null) {
                    viewHolder.telIcon.setOnClickListener {
                        list1CellDelegate!!.cellMobile(row)
                    }
                    viewHolder.telIcon.visibility = View.VISIBLE
                }
            } else {
                viewHolder.telIcon.visibility = View.GONE
            }
        }

        a = v.findViewById<ImageButton>(R.id.mapIcon)
        if (a != null) {

            if (row.address == null || row.address.isEmpty()) {
                viewHolder.mapIcon.visibility = View.GONE
            } else {
                viewHolder.mapIcon.setOnClickListener {
                    if (list1CellDelegate != null) {
                        list1CellDelegate!!.cellShowMap(row)
                    }
                }
            }
        }

        viewHolder.likeIcon.setOnClickListener {
            if (list1CellDelegate != null) {
                list1CellDelegate!!.cellLike(row)
                if (member.isLoggedIn) {
                    setLike(viewHolder)
                }
            }
        }

        isLike = !row.like
        setLike(viewHolder)
    }

    override fun getLayout() = R.layout.course_list_cell

    fun setLike(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder) {
        isLike = !isLike
        if (isLike) {
            viewHolder.likeIcon.setImage("like1")
        } else {
            viewHolder.likeIcon.setImage("like")
        }
    }
}

