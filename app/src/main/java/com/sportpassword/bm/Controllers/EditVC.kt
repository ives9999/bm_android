package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import kotlinx.android.synthetic.main.edit_vc.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Models.Arena
import com.sportpassword.bm.Services.ArenaService
import com.sportpassword.bm.Services.CoachService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ImagePicker
import com.sportpassword.bm.member
import com.xwray.groupie.*
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.edit_item.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.toast
import java.io.File
import kotlinx.android.synthetic.main.mask.*

class EditVC : MyTableVC(), ImagePicker {

    override val ACTION_CAMERA_REQUEST_CODE = 100
    override val ACTION_PHOTO_REQUEST_CODE = 200
    val SELECT_REQUEST_CODE = 1
    override val activity = this
    override val context = this
    lateinit override var imagePickerLayer: AlertDialog
    lateinit override var alertView: View
    override var currentPhotoPath = ""
    override var filePath: String = ""
    override var file: File? = null
    lateinit override var imageView: ImageView

    var source: String = "team"
    var token: String = ""
    var title: String = ""

    private var originW: Int = 0
    private var originH: Int = 0
    private var originMarginTop = 0
    private var originMarginBottom = 0
    private lateinit var originScaleType: ImageView.ScaleType
    private lateinit var inputV: List<View>
    private var isFeaturedChange: Boolean = false

    var model: SuperData = Team(0, "", "", "")
    var action: String = "INSERT"
    val editTexts: HashMap<String, Int> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_vc)

        source = intent.getStringExtra("source")
        token = intent.getStringExtra("token")
        val tmp = intent.getStringExtra("title")

        if (source == "coach") {
            title = "新增教練"
            dataService = CoachService
            model = Coach(0, ", ", "")
        } else if (source == "team") {
            title = "新增球隊"
            dataService = TeamService
            model = Team(0, "", "", "")
        } else if (source == "arena") {
            title = "新增球館"
            dataService = ArenaService
            model = Arena(0, "")
        }

        if (tmp.length > 0) {
            title = tmp
        }
        setMyTitle(title)
        model.dataReset()
        //println(model.data)

        imageView = edit_featured
        getImageViewParams()
        initImagePicker(R.layout.image_picker_layer)
        edit_featured_container.onClick {
            showImagePickerLayer()
        }

        sections = model.sections
        recyclerView = edit_list
        initAdapter(true)

        //val allV = getAllChildrenBFS(edit_list)
        //inputV = filterInputField(allV)

        //textFieldDidEndEditing(inputV)

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.edit_refresh)
        //println(refreshLayout)
        setRefreshListener()
        refresh()
    }

    override fun generateItems(section: Int): ArrayList<Item> {

        var items: ArrayList<Item> = arrayListOf()
        val rows = model.rows[section]
        for ((idx, key) in rows.withIndex()) {
            val indexPath = IndexPath(section, idx)
            val dataRow = model.getDataRowWithKey(key)
            items.add(EditItem(source, indexPath, dataRow, { key, id ->
                editTexts[key] = id
            }, {
                clear(it)
            }))
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {

        val editItem = item as EditItem
        val dataRow = editItem.row
        if (dataRow.containsKey("key")) {
            val key = dataRow.get("key")!! as String
            if (dataRow.containsKey("atype")) {
                val aType = dataRow.get("atype")!! as String
                if (aType == "more") {
                    prepare(key)
                }
            }
        }
    }

    fun clear(key: String) {
        if (key == CITY_KEY) {
            updateCity()
        } else if (key == ARENA_KEY) {
            updateArena()
        } else if (key == TEAM_DAYS_KEY) {
            updateDays()
        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
            updateTime(key)
        } else if (key == TEAM_DEGREE_KEY) {
            updateDegree()
        } else if (key == TEAM_TEMP_CONTENT_KEY || key == CHARGE_KEY || key == CONTENT_KEY) {
            updateContent(key)
        }
        model.data[key]!!["change"] = true
        notifyChanged(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.button, menu)
        return true
    }
    override fun refresh() {
        if (token.length > 0) {
            Loading.show(mask)
            action = "UPDATE"
            dataService.getOne(this, source, "name", token) { success ->
                if (success) {
                    model.data = dataService.data
                    //setTeamData()
//                    println(model.data)
                    //dataToField(inputV)
                    notifyChanged(true)

                    //teamedit_name.setSelection(teamedit_name.length())
                    closeRefresh()
                }
                Loading.hide(mask)
            }
        }
    }

    override fun setImage(newFile: File?, url: String?) {
        featured_text.visibility = View.INVISIBLE
        val layoutParams = edit_featured.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.setMargins(0, 0, 0, 0)
        imageView.layoutParams = layoutParams
        isFeaturedChange = true
        super.setImage(newFile, url)
    }

    override fun removeImage() {
        featured_text.visibility = View.VISIBLE
        val layoutParams = edit_featured.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = originW
        layoutParams.height = originH
        layoutParams.setMargins(0, originMarginTop, 0, originMarginBottom)
        imageView.layoutParams = layoutParams
        imageView.scaleType = originScaleType
        isFeaturedChange = true
        super.removeImage()
        closeImagePickerLayer()
    }

    fun getImageViewParams() {
        val l = edit_featured.layoutParams as LinearLayout.LayoutParams
        originW = l.width
        originH = l.height
        originScaleType = edit_featured.scaleType
        //val m = edit_featured.
        originMarginTop = l.topMargin
        originMarginBottom = l.bottomMargin
    }

    fun submit(view: View) {

        hideKeyboard()
        var params: MutableMap<String, Any> = mutableMapOf()
        var isPass: Boolean = true
        fieldToData()

        val name: String = model.data[NAME_KEY]!!["value"] as String
        if (name.length == 0) {
            isPass = false
            Alert.show(context, "提示", "請填寫隊名")
        }
        val mobile: String = model.data[MOBILE_KEY]!!["value"] as String
        if (mobile.length == 0) {
            isPass = false
            Alert.show(context, "警告", "請填寫電話")
        }

        if (isPass) {
            params = model.makeSubmitArr()
            if (params.count() == 0 && !isFeaturedChange) {
                Alert.show(context, "提示", "沒有修改任何資料或圖片")
            } else {
                Loading.show(mask)
                if (params.count() == 0) {
                    if (model.data[ID_KEY]!!["value"] as Int > 0) {
                        val id: Int = model.data[ID_KEY]!!["value"] as Int
                        params[ID_KEY] = id
                    } else {
                        params[CREATED_ID_KEY] = member.id
                    }
                }
                val created_id = model.data[CREATED_ID_KEY]!!["value"] as Int
                if (created_id < 0) {
                    params[CREATED_ID_KEY] = member.id
                }

                dataService.update(context, source, params, filePath) { success ->
                    Loading.hide(mask)
                    if (success) {
                        if (dataService.success) {
                            val id: Int = TeamService.id
                            model.data[ID_KEY]!!["value"] = id
                            model.data[ID_KEY]!!["show"] = id
                            Alert.update(this, this.action, {
                                if (file != null) {
                                    file!!.delete()
                                }
                                val update = Intent(NOTIF_TEAM_UPDATE)
                                LocalBroadcastManager.getInstance(this).sendBroadcast(update)
                                finish()
                            })
                        } else {
                            Alert.show(context, "錯誤", TeamService.msg)
                        }
                    } else {
                        Alert.show(context, "錯誤", TeamService.msg)
                    }

                }

            }
        }

    }

    private fun fieldToData() {
        for ((key, idx) in editTexts) {
            if (model.data.containsKey(key)) {
                val it = edit_list.findViewHolderForAdapterPosition(idx) as com.xwray.groupie.kotlinandroidextensions.ViewHolder
                val newValue: String = it.edit_text.text.toString()
//                println("key: $key")
//                println("newValue: $newValue")
                val dataRow = model.data[key]!!
                //if (newValue.length > 0) {
                val oldValue: Any = dataRow["value"] as Any
                val vtype = dataRow["vtype"] as String
                _fieldToData(oldValue, newValue, vtype, key)
            }
        }
    }
    private fun _fieldToData(_oldValue: Any, _newValue: String, vtype: String, key: String) {
        var oldValue: Any = _oldValue
        if (vtype == "String") {
            oldValue = oldValue as String
            val newValue = _newValue
            if (oldValue != newValue) {
//                println("oldValue: $oldValue")
//                println("newValue: $newValue")
                model.data[key]!!["value"] = newValue
                model.data[key]!!["change"] = true
            }
        } else if (vtype == "Int") {
            oldValue = oldValue as Int
            var newValue: Int = -1
            if (_newValue.length > 0) {
                newValue = _newValue.toInt()
            }
            if (oldValue != newValue) {
                model.data[key]!!["value"] = newValue
                model.data[key]!!["change"] = true
            }
        } else if (vtype == "Bool") {
            oldValue = oldValue as Boolean
            var newValue: Boolean = true
            if (_newValue.length > 0) {
                newValue = _newValue.toBoolean()
            }
            if (oldValue != newValue) {
                model.data[key]!!["value"] = newValue
                model.data[key]!!["change"] = true
            }
        }
    }

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


    private fun prepare(key: String) {

//        println(key)
        hideKeyboard()
        val intent = Intent(this@EditVC, EditTeamItemActivity::class.java)
        intent.putExtra("key", key)
        if (key == TEAM_DAYS_KEY) {
            val value: MutableList<Int> = model.data[key]!!["value"] as MutableList<Int>
            val days: ArrayList<Int> = arrayListOf()
            if (value.size > 0) {
                value.forEach {
                    days.add(it)
                }
            }
            intent.putIntegerArrayListExtra("days", days)
        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
            val row: MutableMap<String, Any> = model.data[key]!!["sender"] as MutableMap<String, Any>
            var times: HashMap<String, Any> = hashMapOf()
            times["type"] = if (key == TEAM_PLAY_START_KEY) SELECT_TIME_TYPE.play_start else SELECT_TIME_TYPE.play_end
            var value = ""
            if (row.isNotEmpty()) {
                value = row["time"] as String
                times["time"] = value
            }
            intent.putExtra("times", times)
        } else if (key == TEAM_DEGREE_KEY) {
            val degrees: ArrayList<DEGREE> = model.data[key]!!["sender"] as ArrayList<DEGREE>
            intent.putExtra("degrees", degrees)
        } else if (key == CITY_KEY) {
            val value: Int = model.data[key]!!["sender"] as Int
            val city = City(value, "")
            val citys: ArrayList<City> = arrayListOf(city)
            intent.putParcelableArrayListExtra("citys", citys)
        } else if (key == ARENA_KEY) {
            val city_id: Int = model.data[CITY_KEY]!!["value"] as Int
            if (city_id == 0) {
                Alert.show(this, "警告", "請先選擇區域")
            } else {
                val tmp: MutableMap<String, Int> = model.data[key]!!["sender"] as MutableMap<String, Int>
                if (tmp.containsKey("city_id")) {
                    val city_id: Int = tmp["city_id"]!!
                    val citysForArena: ArrayList<Int> = arrayListOf(city_id)
                    intent.putIntegerArrayListExtra("citys_for_arena", citysForArena)
                }
                if (tmp.containsKey("arena_id")) {
                    val arena_id: Int = tmp["arena_id"]!!
                    val arenas: ArrayList<com.sportpassword.bm.Controllers.Arena> = arrayListOf(com.sportpassword.bm.Controllers.Arena(arena_id, ""))
                    intent.putParcelableArrayListExtra("arenas", arenas)
                }
//                intent.putExtra("city_id", city_id)
//                intent.putExtra("arena_id", arena_id)
            }
        } else {
            val value: String = model.data[key]!!["value"] as String
            intent.putExtra("value", value)
        }
        startActivityForResult(intent, SELECT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //println(data)
        when (requestCode) {
            ACTION_PHOTO_REQUEST_CODE -> {
                //println(data!!.data)
                dealPhoto(requestCode, resultCode, data)
            }
            ACTION_CAMERA_REQUEST_CODE -> {
                dealCamera(requestCode, resultCode, data)
            }
            SELECT_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val key = data!!.getStringExtra("key")
                    if (key == TEAM_DAYS_KEY) {
                        val days: ArrayList<Int> = data!!.getIntegerArrayListExtra("days")
                        updateDays(days)
                    } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
                        val times: HashMap<String, Any> = data!!.getSerializableExtra("times") as HashMap<String, Any>
                        var time: String = ""
                        if (times.containsKey("time")) {
                            time = times.get("time") as String + ":00"
                        }
                        updateTime(key, time)
                    } else if (key == TEAM_DEGREE_KEY) {
                        val degrees: ArrayList<DEGREE> = data!!.getSerializableExtra("degrees") as ArrayList<DEGREE>
                        updateDegree(degrees)
                    } else if (key == CITY_KEY) {
                        val citys: ArrayList<City> = data!!.getParcelableArrayListExtra("citys")
                        updateCity(citys)
                    } else if (key == ARENA_KEY) {
                        val arenas:ArrayList<com.sportpassword.bm.Controllers.Arena> = data!!.getParcelableArrayListExtra("arenas")
//                        val id: Int = data!!.getIntExtra("id", model.data[TEAM_ARENA_KEY]!!["value"] as Int)
//                        val name: String = data!!.getStringExtra("name")
                        updateArena(arenas)
                    } else {
                        val content: String = data!!.getStringExtra("res")
                        updateContent(key, content)
                    }
                    model.data[key]!!["change"] = true
                    notifyChanged(true)
//                    dataToField(inputV)
                }
            }
            else -> {
                activity.toast("請重新選擇")
            }
        }
    }

    fun updateCity(citys: ArrayList<City>?=null) {
        if (citys != null && citys.size > 0) {
            val city = citys[0]
            model.updateCity(city)
        } else {
            model.updateCity()
        }
    }

    fun updateArena(arenas: ArrayList<com.sportpassword.bm.Controllers.Arena>?=null) {
        if (arenas != null && arenas.size > 0) {
            val id = arenas[0].id
            val name = arenas[0].name
            val arena = Arena(id, name)
            model.updateArena(arena)
        } else {
            model.updateArena()
        }
    }

    fun updateDays(days: ArrayList<Int>? = null) {
        if (days != null && days.size > 0) {
            model.updateDays(days)
        } else {
            model.updateDays()
        }
    }

    fun updateTime(type: String, time: String?=null) {
        if (time != null) {
            when (type) {
                TEAM_PLAY_START_KEY -> model.updatePlayStartTime(time)
                TEAM_PLAY_END_KEY -> model.updatePlayEndTime(time)
            }
        } else {
            when (type) {
                TEAM_PLAY_START_KEY -> model.updatePlayStartTime()
                TEAM_PLAY_END_KEY -> model.updatePlayEndTime()
            }
        }
    }

    fun updateDegree(degrees: ArrayList<DEGREE>?=null) {
        if (degrees != null && degrees.size > 0) {
            model.updateDegree(degrees)
        } else {
            model.updateDegree()
        }
    }

    fun updateContent(type: String, content: String?=null) {
        if (content != null) {
            if (type == TEAM_TEMP_CONTENT_KEY) {
                model.updateTempContent(content)
            } else if (type == CHARGE_KEY) {
                model.updateCharge(content)
            } else if (type == CONTENT_KEY) {
                model.updateContent(content)
            }
        } else {
            if (type == TEAM_TEMP_CONTENT_KEY) {
                model.updateTempContent()
            } else if (type == CHARGE_KEY) {
                model.updateCharge()
            } else if (type == CONTENT_KEY) {
                model.updateContent()
            }
        }
    }
}

class EditItem(val source: String, val indexPath: IndexPath, val row: HashMap<String, Any>, val addEditText:(String, Int)->Unit, val clearClick:(key:String)->Unit): Item() {
    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

        var key = ""
        if (row.containsKey("key")) {
            key = row.get("key")!! as String
        }
        var title = ""
        if (row.containsKey("ch")) {
            title = row.get("ch")!! as String
            viewHolder.title.text = title
        }
        var aType = "none"
        if (row.containsKey("atype")) {
            aType = row.get("atype")!! as String
            if (aType == "more") {
                viewHolder.more.visibility = View.VISIBLE
                viewHolder.detail.visibility = View.VISIBLE
                if (row.containsKey("show")) {
                    viewHolder.detail.text = row["show"]!! as String
                }
            } else {
                viewHolder.more.visibility = View.INVISIBLE
                viewHolder.detail.visibility = View.INVISIBLE
            }
        }
        if (row.containsKey("text_field")) {
            val text_field = row.get("text_field")!! as Boolean
            if (text_field) {
                viewHolder.edit_text.visibility = View.VISIBLE
                var inputType = defaultPad
                if (row.containsKey("keyboardType")) {
                    inputType = row["keyboardType"]!! as Int
                    viewHolder.edit_text.inputType = inputType
                }
                if (row.containsKey("value")) {
                    val value = row.get("value").toString()
                    viewHolder.edit_text.setText(value)
                }
//                println("$key => $position")
                addEditText(key, position)
            }
        }

        viewHolder.clear.setOnClickListener {
            viewHolder.edit_text.setText("")
            if (aType == "more") {
                clearClick(key)
            }
        }
    }

    override fun getLayout() = R.layout.edit_item

}















