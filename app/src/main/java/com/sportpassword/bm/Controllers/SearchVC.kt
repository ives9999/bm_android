package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Adapters.SearchSectionAdapter
import com.sportpassword.bm.Adapters.TeamAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Data.SearchSection
import com.sportpassword.bm.Models.ArenaTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.Models.TeamsTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.TabSearch
import com.sportpassword.bm.Views.Tag
import kotlinx.android.synthetic.main.activity_search_vc.*
import kotlinx.android.synthetic.main.bottom_view.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_search.view.*
import kotlinx.android.synthetic.main.tag.view.*
import kotlinx.android.synthetic.main.top_view.*
import org.jetbrains.anko.Android
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.runOnUiThread

class SearchVC : MyTableVC() {

    var mysTable: TeamsTable? = null
    lateinit var tableAdapter: TeamAdapter

    var searchTags: ArrayList<HashMap<String, Any>> = arrayListOf (
        hashMapOf("key" to "like", "selected" to true, "tag" to 0, "icon" to "like", "text" to "喜歡", "class" to ""),
        hashMapOf("key" to "search", "selected" to false, "tag" to 1, "icon" to "member", "text" to "搜尋", "class" to ""),
        hashMapOf("key" to "like", "selected" to false, "tag" to 2, "icon" to "search_w", "text" to "全部", "class" to "")
    )
    var selectedTagIdx: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        able_type = "team"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_vc)

        setBottomTabFocus()
        topTitleLbl.setText("球隊")

        dataService = TeamService
        delegate = this

        val btn = findViewById<Button>(R.id.submit_btn)
        btn.setOnClickListener { searchSubmit() }

        footer.visibility = View.GONE
        //remain.visibility = View.GONE

        recyclerView = list_container
        maskView = mask
        recyclerView.setHasFixedSize(true)

        tableAdapter = TeamAdapter(R.layout.team_list_cell, this)
        recyclerView.adapter = tableAdapter

//        oneSectionAdapter = OneSectionAdapter(this, R.layout.cell_section, this)
//        oneSections = initSectionRows()
        oneSectionAdapter.setOneSection(oneSections)

        member_like = true

        init()
    }

    override fun init() {
        super.init()

        //initTag()
        initSearchTab()
        refresh()
    }

    private fun initSearchTab() {

        val lp_tag = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp_tag.gravity = Gravity.CENTER
        lp_tag.weight = 1F
        val textSize: Float = 16F

        for ((i, searchTab) in searchTags.withIndex()) {
            val tab: TabSearch = TabSearch(this)
            tab.layoutParams = lp_tag
            tab.gravity = Gravity.CENTER

            var idx: Int = 1000
            if (searchTab.containsKey("tag")) {
                idx = searchTab["tag"] as Int
            }
            tab.tag = idx

            var icon: String = "like"
            if (searchTab.containsKey("icon")) {
                icon = searchTab["icon"] as String
            }
            tab.icon.setImage(icon)

            var text: String = "預設"
            if (searchTab.containsKey("text")) {
                text = searchTab["text"] as String
            }
            tab.text.text = text
            tab.text.textSize = textSize
            searchTags[i]["class"] = tab

            tag_container.addView(tab)

            tab.setOnClickListener {
                tabPressed(it)
            }
        }
        updateTabSelected(selectedTagIdx)
    }
    
    private fun initTag() {

//        val w = (this.screenWidth.toFloat() / this.density).toInt()
//        val h = ((this.screenHeight.toFloat()) / (this.density)).toInt()
//        val lp = RelativeLayout.LayoutParams(w - (2 * this.layerRightLeftPadding), h - this.layerTopPadding)
//        val adContainer: RelativeLayout = RelativeLayout(this)
//        adContainer.layoutParams = lp
//        adContainer.backgroundColor = ContextCompat.getColor(this, R.color.MY_RED)

        val lp_tag = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp_tag.gravity = Gravity.CENTER
        lp_tag.weight = 1F

//        val lp_tag_view = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        lp_tag_view.gravity = Gravity.CENTER_VERTICAL
        //lp1.weight = 1F
        val textSize: Float = 16F

        for ((i, searchTag) in searchTags.withIndex()) {

            val tag: Tag = Tag(this)
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
    }

    override fun genericTable() {
//        println(dataService.jsonString)
        try {
            mysTable = jsonToModels<TeamsTable>(jsonString!!)
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

    private fun tabPressed(view: View) {

        val tab: TabSearch = view as TabSearch
        val idx: Int? = tab.tag as? Int
        if (idx != null) {
            val selectedTag: HashMap<String, Any> = searchTags[idx]
            val selected: Boolean = selectedTag["selected"] as? Boolean == true

            //按了其他頁面的按鈕
            if (!selected) {
                updateTabSelected(idx)
                selectedTagIdx = idx
                when (selectedTagIdx) {
                    1-> {
                        val params: ViewGroup.MarginLayoutParams = tableViewContainer.layoutParams as ViewGroup.MarginLayoutParams
                        params.marginStart = 120
                        params.marginEnd = 120
                        params.topMargin = 24
                        tableViewContainer.layoutParams = params

                        val color = ContextCompat.getColor(this, R.color.MY_RED)
                        tableViewContainer.backgroundColor = color

                        footer.visibility = View.VISIBLE
                        //remain.visibility = View.VISIBLE
                        recyclerView.adapter = oneSectionAdapter
                        //generateSections()
                    }
                    0-> {
                        val params1: ViewGroup.MarginLayoutParams = tableViewContainer.layoutParams as ViewGroup.MarginLayoutParams
                        params1.marginStart = 0
                        params1.marginEnd = 0
                        tableViewContainer.layoutParams = params1

                        val color = ContextCompat.getColor(this, android.R.color.transparent)
                        tableViewContainer.backgroundColor = color
                        footer.visibility = View.GONE
                        //remain.visibility = View.GONE
                        member_like = true
                        recyclerView.adapter = tableAdapter
                        params.clear()
                        refresh()
                    }
                    2-> {
                        val color = ContextCompat.getColor(this, android.R.color.transparent)
                        tableViewContainer.backgroundColor = color
                        footer.visibility = View.GONE
                        //remain.visibility = View.GONE
                        member_like = false
                        recyclerView.adapter = tableAdapter
                        params.clear()
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
                val tag: TabSearch? = searchTag["class"] as? TabSearch
                if (tag != null) {
                    tag.isChecked = searchTag["selected"] as Boolean
                    tag.setSelectedStyle()
                }
            }
        }
    }

    override fun initSectionRows(): ArrayList<OneSection> {

        val sections: ArrayList<OneSection> = arrayListOf()

        sections.add(makeSection0Row())
        sections.add(makeSection1Row())

        return sections
    }

    override fun makeSection0Row(isExpanded: Boolean): OneSection {
        val rows: ArrayList<OneRow> = arrayListOf()
        //if (isExpanded) {
        val r1: OneRow = OneRow("關鍵字", "", "", KEYWORD_KEY, "textField")
        rows.add(r1)
        val r2: OneRow = OneRow("縣市", "", "全部", CITY_KEY, "more")
        rows.add(r2)
        val r3: OneRow = OneRow("星期幾", "", "全部", WEEKDAYS_KEY, "more")
        rows.add(r3)
        val r4: OneRow = OneRow("時段", "", "全部", START_TIME_KEY, "more")
        rows.add(r4)
        //}

        val s: OneSection = OneSection("一般", "general", isExpanded)
        s.items.addAll(rows)
        return s
    }

    private fun makeSection1Row(isExpanded: Boolean=true): OneSection {
        val rows: ArrayList<OneRow> = arrayListOf()
        //if (isExpanded) {
        val r1: OneRow = OneRow("球館", "", "全部", ARENA_KEY, "more")
        rows.add(r1)
        val r2: OneRow = OneRow("程度", "", "全部", DEGREE_KEY, "more")
        rows.add(r2)
        //}

        val s: OneSection = OneSection("更多", "more", isExpanded)
        s.items.addAll(rows)
        return s
    }

    override fun cellClick(row: Table) {
        if (selectedTagIdx == 1) {
        } else {
            toShowTeam(row.token)
        }
    }

    override fun cellClick(sectionIdx: Int, rowIdx: Int) {
        prepare(sectionIdx, rowIdx)
    }

    override fun cellShowMap(row: Table) {

        val _row: TeamTable = row as TeamTable
        val arenaTable: ArenaTable = _row.arena!!

        val intent = Intent(this, MyMapVC::class.java)
        var name: String = ""
        if (arenaTable.name.isNotEmpty()) {
            name = arenaTable.name
        }
        intent.putExtra("title", name)
        intent.putExtra("address", arenaTable.address)
        startActivity(intent)
    }

    override fun cellCity(row: Table) {
        val key: String = CITY_KEY
        val row1: TeamTable = row as TeamTable
        val row2: OneRow = getOneRowFromKey(key)
        row2.value = row1.arena!!.city_id.toString()
        row2.show = row1.arena!!.city_show
        prepareParams()
        page = 1
        tableLists.clear()
        getDataStart(page, perPage)
    }

    override fun cellArena(row: Table) {
        val key: String = ARENA_KEY
        val row1: TeamTable = row as TeamTable

        val row2: OneRow = getOneRowFromKey(key)
        row2.value = row1.arena!!.id.toString()
        row2.show = row1.arena!!.name

        val row3: OneRow = getOneRowFromKey(CITY_KEY)
        row3.value = row1.arena!!.city_id.toString()
        row3.show = Global.zoneIDToName(row1.arena!!.city_id)

        prepareParams()
        page = 1
        tableLists.clear()
        getDataStart(page, perPage)
    }

    override fun searchSubmit() {

        prepareParams()

        toTeam(params, false, true)
    }
}