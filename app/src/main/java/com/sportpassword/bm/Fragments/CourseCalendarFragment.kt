package com.sportpassword.bm.Fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.Models.SuperCourses
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.course_calendar_item.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_course.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CourseCalendarFragment : TabFragment() {

    var superCourses: SuperCourses? = null

    var year: Int = Date().getY()
    var month: Int = Date().getm()
    var monthLastDay: Int = 31

    override val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to "不限"),
            hashMapOf("title" to "縣市","key" to CITY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
            hashMapOf("title" to "日期","key" to WEEKDAY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
            hashMapOf("title" to "開始時間之後","key" to START_TIME_KEY,"value" to "","value_type" to "String","show" to "不限"),
            hashMapOf("title" to "結束時間之前","key" to END_TIME_KEY,"value" to "","value_type" to "String","show" to "不限")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataService = CourseService
        setHasOptionsMenu(true)

        monthLastDay = Global.getMonthLastDay(year, month)
        //println(monthLastDay)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_manager, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val memuView = menu.findItem(R.id.menu_search_manager).actionView

        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        //val ManagerBtn = memuView.findViewById<ImageButton>(R.id.manager)

        searchBtn.tag = type
        //ManagerBtn.tag = type
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_course_calendar, container, false)

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
        //refresh()

    }

    override fun initAdapter(include_section: Boolean) {
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isCourseShow = isVisibleToUser
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        for (i in 1..monthLastDay) {

            val day: Int = i
            val date: String = "%4d-%02d-%02d".format(year, month, i)
            //println(date)
            var rows: ArrayList<SuperCourse> = arrayListOf()
            if (superCourses != null) {
                rows = superCourses!!.rows
            }
            items.add(CalendarItem(context!!, date, rows))

        }

        return items
    }
    override fun generateItems(section: Int): ArrayList<Item> {
        return arrayListOf()
    }

    class CalendarItem(val context: Context, val date: String, val superCourses: ArrayList<SuperCourse>): Item() {

        override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

            val d: Date = date.toDate()!!
            val weekday_i = d.dateToWeekday()
            //println(weekday_i)
            val weekday_c: String = d.dateToWeekdayForChinese()

            viewHolder.date.text = "%s(%s)".format(date, weekday_c)
        }

        override fun getLayout() = R.layout.course_calendar_item

    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "TYPE"
        private const val ARG_PARAM2 = "SCREEN_WIDTH"

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
            val fragment = CourseCalendarFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}