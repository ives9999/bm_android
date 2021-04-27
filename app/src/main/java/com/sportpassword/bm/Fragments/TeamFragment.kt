package com.sportpassword.bm.Fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.CoursesTable
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.Models.TeamsTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.TeamService
import kotlinx.android.synthetic.main.mask.*
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.team_list_cell.*
import kotlinx.android.synthetic.main.tab_course.*

/**
 * Created by ives on 2018/2/25.
 */
class TeamFragment: TabFragment() {

    //var teamsTable: TeamsTable? = null
    var teamsTable: CoursesTable? = null

    var bInit: Boolean = false

    override val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
        hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY),
        hashMapOf("title" to "球館","detail" to "全部","key" to ARENA_KEY),
        hashMapOf("title" to "日期","detail" to "全部","key" to TEAM_WEEKDAYS_KEY),
        hashMapOf("title" to "時段","detail" to "全部","key" to TEAM_PLAY_START_KEY),
        hashMapOf("title" to "程度","detail" to "全部","key" to TEAM_DEGREE_KEY)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //this.dataService = TeamService
        this.dataService = CourseService
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
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
        refresh()
        bInit = true
    }

    override fun refresh() {
        page = 1
        theFirstTime = true
        getDataStart1(page, perPage)
    }

    override fun genericTable() {
        //teamsTable = jsonToModel<TeamsTable>(dataService.jsonString)
        teamsTable = jsonToModel<CoursesTable>(dataService.jsonString)
        if (teamsTable != null) {
            tables = teamsTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (teamsTable != null) {
            for (row in teamsTable!!.rows) {
                row.filterRow()
                //val teamItem = TeamItem(context!!, row)
                val teamItem = CourseItem(context!!, row)
                teamItem.list1CellDelegate = this
                items.add(teamItem)
            }
        }

        return items
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isTeamShow = isVisibleToUser
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
            val fragment = TeamFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}

class TeamItem(val context: Context, val row: TeamTable): Item() {

    var list1CellDelegate: List1CellDelegate? = null

    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

        Picasso.with(context)
                .load(row.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(viewHolder.listFeatured)

        viewHolder.titleLbl.text = row.name

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

        viewHolder.likeIcon.setOnClickListener {
            if (list1CellDelegate != null) {
                list1CellDelegate!!.cellLike(row)
            }
        }

        viewHolder.refreshIcon.setOnClickListener {
            if (list1CellDelegate != null) {
                list1CellDelegate!!.cellRefresh()
            }
        }

        if (row.address == null || row.address.isEmpty()) {
            viewHolder.mapIcon.visibility = View.GONE
        } else {
            viewHolder.mapIcon.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellShowMap(row)
                }
            }
        }

        if (row.mobile == null || row.mobile.isEmpty()) {
            viewHolder.telIcon.visibility = View.GONE
        } else {
            viewHolder.telIcon.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellMobile(row.mobile)
                }
            }
        }
    }

    override fun getLayout() = R.layout.team_list_cell

}