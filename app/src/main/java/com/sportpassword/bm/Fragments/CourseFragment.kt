package com.sportpassword.bm.Fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import com.google.gson.JsonParseException
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_course.*
import kotlinx.android.synthetic.main.course_list_cell.view.*
import org.jetbrains.anko.support.v4.runOnUiThread

class CourseFragment : TabFragment() {

    var mysTable: CoursesTable? = null
    lateinit var tableAdapter: CourseAdapter

    var bInit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        searchRows = arrayListOf(
            hashMapOf("title" to "關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to ""),
            hashMapOf("title" to "縣市","key" to CITY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
            hashMapOf("title" to "星期幾","key" to WEEKDAY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
            hashMapOf("title" to "開始時間","key" to START_TIME_KEY,"value" to "","value_type" to "String","show" to "不限"),
            hashMapOf("title" to "結束時間","key" to END_TIME_KEY,"value" to "","value_type" to "String","show" to "不限")
        )
        able_type = "course"
        super.onCreate(savedInstanceState)

        dataService = CourseService

        //initAdapter(false)
//        adapter = GroupAdapter()
//        adapter.setOnItemClickListener { item, view ->
//            rowClick(item, view)
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_course, container, false)
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = list_container
        refreshLayout = tab_refresh
        maskView = mask
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()

        tableAdapter = CourseAdapter(R.layout.course_list_cell, this)
        recyclerView.adapter = tableAdapter

        refresh()
        bInit = true
    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            mysTable = jsonToModels<CoursesTable>(jsonString)
        } catch (e: JsonParseException) {
            println(e.localizedMessage)
        }
        if (mysTable != null) {
            tables = mysTable
            getPage()
            tableLists += generateItems1(CourseTable::class, mysTable!!.rows)
            tableAdapter.setMyTableList(tableLists)
            runOnUiThread {
                tableAdapter.notifyDataSetChanged()
            }
        }
    }

    fun prepare(idx: Int) {

        val row = searchRows.get(idx)
        var key: String = ""
        if (row.containsKey("key")) {
            key = row["key"]!!
        }

        var value: String = ""
        if (row.containsKey("value")) {
            value = row["value"]!!
        }
        if (key == CITY_KEY) {
            mainActivity!!.toSelectCity(value, null, able_type)
        } else if (key == WEEKDAY_KEY) {
            mainActivity!!.toSelectWeekday(value, null, able_type)
        } else if (key == START_TIME_KEY || key == END_TIME_KEY) {

            mainActivity!!.toSelectTime(key, value, null, able_type)
        }
    }


    //當fragment啟動時，第一個被執行的韓式，甚至還在OnCreate函式之前
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mainActivity?.isSearchIconShow = true
        if (isVisibleToUser && !bInit) {
            refresh()
        }
    }

//    override fun generateItems(): ArrayList<Item> {
//        if (mysTable != null) {
//            for (row in mysTable!!.rows) {
//                row.filterRow()
//                val myItem = CourseItem(requireContext(), row)
//                myItem.list1CellDelegate = this
//                items.add(myItem)
//            }
//        }
//
//        return items
//    }
//
//    override fun rowClick(item: com.xwray.groupie.Item<GroupieViewHolder>, view: View) {
//
//        val courseItem = item as CourseItem
//        val table = courseItem.row
//        mainActivity!!.toShowCourse(table.token)
//    }

    override fun cellCity(row: Table) {
        println(row::class)
        mainActivity!!.toShowCourse(row.token)
    }

    fun remove(indexPath: IndexPath) {
        var row: HashMap<String, String>? = null
        if (searchRows.size >= indexPath.row) {
            row = searchRows[indexPath.row]
        }
        var key: String? = null
        if (row != null && row.containsKey("key") && row.get("key")!!.length > 0) {
            key = row.get("key")
        }
        if (row != null) {
            row["value"] = ""
            row["show"] = "不限"
            replaceRows(key!!, row)
        }
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
            val fragment = CourseFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}

class CourseAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<CourseViewHolder>(resource, ::CourseViewHolder, list1CellDelegate) {}

class CourseViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: CourseTable = _row as CourseTable

        if (row.city_show.length > 0) {
            viewHolder.cityBtn.text = row.city_show
            viewHolder.cityBtn.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellCity(row)
                }
            }
        } else {
            viewHolder.cityBtn.visibility = View.GONE
        }

        if (row.price_text_short != null && row.price_text_short.isNotEmpty()) {
            viewHolder.priceLbl.text = row.price_text_short
        } else {
            viewHolder.priceLbl.text = "價格:未提供"
        }

        if (row.weekdays_show.length > 0) {
            viewHolder.weekdayLbl.text = row.weekdays_show
        } else {
            viewHolder.weekdayLbl.text = "未提供"
        }

        if (row.interval_show.length > 0) {
            viewHolder.intervalLbl.text = row.interval_show
        } else {
            viewHolder.intervalLbl.text = "未提供"
        }

        viewHolder.people_limitLbl.text = row.people_limit_show

        if (row.signup_count_show.length > 0 && row.people_limit > 0) {
            viewHolder.signup_countLbl.text = "已報名:${row.signup_count_show}"
        } else {
            viewHolder.signup_countLbl.visibility = View.INVISIBLE
        }
    }
}

//class CourseItem(override var context: Context, var _row: CourseTable): ListItem<Table>(context, _row) {
//
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
//
//        super.bind(viewHolder, position)
//
//        val row: CourseTable = _row
//
//        if (row.city_show.length > 0) {
//            viewHolder.cityBtn.text = row.city_show
//            viewHolder.cityBtn.setOnClickListener {
//                if (list1CellDelegate != null) {
//                    list1CellDelegate!!.cellCity(row)
//                }
//            }
//        } else {
//            viewHolder.cityBtn.visibility = View.GONE
//        }
//
//        if (row.price_text_short != null && row.price_text_short.isNotEmpty()) {
//            viewHolder.priceLbl.text = row.price_text_short
//        } else {
//            viewHolder.priceLbl.text = "價格:未提供"
//        }
//
//        if (row.weekdays_show.length > 0) {
//            viewHolder.weekdayLbl.text = row.weekdays_show
//        } else {
//            viewHolder.weekdayLbl.text = "未提供"
//        }
//
//        if (row.interval_show.length > 0) {
//            viewHolder.intervalLbl.text = row.interval_show
//        } else {
//            viewHolder.intervalLbl.text = "未提供"
//        }
//
//        viewHolder.people_limitLbl.text = row.people_limit_show
//
//        if (row.signup_count_show.length > 0 && row.people_limit > 0) {
//            viewHolder.signup_countLbl.text = "已報名:${row.signup_count_show}"
//        } else {
//            viewHolder.signup_countLbl.visibility = View.INVISIBLE
//        }
//    }
//
//    override fun getLayout() = R.layout.course_list_cell
//
//}
