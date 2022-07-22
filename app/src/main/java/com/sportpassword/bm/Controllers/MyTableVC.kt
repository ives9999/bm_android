package com.sportpassword.bm.Controllers

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_payment_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlin.reflect.KClass

abstract class MyTableVC : BaseActivity() {

    var tables: Tables? = null

//    var sections: ArrayList<String> = arrayListOf()
//    var rows: ArrayList<HashMap<String, String>> = arrayListOf()
//
//    var mySections: ArrayList<HashMap<String, Any>> = arrayListOf()
//    var myRows: ArrayList<HashMap<String, Any>> = arrayListOf()

//    protected lateinit var adapter: GroupAdapter<GroupieViewHolder>
//    val adapterSections: ArrayList<Section> = arrayListOf()

    protected lateinit var recyclerView: RecyclerView
    protected lateinit var listAdapter: ListAdapter

    protected var loading: Boolean = false
    protected lateinit var maskView: View

//    protected lateinit var form: BaseForm

    protected var theFirstTime: Boolean = true
    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0
//    val items: ArrayList<Item> = arrayListOf()

    var jsonString: String? = null

    var member_like: Boolean = false

    //protected lateinit var superModels: SuperModel

    //取代superDataLists(define in BaseActivity)，放置所有拿到的SuperModel，分頁時會使用到
    //var <T> allSuperModels: ArrayList<T> = arrayListOf()

    val rowHeight: Int = 200
    var blackViewHeight: Int = 500
    val blackViewPaddingLeft: Int = 80
    var blackView: RelativeLayout? = null
    var layerTableView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("member_like")) {
            member_like = intent.getBooleanExtra("member_like", false)
        }

        recyclerView = RecyclerView(this)
        recyclerView.setHasFixedSize(true)
    }

    override fun init() {
        super.init()

        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
    }

//    open fun initAdapter(include_section: Boolean=false) {
//        adapter = GroupAdapter()
//        adapter.setOnItemClickListener { item, view ->
//            rowClick(item, view)
//        }
//
//
//        // for member register and member update personal data
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
//
//        recyclerView.adapter = adapter
//        recyclerView.setHasFixedSize(true)
//        if (refreshLayout != null) {
//            setRefreshListener()
//        }
//        setRecyclerViewScrollListener()
//    }

    override fun refresh() {
//        page = 1
//        theFirstTime = true
//        getDataStart(page, perPage)

        page = 1
        theFirstTime = true
//        adapter.clear()
//        items.clear()
//        params.clear()
        tableLists.clear()
        getDataStart(page, perPage)
    }

    open fun getDataStart(_page: Int, _perPage: Int, token: String? = null) {
        Loading.show(mask)
        loading = true

        if (member_like) {
            MemberService.likelist(this, able_type) { success ->
                jsonString = MemberService.jsonString
                getDataEnd(success)
            }
        } else {
            dataService.getList(this, token, params, _page, _perPage) { success ->
                jsonString = dataService.jsonString
                getDataEnd(success)
            }
        }
    }

    open fun getDataEnd(success: Boolean) {
        if (success) {
            //if (theFirstTime) {

                if (jsonString != null && jsonString!!.isNotEmpty()) {
//                    println(dataService.jsonString)
                    genericTable()

                    //superCourses = dataService.superModel as SuperCourses
//                    if (tables != null) {
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
//                        page++
//                    } else {
//                        warning(Global.message)
//                        Global.message = ""
//                    }
                } else {
                    warning("沒有取得回傳的json字串，請洽管理員")
                }

            //}

            //notifyDataSetChanged()
            page++
        } else {
            runOnUiThread {
                warning(dataService.msg)
            }
        }
//        mask?.let { mask?.dismiss() }
        runOnUiThread {
            Loading.hide(mask)
        }
        loading = false
        if (refreshLayout != null) {
            refreshLayout!!.isRefreshing = false
        }
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    open fun getPage() {
        page = tables!!.page
        perPage = tables!!.perPage
        totalCount = tables!!.totalCount
        val _totalPage: Int = totalCount / perPage
        totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
        theFirstTime = false
    }

//    protected open fun getDataStart(_page: Int, _perPage: Int) {}
//
//    protected open fun getDataEnd(success: Boolean) {}

    //protected open fun notifyDataSetChanged() {
        //if (page == 1) {
            //superDataLists = arrayListOf()
        //}
        //superDataLists.addAll(dataService.superDataLists)
//        for (data in superDataLists) {
//            data.print()
//            println("===================")
//        }
        //listAdapter.lists = superDataLists
        //listAdapter.notifyDataSetChanged()
    //}

//    open fun notifyChanged(include_section: Boolean=false) {
//        if (include_section) {
//            for ((idx, _) in sections.withIndex()) {
//                val items = generateItems(idx)
//                adapterSections[idx].update(items)
//            }
//        } else {
//            val items = generateItems()
//            adapter.update(items)
//        }
//        adapter.notifyDataSetChanged()
//    }

    open fun <T: Table> generateItems1(t: KClass<T>, rows: ArrayList<T>): ArrayList<T> {
        val temp: ArrayList<T> = arrayListOf()
        for (row in rows) {
            row.filterRow()
            temp.add(row)
        }
        return temp
    }

//    open fun generateItems(): ArrayList<Item> {
//        return arrayListOf()
//    }
////
//    open fun generateItems(section: Int): ArrayList<Item> {
//        return arrayListOf()
//    }
//
    open fun genericTable() {}
//    open fun rowClick(item: com.xwray.groupie.Item<GroupieViewHolder>, view: View) {}

//    override fun prepareParams(city_type: String) {
//        params.clear()
//
//        for (searchRow in searchRows) {
//
//            var key: String? = null
//            if (searchRow.containsKey("key")) {
//                key = searchRow.get("key")!!
//            }
//
//            if (key == null) {
//                continue
//            }
//
//            var value: String = ""
//            if (searchRow.containsKey("value")) {
//                value = searchRow.get("value")!!
//            }
//            if (value.isEmpty()) {
//                continue
//            }
//
//            params[key] = value
//        }
//    }

    fun showTableLayer(tableViewHeight: Int) {
        layerMask = top.mask(this)
        layerMask!!.setOnClickListener {
            top.unmask()
        }

        val layerButtonLayoutHeight: Int = setButtonLayoutHeight()
        blackViewHeight = tableViewHeight + layerButtonLayoutHeight + 200

        val statusBarHeight: Int = getStatusBarHeight()
//        val appBarHeight: Int = 64
        val frame_width = Resources.getSystem().displayMetrics.widthPixels
        val frame_height = Resources.getSystem().displayMetrics.heightPixels - statusBarHeight - 200
        val width: Int = frame_width - 2*blackViewPaddingLeft
        val topX: Int = (frame_height-blackViewHeight)/2;

        blackView = layerMask!!.blackView(
            this,
            blackViewPaddingLeft,
            topX,
            width,
            blackViewHeight)

        layerTableView = blackView!!.tableView(this, 0, layerButtonLayoutHeight)
        layerButtonLayout = blackView!!.buttonPanel(this, layerButtonLayoutHeight)

        addPanelBtn()
    }

    open fun setButtonLayoutHeight(): Int {
        val buttonViewHeight: Int = 180

        return buttonViewHeight
    }

    open fun addPanelBtn() {
        layerCancelBtn = layerButtonLayout.cancelButton(this, 120) {
            top.unmask()
        }
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

//    override fun showSearchPanel() {
//        searchSectionAdapter.setSearchSection(searchSections)

        //val p: ConstraintLayout = mainActivity!!.getMyParent()
        //searchPanel.addSearchLayer(mainActivity!!, p, able_type, searchSectionAdapter)
//    }

    protected open fun setRecyclerViewScrollListener() {

        if (recyclerView != null) {
            var pos: Int = 0

            scrollerListenr = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    //println("dy:${dy}")
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    if (tableLists.size < totalCount) {
                        //pos = layoutManager.findLastCompletelyVisibleItemPosition()
                        pos = layoutManager.findLastVisibleItemPosition()
                        //println("pos:${pos}")
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    //println("tableLists.size:${tableLists.size}")
                    if (tableLists.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && tableLists.size < totalCount && !loading) {
                        getDataStart(page, perPage)
                    }
                }
            }
            recyclerView.addOnScrollListener(scrollerListenr)
        }
    }

    protected open fun setRecyclerViewRefreshListener() {
        if (refreshLayout != null) {
            refreshListener = SwipeRefreshLayout.OnRefreshListener {
                params.clear()
                refresh()

                refreshLayout!!.isRefreshing = false
            }
            refreshLayout!!.setOnRefreshListener(refreshListener)
        }
    }

//    fun <T: FormItem> getFormItemFromKey(cls: Class<T>, key: String): T? {
//
//        var res: T? = null
//        for (formItem in form.formItems) {
//            if (formItem.name == key) {
//                res = formItem as? T
//                break
//            }
//        }
//
//        return res
//    }
//
//    open fun getFormItemFromKey(key: String): FormItem? {
//
//        var res: FormItem? = null
//        for (formItem in form.formItems) {
//            if (formItem.name == key) {
//                res = formItem
//                break
//            }
//        }
//
//        return res
//    }

//    fun getDefinedRow(key: String): HashMap<String, String> {
//
//        for (row in searchRows) {
//            if (row["key"] == key) {
//                return row
//            }
//        }
//
//        return hashMapOf()
//    }

//    fun replaceRows(key: String, row: HashMap<String, String>) {
//        for ((idx, _row) in searchRows.withIndex()) {
//            if (_row["key"] == key) {
//                searchRows[idx] = row
//                break
//            }
//        }
//    }

    //    myRows = [
    //        ["key":"data", "rows": fixedRows],
    //        ["key":"order", "rows": orderRows],
    //        ["key":"like", "rows": likeRows],
    //        ["key":"manager", "rows": courseRows],
    //    ]

//    fun getSectionRowFromMyRowsByKey(key: String): HashMap<String, Any> {
//
//        for (row in myRows) {
//            val key1: String? = row["key"] as? String
//            if (key1 != null) {
//                if (key == key1) {
//                    return row
//                }
//            }
//        }
//
//        return hashMapOf()
//    }

//    fun getSectionRowFromMyRowsByIdx(idx: Int): HashMap<String, Any> {
//
//        return myRows[idx]
//    }

    //    let fixedRows: [[String: String]] = [
    //        ["text": "帳戶資料", "icon": "account", "segue": TO_PROFILE],
    //        ["text": "更改密碼", "icon": "password", "segue": TO_PASSWORD]
    //    ]
//    fun getRowRowsFromMyRowsByKey(key: String): ArrayList<HashMap<String, String>> {
//
//        val sectionRow: HashMap<String, Any> = getSectionRowFromMyRowsByKey(key)
//        if (sectionRow.containsKey("rows")) {
//            @Suppress("UNCHECKED_CAST")
//            val tmp: ArrayList<HashMap<String, String>> = sectionRow["rows"] as ArrayList<HashMap<String, String>>
//            if (tmp != null) {
//            return tmp
//        }
//        }
//
//        return arrayListOf()
//    }

//    fun getRowRowsFromMyRowsByKey1(key: String): HashMap<String, String> {
//
//        for (sectionRow in myRows) {
//            if (sectionRow.containsKey("rows")) {
//                val rowRows = sectionRow["rows"] as ArrayList<HashMap<String, String>>
//                for (rowRow in rowRows) {
//                    if (rowRow["key"] == key) {
//                        return rowRow
//                    }
//                }
//            }
//        }
//
//        return hashMapOf()
//    }

//    fun replaceRowsByKey(sectionKey: String, rows: ArrayList<HashMap<String, String>>) {
//
//        for ((sectionIdx, sectionRow) in myRows.withIndex()) {
//
//            var sectionKey1: String? = sectionRow["key"] as? String
//            if (sectionKey1 != null) {
//                if (sectionKey1 == sectionKey) {
//                    myRows[sectionIdx]["rows"] = rows
//                }
//            }
//        }
//    }
//
//    fun replaceRowByKey(rowKey: String, _row: HashMap<String, String>) {
//        //var sectionIdx: Int = -1
//        var sectionKey: String = ""
//        for ((idx, row) in myRows.withIndex()) {
//
//            // row is  ["key":"product", "rows": productRows]
//            if (row.containsKey("rows")) {
//                val rows: ArrayList<HashMap<String, String>> = row["rows"] as ArrayList<HashMap<String, String>>
//                for (row1 in rows) {
//                    val key1: String? = row1["key"]
//                    if (key1 != null) {
//                        if (rowKey == key1) {
//                            //sectionIdx = idx
//                            //找出 section row 的 key
//                            sectionKey = myRows[idx]["key"] as String
//                        }
//                    }
//                }
//            }
//        }
//
//        replaceRowByKey(sectionKey, rowKey, _row)
//    }
//
//    fun replaceRowByKey(sectionKey: String, rowKey: String, _row: HashMap<String, String>) {
//
//        var tmpRows: ArrayList<HashMap<String, String>> = arrayListOf()
//
//
//        for ((sectionIdx, sectionRow) in myRows.withIndex()) {
//
//            tmpRows = sectionRow["rows"] as ArrayList<HashMap<String, String>>
//            val sectionKey1: String = sectionRow["key"] as String
//            if (sectionKey1 == sectionKey) {
//
//                val sectionRows: ArrayList<HashMap<String, Any>> = sectionRow["rows"] as ArrayList<HashMap<String, Any>>
//                for ((rowIdx, rowRow) in sectionRows.withIndex()) {
//                    val rowKey1: String? = rowRow["key"] as? String
//                    if (rowKey1 != null) {
//
//                        //2.用row key找出 row row
//                        if (rowKey1 == rowKey) {
//                            tmpRows[rowIdx] = _row
//                        }
//                    }
//                }
//            }
//            myRows[sectionIdx]["rows"] = tmpRows
//        }
//    }
//
//    fun replaceRowByKey(rows: ArrayList<HashMap<String, String>>, key: String, newRow: HashMap<String, String>): ArrayList<HashMap<String, String>> {
//
//        val _rows = rows
//        for ((idx, row) in rows.withIndex()) {
//
//            val key1: String = row["key"] ?: run {""}
//            if (key1 == key) {
//                _rows[idx] = newRow
//                break
//            }
//        }
//        return _rows
//    }

//    fun getRowFromKey(rows: ArrayList<HashMap<String, String>>, key: String): HashMap<String, String> {
//
//        for (row in rows) {
//
//            val key1: String = row["key"] ?: run {""}
//            if (key1 == key) {
//                return row
//            }
//        }
//        return hashMapOf()
//    }

//    open fun getRowValue(rowKey: String): String {
//
//        val row = getRowRowsFromMyRowsByKey1(rowKey)
//        var value: String = ""
//        val tmp: String? = row["value"]
//        if (tmp != null)  {
//            value = tmp
//        }
//
//        return value
//    }

    fun makeSectionRow(title: String, key: String, rows: ArrayList<OneRow>, isExpanded: Boolean=true): OneSection {

        val s: OneSection = OneSection(title, key, isExpanded)
        s.items.addAll(rows)
        return s
    }

//    fun getRowShow(rowKey: String): String {
//
//        val row = getRowRowsFromMyRowsByKey1(rowKey)
//        var show: String = ""
//        val tmp: String? = row["show"]
//        if (tmp != null)  {
//            show = tmp
//        }
//
//        return show
//    }

    fun getRowsFromSectionKey(key: String): ArrayList<OneRow> {
        for (section in oneSections) {
            if (section.key == key) {
                return section.items
            }
        }
        return arrayListOf()
    }

    fun getRowFromRowKey(key: String): OneRow {
        for (section in oneSections) {
            for (row in section.items) {
                if (key == row.key) {
                    return row
                }
            }
        }

        return OneRow()
    }

    open fun getRowValue(rowKey: String): String {
        val row = getRowFromRowKey(rowKey)
        return row.value
    }

//    fun replaceRowFromKey(key: String, row1: OneRow) {
//        for ((sectionIdx, section) in oneSections.withIndex()) {
//            for ((rowIdx, row) in section.items.withIndex()) {
//                if (key == row.key) {
//                    oneSections[sectionIdx].items[rowIdx] = row
//                    break
//                }
//            }
//        }
//    }
//
//    fun replaceRowFromIdx(sectionIdx: Int, rowIdx: Int, row1: OneRow) {
//        oneSections[sectionIdx].items[rowIdx] = row1
//    }

//    private fun updateAdapter() {
//        val rows = generateSearchItems(able_type)
//        searchPanel.searchAdapter.update(rows)
//
//    }

    override fun arenaSelected(selected: String, show: String) {

        val key: String = ARENA_KEY
        val row = getOneRowFromKey(key)
        row.value = selected
        row.show = show
        val idx: Int = getOneSectionIdxFromRowKey(key)
        oneSectionAdapter.notifyItemChanged(idx)
    }

    override fun contentEdit(key: String, content: String) {
        super.contentEdit(key, content)
        val idx: Int = getOneSectionIdxFromRowKey(key)
        oneSectionAdapter.notifyItemChanged(idx)
    }

//    override fun textChanged(str: String) {
//        val key: String = KEYWORD_KEY
//        val row = getDefinedRow(key)
//        row["value"] = str
//    }
//
//    override fun switchChanged(pos: Int, b: Boolean) {
//
//        val row = searchRows[pos]
//        val key = row["key"]!!
//        if (b) { row["value"] = "1" } else { row["value"] = "0" }
//        replaceRows(key, row)
//    }

    override fun prepare(sectionIdx: Int, rowIdx: Int) {
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

        val section = oneSections[sectionIdx]
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
            toSelectCity(value, null, able_type)
        } else if (key == WEEKDAY_KEY) {
            toSelectWeekday(value, null, able_type)
        } else if (key == START_TIME_KEY || key == END_TIME_KEY) {
            toSelectTime(key, value, null, able_type)
        } else if (key == AREA_KEY) {
            row = getOneRowFromKey(CITY_KEY)
            if (row.value.isNotEmpty()) {
                val city_id: Int = row.value.toInt()
                toSelectArea(value, city_id, null, able_type)
            } else {
                warning("纖纖選擇縣市")
            }
        } else if (key == ARENA_KEY) {
            row = getOneRowFromKey(CITY_KEY)
            if (row.value.isNotEmpty()) {
                val city_id: Int = row.value.toInt()
                toSelectArena(value, city_id, null, able_type)
            } else {
                warning("纖纖選擇縣市")
            }
        } else if (key == DEGREE_KEY) {
            toSelectDegree(value, null, able_type)
        }
    }

    override fun cellClick(row: Table) {
        val t = row::class
        if (t == ProductTable::class) {
            toShowProduct(row.token)
        } else if (t == TeachTable::class) {
            toShowTeach(row.token)
        } else if (t == CoachTable::class) {
            toShowCoach(row.token)
        } else if (t == StoreTable::class) {
            toShowStore(row.token)
        } else if (t == TeamTable::class) {
            toShowTeam(row.token)
        } else if (t == ArenaTable::class) {
            toShowArena(row.token)
        } else if (t == CourseTable::class) {
            toShowCourse(row.token)
        } else if (t == OrderTable::class) {
            toPayment(row.token)
        }
    }

    override fun cellLike(row: Table) {
        if (!member.isLoggedIn) {
            toLogin()
        } else {
            dataService.like(this, row.token, row.id)
        }
    }

    override fun cellShowMap(row: Table) {
//        println(row.address)
        val intent = Intent(this, MyMapVC::class.java)
        var name: String = ""
        if (row.name.isNotEmpty()) {
            name = row.name
        } else if (row.title.isNotEmpty()) {
            name = row.title
        }
        intent.putExtra("title", name)
        intent.putExtra("address", row.address)
        startActivity(intent)
    }

    override fun cellMobile(row: Table) {
        if (row.tel_show.isNotEmpty()) {
//            println(row.tel)
            row.tel.makeCall(this)
        } else if (row.mobile_show.isNotEmpty()) {
//            println(row.mobile)
            row.mobile.makeCall(this)
        }
    }

    override fun cellEdit(row: Table) {
//        if (storesTable != null && storesTable!!.totalCount > index) {
//            val row = storesTable!!.rows[index]
//
//        }
    }

    override fun cellDelete(row: Table) {
//        if (storesTable != null && storesTable!!.totalCount > index) {
//            val row = storesTable!!.rows[index]
//
//        }
    }

//    override fun cellCity(row: Table) {
//        val key: String = CITY_KEY
//        val city_id: Int = row.city_id
//        val row = getDefinedRow(key)
//        row["value"] = city_id.toString()
//        replaceRows(key, row)
//        prepareParams()
//        page = 1
//        tableLists.clear()
//        getDataStart(page, perPage)
//    }

//    override fun handleSearchSectionExpanded(idx: Int) {
//        //println(idx)
//        val searchSection = oneSections[idx]
//        var isExpanded: Boolean = searchSection.isExpanded
//        isExpanded = !isExpanded
//        oneSections[idx].isExpanded = isExpanded
//        oneSectionAdapter.setOneSection(oneSections)
//        oneSectionAdapter.notifyItemChanged(idx)
//    }

    override fun handleOneSectionExpanded(idx: Int) {
        //println(idx)
        val oneSection = oneSections[idx]
        var isExpanded: Boolean = oneSection.isExpanded
        isExpanded = !isExpanded
        oneSections[idx].isExpanded = isExpanded
        oneSectionAdapter.setOneSection(oneSections)
        oneSectionAdapter.notifyItemChanged(idx)
    }
}

interface List1CellDelegate {
    fun cellClick(row: Table) {}
    fun cellClick(idx: Int) {}
    fun cellClick(sectionIdx: Int, rowIdx: Int) {}
    fun cellRefresh(){}
    fun cellLike(row: Table){}
    fun cellShowMap(row: Table){}
    fun cellMobile(row: Table){}
    fun cellEdit(row: Table){}
    fun cellEdit(sectionIdx: Int, rowIdx: Int) {}
    fun cellDelete(row: Table){}
    fun cellDelete(sectionIdx: Int, rowIdx: Int) {}
    fun cellCity(row: Table){}
    fun cellArea(row: Table){}
    fun cellArena(row: Table){}
    //管理者檢視報名列表
    fun cellSignup(row: Table){}
    fun cellPrompt(sectionIdx: Int, rowIdx: Int) {}

    fun cellTextChanged(sectionIdx: Int, rowIdx: Int, str: String) {}
    fun cellSwitchChanged(sectionIdx: Int, rowIdx: Int, b: Boolean) {}
    fun cellNumberChanged(sectionIdx: Int, rowIdx: Int, number: Int) {}
    fun cellRadioChanged(key: String, sectionIdx: Int, rowIdx: Int, idx: Int) {}
    fun cellSexChanged(key: String, sectionIdx: Int, rowIdx: Int, sex: String) {}
    fun cellPrivacyChanged(sectionIdx: Int, rowIdx: Int, checked: Boolean) {}
    fun cellMoreClick(sectionIdx: Int, rowIdx: Int) {}

    fun cellClear(sectionIdx: Int, rowIdx: Int) {}

    fun cellSetTag(sectionIdx: Int, rowIdx: Int, value: String, isChecked: Boolean) {}

    fun handleOneSectionExpanded(idx: Int) {}
    fun handleSearchSectionExpanded(idx: Int) {}
    fun handleMemberSectionExpanded(idx: Int) {}
}