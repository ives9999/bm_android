package com.sportpassword.bm.Fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import com.google.gson.JsonParseException
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.arena_list_cell.*
import kotlinx.android.synthetic.main.arena_list_cell.view.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_course.*
import org.jetbrains.anko.support.v4.runOnUiThread

class ArenaFragment : TabFragment() {

    var mysTable: ArenasTable? = null
    lateinit var tableAdapter: ArenaAdapter

    var bInit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        searchRows = arrayListOf(
            hashMapOf("title" to "關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to ""),
            hashMapOf("title" to "縣市","show" to "全部","key" to CITY_KEY,"value" to ""),
            hashMapOf("title" to "區域","show" to "全部","key" to AREA_KEY,"value" to ""),
            hashMapOf("title" to "空調","show" to "全部","key" to ARENA_AIR_CONDITION_KEY,"switch" to "true","value" to ""),
            hashMapOf("title" to "盥洗室","show" to "全部","key" to ARENA_BATHROOM_KEY,"switch" to "true","value" to ""),
            hashMapOf("title" to "停車場","show" to "全部","key" to ARENA_PARKING_KEY,"switch" to "true","value" to "")
        )

        able_type = "arena"
        super.onCreate(savedInstanceState)

        dataService = ArenaService

        //initAdapter(false)
//        adapter = GroupAdapter()
//        adapter.setOnItemClickListener { item, view ->
//            rowClick(item, view)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val memuView = menu.findItem(R.id.menu_all).actionView
        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        searchBtn.visibility = View.VISIBLE
        searchBtn.tag = able_type
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_course, container, false)
        setHasOptionsMenu(true)//出現上方的menu

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = list_container
        refreshLayout = tab_refresh
        maskView = mask
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()

        tableAdapter = ArenaAdapter(R.layout.arena_list_cell, this)
        recyclerView.adapter = tableAdapter

        refresh()
        bInit = true
    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            mysTable = jsonToModels<ArenasTable>(jsonString)
        } catch (e: JsonParseException) {
            println(e.localizedMessage)
        }
        if (mysTable != null) {
            tables = mysTable
            getPage()
            tableLists += generateItems1(ArenaTable::class, mysTable!!.rows)
            tableAdapter.setMyTableList(tableLists)
            runOnUiThread {
                tableAdapter.notifyDataSetChanged()
            }

        }
    }

    fun prepare(idx: Int) {

        var row = searchRows.get(idx)
        var key: String = ""
        if (row.containsKey("key")) {
            key = row["key"]!!
        }

        var value: String = ""
        if (row.containsKey("value") && row["value"]!!.isNotEmpty()) {
            value = row["value"]!!
        }

        when (key) {
            CITY_KEY -> {
                mainActivity!!.toSelectCity(value, null, able_type)
            }
            AREA_KEY -> {
                row = getDefinedRow(CITY_KEY)
                var city_id: Int? = null
                if (row.containsKey("value") && row["value"] != null && row["value"]!!.length > 0) {
                    city_id = row["value"]!!.toInt()
                }
                if (city_id != null && city_id > 0) {
                    mainActivity!!.toSelectArea(value, city_id, null, able_type)
                } else {
                    mainActivity!!.warning("請先選擇縣市")
                }
            }
        }
    }


    //當fragment啟動時，第一個被執行的韓式，甚至還在OnCreate函式之前
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && bInit) {
            refresh()
        }
    }

//    override fun generateItems(): ArrayList<Item> {
//        if (mysTable != null) {
//            for (row in mysTable!!.rows) {
//                row.filterRow()
//                val myItem = ArenaItem(requireContext(), row)
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
//        val arenaItem = item as ArenaItem
//        val table = arenaItem.row
//        mainActivity!!.toShowArena(table.token)
//    }

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
            val fragment = ArenaFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}

class ArenaAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<ArenaViewHolder>(resource, ::ArenaViewHolder, list1CellDelegate) {}

class ArenaViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: ArenaTable = _row as ArenaTable

        if (row.city_show.isNotEmpty()) {
            viewHolder.cityBtn.text = row.city_show
            viewHolder.cityBtn.setOnClickListener {
                list1CellDelegate?.cellCity(row)
            }
        } else {
            viewHolder.cityBtn.visibility = View.GONE
        }

        if (row.area_show.isNotEmpty()) {
            viewHolder.areaBtn.text = row.area_show
            viewHolder.areaBtn.setOnClickListener {
                list1CellDelegate?.cellArea(row)
            }
        } else {
            viewHolder.cityBtn.visibility = View.GONE
        }

        if (row.tel_show.isNotEmpty()) {
            viewHolder.telLbl.text = row.tel_show
        } else {
            viewHolder.telLbl.text = "電話：未提供"
        }

        viewHolder.parkingLbl.text = "停車場:${row.parking_show}"

        if (row.interval_show.isNotEmpty()) {
            viewHolder.intervalLbl.text = row.interval_show
        } else {
            viewHolder.intervalLbl.text = "未提供"
        }

        viewHolder.air_conditionLbl.text = "空調:${row.air_condition_show}"
    }
}

//class ArenaItem(override var context: Context, var _row: ArenaTable): ListItem<Table>(context, _row) {
//
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
//
//        //println(superStore);
//
//        super.bind(viewHolder, position)
//
//        val row: ArenaTable = _row
//
//        if (row.city_show.isNotEmpty()) {
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
//        if (row.area_show.isNotEmpty()) {
//            viewHolder.areaBtn.text = row.area_show
//            viewHolder.areaBtn.setOnClickListener {
//                if (list1CellDelegate != null) {
//                    list1CellDelegate!!.cellArea(row)
//                }
//            }
//        } else {
//            viewHolder.cityBtn.visibility = View.GONE
//        }
//
//        if (row.tel_show.isNotEmpty()) {
//            viewHolder.telLbl.text = row.tel_show
//        } else {
//            viewHolder.telLbl.text = "電話：未提供"
//        }
//
//        viewHolder.parkingLbl.text = "停車場:${row.parking_show}"
//
//        if (row.interval_show.isNotEmpty()) {
//            viewHolder.intervalLbl.text = row.interval_show
//        } else {
//            viewHolder.intervalLbl.text = "未提供"
//        }
//
//        viewHolder.air_conditionLbl.text = "空調:${row.air_condition_show}"
//    }
//
//    override fun getLayout() = R.layout.arena_list_cell
//}

