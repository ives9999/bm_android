package com.sportpassword.bm.Fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import com.google.gson.Gson
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Controllers.ShowCourseVC
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.search_row_item.*
import kotlinx.android.synthetic.main.tab_course.*
import kotlinx.android.synthetic.main.tab_list_item.*
import kotlinx.android.synthetic.main.tempplay_signup_one_item.*
import java.lang.Exception

class CourseFragment : TabFragment() {

    //var superCourses: SuperCourses? = null
    var coursesTable: CoursesTable? = null

    override val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to ""),
            hashMapOf("title" to "縣市","key" to CITY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
            hashMapOf("title" to "日期","key" to WEEKDAY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
            hashMapOf("title" to "開始時間之後","key" to START_TIME_KEY,"value" to "","value_type" to "String","show" to "不限"),
            hashMapOf("title" to "結束時間之前","key" to END_TIME_KEY,"value" to "","value_type" to "String","show" to "不限")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity!!.source_activity = "course"
        dataService = CourseService
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_manager, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val memuView = menu!!.findItem(R.id.menu_search_manager).actionView

        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        val ManagerBtn = memuView.findViewById<ImageButton>(R.id.manager)

        searchBtn.tag = type
        ManagerBtn.tag = type
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_course, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        getDataStart1(page, perPage)
    }

    override fun genericTable() {
        if (tables != null) {
            coursesTable = tables as CoursesTable
        }
    }

//    fun initAdapter(include_section: Boolean=false) {
//        adapter = GroupAdapter()
//        adapter.setOnItemClickListener { item, view ->
//            rowClick(item, view)
//        }
//        if (include_section) {
//            for (section in sections) {
//                adapterSections.add(Section())
//            }
//            for ((idx, title) in sections.withIndex()) {
//                val expandableGroup = ExpandableGroup(GroupSection(title), true)
//                val items = generateItems(idx)
//                adapterSections[idx].addAll(items)
//                expandableGroup.add(adapterSections[idx])
//                adapter.add(expandableGroup)
//            }
//        } else {
//            val items = generateItems()
//            adapter.addAll(items)
//        }
//        recyclerView.adapter = adapter
//    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isCourseShow = isVisibleToUser
    }

//    fun getDataEnd1(success: Boolean) {
//        if (success) {
//            if (theFirstTime) {
//
//                if (dataService.jsonString.isNotEmpty()) {
//                    tables = jsonToModel<CoursesTable>(dataService.jsonString)
//
//                    //superCourses = dataService.superModel as SuperCourses
//                    if (tables != null) {
//                        coursesTable = tables as CoursesTable
//                        page = tables!!.page
//                        perPage = tables!!.perPage
//                        totalCount = tables!!.totalCount
//                        val _totalPage: Int = totalCount / perPage
//                        totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
//                        theFirstTime = false
//
//                        val items = generateItems()
//                        adapter.update(items)
//                        adapter.notifyDataSetChanged()
//                    } else {
//                        mainActivity!!.warning(Global.message)
//                        Global.message = ""
//                    }
//                } else {
//                    mainActivity!!.warning("沒有取得回傳的json字串，請洽管理員")
//                }
//
//            }
//
//            //notifyDataSetChanged()
//            page++
//        }
////        mask?.let { mask?.dismiss() }
//        Loading.hide(maskView)
//        loading = false
////        println("page:$page")
////        println("perPage:$perPage")
////        println("totalCount:$totalCount")
////        println("totalPage:$totalPage")
//    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (coursesTable != null) {
            for (row in coursesTable!!.rows) {
                row.filterRow()
                items.add(CourseItem(context!!, row))
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {

        val courseItem = item as CourseItem
        val courseTable = courseItem.courseTable
        //superCourse.print()
        val intent = Intent(activity, ShowCourseVC::class.java)
        intent.putExtra("course_token", courseTable.token)
        intent.putExtra("title", courseTable.title)
        startActivity(intent)
    }

    fun layerSubmit() {
        prepareParams()
        page = 1
        theFirstTime = true
        refresh()
    }

    fun prepareParams() {
        params.clear()
        if (mainActivity!!.keyword.length > 0) {
            val row = getSearchRow(KEYWORD_KEY)
            if (row != null && row.containsKey("value")) {
                row["value"] = mainActivity!!.keyword
                updateSearchRow(KEYWORD_KEY, row)
            }
        }
        for (searchRow in _searchRows) {
            var value_type: String? = null
            if (searchRow.containsKey("value_type")) {
                value_type = searchRow.get("value_type")
            }
            var value: String = ""
            if (searchRow.containsKey("value")) {
                value = searchRow.get("value")!!
            }
            var key: String? = null
            if (searchRow.containsKey("key")) {
                key = searchRow.get("key")!!
            }
            if (value_type != null && key != null && value.length > 0) {
                var values: Array<String>? = null
                if (value_type == "String") {
                    params[key] = value
                } else if (value_type == "Array") {
                    value = searchRow.get("value")!!
                    values = value.split(",").toTypedArray()
                }
                if (values != null) {
                    params[key] = values
                }
            }
        }
    }

    override fun remove(indexPath: IndexPath) {
        var row: HashMap<String, String>? = null
        if (_searchRows.size >= indexPath.row) {
            row = _searchRows[indexPath.row]
        }
        var key: String? = null
        if (row != null && row.containsKey("key") && row.get("key")!!.length > 0) {
            key = row!!.get("key")
        }
        if (row != null) {
            row["value"] = ""
            row["show"] = "不限"
            updateSearchRow(indexPath.row, row)
        }
    }

    fun getSearchRow(key: String): HashMap<String, String>? {
        var row: HashMap<String, String>? = null
        for ((i, searchRow) in _searchRows.withIndex()) {
            if (searchRow.containsKey("key")) {
                if (key == searchRow.get("key")) {
                    row = searchRow
                    break
                }
            }
        }

        return row
    }

    fun updateSearchRow(idx: Int, row: HashMap<String, String>) {
        _searchRows[idx] = row
    }

    fun updateSearchRow(key: String, row: HashMap<String, String>) {
        var idx: Int = -1
        for ((i, searchRow) in _searchRows.withIndex()) {
            if (searchRow.containsKey("key")) {
                if (key == searchRow.get("key")) {
                    idx = i
                    break
                }
            }
        }
        if (idx >= 0) {
            _searchRows[idx] = row
        }
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
            val fragment = CourseFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    class CourseItem(val context: Context, val courseTable: CourseTable): Item() {
        override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

//            val citys = superCourse.coach.citys
//            if (citys.size > 0) {
//                viewHolder.listCityBtn.text = citys[0].name
//            }
            if (courseTable.city_show.length > 0) {
                viewHolder.listCityBtn.text = courseTable.city_show
            } else {
                viewHolder.listCityBtn.visibility = View.GONE
            }
            viewHolder.title.text = courseTable.title
            Picasso.with(context)
                    .load(courseTable.featured_path)
                    .placeholder(R.drawable.loading_square_120)
                    .error(R.drawable.loading_square_120)
                    .into(viewHolder.listFeatured)
            viewHolder.listArenaTxt.text = courseTable.price_text_short
            viewHolder.listDayTxt.text = courseTable.weekday_text
            viewHolder.listIntervalTxt.text = courseTable.start_time_show+"~"+courseTable.end_time_show
            viewHolder.marker.visibility = View.INVISIBLE
        }

        override fun getLayout() = R.layout.tab_list_item

    }
}
