package com.sportpassword.bm.Fragments


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
import com.sportpassword.bm.App
import com.sportpassword.bm.Controllers.HomeTotalAdVC
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor

/**
 * A simple [Fragment] subclass.
 * Use the [TempPlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TempPlayFragment : TabFragment(), inter {

//    protected lateinit var searchAdapter: SearchAdapter
    protected val rows: ArrayList<ArrayList<HashMap<String, String>>> = arrayListOf(
            arrayListOf(
                    hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
                    hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY),
                    hashMapOf("title" to "日期","detail" to "全部","key" to TEAM_WEEKDAYS_KEY),
                    hashMapOf("title" to "時段","detail" to "全部","key" to TEAM_PLAY_START_KEY)
            ),
            arrayListOf(
                    hashMapOf("title" to "球館","detail" to "全部","key" to ARENA_KEY),
                    hashMapOf("title" to "程度","detail" to "全部","key" to TEAM_DEGREE_KEY)
            )
    )
//    protected val data: ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> = arrayListOf()

    val SELECT_REQUEST_CODE = 1

    var citys: ArrayList<City> = arrayListOf()
    var weekdays: ArrayList<Int> = arrayListOf()
    var times: HashMap<String, Any> = hashMapOf()
    var arenas: ArrayList<Arena> = arrayListOf()
    var degrees: ArrayList<DEGREE> = arrayListOf()
    var keyword: String = ""

    var searchSections: ArrayList<Section> = arrayListOf(Section(), Section())

    var firstTimeLoading: Boolean = false
    //var firstTimeLoading: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (!Session.exist(Session.loginResetKey)) {
//            Session.loginReset = gReset
//        }

        sections = arrayListOf("一般", "更多")
        if (firstTimeLoading) {
            val intent = Intent(activity, HomeTotalAdVC::class.java)
            startActivity(intent)
            firstTimeLoading = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_tempplay_search, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.search_container)
        initAdapter(true)

        val btn = view.findViewById<Button>(R.id.submit_btn)
        btn.setOnClickListener { submit(view) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val w = (mainActivity!!.screenWidth.toFloat() / mainActivity!!.density).toInt()
        val h = ((mainActivity!!.screenHeight.toFloat()) / (mainActivity!!.density)).toInt()
        val lp = RelativeLayout.LayoutParams(w - (2 * mainActivity!!.layerRightLeftPadding), h - mainActivity!!.layerTopPadding)
        val adContainer: RelativeLayout = RelativeLayout(mainActivity!!)
        adContainer.layoutParams = lp
        adContainer.backgroundColor = ContextCompat.getColor(mainActivity!!, R.color.MY_RED)
//        val aaa = view.findViewById<ConstraintLayout>(R.id.aaa)
//        aaa.addView(adContainer)
    }

//    override fun onResume() {
//        gReset = Session.loginReset
//        super.onResume()
//        if (!gReset) {
//            mainActivity!!.info("因為更新APP系統，請重新登出再登入，方能正常使用會員功能")
//        }
//    }

    override fun initAdapter(include_section: Boolean) {

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
            val items = generateItems1(idx)
            searchSections[idx].addAll(items)
            expandableGroup.add(searchSections[idx])
            adapter.add(expandableGroup)
        }
    }

    fun generateItems1(section: Int): ArrayList<SearchItem> {
        val _rows: ArrayList<SearchItem> = arrayListOf()
        val row = rows[section]
        for (i in 0..row.size-1) {
            val title = row[i].get("title")!!
            val detail = row[i].get("detail")!!
            var bSwitch = false
            if (row[i].containsKey("switch")) {
                bSwitch = row[i].get("switch")!!.toBoolean()
            }
            val searchItem = SearchItem(title, detail, keyword, bSwitch, section, i, { k ->
                keyword = k
            }, { idx, b ->

            })
            searchItem.delegate = this
            _rows.add(searchItem)
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
                    2 -> {// weekdays
                        intent.putExtra("key", TEAM_WEEKDAYS_KEY)
                        intent.putExtra("source", "search")
                        intent.putIntegerArrayListExtra("weekdays", weekdays)
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
                    var key = ""
                    if (data != null && data!!.hasExtra("key")) {
                        key = data!!.getStringExtra("key")
                    }

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
                    } else if (key == TEAM_WEEKDAYS_KEY) {
                        section = 0
                        row = 2
                        weekdays = data!!.getIntegerArrayListExtra("weekdays")
//                        println(weekdays)

                        if (weekdays.size > 0) {
                            var arr: ArrayList<String> = arrayListOf()
                            val gDays = Global.weekdays
                            for (day in weekdays) {
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
                    val items = generateItems1(section)
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
        intent.putIntegerArrayListExtra("weekdays", weekdays)
        intent.putExtra("keyword", keyword)

        startActivity(intent)
    }

    override fun remove(indexPath: IndexPath) {
        val row = rows[indexPath.section][indexPath.row]
        val key = row["key"]!!
        //println(key)
        when (key) {
            CITY_KEY -> citys.clear()
            TEAM_WEEKDAYS_KEY -> weekdays.clear()
            TEAM_PLAY_START_KEY -> times.clear()
            ARENA_KEY -> arenas.clear()
            TEAM_DEGREE_KEY -> degrees.clear()
        }
        rows[indexPath.section][indexPath.row]["detail"] = "全部"
        val items = generateItems1(indexPath.section)
        searchSections[indexPath.section].update(items)
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
