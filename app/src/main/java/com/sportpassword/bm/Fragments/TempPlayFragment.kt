package com.sportpassword.bm.Fragments


import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sportpassword.bm.Adapters.inter
import com.sportpassword.bm.Controllers.Arena
import com.sportpassword.bm.Controllers.EditItemActivity
import com.sportpassword.bm.Controllers.TempPlayVC
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Adapters.GroupSection
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.sportpassword.bm.Adapters.SearchItem

/**
 * A simple [Fragment] subclass.
 * Use the [TempPlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TempPlayFragment : TabFragment(), inter {

//    protected lateinit var searchAdapter: SearchAdapter
    protected lateinit var adapter: GroupAdapter<ViewHolder>

    protected val sections: ArrayList<String> = arrayListOf("一般", "更多")
    protected val rows: ArrayList<ArrayList<HashMap<String, String>>> = arrayListOf(
            arrayListOf(
                    hashMapOf("title" to "關鍵字","detail" to "全部"),
                    hashMapOf("title" to "縣市","detail" to "全部"),
                    hashMapOf("title" to "日期","detail" to "全部"),
                    hashMapOf("title" to "時段","detail" to "全部")
            ),
            arrayListOf(
                    hashMapOf("title" to "球館","detail" to "全部"),
                    hashMapOf("title" to "程度","detail" to "全部")
            )
    )
//    protected val data: ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> = arrayListOf()

    val SELECT_REQUEST_CODE = 1

    var citys: ArrayList<City> = arrayListOf()
    var days: ArrayList<Int> = arrayListOf()
    var times: HashMap<String, Any> = hashMapOf()
    var arenas: ArrayList<Arena> = arrayListOf()
    var degrees: ArrayList<DEGREE> = arrayListOf()
    var keyword: String = ""

    var searchSections: ArrayList<Section> = arrayListOf(Section(), Section())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_tempplay_search, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.search_container)
        initAdapter()

        val btn = view.findViewById<Button>(R.id.submit_btn)
        btn.setOnClickListener { submit(view) }
        return view
    }

    override fun initAdapter() {

        adapter = GroupAdapter()
        adapter.clear()
        searchSections = arrayListOf(Section(), Section())

        setData()

        adapter.setOnItemClickListener { item, view ->
            val searchItem = item as SearchItem
            val section = searchItem.section
            val row = searchItem.row
            if (section == 0 && row == 0) {
            } else {
                prepare(section, row)
            }
        }
        recyclerView.adapter = adapter
    }

    private fun setData() {
        var isExpanded = true
        var isShow = true
        for ((idx, section) in sections.withIndex()) {
            if (idx == 1) isExpanded = false else isExpanded = true
            if (idx == 0) isShow = false else isShow = true

            val expandableGroup = ExpandableGroup(GroupSection(section, isShow), isExpanded)
            val items = generateItems(idx)
            searchSections[idx].addAll(items)
            expandableGroup.add(searchSections[idx])
            adapter.add(expandableGroup)
        }
    }

    private fun generateItems(section: Int): ArrayList<SearchItem> {
        val _rows: ArrayList<SearchItem> = arrayListOf()
        val row = rows[section]
        for (i in 0..row.size-1) {
            val title = row[i].get("title")!!
            val detail = row[i].get("detail")!!
            var bSwitch = false
            if (row[i].containsKey("switch")) {
                bSwitch = row[i].get("switch")!!.toBoolean()
            }
            _rows.add(SearchItem(title, detail, bSwitch, section, i, { k ->
                keyword = k
            }, { idx, b ->

            })
            )
        }
        return _rows
    }

    override fun prepare(section: Int, row: Int) {
//        println(section)
//        println(row)
        var intent = Intent(activity, EditItemActivity::class.java)
        when (section) {
            0 -> {
                when (row) {
                    1 -> {// city
                        intent.putExtra("key", CITY_KEY)
                        intent.putExtra("source", "search")
                        intent.putExtra("type", "simple")
                        intent.putExtra("select", "multi")
                        intent.putParcelableArrayListExtra("citys", citys)
                    }
                    2 -> {// days
                        intent.putExtra("key", TEAM_DAYS_KEY)
                        intent.putExtra("source", "search")
                        intent.putIntegerArrayListExtra("days", days)
                    }
                    3 -> {// times
                        intent.putExtra("key", TEAM_PLAY_START_KEY)
                        intent.putExtra("source", "search")
//                        times["time"] = "09:00"
                        times["type"] = SELECT_TIME_TYPE.play_start
                        intent.putExtra("times", times)
                    }
                }
            }
            1 -> {
                when (row) {
                    0 -> {// arena
//                        citys.add(City(10, ""))
//                        citys.add(City(11, ""))

                        if (citys.size == 0) {
                            Alert.warning(this@TempPlayFragment.context!!, "請先選擇縣市")
                            return
                        }
                        intent.putExtra("key", ARENA_KEY)
                        intent.putExtra("source", "search")
                        intent.putExtra("type", "simple")
                        intent.putExtra("select", "multi")

                        var citysForArena: ArrayList<Int> = arrayListOf()
                        for (city in citys) {
                            citysForArena.add(city.id)
                        }
                        intent.putIntegerArrayListExtra("citys_for_arena", citysForArena)

                        intent.putParcelableArrayListExtra("arenas", arenas)
                    }
                    1 -> {// degree
                        intent.putExtra("key", TEAM_DEGREE_KEY)
                        intent.putExtra("source", "search")
                        intent.putExtra("degrees", degrees)
                    }
                }
            }
        }
        startActivityForResult(intent!!, SELECT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var section = 0
        var row = 0
        var value = "全部"

        when (requestCode) {
            SELECT_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val key = data!!.getStringExtra("key")

                    if (key == CITY_KEY) { // city
                        section = 0
                        row = 1
                        citys = data!!.getParcelableArrayListExtra("citys")
                        if (citys.size > 0) {
                            var arr: ArrayList<String> = arrayListOf()
                            for (city in citys) {
                                arr.add(city.name)
                            }
                            value = arr.joinToString()
                        } else {
                            value = "全部"
                        }
                    } else if (key == ARENA_KEY) { // arena
                        section = 1
                        row = 0
                        arenas = data!!.getParcelableArrayListExtra("arenas")
                        if (arenas.size > 0) {
                            var arr: ArrayList<String> = arrayListOf()
                            for (arena in arenas) {
                                arr.add(arena.name)
                            }
                            value = arr.joinToString()
                        } else {
                            value = "全部"
                        }
                    } else if (key == TEAM_DAYS_KEY) {
                        section = 0
                        row = 2
                        days = data!!.getIntegerArrayListExtra("days")
//                        println(days)

                        if (days.size > 0) {
                            var arr: ArrayList<String> = arrayListOf()
                            val gDays = Global.days
                            for (day in days) {
                                for (gDay in gDays) {
                                    if (day == gDay.get("value")!! as Int) {
                                        arr.add(gDay.get("simple_text")!! as String)
                                        break
                                    }
                                }
                            }
                            value = arr.joinToString()
                        } else {
                            value = "全部"
                        }
                    } else if (key == TEAM_PLAY_START_KEY) {
                        section = 0
                        row = 3
                        times = data!!.getSerializableExtra("times") as HashMap<String, Any>
                        if (times.containsKey("time")) {
                            value = times["time"]!! as String
                        } else {
                            value = "全部"
                        }
                    } else if (key == TEAM_DEGREE_KEY) {
                        section = 1
                        row = 1
                        degrees = data!!.getSerializableExtra("degrees") as ArrayList<DEGREE>
                        if (degrees.size > 0) {
                            var arr: ArrayList<String> = arrayListOf()
                            degrees.forEach {
                                arr.add(it.value)
                            }
                            value = arr.joinToString()
                        } else {
                            value = "全部"
                        }
                    }
                    rows[section][row]["detail"] = value
                    val items = generateItems(section)
                    searchSections[section].update(items)
//                    adapter.notifyDataSetChanged()
                }
            }
        }
        rows[section][row]["detail"] = value
//        setData()
//        searchAdapter.data = this.data
        adapter.notifyDataSetChanged()
    }

    fun submit(view: View) {
        val intent = Intent(activity, TempPlayVC::class.java)
        intent.putExtra("type", "type")
        intent.putExtra("titleField", "name")
        intent.putExtra("citys", citys)
        intent.putExtra("arenas", arenas)
        intent.putExtra("degrees", degrees)
        intent.putExtra("times", times)
        intent.putIntegerArrayListExtra("days", days)
        intent.putExtra("keyword", keyword)

        startActivity(intent)
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
