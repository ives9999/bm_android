package com.sportpassword.bm.Fragments

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Controllers.ShowCourseVC
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.TeamService
import kotlinx.android.synthetic.main.mask.*
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.team_list_cell.*
import kotlinx.android.synthetic.main.tab_course.*

/**
 * Created by ives on 2018/2/25.
 */
class TeamFragment: TabFragment() {

    var mysTable: TeamsTable? = null

    var bInit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        searchRows = arrayListOf(
            hashMapOf("title" to "關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to ""),
            hashMapOf("title" to "縣市","show" to "全部","key" to CITY_KEY,"value" to ""),
            hashMapOf("title" to "球館","show" to "全部","key" to ARENA_KEY,"value" to ""),
            hashMapOf("title" to "星期幾","show" to "全部","key" to WEEKDAY_KEY,"value" to ""),
            hashMapOf("title" to "時段","show" to "全部","key" to START_TIME_KEY,"value" to ""),
            hashMapOf("title" to "程度","show" to "全部","key" to DEGREE_KEY,"value" to "")
        )
        able_type = "team"

        super.onCreate(savedInstanceState)

        this.dataService = TeamService
        setHasOptionsMenu(true)

        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->
            rowClick(item, view)
        }

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
        val rootView = inflater.inflate(R.layout.tab_course, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = list_container
        refreshLayout = tab_refresh
        maskView = mask

        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()

        recyclerView.adapter = adapter

        refresh()
        bInit = true
    }

    override fun refresh() {
        page = 1
        params.clear()
        theFirstTime = true
        getDataStart1(page, perPage)
    }

    override fun genericTable() {
        mysTable = jsonToModels<TeamsTable>(dataService.jsonString)
        if (mysTable != null) {
            tables = mysTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        if (mysTable != null) {
            for (row in mysTable!!.rows) {
                row.filterRow()
                val myItem = TeamItem(requireContext(), row)
                myItem.list1CellDelegate = this
                items.add(myItem)
            }
        }

        return items
    }

    override fun prepare(idx: Int) {

        var row = searchRows.get(idx)
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
        } else if (key == ARENA_KEY) {
            row = getDefinedRow(CITY_KEY)
            var city_id: Int? = null
            if (row.containsKey("value") && row["value"] != null && row["value"]!!.length > 0) {
                city_id = row["value"]!!.toInt()
            }
            if (city_id != null && city_id > 0) {
                mainActivity!!.toSelectArena(value, city_id, null, able_type)
            } else {
                mainActivity!!.warning("請先選擇縣市")
            }
        } else if (key == WEEKDAY_KEY) {
            mainActivity!!.toSelectWeekday(value, null, able_type)
        } else if (key == START_TIME_KEY || key == END_TIME_KEY) {

            mainActivity!!.toSelectTime(key, value, null, able_type)
        } else if (key == DEGREE_KEY) {
            mainActivity!!.toSelectDegree(value, null, able_type)
        }
    }

    override fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {

        val teamItem = item as TeamItem
        val table = teamItem.row
        mainActivity!!.toShowTeam(table.token)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && bInit) {
            refresh()
        }
    }

//    override fun getDataStart(_page: Int, _perPage: Int) {
//        super.getDataStart(_page, _perPage)
//        Loading.show(maskView)
//        //println("page: $_page")
//        //println(mainActivity!!.params)
//        TeamService.getList(context!!, "team", "name", mainActivity!!.params, _page, _perPage, null) { success ->
//            getDataEnd(success)
//        }
//    }

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
            val fragment = TeamFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}

class TeamItem(override var context: Context, var _row: TeamTable): ListItem<Table>(context, _row) {

    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

        super.bind(viewHolder, position)

        val row: TeamTable = _row

        if (row.city_show.length > 0) {
            viewHolder.cityBtn.text = row.city_show
        } else {
            viewHolder.cityBtn.visibility = View.GONE
        }

        if (row.arena != null) {
            viewHolder.arenaBtn.text = row.arena!!.name
        } else {
            viewHolder.arenaBtn.visibility = View.GONE
        }

        if (row.weekdays_show.isNotEmpty()) {
            viewHolder.weekdayLbl.text = row.weekdays_show
        } else {
            viewHolder.weekdayLbl.text = "臨打日期:未提供"
        }

        if (row.interval_show.isNotEmpty()) {
            viewHolder.intervalLbl.text = row.interval_show
        } else {
            viewHolder.weekdayLbl.text = "臨打時段:未提供"
        }

        viewHolder.temp_quantityLbl.text = row.temp_quantity_show
        viewHolder.signup_countLbl.text = row.temp_signup_count_show
    }

    override fun getLayout() = R.layout.team_list_cell

}