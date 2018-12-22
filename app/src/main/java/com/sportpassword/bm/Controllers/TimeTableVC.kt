package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sportpassword.bm.Adapters.Form.FormItemAdapter
import com.sportpassword.bm.Form.FormItem.ColorFormItem
import com.sportpassword.bm.Form.FormItem.StatusFormItem
import com.sportpassword.bm.Form.FormItem.TimeFormItem
import com.sportpassword.bm.Form.FormItem.WeekdayFormItem
import com.sportpassword.bm.Form.TimeTableForm
import com.sportpassword.bm.Models.TimeTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_time_table_vc.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk25.coroutines.onScrollChange
import org.jetbrains.anko.textColor
import com.sportpassword.bm.Utilities.*
import kotlin.reflect.full.declaredMemberProperties

class TimeTableVC : BaseActivity() {

    var source: String = "coach"
    var token: String = "token"
//    protected lateinit var adapter: TimeTableAdapter
//    var rows: ArrayList<String> = arrayListOf()

    val startNum: Int = 6
    val endNum: Int = 23
    val columnNum: Int = 8
    val totalGridCount = (endNum-startNum)*columnNum

    var cellWidth: Float = 150f
    var cellHeight: Int = 150
    var cellBorderWidth: Int = 1

    val gridViews: ArrayList<ViewGroup> = arrayListOf()
    val eventViews: ArrayList<ViewGroup> = arrayListOf()
    var eventTag: Int = 0

    //model
    lateinit var timeTable: TimeTable

    //Form
    var form: TimeTableForm = TimeTableForm()
    val test: HashMap<String, String> = hashMapOf(
            TT_TITLE to "練球",
            TT_WEEKDAY to "5",
            TT_START to "14:00",
            TT_END to "17:00",
            TT_LIMIT to "6",
            TT_COLOR to "warning",
            TT_STATUS to "offline",
            TT_CONTENT to "大家來練球")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_vc)

        source = intent.getStringExtra("source")
        token = intent.getStringExtra("token")
        dataService = CoachService

        setMyTitle("時刻表")
        prev.text = "<"
        next.text = ">"

        cellWidth = screenWidth.toFloat() / columnNum.toFloat()
        addGrid()

        refreshLayout = refresh
        setRefreshListener()
        refresh.isEnabled = false
        refresh()

        scroll.onScrollChange { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY <= 0) {
                refresh.isEnabled = true
            } else {
                refresh.isEnabled = false
            }
        }

        //adapter = TimeTableAdapter(this, rows, startNum, endNum, columnNum)
        //TimeTableView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add, menu)
        return true
    }

    override fun refresh() {
        super.refresh()
        Loading.show(mask)
        dataService.getTT(this, token, source) { success ->
            if (success) {
                timeTable = dataService.timeTable
                markEvent(container)
            } else {
                warning(dataService.msg)
            }
            closeRefresh()
            Loading.hide(mask)
        }
    }

    fun addGrid() {
        for (i in 0..totalGridCount-1) {
            val startTime: Int = i / columnNum + startNum
            val weekday: Int = i % columnNum

            val grid = generateView(cellWidth.toInt(), cellHeight, i)
            grid.setBackgroundResource(R.drawable.timetable_item_border)

            if (weekday == 0) {
                addTimeLabel(grid, startTime)
            }

            container.addView(grid)
            gridViews.add(grid)
            val c = ConstraintSet()
            c.clone(container)
            if (startTime == startNum) {
                c.connect(grid.id, ConstraintSet.TOP, container.id, ConstraintSet.TOP, 0)
                if (weekday == 0) {
                    c.connect(grid.id, ConstraintSet.LEFT, container.id, ConstraintSet.LEFT, 0)
                } else {
                    c.connect(grid.id, ConstraintSet.LEFT, gridViews[i-1].id, ConstraintSet.RIGHT, 0)
                }
            } else {
                c.connect(grid.id, ConstraintSet.TOP, gridViews[i-8].id, ConstraintSet.BOTTOM, 0)
                if (weekday == 0) {
                    c.connect(grid.id, ConstraintSet.LEFT, container.id, ConstraintSet.LEFT, 0)
                } else {
                    c.connect(grid.id, ConstraintSet.LEFT, gridViews[i-1].id, ConstraintSet.RIGHT, 0)
                }
            }
            c.applyTo(container)
        }
    }

    fun addTimeLabel(parent: ViewGroup, time: Int) {
        val t = TextView(this)
        t.id = View.generateViewId()
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        t.layoutParams = lp
        t.text = String.format("%d-%d", time, time+1)
        t.textColor = Color.WHITE
        t.gravity = Gravity.CENTER
        parent.addView(t)
    }

    fun markEvent(parent: ViewGroup) {
        for (i in 0..timeTable.rows.size-1) {
            val row = timeTable.rows[i]
            val hours = row._end - row._start
            val eventViewWidth = cellWidth.toInt()-2*cellBorderWidth
            val eventViewHeight = cellHeight*hours-2*cellBorderWidth
            val eventView = generateView(eventViewWidth, eventViewHeight, 100+i , row._color.toColor())
            parent.addView(eventView)
            eventViews.add(eventView)
            val c1 = ConstraintSet()
            c1.clone(container)
            val day = row.day
            val _start = row._start
            val idx = columnNum*(_start-startNum) + day
            c1.connect(eventView.id, ConstraintSet.TOP, gridViews[idx].id, ConstraintSet.TOP, cellBorderWidth)
            c1.connect(eventView.id, ConstraintSet.LEFT, gridViews[idx].id, ConstraintSet.LEFT, cellBorderWidth)
            c1.applyTo(container)

            val titleLbl = TextView(this)
            var lp = LinearLayout.LayoutParams(eventViewWidth, 50)
            lp.setMargins(3, 10, 0, 0)
            titleLbl.layoutParams = lp
            titleLbl.gravity = Gravity.CENTER
            titleLbl.text = row.title
            titleLbl.textSize = 12f
            titleLbl.textColor = Color.BLACK
            eventView.addView(titleLbl)

            val line = DrawLine(this, 3f, 55f, eventViewWidth-6f, 55f)
            eventView.addView(line)

            val contentLbl = TextView(this)
            lp = LinearLayout.LayoutParams(eventViewWidth, eventViewHeight-55)
            lp.setMargins(3, 10, 0, 0)
            contentLbl.layoutParams = lp
            contentLbl.gravity = Gravity.CENTER
            contentLbl.text = row.content
            contentLbl.textSize = 12f
            contentLbl.textColor = Color.BLACK
            eventView.addView(contentLbl)
        }
    }

    protected fun generateView(width: Int, height: Int, tag: Any?=null, bColor: Int?=null): ViewGroup {
        val view = ConstraintLayout(this)
        if (tag != null) {
            view.tag = tag
        }
        view.id = View.generateViewId()
        val lp = LinearLayout.LayoutParams(width, height)
        view.layoutParams = lp
        if (bColor != null) {
            view.backgroundColor = bColor
        }
        view.setOnClickListener {
            clickEvent(it)
        }

        return view
    }

    protected fun clickEvent(view: View) {
        val tag = view.tag as Int
//        println(tag)
        //event
        if (tag >= 100) {
            val idx = tag - 100
            val event = timeTable.rows[idx]
            var values: HashMap<String, String> = hashMapOf()
            for (formItem in form.formItems) {
                if (formItem.name != null) {
                    val name: String = formItem.name!!
                    var value: String? = null
                    event.javaClass.kotlin.declaredMemberProperties.forEach {
                        if (it.name == name) {
                            value = it.get(event).toString()
                            //val type = it.returnType
                        }
                    }
                    //print(value)
                    if (value != null) {
                        if (name == TT_START || name == TT_END) {
                            value = value!!.noSec()
                        }
                        values[name] = value!!
                    }
                }
            }
            //print(values)
            form = TimeTableForm(event.id, values)
            showEditEvent(3)
        } else {
            val startTime: Int = tag / columnNum + startNum
            val weekday: Int = tag % columnNum
            val values: HashMap<String, String> = hashMapOf(TT_START to startTime.toString() + ":00", TT_WEEKDAY to weekday.toString())
            eventTag = tag
            //print(eventTag)
            form = TimeTableForm(null, values)
            showEditEvent(2)
        }
    }

    fun add(view: View) {
        form = TimeTableForm(null, test)
        showEditEvent()
    }

    fun showEditEvent(buttonCount: Int = 2) {
        mask()
        layerBtnCount = buttonCount
        addLayer("")
    }

    override fun _addLayer(page: String) {
        val parent = getMyParent()
        val w = parent.measuredWidth
        addEditTableView(page, w, 0)
        layerAddButtonLayout()
        layerAddSubmitBtn(page)
        layerAddCancelBtn()
        if (layerBtnCount > 2) {
            layerAddDeleteBtn()
        }
    }

    protected fun addEditTableView(page: String, w: Int, padding: Int) {
        val editTableView = RecyclerView(this)
        editTableView.id = R.id.SearchRecycleItem
        editTableView.backgroundColor = Color.BLACK
        val lp1 = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp1.setMargins(0, 10, 0, 0)
        editTableView.layoutParams = lp1
        editTableView.layoutManager = LinearLayoutManager(this)
        //editTableView.
        searchAdapter = GroupAdapter<ViewHolder>()
        searchAdapter.setOnItemClickListener { item, view ->
            val itemAdapter = item as FormItemAdapter
            val idx = itemAdapter.row
            val formItem = form.formItems[idx]
            if (formItem.name != TT_TITLE && formItem.name != TT_LIMIT) {
                val intent = Intent(this, EditItemActivity::class.java)
                when (idx) {
                    1 -> {
                        intent.putExtra("key", TEAM_DAYS_KEY)
                        intent.putExtra("source", "search")
                        intent.putIntegerArrayListExtra("weekdays", formItem.sender as java.util.ArrayList<Int>)
                        startActivityForResult(intent, SEARCH_REQUEST_CODE)
                    }
                    2-> {
                        intent.putExtra("key", TEAM_PLAY_START_KEY)
                        intent.putExtra("source", "search")
                        intent.putExtra("start", "06:00")
                        times["type"] = SELECT_TIME_TYPE.play_start
                        intent.putExtra("times", formItem.sender as HashMap<String, Any>)
                        startActivityForResult(intent, SEARCH_REQUEST_CODE)
                    }
                    3-> {
                        intent.putExtra("key", TEAM_PLAY_END_KEY)
                        intent.putExtra("source", "search")
                        intent.putExtra("start", "06:00")
                        times["type"] = SELECT_TIME_TYPE.play_start
                        intent.putExtra("times", formItem.sender as HashMap<String, Any>)
                        startActivityForResult(intent, SEARCH_REQUEST_CODE)
                    }
                    5-> {
                        val intent1 = Intent(this, ColorSelectVC::class.java)
                        intent1.putExtra("key", COLOR_SELECT_KEY)
                        intent1.putExtra("selecteds", formItem.sender as ArrayList<MYCOLOR>)
                        startActivityForResult(intent1, SEARCH_REQUEST_CODE)
                    }
                    6-> {
                        val intent1 = Intent(this, StatusSelectVC::class.java)
                        intent1.putExtra("key", STATUS_SELECT_KEY)
                        intent1.putExtra("selected", formItem.sender as STATUS)
                        startActivityForResult(intent1, SEARCH_REQUEST_CODE)
                    }
                }
            }
        }
        val rows = generateFormItems()
        searchAdapter.addAll(rows)

        editTableView.adapter = searchAdapter
        layerContainerView!!.addView(editTableView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)

        var value = "全部"
        var idx = 0
        when (requestCode) {
            SEARCH_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val key = data!!.getStringExtra("key")
                    when (key) {
                        TEAM_DAYS_KEY -> {
                            idx = 1
                            weekdays = data!!.getIntegerArrayListExtra("weekdays")
                            val item = form.formItems[idx] as WeekdayFormItem
                            item.weekdays = weekdays
                            item.make()
                        }
                        TEAM_PLAY_START_KEY -> {
                            idx = 2
                            times = data!!.getSerializableExtra("times") as HashMap<String, Any>
                            if (times.containsKey("time")) {
                                val item = form.formItems[idx] as TimeFormItem
                                item.value = times["time"] as String
                                item.make()
                            }
                        }
                        TEAM_PLAY_END_KEY -> {
                            idx = 3
                            times = data!!.getSerializableExtra("times") as HashMap<String, Any>
                            if (times.containsKey("time")) {
                                val item = form.formItems[idx] as TimeFormItem
                                item.value = times["time"] as String
                                item.make()
                            }
                        }
                        COLOR_SELECT_KEY -> {
                            idx = 5
                            val colors = data!!.getSerializableExtra("selecteds") as ArrayList<MYCOLOR>
                            val item = form.formItems[idx] as ColorFormItem
                            item.value = colors[0].toString()
//                            println(item.value)
                            item.make()
                        }
                        STATUS_SELECT_KEY -> {
                            idx = 6
                            val status = data!!.getSerializableExtra("selected") as STATUS
                            val item = form.formItems[idx] as StatusFormItem
                            item.value = status.toString()
                            item.make()
                        }
                    }
                    val rows = generateFormItems()
                    searchAdapter.update(rows)
                }
            }
        }
    }

    fun generateFormItems(): ArrayList<FormItemAdapter> {
        val rows: ArrayList<FormItemAdapter> = arrayListOf()
        val section: Int = 0
//        var indexPath: HashMap<String, Int> = hashMapOf()
//        indexPath["section"] = section
        for ((idx, formItem) in form.formItems.withIndex()) {
//            indexPath["row"] = idx
            rows.add(FormItemAdapter(form, idx, 0, { i ->
                val forItem = form.formItems[i]
                forItem.reset()
                val rows = generateFormItems()
                searchAdapter.update(rows)
            })
            )
        }

        return rows
    }

}














