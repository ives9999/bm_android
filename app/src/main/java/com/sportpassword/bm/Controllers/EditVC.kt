package com.sportpassword.bm.Controllers

import android.app.Activity
import android.os.Bundle
import com.sportpassword.bm.R
import org.jetbrains.anko.sdk27.coroutines.onClick
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import java.io.File

open class EditVC : MyTableVC() {

    //private lateinit var view: ViewGroup

    var token: String = ""
    var title: String = ""

    var originW: Int = 0
    var originH: Int = 0
    var originMarginTop = 0
    var originMarginBottom = 0
    lateinit var originScaleType: ImageView.ScaleType
    lateinit var inputV: List<View>
    var isFeaturedChange: Boolean = false

//    var model: SuperData = Team(0, "", "", "")
    var action: String = "INSERT"
//    val editTexts: HashMap<String, Int> = hashMapOf()

    var table: Table? = null

    var bottom_button_count: Int = 2
    val button_width: Int = 400

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        setContentView(R.layout.edit_vc)

        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")!!
        }
        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")!!
        }

//        source = intent.getStringExtra("source")!!
//        token = intent.getStringExtra("token")!!
//        val tmp = intent.getStringExtra("title")!!

//        if (source == "coach") {
//            title = "新增教練"
//            dataService = CoachService
//            model = Coach(0, ", ", "")
//        } else if (source == "team") {
//            title = "新增球隊"
//            dataService = TeamService
//            model = Team(0, "", "", "")
//        } else if (source == "arena") {
//            title = "新增球館"
//            dataService = ArenaService
//            model = Arena(0, "")
//        }

//        if (tmp.length > 0) {
//            title = tmp
//        }
        setMyTitle(title)
//        model.dataReset()
        //println(model.data)

        imageView = findViewById(R.id.edit_featured)
        getImageViewParams()
        initImagePicker(R.layout.image_picker_layer)
        val a: RelativeLayout = findViewById(R.id.edit_featured_container)
        a.onClick {
            showImagePickerLayer()
        }

//        sections = model.sections
        findViewById<RecyclerView>(R.id.editTableView) ?. let {
            recyclerView = it
        }

        findViewById<SwipeRefreshLayout>(R.id.refresh) ?. let {
            refreshLayout = it
        }

        //move to MyTableVC的 init()
//        setRefreshListener()

        oneSectionAdapter = OneSectionAdapter(this, R.layout.cell_section, this, hashMapOf())
        // init is deal
//        oneSectionAdapter.setOneSection(oneSections)

        recyclerView.adapter = oneSectionAdapter

        //initAdapter(true)

        //val allV = getAllChildrenBFS(edit_list)
        //inputV = filterInputField(allV)

        //textFieldDidEndEditing(inputV)

//        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.edit_refresh)
        //println(refreshLayout)
//        setRefreshListener()
//        refresh()

        setBottomButtonPadding()

        oneSections.clear()
        init()
        if (token != null && token.isNotEmpty()) {
            refresh()
        } else {
            initData()
        }
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun setBottomButtonPadding() {

        val padding: Int = (screenWidth - bottom_button_count * button_width) / (bottom_button_count + 1)
        //val leading: Int = bottom_button_count * padding + (bottom_button_count - 1) * button_width

        findViewById<Button>(R.id.submitBtn) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }

        findViewById<Button>(R.id.cancelBtn) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }
    }

    open fun initData() {}

    override fun refresh() {
        //Loading.show(view)
        val params: HashMap<String, String> = hashMapOf("token" to token)
        dataService.getOne(this, params) { success ->
            if (success) {
                genericTable()
                runOnUiThread {
                    initData()
                    initFeatured()
                    oneSectionAdapter.notifyDataSetChanged()
                    setMyTitle(title)
                    //putValue()
                }
                //notifyChanged(true)

                //teamedit_name.setSelection(teamedit_name.length())
                closeRefresh()
            }
            runOnUiThread {
                //Loading.hide(view)
            }
        }
    }

    fun initFeatured() {

        findViewById<ImageView>(R.id.edit_featured) ?. let {
            if (table != null) {
                if (table!!.featured_path.count() > 0) {
                    table!!.featured_path.image(this, it)
//            val featured: String = myTable!!.featured_path
//            setImage(null, featured)
                }
            }
        }
    }

//    override fun generateItems(section: Int): ArrayList<Item> {
//
//        val items: ArrayList<Item> = arrayListOf()
//        val rows = model.rows[section]
//        for ((idx, key) in rows.withIndex()) {
//            val indexPath = IndexPath(section, idx)
//            val dataRow = model.getDataRowWithKey(key)
//            items.add(EditItem(source, indexPath, dataRow, { key, id ->
//                editTexts[key] = id
//            }, {
//                clear(it)
//            }))
//        }
//
//        return items
//    }

//    override fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {
//
//        val editItem = item as EditItem
//        val dataRow = editItem.row
//        if (dataRow.containsKey("key")) {
//            val key = dataRow.get("key")!! as String
//            if (dataRow.containsKey("atype")) {
//                val aType = dataRow.get("atype")!! as String
//                if (aType == "more") {
//                    prepare(key)
//                }
//            }
//        }
//    }
//
//    fun clear(key: String) {
//        if (key == CITY_KEY) {
//            updateCity()
//            updateArena()
//            notifyChanged(true)
//        } else if (key == ARENA_KEY) {
//            updateArena()
//        } else if (key == TEAM_WEEKDAYS_KEY) {
//            updateDays()
//        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
//            updateTime(key)
//        } else if (key == TEAM_DEGREE_KEY) {
//            updateDegree()
//        } else if (key == TEAM_TEMP_CONTENT_KEY || key == CHARGE_KEY || key == CONTENT_KEY || key == COACH_EXP_KEY || key == COACH_FEAT_KEY || key == COACH_LICENSE_KEY) {
//            val type = model.contentKey2Type(key)
//            updateText(key)
//        }
//        model.data[key]!!["change"] = true
//        notifyChanged(true)
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.button, menu)
//        return true
//    }
//    override fun refresh() {
//        if (token.length > 0) {
//            Loading.show(mask)
//            action = "UPDATE"
//            dataService.getOne(this, source, "name", token) { success ->
//                if (success) {
//                    model.data = dataService.data
//                    //setTeamData()
//                    //dataToField(inputV)
//                    if (model.data.containsKey(FEATURED_KEY)) {
//                        val featured: String = model.data[FEATURED_KEY]!!["value"] as String
////                        println(featured)
//                        if (featured.length > 0) {
//                            setImage(null, featured)
//                        }
//                    }
//                    notifyChanged(true)
//
//                    //teamedit_name.setSelection(teamedit_name.length())
//                    closeRefresh()
//                }
//                Loading.hide(mask)
//            }
//        }
//    }

    override fun setImage(newFile: File?, url: String?) {

        findViewById<TextView>(R.id.featured_text) ?. let {
            it.visibility = View.INVISIBLE
        }

        findViewById<ImageView>(R.id.edit_featured) ?. let {
            val layoutParams = it.layoutParams as RelativeLayout.LayoutParams
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
            layoutParams.setMargins(0, 0, 0, 0)
            imageView.layoutParams = layoutParams
            isFeaturedChange = true
            super.setImage(newFile, url)
        }
    }

    override fun removeImage() {

        findViewById<TextView>(R.id.featured_text) ?. let {
            it.visibility = View.VISIBLE
        }

        findViewById<ImageView>(R.id.edit_featured) ?. let {
            val layoutParams = it.layoutParams as LinearLayout.LayoutParams
            layoutParams.width = originW
            layoutParams.height = originH
            layoutParams.setMargins(0, originMarginTop, 0, originMarginBottom)
            imageView.layoutParams = layoutParams
            imageView.scaleType = originScaleType
            isFeaturedChange = true
            super.removeImage()
            closeImagePickerLayer()
        }
    }

    private fun getImageViewParams() {
        findViewById<ImageView>(R.id.edit_featured) ?. let {
            val l = it.layoutParams as RelativeLayout.LayoutParams
            originW = l.width
            originH = l.height
            originScaleType = it.scaleType
            //val m = edit_featured.
            originMarginTop = l.topMargin
            originMarginBottom = l.bottomMargin
        }
    }

    open fun submitValidate() {}

    fun submit(view: View) {

        hideKeyboard()

        msg = ""
        for (section in oneSections) {
            for (row in section.items) {
                var isRequire: Boolean = false
                if (row.value.isInt()) {
                    if (row.isRequired && row.value.toInt() <= 0) {
                        isRequire = true
                    }
                } else {
                    if (row.isRequired && row.value.isEmpty()) {
                        isRequire = true
                    }
                }

                if (isRequire) {
                    msg += row.msg + "\n"
                }
            }
        }

        if (msg.isNotEmpty()) {
            warning(msg)
        } else {
            Loading.show(view)
            //val params: HashMap<String, String> = hashMapOf()
            for (section in oneSections) {
                for (row in section.items) {
                    params[row.key] = row.value
                }
            }

            var action = "UPDATE"
            if (token != null && token.length == 0) {
                action = "INSERT"
            }
            if (action == "INSERT") {
                params[CREATED_ID_KEY] = member.id.toString()
            }
            if (token != null) {
                params["token"] = token
            }

            params["device"] = "app"
            params["do"] = "update"

            //println(params)

            dataService.update(this, params, filePath) { success ->
                Loading.hide(view)
                if (success) {
                    if (dataService.success) {
                        runOnUiThread {
                            val jsonString: String = dataService.jsonString
                            val successTable: SuccessTable? = jsonToModel<SuccessTable>(jsonString)
                            if (successTable != null && successTable.success) {
                                runOnUiThread {
                                    info("編輯成功", "", "確定") {
                                        setResult(Activity.RESULT_OK, intent)
                                        finish()
                                    }
                                }
                            } else {
                                if (successTable != null) {
                                    runOnUiThread {
                                        warning(successTable.msg)
                                    }
                                }
                            }
//                            Alert.update(this, action) {
//                                if (file != null) {
//                                    file!!.delete()
//                                }
//                                val update = Intent(NOTIF_COURSE_UPDATE)
//                                LocalBroadcastManager.getInstance(this).sendBroadcast(update)
//                                val intent = Intent()
//                                intent.putExtra("manager_token", member.token)
//                                setResult(Activity.RESULT_OK, intent)
//                                finish()
//                            }
                        }
                    } else {
                        runOnUiThread {
                            warning(dataService.msg)
                        }
                    }
                } else {
                    runOnUiThread {
                        warning(dataService.msg)
                    }
                }
            }
        }
    }

    fun cancel(view: View) {
        prev()
    }

//    private fun fieldToData() {
//        for ((key, idx) in editTexts) {
//            if (model.data.containsKey(key)) {
//                val it = edit_list.findViewHolderForAdapterPosition(idx) as com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
//                val newValue: String = it.edit_text.text.toString()
////                println("key: $key")
////                println("newValue: $newValue")
//                val dataRow = model.data[key]!!
//                //if (newValue.length > 0) {
//                val oldValue: Any = dataRow["value"] as Any
//                val vtype = dataRow["vtype"] as String
//                _fieldToData(oldValue, newValue, vtype, key)
//            }
//        }
//    }
//    private fun _fieldToData(_oldValue: Any, _newValue: String, vtype: String, key: String) {
//        var oldValue: Any = _oldValue
//        if (vtype == "String") {
//            oldValue = oldValue as String
//            val newValue = _newValue
//            if (oldValue != newValue) {
////                println("oldValue: $oldValue")
////                println("newValue: $newValue")
//                model.data[key]!!["value"] = newValue
//                model.data[key]!!["change"] = true
//            }
//        } else if (vtype == "Int") {
//            oldValue = oldValue as Int
//            var newValue: Int = -1
//            if (_newValue.length > 0) {
//                newValue = _newValue.toInt()
//            }
//            if (oldValue != newValue) {
//                model.data[key]!!["value"] = newValue
//                model.data[key]!!["change"] = true
//            }
//        } else if (vtype == "Bool") {
//            oldValue = oldValue as Boolean
//            var newValue: Boolean = true
//            if (_newValue.length > 0) {
//                newValue = _newValue.toBoolean()
//            }
//            if (oldValue != newValue) {
//                model.data[key]!!["value"] = newValue
//                model.data[key]!!["change"] = true
//            }
//        }
//    }

    // filter the not text input field
//    private fun filterInputField(allV: List<View>): List<View> {
//        var res: ArrayList<View> = arrayListOf()
//        for (i in 0..allV.size-1) {
//            val v = allV.get(i)
//            val _tag = v.tag
//            var tag = ""
//            if (_tag != null) {
//                tag = _tag as String
//            }
//            if (v is EditText && tag.length > 0 && model.data.containsKey(tag)) {
////            for ((key, value) in model.data) {
////                if (key == tag) {
//                    res.add(v)
////                }
//            }
//        }
//        return res
//    }
//    private fun dataToField(inputV: List<View>) {
//        for (i in 0..inputV.size-1) {
//            val v = inputV.get(i)
//            val tag = v.tag
//            if (v is EditText) {
//                for ((key, value) in model.data) {
//                    if (tag == key) {
//                        val tmp: Any = value["value"] as Any
//                        val vtype = value["vtype"] as String
//                        if (vtype == "String") {
//                            v.setText(tmp.toString())
//                        } else if (vtype == "Int") {
//                            var tmp1: Int = tmp as Int
//                            val tmp2: String = if (tmp1 < 0) "" else tmp1.toString()
//                            v.setText(tmp2)
//                        } else if (vtype == "Bool") {
//                            v.setText(tmp.toString())
//                        }
//                    }
//                }
//            } else if (v is TextView) {
//                for ((key, value) in model.data) {
//                    if (tag == key) {
//                        val tmp: String = value["show"] as String
//                        v.text = tmp
//                    }
//                }
//            }
//        }
//        val featured: String = model.data[FEATURED_KEY]!!["value"] as String
//        if (featured.length > 0) {
//            setImage(null, featured)
//        }
//    }


//    private fun textFieldDidEndEditing(inputV: List<View>) {
//        for (i in 0..inputV.size-1) {
//            val v = inputV.get(i)
//            if (v is EditText) {
//                val id = v.resources.getResourceName(v.id)
//                //println(id)
//                val feature = _idRegex(id)
//                if (feature != null) {
//                    val clear_id = "clearbutton_${feature}"
//                    val resId = resources.getIdentifier(clear_id, "id", packageName)
//                    if (resId != 0) {
//                        val clear_button = findViewById<ImageButton>(resId)
//                        clear_button.setOnClickListener() { view ->
//                            v.setText("")
//                        }
//                    }
//                }
//                v.setOnFocusChangeListener(this)
//            } else if (v is TextView) {
//                val tmp = v.parent
//                var l: ViewGroup? = null
//                if (tmp is LinearLayout) {
//                    l = tmp as LinearLayout
//                } else if (tmp is ConstraintLayout) {
//                    l = tmp as ConstraintLayout
//                }
//                if (l != null) {
//                    l.onClick {
//                        val key: String = l.tag.toString()
//                        prepare(key)
//                    }
//                }
//            }
//        }
//    }
//    private fun _idRegex(id: String): String? {
//        var res: String? = null
//        val  regex = "([^_]*)_(.*)".toRegex()
//        val matches = regex.find(id)
//        if (matches != null && matches.groupValues.size > 2) {
//            val groups = matches.groupValues
//            res = groups[2]
//        }
//        return res
//    }
//    private fun setTextField(field: EditText) {
//        val tag = field.tag
//        val newValue = field.text.toString()
//        //println(tag)
//        //println(field.text)
//
//        for ((key, value) in model.data) {
//            if (key == tag) {
//                val oldValue: Any = value["value"] as Any
//                val vtype: String = value["vtype"] as String
//                _fieldToData(oldValue, newValue, vtype, key)
//            }
//        }
//    }


//    private fun prepare(key: String) {
//
////        println(key)
//        hideKeyboard()
//        val intent = Intent(this@EditVC1, EditItemActivity::class.java)
//        intent.putExtra("key", key)
//        if (key == TEAM_WEEKDAYS_KEY) {
//            val value: MutableList<Int> = model.data[key]!!["value"] as MutableList<Int>
//            val days: ArrayList<Int> = arrayListOf()
//            if (value.size > 0) {
//                value.forEach {
//                    days.add(it)
//                }
//            }
//            intent.putIntegerArrayListExtra("weekdays", days)
//            intent.putExtra("select", "multi")
//        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
//            val row: MutableMap<String, Any> = model.data[key]!!["sender"] as MutableMap<String, Any>
//            var times: HashMap<String, Any> = hashMapOf()
//            times["type"] = if (key == TEAM_PLAY_START_KEY) SELECT_TIME_TYPE.play_start else SELECT_TIME_TYPE.play_end
//            var value = ""
//            if (row.isNotEmpty()) {
//                value = row["time"] as String
//                times["time"] = value
//            }
//            intent.putExtra("times", times)
//        } else if (key == TEAM_DEGREE_KEY) {
//            val degrees: ArrayList<DEGREE> = model.data[key]!!["sender"] as ArrayList<DEGREE>
//            intent.putExtra("degrees", degrees)
//        } else if (key == CITY_KEY || key == CITYS_KEY) {
//            val value: Int = model.data[key]!!["sender"] as Int
//            val city = City(value, "")
//            val citys: ArrayList<City> = arrayListOf(city)
//            intent.putParcelableArrayListExtra("citys", citys)
//        } else if (key == ARENA_KEY) {
//            val city_id: Int = model.data[CITY_KEY]!!["value"] as Int
//            if (city_id == 0) {
//                Alert.show(this, "警告", "請先選擇區域")
//            } else {
//                val tmp: MutableMap<String, Int> = model.data[key]!!["sender"] as MutableMap<String, Int>
//                if (tmp.containsKey("city_id")) {
//                    val city_id: Int = tmp["city_id"]!!
//                    val citysForArena: ArrayList<Int> = arrayListOf(city_id)
//                    intent.putIntegerArrayListExtra("citys_for_arena", citysForArena)
//                }
//                if (tmp.containsKey("arena_id")) {
//                    val arena_id: Int = tmp["arena_id"]!!
//                    val arenas: ArrayList<Arena> = arrayListOf(Arena(arena_id, ""))
//                    //intent.putParcelableArrayListExtra("arenas", arenas)
//                }
////                intent.putExtra("city_id", city_id)
////                intent.putExtra("arena_id", arena_id)
//            }
//        } else {
//            val value: String = model.data[key]!!["value"] as String
//            intent.putExtra("value", value)
//        }
//        fieldToData()
//        startActivityForResult(intent, SELECT_REQUEST_CODE)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        //println(data)
//        when (requestCode) {
//            //ACTION_PHOTO_REQUEST_CODE -> {
//                //println(data!!.data)
//                //dealPhoto(requestCode, resultCode, data)
//            //}
//            //ACTION_CAMERA_REQUEST_CODE -> {
//                //dealCamera(requestCode, resultCode, data)
//            //}
//            SELECT_REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    var key = ""
//                    if (data != null && data!!.hasExtra("key")) {
//                        key = data!!.getStringExtra("key")!!
//                    }
//                    if (key == TEAM_WEEKDAYS_KEY) {
//                        val days: ArrayList<Int> = data!!.getIntegerArrayListExtra("weekdays")!!
//                        updateDays(days)
//                    } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
//                        val times: HashMap<String, Any> = data!!.getSerializableExtra("times") as HashMap<String, Any>
//                        var time: String = ""
//                        if (times.containsKey("time")) {
//                            time = times.get("time") as String + ":00"
//                        }
//                        updateTime(key, time)
//                    } else if (key == TEAM_DEGREE_KEY) {
//                        val degrees: ArrayList<DEGREE> = data!!.getSerializableExtra("degrees") as ArrayList<DEGREE>
//                        updateDegree(degrees)
//                    } else if (key == CITY_KEY || key == CITYS_KEY) {
//                        val citys: ArrayList<City> = data!!.getSerializableExtra("citys") as ArrayList<City>
//                        updateCity(citys)
//                    } else if (key == ARENA_KEY) {
//                        val arenas:ArrayList<Arena> = data!!.getSerializableExtra("arenas") as ArrayList<Arena>
////                        val id: Int = data!!.getIntExtra("id", model.data[TEAM_ARENA_KEY]!!["value"] as Int)
////                        val name: String = data!!.getStringExtra("name")
//                        updateArena(arenas)
//                    } else {
//                        var content = ""
//                        if (data != null && data.hasExtra("res")) {
//                            content = data.getStringExtra("res")!!
//                            val type = model.contentKey2Type(key)
//                            updateText(key, content)
//                        }
//                    }
//                    if (key.length > 0) {
//                        model.data[key]!!["change"] = true
//                        notifyChanged(true)
////                    dataToField(inputV)
//                    }
//                }
//            }
//            else -> {
//                activity.toast("請重新選擇")
//            }
//        }
//    }
//
//    fun updateCity(citys: ArrayList<City>?=null) {
//        if (citys != null && citys.size > 0) {
//            val city = citys[0]
//            model.updateCity(city)
//        } else {
//            model.updateCity()
//        }
//    }
//
//    fun updateArena(arenas: ArrayList<Arena>?=null) {
//        if (arenas != null && arenas.size > 0) {
//            model.updateArena(arenas[0])
//        } else {
//            model.updateArena()
//        }
//    }
//
//    fun updateDays(days: ArrayList<Int>? = null) {
//        if (days != null && days.size > 0) {
//            model.updateWeekdays(days)
//        } else {
//            model.updateWeekdays()
//        }
//    }
//
//    fun updateTime(key: String, time: String?=null) {
//        model.updateTime(key, time)
//    }
//
//    fun updateDegree(degrees: ArrayList<DEGREE>?=null) {
//        if (degrees != null && degrees.size > 0) {
//            model.updateDegree(degrees)
//        } else {
//            model.updateDegree()
//        }
//    }
//
//    fun updateText(key: String, content: String?=null) {
//        model.updateText(key, content)
//    }
}

//class EditItem(val source: String, val indexPath: IndexPath, val row: HashMap<String, Any>, val addEditText:(String, Int)->Unit, val clearClick:(key:String)->Unit): Item() {
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
//
//        val editText = viewHolder.edit_text
//        val detailView = viewHolder.detail
//        val moreView = viewHolder.more
//        val onoff = viewHolder.onoff
//        val titleView = viewHolder.title
//        val clearView = viewHolder.clear
//
//        var key = ""
//        if (row.containsKey("key")) {
//            key = row.get("key")!! as String
//        }
//        var title = ""
//        if (row.containsKey("ch")) {
//            title = row.get("ch")!! as String
//            titleView.text = title
//        }
//        var aType = "none"
//        if (row.containsKey("atype")) {
//            aType = row.get("atype")!! as String
//            if (aType == "more") {
//                moreView.visibility = View.VISIBLE
//                detailView.visibility = View.VISIBLE
//                editText.visibility = View.INVISIBLE
//                onoff.visibility = View.INVISIBLE
//                if (row.containsKey("show")) {
//                    viewHolder.detail.text = row["show"]!! as String
//                }
//            } else {
//                moreView.visibility = View.INVISIBLE
//                detailView.visibility = View.INVISIBLE
//                onoff.visibility = View.INVISIBLE
//                editText.visibility = View.VISIBLE
//            }
//        }
//        if (row.containsKey("text_field")) {
//            val text_field = row.get("text_field")!! as Boolean
//            if (text_field) {
//                editText.visibility = View.VISIBLE
//                moreView.visibility = View.INVISIBLE
//                detailView.visibility = View.INVISIBLE
//                onoff.visibility = View.INVISIBLE
//                var inputType = defaultPad
//                if (row.containsKey("keyboardType")) {
//                    inputType = row["keyboardType"]!! as Int
//                    viewHolder.edit_text.inputType = inputType
//                }
//                if (row.containsKey("value")) {
//                    var value = row.get("value").toString()
//                    if (value == "-1") {
//                        value = ""
//                    }
//                    viewHolder.edit_text.setText(value)
//                }
////                println("$key => $position")
//                addEditText(key, position)
//            }
//        }
//
//        clearView.setOnClickListener {
//            editText.setText("")
//            if (aType == "more") {
//                clearClick(key)
//            }
//        }
//    }
//
//    override fun getLayout() = R.layout.edit_item
//
//}















