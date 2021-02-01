package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.*
import com.sportpassword.bm.Adapters.Form.*
//import com.sportpassword.bm.Adapters.Form.ViewDelegate
import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Form.TimeTableForm
import com.sportpassword.bm.Models.Timetables
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.xwray.groupie.GroupAdapter
import kotlinx.android.synthetic.main.activity_time_table_vc.*
import kotlinx.android.synthetic.main.mask.*
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import kotlin.reflect.full.declaredMemberProperties
import com.sportpassword.bm.Form.ValueChangedDelegate

class TimeTableVC : BaseActivity(), ValueChangedDelegate {

    var source: String = "coach"
    var token: String = "token"
//    protected lateinit var adapter: TimeTableAdapter
//    var rows: ArrayList<String> = arrayListOf()

    val startNum: Int = 6
    val endNum: Int = 23
    val columnNum: Int = 8
    val totalGridCount = (endNum-startNum)*columnNum

    var cellWidth: Float = 150f
    var cellHeight: Float = 150f
    var cellBorderWidth: Int = 1

    val gridViews: ArrayList<ViewGroup> = arrayListOf()
    val eventViews: ArrayList<ViewGroup> = arrayListOf()
    var eventTag: Int = 0

    lateinit var dialog: DialogInterface

    //model
    lateinit var timetables: Timetables
    lateinit var editTableView: RecyclerView

    //Form
    var form: TimeTableForm = TimeTableForm()
    val test: HashMap<String, String> = hashMapOf(
            TITLE_KEY to "練球",
            WEEKDAY_KEY to "2",
            TT_START_DATE to "2019-01-01",
            TT_END_DATE to "2019-12-31",
            START_TIME_KEY to "08:00",
            END_TIME_KEY to "10:00",
            TT_CHARGE to "800",
            TT_LIMIT to "6",
            TT_COLOR to "warning",
            TT_STATUS to "online",
            TT_CONTENT to "大家來練球")
    var TTEditAction: String = "INSERT"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_vc)

        source = intent.getStringExtra("source")
        token = intent.getStringExtra("token")
        dataService = CoachService

        setMyTitle("時刻表")
//        prev.text = "<"
//        next.text = ">"


        cellWidth = screenWidth.toFloat() / columnNum.toFloat()
        addGrid()

        refreshLayout = refresh
        setRefreshListener()
        refresh.isEnabled = false
        refresh()


//        scroll.onScrollChange { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            if (scrollY <= 0) {
//                refresh.isEnabled = true
//            } else {
//                refresh.isEnabled = false
//            }
//        }
        scroll.viewTreeObserver.addOnScrollChangedListener(object: ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                val scrollY = scroll.scrollY
                //println(scrollY)
                // refresh is enable untill scroll is reach top
                if (scrollY <= 0) {
                    refresh.isEnabled = true
                } else {
                    refresh.isEnabled = false
                }
            }

        })
        //adapter = TimeTableAdapter(this, rows, startNum, endNum, columnNum)
        //TimeTableView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add, menu)
        return true
    }

    override fun refresh() {
        //super.refresh()
        Loading.show(mask)
        dataService.getTT(this, token, source) { success ->
            if (success) {
                refreshEvent(container)
            } else {
                warning(dataService.msg)
            }
            closeRefresh()
            Loading.hide(mask)
        }
    }

    fun refreshEvent(parent: ViewGroup) {
        timetables = dataService.timetables
        markEvent(parent)
    }

    fun addGrid() {
        for (i in 0..totalGridCount-1) {
            val startTime: Int = i / columnNum + startNum
            val weekday: Int = i % columnNum

            val grid = generateView(cellWidth, cellHeight, i)
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

        for (i in 0..eventViews.size-1) {
            parent.removeView(eventViews[i])
        }
        eventViews.clear()
        for (i in 0..timetables.rows.size-1) {
            val row = timetables!!.rows[i]
            val hours = (row.end_time.toDateTime("HH:mm:ss")!!.timeIntervalSince(row.start_time.toDateTime("HH:mm:ss")!!)).toFloat()/(60*60)
            //println(hours)

            val eventViewWidth = cellWidth-8*cellBorderWidth
            val eventViewHeight = cellHeight*hours-8*cellBorderWidth
            val eventView = generateView(eventViewWidth, eventViewHeight, 1000+i , row._color.toColor())
            parent.addView(eventView)
            eventViews.add(eventView)
            val c1 = ConstraintSet()
            c1.clone(container)
            val weekday = row.weekday
            val _start_hour = row._start_hour
            val _start_minute = row._start_minute
            var y: Float = cellBorderWidth*2.toFloat()
            if (_start_minute > 0) {
                val a = _start_minute.toFloat()/60
                val b = (cellHeight-2*cellBorderWidth).toFloat()
                y = a * b
            }
            val idx = columnNum*(_start_hour-startNum) + weekday
            c1.connect(eventView.id, ConstraintSet.TOP, gridViews[idx].id, ConstraintSet.TOP, y.toInt())
            c1.connect(eventView.id, ConstraintSet.LEFT, gridViews[idx].id, ConstraintSet.LEFT, cellBorderWidth*4)
            c1.applyTo(container)

            val titleLbl = TextView(this)
            var lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            lp.setMargins(3, 3, 3, 3)
            titleLbl.layoutParams = lp
            titleLbl.gravity = Gravity.CENTER
            titleLbl.text = row.title
            titleLbl.textSize = 12f
            titleLbl.textColor = Color.BLACK
            //titleLbl.setBackgroundColor(Color.WHITE)
            eventView.addView(titleLbl)

            /*
            val line = DrawLine(this, 3f, 55f, eventViewWidth-6f, 55f)
            eventView.addView(line)

            val contentLbl = TextView(this)
            lp = LinearLayout.LayoutParams(eventViewWidth, eventViewHeight-55)
            lp.setMargins(3, 10, 0, 0)
            contentLbl.layoutParams = lp
            contentLbl.gravity = Gravity.CENTER
            contentLbl.text = "人數：\n" + row.limit_text
            contentLbl.textSize = 12f
            contentLbl.textColor = Color.BLACK
            eventView.addView(contentLbl)
            */
        }
    }

    protected fun generateView(width: Float, height: Float, tag: Any?=null, bColor: Int?=null): ViewGroup {
        val view = ConstraintLayout(this)
        if (tag != null) {
            view.tag = tag
        }
        view.id = View.generateViewId()
        val lp = LinearLayout.LayoutParams(width.toInt(), height.toInt())
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
        if (tag >= 1000) {
            val idx = tag - 1000
            val event = timetables.rows[idx]
            //event.print()
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
                        if (name == START_TIME_KEY || name == END_TIME_KEY) {
                            value = value!!.noSec()
                        }
                        if (name == TT_CHARGE) {
                            if (value == "-1") {
                                value = ""
                            }
                        }
                        values[name] = value!!
                    }
                }
            }
            //println(values)
            form = TimeTableForm(event.id, values)
            dialog = alert {
                title = "選項"
                customView {
                    verticalLayout {
                        button("檢視") {
                            onClick {
                                dialog.dismiss()
                                val intent = Intent(this@TimeTableVC, ShowTimetableVC::class.java)
                                intent.putExtra("tt_id", event.id)
                                intent.putExtra("source", source)
                                intent.putExtra("token", token)
                                startActivity(intent)
                            }
                        }
                        button("編輯") {
                            onClick {
                                dialog.dismiss()
                                TTEditAction = "UPDATE"
                                showEditEvent(3)
                            }
                        }
                        button("刪除") {
                            onClick {
                                dialog.dismiss()
                                layerDelete()
                            }
                        }
                        button("取消") {
                            onClick {dialog.dismiss()}
                        }
                    }
                }
            }.show()
        } else {
            val startTime: Int = tag / columnNum + startNum
            val weekday: Int = tag % columnNum
            val values: HashMap<String, String> = hashMapOf(START_TIME_KEY to startTime.toString() + ":00", WEEKDAY_KEY to weekday.toString())
            eventTag = tag
            //print(eventTag)
            form = TimeTableForm(null, values)
            showEditEvent(2)
        }
    }

    fun add(view: View) {
        form = TimeTableForm()
        //form = TimeTableForm(null, test)
        TTEditAction = "INSERT"
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

        //editTableView.
        editTableView = RecyclerView(this)
        /*
        editTableView.setRecyclerListener(object: RecyclerView.RecyclerListener {
            override fun onViewRecycled(p0: RecyclerView.ViewHolder) {
                val view = p0.itemView
                if (view.hasFocus()) {
                    view.clearFocus()
                    if (p0.itemView is EditText) {
                        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }

            }
        })
        editTableView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val imm = this@TimeTableVC.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (currentFocus != null) {
                    imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                    currentFocus.clearFocus()
                }
            }
        })
*/
        editTableView.id = R.id.SearchRecycleItem
        editTableView.backgroundColor = Color.BLACK
        val lp1 = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp1.setMargins(0, 10, 0, 0)
        editTableView.layoutParams = lp1
        editTableView.layoutManager = LinearLayoutManager(this)

        searchAdapter = GroupAdapter()
        searchAdapter.setOnItemClickListener { item, view ->
            val itemAdapter = item as FormItemAdapter
            val formItem = itemAdapter.formItem
            if (formItem.name != TITLE_KEY && formItem.name != TT_LIMIT) {
                val intent = Intent(this, EditItemActivity::class.java)
                when (formItem.name) {
                    //0 is title
                    //weekday
                    WEEKDAY_KEY -> {
                        layerCancel()
                        removeLayerChildViews()
                        intent.putExtra("key", TEAM_WEEKDAYS_KEY)
                        intent.putExtra("source", "search")
                        intent.putIntegerArrayListExtra("weekdays", formItem.sender as java.util.ArrayList<Int>)
                        startActivityForResult(intent, SEARCH_REQUEST_CODE)
                    }
                    //start_date
                    TT_START_DATE-> {
                        layerCancel()
                        removeLayerChildViews()
                        val intent1 = Intent(this, DateSelectVC::class.java)
                        val sender: HashMap<String, Any> = formItem.sender as HashMap<String, Any>
                        val type: SELECT_DATE_TYPE = sender["type"] as SELECT_DATE_TYPE
                        val value: String = sender["date"] as String
                        intent1.putExtra("key", DATE_SELECT_KEY)
                        intent1.putExtra("type", type)
                        intent1.putExtra("selected", value)
                        startActivityForResult(intent1, SEARCH_REQUEST_CODE)
                    }
                    //end_date
                    TT_END_DATE-> {
                        layerCancel()
                        removeLayerChildViews()
                        val intent1 = Intent(this, DateSelectVC::class.java)
                        val sender: HashMap<String, Any> = formItem.sender as HashMap<String, Any>
                        val type: SELECT_DATE_TYPE = sender["type"] as SELECT_DATE_TYPE
                        val value: String = sender["date"] as String
                        intent1.putExtra("key", DATE_SELECT_KEY)
                        intent1.putExtra("type", type)
                        intent1.putExtra("selected", value)
                        startActivityForResult(intent1, SEARCH_REQUEST_CODE)
                    }
                    //start_time
                    START_TIME_KEY-> {
                        layerCancel()
                        removeLayerChildViews()
                        intent.putExtra("key", TEAM_PLAY_START_KEY)
                        intent.putExtra("source", "search")
                        intent.putExtra("start", "07:00")
                        times["type"] = SELECT_TIME_TYPE.play_start
                        intent.putExtra("times", formItem.sender as HashMap<String, Any>)
                        startActivityForResult(intent, SEARCH_REQUEST_CODE)
                    }
                    //end_time
                    END_TIME_KEY-> {
                        layerCancel()
                        removeLayerChildViews()
                        intent.putExtra("key", TEAM_PLAY_END_KEY)
                        intent.putExtra("source", "search")
                        intent.putExtra("start", "06:00")
                        times["type"] = SELECT_TIME_TYPE.play_start
                        intent.putExtra("times", formItem.sender as HashMap<String, Any>)
                        startActivityForResult(intent, SEARCH_REQUEST_CODE)
                    }
                    //6 is charge
                    //7 is limit
                    //color
                    TT_COLOR-> {
                        layerCancel()
                        removeLayerChildViews()
                        val intent1 = Intent(this, ColorSelectVC1::class.java)
                        intent1.putExtra("key", COLOR_SELECT_KEY)
                        if (formItem.sender != null) {
                            intent1.putExtra("selecteds", formItem.sender as ArrayList<MYCOLOR>)
                        }
                        startActivityForResult(intent1, SEARCH_REQUEST_CODE)
                    }
                    //status
                    TT_STATUS-> {
                        layerCancel()
                        removeLayerChildViews()
                        val intent1 = Intent(this, StatusSelectVC1::class.java)
                        intent1.putExtra("key", STATUS_SELECT_KEY)
                        intent1.putExtra("selected", formItem.sender as STATUS)
                        startActivityForResult(intent1, SEARCH_REQUEST_CODE)
                    }
                    //content
                    TT_CONTENT-> {
                        layerCancel()
                        removeLayerChildViews()
                        intent.putExtra("key", CONTENT_KEY)
                        intent.putExtra("value", formItem.sender as String)
                        startActivityForResult(intent, SEARCH_REQUEST_CODE)
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
                    var key = ""
                    if (data != null && data!!.hasExtra("key")) {
                        key = data!!.getStringExtra("key")
                    }
                    when (key) {
                        TEAM_WEEKDAYS_KEY -> {
                            idx = 1
                            weekdays = data!!.getIntegerArrayListExtra("weekdays")
                            val item = form.formItems[idx] as WeekdayFormItem
                            item.weekdays = weekdays
                            item.make()
                        }
                        DATE_SELECT_KEY -> {
                            val type = data!!.getSerializableExtra("type") as SELECT_DATE_TYPE
                            if (type == SELECT_DATE_TYPE.start) {
                                idx = 2
                            } else {
                                idx = 3
                            }
                            val selected = data!!.getStringExtra("selected")
                            val item = form.formItems[idx] as DateFormItem
                            item.value = selected
                            item.make()
                        }
                        TEAM_PLAY_START_KEY -> {
                            idx = 4
                            times = data!!.getSerializableExtra("times") as HashMap<String, Any>
                            if (times.containsKey("time")) {
                                val item = form.formItems[idx] as TimeFormItem
                                item.value = times["time"] as String
                                item.make()
                            }
                        }
                        TEAM_PLAY_END_KEY -> {
                            idx = 5
                            times = data!!.getSerializableExtra("times") as HashMap<String, Any>
                            if (times.containsKey("time")) {
                                val item = form.formItems[idx] as TimeFormItem
                                item.value = times["time"] as String
                                item.make()
                            }
                        }
                        COLOR_SELECT_KEY -> {
                            idx = 8
                            val colors = data!!.getSerializableExtra("selecteds") as ArrayList<MYCOLOR>
                            val item = form.formItems[idx] as ColorFormItem
                            item.value = colors[0].toString()
//                            println(item.value)
                            item.make()
                        }
                        STATUS_SELECT_KEY -> {
                            idx = 9
                            val status = data!!.getSerializableExtra("selected") as STATUS
                            val item = form.formItems[idx] as StatusFormItem
                            item.value = status.toString()
                            item.make()
                        }
                        CONTENT_KEY -> {
                            idx = 10
                            val value = data!!.getStringExtra("res")
                            val item = form.formItems[idx] as ContentFormItem
                            item.value = value
                            item.make()
                        }
                    }
//                    TTEditAction = "INSERT"
                    showEditEvent(3)
                    //val rows = generateFormItems()
                    //searchAdapter.addAll(rows)
                }
            }
        }
    }

    fun generateFormItems(): ArrayList<FormItemAdapter> {
        val rows: ArrayList<FormItemAdapter> = arrayListOf()
        val section: Int = 0
//        var indexPath: HashMap<String, Int> = hashMapOf()
//        indexPath["section"] = section

        val clearClick = { formItem: FormItem ->
            formItem.reset()
            val rows = generateFormItems()
            searchAdapter.update(rows)
        }

        val promptClick = {formItem: FormItem ->
            if (formItem.tooltip != null) {
                Alert.show(this, "提示", formItem.tooltip!!)
            }
        }

        val rowClick = { formItem: FormItem ->

        }


        for ((idx, formItem) in form.formItems.withIndex()) {
//            indexPath["row"] = idx

            val indexPath: IndexPath = IndexPath(section, idx)

            var formItemAdapter: FormItemAdapter? = null
            if (formItem.uiProperties.cellType == FormItemCellType.textField) {
                formItemAdapter = TextFieldAdapter(formItem, clearClick, promptClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.content) {
                formItemAdapter = ContentAdapter(formItem, clearClick, promptClick, rowClick)
            } else {
                formItemAdapter = MoreAdapter(formItem, clearClick, promptClick, rowClick)
            }

            if (formItemAdapter != null) {
                formItemAdapter!!.valueChangedDelegate = this
                rows.add(formItemAdapter!!)
            }
        }

        return rows
    }

    fun prepareParams() {
        params.clear()
        params["model_token"] = token
        params["created_token"] = member.token!!
        for (formItem in form.formItems) {
            if (formItem.name != null && formItem.value != null) {
                val value = formItem.value!!
                params[formItem.name!!] = formItem.value!!
            }
        }
        if (form.id != null) {
            params["id"] = form.id!!.toString()
        }
        //print(params)
        Loading.show(mask)
        dataService.updateTT(this, source, params) { success ->
                Loading.hide(mask)
            if (success) {
                unmask()
                refreshEvent(container)
            } else {
                warning(dataService.msg)
            }
        }
    }

    override fun layerSubmit(page: String) {
        if (TTEditAction == "UPDATE") {
            val (isChange, msg) = form.isChanged()
            if (!isChange) {
                val _msg = if (msg != null) {"沒有更改任何值，所以不用送出更新"}else{msg}
                warning(_msg!!)
                return
            }
        }
        val (isValid, msg) = form.isValid()
        if (!isValid) {
            var _msg = "欄位驗證錯誤"
            if (msg != null) {
                _msg = msg!!
            }
            warning(_msg)
            return
        }

        prepareParams()
    }

    override fun layerDelete() {
        warning("是否真的要刪除此事件？", "取消", "確定", {_layerDelete()})
    }

    fun _layerDelete() {
        params.clear()
        params["model_token"] = token
        if (form.id != null) {
            params["id"] = form.id!!.toString()
        }
        //print(params)
        unmask()
        Loading.show(mask)
        dataService.deleteTT(this, "coach", params) { success ->
            Loading.hide(mask)
            if (success) {
                refreshEvent(container)
            } else {
                warning(dataService.msg)
            }
        }
    }

    override fun textFieldTextChanged(formItem: FormItem, text: String) {
        //println(row)
        //println(text)
        //val item = form.formItems[indexPath.row]
//        formItem.value = text
//        formItem.make()
    }

    override fun sexChanged(sex: String) {}

    override fun privateChanged(checked: Boolean) {}

    override fun tagChecked(checked: Boolean, name: String, key: String, value: String) {}

    override fun stepperValueChanged(number: Int, name: String) {}
}














