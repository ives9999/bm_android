package com.sportpassword.bm.Fragments

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.inter
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Controllers.HomeTotalAdVC
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Controllers.MyMapVC
import com.sportpassword.bm.Data.*
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Views.Tag
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_tempplay_search.*
import kotlinx.android.synthetic.main.tag.view.*
import kotlinx.android.synthetic.main.team_list_cell.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * A simple [Fragment] subclass.
 * Use the [TempPlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("UNCHECKED_CAST")
class TempPlayFragment : TabFragment() {

//    protected lateinit var searchAdapter: SearchAdapter
//    protected val rows: ArrayList<ArrayList<HashMap<String, String>>> = arrayListOf(
//            arrayListOf(
//                    hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
//                    hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY),
//                    hashMapOf("title" to "星期幾","detail" to "全部","key" to WEEKDAY_KEY),
//                    hashMapOf("title" to "時段","detail" to "全部","key" to TEAM_PLAY_START_KEY)
//            ),
//            arrayListOf(
//                    hashMapOf("title" to "球館","detail" to "全部","key" to ARENA_KEY),
//                    hashMapOf("title" to "程度","detail" to "全部","key" to TEAM_DEGREE_KEY)
//            )
//    )
//    protected val data: ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> = arrayListOf()

//    val SELECT_REQUEST_CODE = 1

//    var citys: ArrayList<City> = arrayListOf()
//    var weekdays: ArrayList<Int> = arrayListOf()
//    var times: HashMap<String, Any> = hashMapOf()
//    var arenas: ArrayList<Arena> = arrayListOf()
//    var degrees: ArrayList<DEGREE> = arrayListOf()
//    var keyword: String = ""

    var mysTable: TeamsTable? = null
    lateinit var tableAdapter: TeamAdapter
    lateinit var searchAdapter: TeamSearchAdapter
    
//    var searchSections: ArrayList<Section> = arrayListOf()
    var mySections: ArrayList<HashMap<String, Any>> = arrayListOf()

    var searchTags: ArrayList<HashMap<String, Any>> = arrayListOf (
        hashMapOf("key" to "like", "selected" to true, "tag" to 0, "name" to "喜歡", "class" to ""),
        hashMapOf("key" to "search", "selected" to false, "tag" to 1, "name" to "搜尋", "class" to ""),
        hashMapOf("key" to "like", "selected" to false, "tag" to 2, "name" to "全部", "class" to "")
    )
    var selectedTagIdx: Int = 0

    //是否顯示開啟app進入頁面的廣告
    var firstTimeLoading: Boolean = false
    //var firstTimeLoading: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {

        //sections = arrayListOf("一般", "更多")

        able_type = "team"

        searchRows = arrayListOf(
            hashMapOf("title" to "關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to "","cell" to "textField"),
            hashMapOf("title" to "縣市","show" to "全部","key" to CITY_KEY,"value" to "","cell" to "more"),
            hashMapOf("title" to "星期幾","show" to "全部","key" to WEEKDAY_KEY,"value" to "","cell" to "more"),
            hashMapOf("title" to "時段","show" to "全部","key" to START_TIME_KEY,"value" to "","cell" to "more"),
            hashMapOf("title" to "球館","show" to "全部","key" to ARENA_KEY,"value" to "","cell" to "more"),
            hashMapOf("title" to "程度","show" to "全部","key" to DEGREE_KEY,"value" to "","cell" to "more")
        )

        mySections = arrayListOf(
            hashMapOf("isExpanded" to true,"title" to "一般","key" to arrayListOf(KEYWORD_KEY,CITY_KEY,WEEKDAY_KEY,
                START_TIME_KEY)),
            hashMapOf("isExpanded" to false,"title" to "更多","key" to arrayListOf(ARENA_KEY, DEGREE_KEY))
        )

        super.onCreate(savedInstanceState)
        dataService = TeamService

        if (firstTimeLoading) {
            val intent = Intent(activity, HomeTotalAdVC::class.java)
            startActivity(intent)
            firstTimeLoading = false
        }

//        adapter = GroupAdapter()
        //tableAdapter = TeamAdapter(tableLists, R.layout.team_list_cell)

//        generateSections()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val memuView = menu.findItem(R.id.menu_all).actionView
        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        searchBtn.visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_tempplay_search, container, false)

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

//        recyclerView = search_container
//        recyclerView.adapter = adapter

//        adapter.setOnItemClickListener { item, _ ->
//            onClick(item)
//        }
//        initAdapter(true)

        val btn = view.findViewById<Button>(R.id.submit_btn)
        btn.setOnClickListener { submit(view) }

        val w = (mainActivity!!.screenWidth.toFloat() / mainActivity!!.density).toInt()
        val h = ((mainActivity!!.screenHeight.toFloat()) / (mainActivity!!.density)).toInt()
        val lp = RelativeLayout.LayoutParams(w - (2 * mainActivity!!.layerRightLeftPadding), h - mainActivity!!.layerTopPadding)
        val adContainer: RelativeLayout = RelativeLayout(mainActivity!!)
        adContainer.layoutParams = lp
        adContainer.backgroundColor = ContextCompat.getColor(mainActivity!!, R.color.MY_RED)

        val lp_tag = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp_tag.gravity = Gravity.CENTER
        lp_tag.weight = 1F

//        val lp_tag_view = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        lp_tag_view.gravity = Gravity.CENTER_VERTICAL
        //lp1.weight = 1F
        val textSize: Float = 16F

        for ((i, searchTag) in searchTags.withIndex()) {

            val tag: Tag = Tag(mainActivity!!)
            tag_container.addView(tag)
            tag.layoutParams = lp_tag
            tag.gravity = Gravity.CENTER

            var idx: Int = 1000
            if (searchTag.containsKey("tag")) {
                idx = searchTag["tag"] as Int
            }
            tag.tag = idx

            var name: String = "預設"
            if (searchTag.containsKey("name")) {
                name = searchTag["name"] as String
            }
            tag.tag_view.text = name

//            tag.tag_view.layoutParams = lp_tag_view
            tag.tag_view.textSize = textSize
            tag.tag_view.setPadding(60, 15, 60, 15)

            searchTags[i]["class"] = tag

            tag.setOnClickListener {
                tabPressed(it)
            }
        }
        updateTabSelected(selectedTagIdx)

        footer.visibility = View.GONE
        remain.visibility = View.GONE

        recyclerView = search_container
        maskView = mask
//        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
//        refreshLayout = tab_refresh
//        setRecyclerViewRefreshListener()
        tableAdapter = TeamAdapter(R.layout.team_list_cell, this)
        recyclerView.adapter = tableAdapter

        searchAdapter = TeamSearchAdapter(mainActivity!!, R.layout.cell_section, this)
        searchSections = initRows()
        searchAdapter.setMyTableSection(searchSections)

        member_like = true
        refresh()
    }

    override fun genericTable() {
//        println(dataService.jsonString)
        try {
            mysTable = jsonToModels<TeamsTable>(jsonString)
        } catch (e: JsonParseException) {
            println(e.localizedMessage)
        }
        if (mysTable != null) {
            tables = mysTable
            getPage()
            tableLists += generateItems1(TeamTable::class, mysTable!!.rows)
            tableAdapter.setMyTableList(tableLists)
            runOnUiThread {
                tableAdapter.notifyDataSetChanged()
            }

            //val items = generateItems()
            //adapter.update(items)
            //adapter.notifyDataSetChanged()
        }
    }

//    override fun generateItems(): ArrayList<Item> {
//        if (mysTable != null) {
//            for (row in mysTable!!.rows) {
//                row.filterRow()
//                val myItem = TeamItem(requireContext(), row)
//                myItem.list1CellDelegate = this
//                items.add(myItem)
//            }
//        }
//
//        return items
//    }

    fun tabPressed(view: View) {

        val tag: Tag = view as Tag
        val idx: Int? = tag.tag as? Int
        if (idx != null) {
            val selectedTag: HashMap<String, Any> = searchTags[idx]
            val selected: Boolean = selectedTag["selected"] as? Boolean == true

            //按了其他頁面的按鈕
            if (!selected) {
                updateTabSelected(idx)
                selectedTagIdx = idx
                when (selectedTagIdx) {
                    1-> {
                        footer.visibility = View.VISIBLE
                        remain.visibility = View.VISIBLE
                        recyclerView.adapter = searchAdapter
                        //generateSections()
                    }
                    0-> {
                        footer.visibility = View.GONE
                        remain.visibility = View.GONE
                        member_like = true
                        recyclerView.adapter = tableAdapter
                        refresh()
                    }
                    2-> {
                        footer.visibility = View.GONE
                        remain.visibility = View.GONE
                        member_like = false
                        recyclerView.adapter = tableAdapter
                        refresh()
                    }
                }
            }
        }
    }

    private fun updateTabSelected(idx: Int) {

        // set user click which tag, set tag selected is true
        for ((i, searchTag) in searchTags.withIndex()) {
            searchTag["selected"] = i == idx
            searchTags[i] = searchTag
        }
        setTabSelectedStyle()
    }

    private fun setTabSelectedStyle() {

        for (searchTag in searchTags) {
            if (searchTag.containsKey("class")) {
                val tag: Tag? = searchTag["class"] as? Tag
                if (tag != null) {
                    tag.isChecked = searchTag["selected"] as Boolean
                    tag.setSelectedStyle()
                }
            }
        }
    }

    //row click
//    fun onClick(item: com.xwray.groupie.Item<GroupieViewHolder>) {
//
//        if (selectedTagIdx == 1) {
//            val searchItem = item as SearchItem
//            val idx_section: Int = searchItem.section
//            val idx_row: Int = searchItem.row
//            prepare(idx_section, idx_row)
//        } else {
//            val teamItem = item as TeamItem
//            val table = teamItem.row
//            mainActivity!!.toShowTeam(table.token)
//        }
//    }

//    override fun onResume() {
//        gReset = Session.loginReset
//        super.onResume()
//        if (!gReset) {
//            mainActivity!!.info("因為更新APP系統，請重新登出再登入，方能正常使用會員功能")
//        }
//    }

    //override fun initAdapter(include_section: Boolean) {

        //adapter = GroupAdapter()
        //adapter.clear()
        //searchSections = arrayListOf(Section(), Section())

        //setData()


        //recyclerView.adapter = adapter
    //}

//    private fun setData() {
//
//        adapter.setOnItemClickListener { item, view ->
//            val searchItem = item as SearchItem
//            val section = searchItem.section
//            val row = searchItem.row
//            if (section == 0 && row == 0) {
//            } else {
//                prepare(section, row)
//            }
//        }
//
//        var isExpanded = true
//        var isShow = true
//        for ((idx, section) in sections.withIndex()) {
//            if (idx == 1) isExpanded = false else isExpanded = true
//            if (idx == 0) isShow = false else isShow = true
//
//            val expandableGroup = ExpandableGroup(GroupSection(section, isShow), isExpanded)
//            val items = generateSearchItems(idx)
//            searchSections[idx].addAll(items)
//            expandableGroup.add(searchSections[idx])
//            adapter.add(expandableGroup)
//        }
//    }

//    fun generateSections() {
//
//        adapterSections.clear()
//        adapter.clear()
//        for ((idx, section) in mySections.withIndex()) {
//            adapterSections.add(Section())
//
//            if (section.containsKey("title") && section.containsKey("isExpanded") && section.containsKey("key")) {
//                val title: String = section["title"] as String
//                val isExpanded: Boolean = section["isExpanded"] as Boolean
//                val expandableGroup = ExpandableGroup(GroupSection(title), isExpanded)
//
//                items = generateItems(idx)
//                adapterSections[idx].addAll(items)
//                expandableGroup.add(adapterSections[idx])
//                adapter.add(expandableGroup)
//            }
//        }
//    }
//
//    override fun generateItems(section_idx: Int): ArrayList<Item> {
//
//        val res: ArrayList<Item> = arrayListOf()
//
//        val keys: ArrayList<String>? = mySections[section_idx]["key"] as? ArrayList<String>
//        val rows: ArrayList<HashMap<String, String>> = arrayListOf()
//        if (keys != null) {
//            for (key in keys) {
//                for (searchRow in searchRows) {
//                    if (searchRow.containsKey("key")) {
//                        if (searchRow["key"] == key) {
//                            rows.add(searchRow)
//                        }
//                    }
//                }
//            }
//        }
//
//        for ((row_idx, row) in rows.withIndex()) {
//            val title = row.get("title")!!
//            var detail: String = ""
//            if (row.containsKey("detail")) {
//                detail = row.get("detail")!!
//            } else if (row.containsKey("show")) {
//                detail = row.get("show")!!
//            }
//            var bSwitch = false
//            if (row.containsKey("switch")) {
//                bSwitch = row.get("switch")!!.toBoolean()
//            }
//            val searchItem = SearchItem(title, detail, "", bSwitch, section_idx, row_idx)
//            searchItem.delegate = this
//
//            res.add(searchItem)
//        }
//
//        return res
//    }

//    fun generateItems1(section: Int): ArrayList<SearchItem> {
//        val _rows: ArrayList<SearchItem> = arrayListOf()
//        val row = rows[section]
//        for (i in 0..row.size-1) {
//            val title = row[i].get("title")!!
//            val detail = row[i].get("detail")!!
//            var bSwitch = false
//            if (row[i].containsKey("switch")) {
//                bSwitch = row[i].get("switch")!!.toBoolean()
//            }
//            val searchItem = SearchItem(title, detail, keyword, bSwitch, section, i)
//            searchItem.delegate = this
//            _rows.add(searchItem)
//        }
//        return _rows
//    }

        fun prepare(sectionIdx: Int, rowIdx: Int) {

//        var idx = 0
//        if (section == 0) {
//            idx = row
//        } else {
//            for (i in 0..section-1) {
//                val tmps: ArrayList<String> = mySections[i]["key"] as ArrayList<String>
//                idx += tmps.size
//            }
//            idx += row
//        }

        val section = searchSections[sectionIdx]
        var row = section.items[rowIdx]

//        var row = searchRows.get(idx)
//        var key: String = ""
//        if (row.containsKey("key")) {
//            key = row["key"]!!
//        }

        val key: String = row.key
        val value: String = row.value
//        if (row.containsKey("value")) {
//            value = row["value"]!!
//        }
        if (key == CITY_KEY) {
            mainActivity!!.toSelectCity(value, null, able_type)
        } else if (key == WEEKDAY_KEY) {
            mainActivity!!.toSelectWeekday(value, null, able_type)
        } else if (key == START_TIME_KEY || key == END_TIME_KEY) {
            mainActivity!!.toSelectTime(key, value, null, able_type)
        } else if (key == ARENA_KEY) {
            row = getDefinedRow1(CITY_KEY)
            if (row.value.isNotEmpty()) {
                val city_id: Int = row.value.toInt()
                mainActivity!!.toSelectArena(value, city_id, null, able_type)
            } else {
                mainActivity!!.warning("纖纖選擇縣市")
            }
        } else if (key == DEGREE_KEY) {
            mainActivity!!.toSelectDegree(value, null, able_type)
        }
    }

    override fun singleSelected(key: String, selected: String) {

        val row = getDefinedRow1(key)
        var show = ""

        if (key == START_TIME_KEY || key == END_TIME_KEY) {
            row.value = selected
            show = selected.noSec()
        } else if (key == CITY_KEY || key == AREA_KEY) {
            row.value = selected
            show = Global.zoneIDToName(selected.toInt())
        } else if (key == WEEKDAY_KEY) {
            row.value = selected
            show = WEEKDAY.intToString(selected.toInt())
        }

        row.show = show
        //searchAdapter.notifyItemChanged(0)
        searchAdapter.notifyDataSetChanged()
//        generateSections()
//        adapter.notifyDataSetChanged()
    }

    override fun arenaSelected(selected: String, show: String) {

        val key: String = ARENA_KEY
        val row = getDefinedRow1(key)
        row.value = selected
        row.show = show
        searchAdapter.notifyDataSetChanged()
//        generateSections()
//        adapter.notifyDataSetChanged()
    }

    override fun degreeSelected(selected: String, show: String) {
        val key: String = DEGREE_KEY
        val row = getDefinedRow(key)
        row["value"] = selected
        row["show"] = show
        replaceRows(key, row)
//        generateSections()
//        adapter.notifyDataSetChanged()
    }

    override fun cellClick(row: Table) {
        if (selectedTagIdx == 1) {
//            val searchItem = item as SearchItem
//            val idx_section: Int = searchItem.section
//            val idx_row: Int = searchItem.row
//            prepare(idx_section, idx_row)
        } else {
            mainActivity!!.toShowTeam(row.token)
        }
    }

    override fun cellCity(row: Table) {

        val _row: TeamTable = row as TeamTable
        val arenaTable: ArenaTable = _row.arena!!

        val key: String = CITY_KEY
        val city_id: Int = arenaTable.city_id
        val row1 = getDefinedRow(key)
        row1[VALUE_KEY] = city_id.toString()
        replaceRows(key, row1)
        prepareParams()
        page = 1
        tableLists.clear()
        getDataStart(page, perPage)
    }

    override fun cellArena(row: Table) {

        val _row: TeamTable = row as TeamTable
        val arenaTable: ArenaTable = _row.arena!!

        val key: String = ARENA_KEY
        val arena_id: Int = arenaTable.id
        val row1 = getDefinedRow(key)
        row1[VALUE_KEY] = arena_id.toString()
        replaceRows(key, row1)
        prepareParams()
        page = 1
        tableLists.clear()
        getDataStart(page, perPage)
    }

    override fun cellShowMap(row: Table) {

        val _row: TeamTable = row as TeamTable
        val arenaTable: ArenaTable = _row.arena!!

        val intent = Intent(mainActivity!!, MyMapVC::class.java)
        var name: String = ""
        if (arenaTable.name.isNotEmpty()) {
            name = arenaTable.name
        }
        intent.putExtra("title", name)
        intent.putExtra("address", arenaTable.address)
        startActivity(intent)
    }

    private fun initRows(): ArrayList<SearchSection> {

        val sections: ArrayList<SearchSection> = arrayListOf()

        sections.add(makeSection0Row())
        sections.add(makeSection1Row(false))

        return sections
    }

    private fun updateSectionRow(): ArrayList<SearchSection> {
        val sections: ArrayList<SearchSection> = arrayListOf()
        for ((idx, teamSearchSection) in searchSections.withIndex()) {
            val isExpanded: Boolean = teamSearchSection.isExpanded
            if (idx == 0) {
                sections.add(makeSection0Row(isExpanded))
            } else if (idx == 1) {
                sections.add(makeSection1Row(isExpanded))
            }
        }
        return sections
    }

    private fun makeSection0Row(isExpanded: Boolean=true): SearchSection {
        val rows: ArrayList<SearchRow> = arrayListOf()
        if (isExpanded) {
            val r1: SearchRow = SearchRow("關鍵字", "", "", KEYWORD_KEY, "textField")
            rows.add(r1)
            val r2: SearchRow = SearchRow("縣市", "", "全部", CITY_KEY, "more")
            rows.add(r2)
            val r3: SearchRow = SearchRow("星期幾", "", "全部", WEEKDAY_KEY, "more")
            rows.add(r3)
            val r4: SearchRow = SearchRow("時段", "", "全部", START_TIME_KEY, "more")
            rows.add(r4)
        }

        val s: SearchSection = SearchSection("一般", isExpanded)
        s.items.addAll(rows)
        return s
    }

    private fun makeSection1Row(isExpanded: Boolean=true): SearchSection {
        val rows: ArrayList<SearchRow> = arrayListOf()
        if (isExpanded) {
            val r1: SearchRow = SearchRow("球館", "", "全部", ARENA_KEY, "more")
            rows.add(r1)
            val r2: SearchRow = SearchRow("程度", "", "全部", DEGREE_KEY, "more")
            rows.add(r2)
        }

        val s: SearchSection = SearchSection("更多", isExpanded)
        s.items.addAll(rows)
        return s
    }

    override fun handleSectionExpanded(idx: Int) {
        //println(idx)
        val teamSearchSection = searchSections[idx]
        var isExpanded: Boolean = teamSearchSection.isExpanded
        isExpanded = !isExpanded
        searchSections[idx].isExpanded = isExpanded
        toggleSectionOnOff()
    }

    private fun toggleSectionOnOff() {
        searchSections = updateSectionRow()
        searchAdapter.setMyTableSection(searchSections)
        searchAdapter.notifyDataSetChanged()
    }

    override fun cellClick(sectionIdx: Int, rowIdx: Int) {
        prepare(sectionIdx, rowIdx)
    }

    override fun cellTextChanged(sectionIdx: Int, rowIdx: Int, str: String) {
        val section = searchSections[sectionIdx]
        val row = section.items[rowIdx]
        searchSections[sectionIdx].items[rowIdx].value = str
        searchSections[sectionIdx].items[rowIdx].show = str
    }

    override fun cellClear(sectionIdx: Int, rowIdx: Int) {
        searchSections[sectionIdx].items[rowIdx].value = ""
        searchSections[sectionIdx].items[rowIdx].show = ""
        searchSections = updateSectionRow()
        searchAdapter.setMyTableSection(searchSections)
        searchAdapter.notifyDataSetChanged()
    }

//    private fun getIdxFromKey(key: String): Int {
//
//        var idx: Int = 0
//        for ((section_idx, section) in mySections.withIndex()) {
//
//            var bFind: Boolean = false
//            if (section.containsKey("key")) {
//
//                val keys: ArrayList<String> = section["key"] as ArrayList<String>
//                for (_key in keys) {
//                    if (_key == key) {
//                        idx = section_idx
//                        bFind = true
//                        break
//                    }
//                }
//            }
//            if (bFind) {
//                break
//            }
//        }
//
//        return idx
//    }


//    override fun prepare(section: Int, row: Int) {
//
//        val row = searchRows.get(idx)
//        var key: String = ""
//        if (row.containsKey("key")) {
//            key = row["key"]!!
//        }
//
//        var value: String = ""
//        if (row.containsKey("value")) {
//            value = row["value"]!!
//        }
//
//        val intent = Intent(activity, EditItemActivity::class.java)
//        when (section) {
//            0 -> {
//                when (row) {
//                    1 -> {// city
//                        intent.putExtra("key", CITY_KEY)
//                        intent.putExtra("source", "search")
//                        intent.putExtra("type", "simple")
//                        intent.putExtra("select", "multi")
//                        intent.putParcelableArrayListExtra("citys", citys)
//                    }
//                    2 -> {// weekdays
//                        intent.putExtra("key", TEAM_WEEKDAYS_KEY)
//                        intent.putExtra("source", "search")
//                        intent.putIntegerArrayListExtra("weekdays", weekdays)
//                    }
//                    3 -> {// times
//                        intent.putExtra("key", TEAM_PLAY_START_KEY)
//                        intent.putExtra("source", "search")
//                        times["type"] = SELECT_TIME_TYPE.play_start
//                        intent.putExtra("times", times)
//                    }
//                }
//            }
//            1 -> {
//                when (row) {
//                    0 -> {// arena
//
//                        if (citys.size == 0) {
//                            //Alert.warning(this@TempPlayFragment.context!!, "請先選擇縣市")
//                            return
//                        }
//                        intent.putExtra("key", ARENA_KEY)
//                        intent.putExtra("source", "search")
//                        intent.putExtra("type", "simple")
//                        intent.putExtra("select", "multi")
//
//                        var citysForArena: ArrayList<Int> = arrayListOf()
//                        for (city in citys) {
//                            citysForArena.add(city.id)
//                        }
//                        intent.putIntegerArrayListExtra("citys_for_arena", citysForArena)
//
//                        //intent.putParcelableArrayListExtra("arenas", arenas)
//                    }
//                    1 -> {// degree
//                        intent.putExtra("key", TEAM_DEGREE_KEY)
//                        intent.putExtra("source", "search")
//                        intent.putExtra("degrees", degrees)
//                    }
//                }
//            }
//        }
//        startActivityForResult(intent, SELECT_REQUEST_CODE)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        var section = 0
//        var row = 0
//        var value = "全部"
//
//        when (requestCode) {
//            SELECT_REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    var key = ""
//                    if (data != null && data.hasExtra("key")) {
//                        key = data.getStringExtra("key")!!
//                    }
//
//                    if (key == CITY_KEY) { // city
//                        section = 0
//                        row = 1
//                        citys = data!!.getSerializableExtra("citys") as ArrayList<City>
//                        if (citys.size > 0) {
//                            var arr: ArrayList<String> = arrayListOf()
//                            for (city in citys) {
//                                arr.add(city.name)
//                            }
//                            value = arr.joinToString()
//                        } else {
//                            value = "全部"
//                        }
//                    } else if (key == ARENA_KEY) { // arena
//                        section = 1
//                        row = 0
//                    } else if (key == TEAM_WEEKDAYS_KEY) {
//                        section = 0
//                        row = 2
//                        weekdays = data!!.getIntegerArrayListExtra("weekdays")!!
////                        println(weekdays)
//
//                        if (weekdays.size > 0) {
//                            var arr: ArrayList<String> = arrayListOf()
//                            val gDays = Global.weekdays
//                            for (day in weekdays) {
//                                for (gDay in gDays) {
//                                    if (day == gDay.get("value")!! as Int) {
//                                        arr.add(gDay.get("simple_text")!! as String)
//                                        break
//                                    }
//                                }
//                            }
//                            value = arr.joinToString()
//                        } else {
//                            value = "全部"
//                        }
//                    } else if (key == TEAM_PLAY_START_KEY) {
//                        section = 0
//                        row = 3
//                        times = data!!.getSerializableExtra("times") as HashMap<String, Any>
//                        if (times.containsKey("time")) {
//                            value = times["time"]!! as String
//                        } else {
//                            value = "全部"
//                        }
//                    } else if (key == TEAM_DEGREE_KEY) {
//                        section = 1
//                        row = 1
//                        degrees = data!!.getSerializableExtra("degrees") as ArrayList<DEGREE>
//                        if (degrees.size > 0) {
//                            var arr: ArrayList<String> = arrayListOf()
//                            degrees.forEach {
//                                arr.add(it.value)
//                            }
//                            value = arr.joinToString()
//                        } else {
//                            value = "全部"
//                        }
//                    }
//                    rows[section][row]["detail"] = value
//                    val items = generateItems1(section)
//                    searchSections[section].update(items)
////                    adapter.notifyDataSetChanged()
//                }
//            }
//        }
//        rows[section][row]["detail"] = value
////        setData()
////        searchAdapter.data = this.data
//        adapter.notifyDataSetChanged()
//    }

    fun submit(view: View) {

        prepareParams()

        mainActivity!!.toTeam(params)

//        val intent = Intent(activity, TempPlayVC::class.java)
//        intent.putExtra("type", "type")
//        intent.putExtra("titleField", "name")
//        intent.putExtra("citys", citys)
//        intent.putExtra("arenas", arenas)
//        intent.putExtra("degrees", degrees)
//        intent.putExtra("times", times)
//        intent.putIntegerArrayListExtra("weekdays", weekdays)
//        intent.putExtra("keyword", keyword)
//
//        startActivity(intent)
    }

//    override fun remove(indexPath: IndexPath) {
//        var row: HashMap<String, String>? = null
//        if (searchRows.size >= indexPath.row) {
//            row = searchRows[indexPath.row]
//        }
//        var key: String? = null
//        if (row != null && row.containsKey("key") && row.get("key")!!.length > 0) {
//            key = row.get("key")
//        }
//        if (row != null) {
//            row["value"] = ""
//            row["show"] = "全部"
//            replaceRows(key!!, row)
////            generateSections()
////            adapter.notifyDataSetChanged()
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
            val fragment = TempPlayFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor

class TeamAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<TeamViewHolder>(resource, ::TeamViewHolder, list1CellDelegate) {}

class TeamViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: TeamTable = _row as TeamTable

        if (row.arena?.name != null && row.arena!!.name.length > 0) {
            viewHolder.cityBtn.text = row.arena!!.city_show
            viewHolder.cityBtn.setOnClickListener {
                list1CellDelegate?.cellCity(row)
            }

            viewHolder.arenaBtn.text = row.arena!!.name
            viewHolder.arenaBtn.setOnClickListener {
                list1CellDelegate?.cellArena(row)
            }
        } else {
            viewHolder.cityBtn.visibility = View.GONE
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

        val v = viewHolder.iconView
        val a = v.findViewById<ImageButton>(R.id.mapIcon)
        if (a != null && row.arena != null) {

            if (row.arena!!.address == null || row.arena!!.address.isEmpty()) {
                viewHolder.mapIcon.visibility = View.GONE
            } else {
                viewHolder.mapIcon.visibility = View.VISIBLE
                viewHolder.mapIcon.setOnClickListener {
                    list1CellDelegate?.cellShowMap(row)
                }
            }
        }

        viewHolder.temp_quantityLbl.text = row.temp_quantity_show
        viewHolder.signup_countLbl.text = row.temp_signup_count_show
    }
}

class TeamSearchAdapter(val context: Context, private val resource: Int, var delegate: List1CellDelegate): RecyclerView.Adapter<SearchSectionViewHolder>() {
    private var tableSections: ArrayList<SearchSection> = arrayListOf()
    //lateinit var adapter: TeamSearchItemAdapter

    fun setMyTableSection(tableSections: ArrayList<SearchSection>) {
        this.tableSections = tableSections
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(resource, parent, false)

        return SearchSectionViewHolder(viewHolder)

    }

    override fun onBindViewHolder(holder: SearchSectionViewHolder, position: Int) {
        val section: SearchSection = tableSections[position]
        holder.titleLbl.text = section.title

        val tableSection: SearchSection = tableSections[position]
        var iconID: Int = 0
        if (tableSection.isExpanded) {
            iconID = context.resources.getIdentifier("to_down", "drawable", context.packageName)
        } else {
            iconID = context.resources.getIdentifier("to_right", "drawable", context.packageName)
        }
        holder.greater.setImageResource(iconID)

        val items: ArrayList<SearchRow> = tableSections[position].items
        val adapter =
            TeamSearchItemAdapter(context, position, items, delegate)
//            holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.recyclerView.adapter = adapter

        holder.greater.setOnClickListener {
            delegate.handleSectionExpanded(position)
        }
    }

    override fun getItemCount(): Int {
        return tableSections.size
    }
}

class SearchSectionViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var titleLbl: TextView = viewHolder.findViewById(R.id.titleLbl)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var recyclerView: RecyclerView = viewHolder.findViewById(R.id.recyclerView)
}

class TeamSearchItemAdapter(val context: Context, private val sectionIdx: Int, private val tableRows: ArrayList<SearchRow>, var delegate: List1CellDelegate): RecyclerView.Adapter<TeamSearchItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamSearchItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.search_row_item, parent, false)

        return TeamSearchItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: TeamSearchItemViewHolder, position: Int) {
        val row: SearchRow = tableRows[position]
        holder.title.text = row.title
        holder.show.text = row.show

        val cell = row.cell
        if (cell == "textField") {
            holder.show.visibility = View.INVISIBLE
            holder.greater.visibility = View.INVISIBLE
            holder.keyword.visibility = View.VISIBLE
            if (row.show.length > 0) {
                holder.keyword.setText(row.show)
            }
            holder.keyword.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    delegate.cellTextChanged(sectionIdx, position, p0.toString())
                }

            })
        }

        holder.viewHolder.setOnClickListener {
            delegate.cellClick(sectionIdx, position)
        }

        holder.clear.setOnClickListener {
            delegate.cellClear(sectionIdx, position)
        }
    }

    override fun getItemCount(): Int {
        return tableRows.size
    }

}

class TeamSearchItemViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var title: TextView = viewHolder.findViewById(R.id.row_title)
    var show: TextView = viewHolder.findViewById(R.id.row_detail)
    var clear: ImageView = viewHolder.findViewById(R.id.clearBtn)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var keyword: EditText = viewHolder.findViewById(R.id.keywordTxt)
}

//class TeamItem(override var context: Context, var _row: TeamTable): ListItem<Table>(context, _row) {
//
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
//
//        super.bind(viewHolder, position)
//
//        val row: TeamTable = _row
//
////        if (row.city_show.length > 0) {
////            viewHolder.cityBtn.text = row.city_show
////            viewHolder.cityBtn.setOnClickListener {
////                if (list1CellDelegate != null) {
////                    list1CellDelegate!!.cellCity(row)
////                }
////            }
////        } else {
////            viewHolder.cityBtn.visibility = View.GONE
////        }
//
//        if (row.arena?.name != null && row.arena!!.name.length > 0) {
//            viewHolder.cityBtn.text = row.arena!!.city_show
//            viewHolder.cityBtn.setOnClickListener {
//                if (list1CellDelegate != null) {
//                    list1CellDelegate!!.cellCity(row)
//                }
//            }
//
//            viewHolder.arenaBtn.text = row.arena!!.name
//            viewHolder.arenaBtn.setOnClickListener {
//                if (list1CellDelegate != null) {
//                    list1CellDelegate!!.cellArena(row)
//                }
//            }
//        } else {
//            viewHolder.cityBtn.visibility = View.GONE
//            viewHolder.arenaBtn.visibility = View.GONE
//        }
//
//        if (row.weekdays_show.isNotEmpty()) {
//            viewHolder.weekdayLbl.text = row.weekdays_show
//        } else {
//            viewHolder.weekdayLbl.text = "臨打日期:未提供"
//        }
//
//        if (row.interval_show.isNotEmpty()) {
//            viewHolder.intervalLbl.text = row.interval_show
//        } else {
//            viewHolder.weekdayLbl.text = "臨打時段:未提供"
//        }
//
//        val v = viewHolder.containerView
//        val a = v.findViewById<ImageButton>(R.id.mapIcon)
//        if (a != null && row.arena != null) {
//
//            if (row.arena!!.address == null || row.arena!!.address.isEmpty()) {
//                viewHolder.mapIcon.visibility = View.GONE
//            } else {
//                viewHolder.mapIcon.visibility = View.VISIBLE
//                viewHolder.mapIcon.setOnClickListener {
//                    if (list1CellDelegate != null) {
//                        list1CellDelegate!!.cellShowMap(row)
//                    }
//                }
//            }
//        }
//
//        viewHolder.temp_quantityLbl.text = row.temp_quantity_show
//        viewHolder.signup_countLbl.text = row.temp_signup_count_show
//    }
//
//    override fun getLayout() = R.layout.team_list_cell
//
//}
