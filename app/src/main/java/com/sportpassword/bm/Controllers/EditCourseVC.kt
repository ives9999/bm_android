package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.JsonParseException
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.mask.*

class EditCourseVC : EditVC() {

//    override val ACTION_PHOTO_REQUEST_CODE = 200
//    override val activity = this
//    override val context = this
//    override var currentPhotoPath = ""
//    override var filePath: String = ""
//    override var file: File? = null
//    override lateinit var imagePickerLayer: AlertDialog
//    override lateinit var alertView: View
//    override lateinit var imageView: ImageView

//    var params1: MutableMap<String, String> = mutableMapOf<String, String>()


    var coach_token: String? = null

//    var table: CourseTable? = null
    var myTable: CourseTable? = null

    //    val section_keys: ArrayList<ArrayList<String>> = arrayListOf(
//            arrayListOf(TITLE_KEY, YOUTUBE_KEY),
//            arrayListOf(PRICE_KEY, PRICE_UNIT_KEY)
//    )
//    var section_keys: ArrayList<ArrayList<String>> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {

        able_type = "course"
        dataService = CourseService

        if (intent.hasExtra("coach_token")) {
            coach_token = intent.getStringExtra("coach_token")!!
        }

        if (coach_token == null) {
            warningWithPrev("沒有傳送教練代碼，無法編輯課程，請洽管理員")
        }

        setContentView(R.layout.activity_edit_course_vc)
        super.onCreate(savedInstanceState)

        //form = CourseForm()


//        imageView = edit_featured
//        getImageViewParams()
//        initImagePicker(R.layout.image_picker_layer)
//        edit_featured_container.onClick {
//            showImagePickerLayer()
//        }

//        sections = form.getSections()
//        section_keys = form.getSectionKeys()
//        println(sections)
//        println(section_keys)

//        recyclerView = editTableView
        //initAdapter(true)

//        refreshLayout = refresh
//        setRefreshListener()

    }

    override fun initData() {

        if (myTable != null) {
            val rows: ArrayList<OneRow> = arrayListOf()
            var row = OneRow(
                "標題",
                myTable!!.title,
                myTable!!.title,
                TITLE_KEY,
                "textField",
                KEYBOARD.default,
                "兒童訓練班",
                "",
                true
            )
            row.msg = "課程標題沒有填寫"
            rows.add(row)
            row = OneRow(
                "youtube代碼",
                myTable!!.youtube,
                myTable!!.youtube,
                YOUTUBE_KEY,
                "textField",
                KEYBOARD.default,
                "請輸入youtube影片代碼"
            )
            rows.add(row)
            var section = makeSectionRow("一般", "general", rows, true)
            oneSections.add(section)

            rows.clear()
            row = OneRow(
                "收費標準",
                myTable!!.price.toString(),
                myTable!!.price.toString(),
                PRICE_KEY,
                "textField",
                KEYBOARD.numberPad,
                "請輸入收費費用"
            )
            rows.add(row)
            row = OneRow(
                "收費週期",
                myTable!!.price_unit.toString(),
                myTable!!.price_unit_show,
                PRICE_UNIT_KEY,
                "more"
            )
            rows.add(row)
            row = OneRow(
                "補充說明",
                myTable!!.price_short_show,
                myTable!!.price_short_show,
                PRICE_DESC_KEY,
                "textField",
                KEYBOARD.default,
                "收費標準的補充說明"
            )
            rows.add(row)
            section = makeSectionRow("收費", "charge", rows, true)
            oneSections.add(section)

            rows.clear()
            row = OneRow(
                "課程週期",
                myTable!!.kind,
                myTable!!.kind_show,
                COURSE_KIND_KEY,
                "more"
            )
            rows.add(row)
            row = OneRow(
                "週期",
                myTable!!.cycle_unit,
                myTable!!.cycle_unit_show,
                CYCLE_UNIT_KEY,
                "more"
            )
            rows.add(row)
            row = OneRow(
                "星期幾",
                myTable!!.weekday.toString(),
                myTable!!.weekdays_show,
                WEEKDAY_KEY,
                "more"
            )
            rows.add(row)
            row = OneRow(
                "開始時間",
                myTable!!.start_time,
                myTable!!.start_time_show,
                START_TIME_KEY,
                "more"
            )
            rows.add(row)
            row = OneRow(
                "結束時間",
                myTable!!.end_time,
                myTable!!.end_time_show,
                END_TIME_KEY,
                "more"
            )
            rows.add(row)
            row = OneRow(
                "開始日期",
                myTable!!.start_date,
                myTable!!.start_date,
                START_DATE_KEY,
                "more"
            )
            rows.add(row)
            row = OneRow(
                "結束日期",
                myTable!!.end_date,
                myTable!!.end_date,
                END_DATE_KEY,
                "more"
            )
            rows.add(row)
            row = OneRow(
                "人數限制",
                myTable!!.people_limit.toString(),
                myTable!!.people_limit_show,
                PEOPLE_LIMIT_KEY,
                "textField"
            )
            rows.add(row)
            row = OneRow(
                "詳細介紹",
                myTable!!.content,
                myTable!!.content,
                CONTENT_KEY,
                "textField"
            )
            rows.add(row)

            section = makeSectionRow("課程", "charge", rows, true)
            oneSections.add(section)
        }
    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            myTable = jsonToModel<CourseTable>(dataService.jsonString)
            if (myTable != null) {
                title = myTable!!.name
            }
        } catch (e: JsonParseException) {
            warning(e.localizedMessage)
            //println(e.localizedMessage)
        }
        if (myTable != null) {
            myTable!!.filterRow()
            table = myTable
        } else {
            warning("解析伺服器所傳的字串失敗，請洽管理員")
        }
    }

//    private fun putValue() {
//        if (myTable != null) {
//            val kc = myTable!!::class
//            for (formItem in form.formItems) {
//                val name = formItem.name!!
//                kc.memberProperties.forEach {
//                    if (it.name == formItem.name) {
//
//                        var value = it.getter.call(myTable).toString()
//                        if (value == "null") { value = "" }
//                        formItem.value = value
//
//                        formItem.make()
//                    }
//                }
//            }
//            if (myTable!!.featured_path.count() > 0) {
//                val featured: String = myTable!!.featured_path
////                        println(featured)
//                setImage(null, featured)
//            }
//        }
//    }

//    override fun generateItems(section: Int): ArrayList<Item> {
//
//        val rows: ArrayList<Item> = arrayListOf()
//
//        val clearClick = { formItem: FormItem ->
//            formItem.reset()
//
//        }
//
//        val promptClick = {formItem: FormItem ->
//            if (formItem.tooltip != null) {
//                Alert.show(this, "提示", formItem.tooltip!!)
//            }
//        }
//
//        val rowClick = { formItem: FormItem ->
//            prepare(formItem)
//        }
//
//        val arr: ArrayList<FormItem> = arrayListOf()
//        for (key in section_keys[section]) {
//            for (formItem in form.formItems) {
//                if (key == formItem.name) {
//                    arr.add(formItem)
//                    break
//                }
//            }
//        }
//
////        println(arr)
//
////        var idx: Int = 0
////        for (i in 0..(section-1)) {
////            idx += section_keys[i].size
////        }
//
//        for ((i,formItem) in arr.withIndex()) {
//
//            val indexPath: IndexPath = IndexPath(section, i)
//            var idx: Int = 0
//            for ((j, _formItem) in form.formItems.withIndex()) {
//                if (formItem.name == _formItem.name) {
//                    idx = j
//                    break
//                }
//            }
//
//
//            var formItemAdapter: FormItemAdapter? = null
//            if (formItem.uiProperties.cellType == FormItemCellType.textField) {
//                formItemAdapter = TextFieldAdapter(formItem, clearClick, promptClick)
//            } else if (formItem.uiProperties.cellType == FormItemCellType.content) {
//                formItemAdapter = ContentAdapter(formItem, clearClick, rowClick)
//            } else if (formItem.uiProperties.cellType == FormItemCellType.more) {
//                formItemAdapter = MoreAdapter(formItem, clearClick, rowClick)
//            } else if (formItem.uiProperties.cellType == FormItemCellType.section) {
//                break
//            } else if (formItem.uiProperties.cellType == FormItemCellType.weekday) {
//                formItemAdapter = MoreAdapter(formItem, clearClick, rowClick)
//            } else if (formItem.uiProperties.cellType == FormItemCellType.time) {
//                formItemAdapter = MoreAdapter(formItem, clearClick, rowClick)
//            } else if (formItem.uiProperties.cellType == FormItemCellType.date) {
//                formItemAdapter = MoreAdapter(formItem, clearClick, rowClick)
//            }
//
//            if (formItemAdapter != null) {
//                formItemAdapter.valueChangedDelegate = this
//                rows.add(formItemAdapter)
//            }
////            idx++
//        }
//
//        return rows
//    }

//    fun prepare(formItem: FormItem) {
//
//        val key = formItem.name
//
//        val dateSelectIntent = Intent(this, DateSelectVC::class.java)
//        dateSelectIntent.putExtra("title", formItem.title)
//        dateSelectIntent.putExtra("key", key)
//
//        if (key == PRICE_UNIT_KEY) {
//
//            val selected = formItem.sender as String
//            toSelectSingle(SelectPriceUnitVC::class.java, key, selected, this, able_type)
//
//        } else if (key == COURSE_KIND_KEY) {
//
//            val selected = formItem.sender as String
//            toSelectSingle(SelectCourseKindVC::class.java, key, selected, this, able_type)
//
//        } else if (key == CYCLE_UNIT_KEY) {
//
//            val selected = formItem.sender as String
//            toSelectSingle(SelectCycleUnitVC::class.java, key, selected, this, able_type)
//
//        } else if (key == WEEKDAY_KEY) {
//
//            val tmp = formItem.sender as ArrayList<String>
//            val selecteds: String = tmp.joinToString(",")
//            toSelectWeekdays(selecteds, this, able_type)
//
//        } else if (key == START_TIME_KEY || key == END_TIME_KEY) {
//
//            val tmp = formItem.sender as HashMap<String, String>
//            val selected = tmp.get("time")!!
//            toSelectSingle(SelectTimeVC::class.java, key, selected, this, able_type)
//
//        } else if (key == CONTENT_KEY) {
//
//            val content = formItem.sender as String
//            toEditContent(key, "詳細介紹", content, this)
//
//        } else if (key == START_DATE_KEY || key == END_DATE_KEY) {
//
//            val tmp = formItem.sender as HashMap<String, String>
//            val selected = tmp.get("date")!!
//            toSelectDate(key, selected, this, able_type)
//        }
//
//    }

//    override fun singleSelected(key: String, selected: String) {

//        val row = getDefinedRow(key)
//        var show = ""
//        var item: FormItem? = null
//
//        if (key == CITY_KEY || key == AREA_KEY) {
//            row["value"] = selected
//            show = Global.zoneIDToName(selected.toInt())
//        } else if (key == PRICE_UNIT_KEY) {
//            item = getFormItemFromKey(PriceUnitFormItem::class.java, key)
//        } else if (key == START_TIME_KEY || key == END_TIME_KEY) {
//            item = getFormItemFromKey(TimeFormItem::class.java, key)
//        } else if (key == COURSE_KIND_KEY) {
//            item = getFormItemFromKey(CourseKindFormItem::class.java, key)
//        } else if (key == CYCLE_UNIT_KEY) {
//            item = getFormItemFromKey(CycleUnitFormItem::class.java, key)
//        }
//        if (item != null) {
//            item.value = selected
//            item.make()
//        }

        //notifyChanged(true)
//    }

//    override fun contentEdit(key: String, content: String) {

//        val item: ContentFormItem = getFormItemFromKey(key) as ContentFormItem
//        item.value = content
//        item.make()

        //notifyChanged(true)
//    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        //println(data)
//        when (requestCode) {
//            ACTION_PHOTO_REQUEST_CODE -> {
//                //println(data!!.data)
//                dealPhoto(requestCode, resultCode, data)
//            }
//            ACTION_CAMERA_REQUEST_CODE -> {
//                dealCamera(requestCode, resultCode, data)
//            }
//        }
//    }

//    override fun setImage(newFile: File?, url: String?) {
//        featured_text.visibility = View.INVISIBLE
//        val layoutParams = edit_featured.layoutParams as LinearLayout.LayoutParams
//        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
//        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
//        layoutParams.setMargins(0, 0, 0, 0)
//        imageView.layoutParams = layoutParams
//        isFeaturedChange = true
//        super<MyTableVC>.setImage(newFile, url)
//    }
//
//    override fun removeImage() {
//        featured_text.visibility = View.VISIBLE
//        val layoutParams = edit_featured.layoutParams as LinearLayout.LayoutParams
//        layoutParams.width = originW
//        layoutParams.height = originH
//        layoutParams.setMargins(0, originMarginTop, 0, originMarginBottom)
//        imageView.layoutParams = layoutParams
//        imageView.scaleType = originScaleType
//        isFeaturedChange = true
//        super<MyTableVC>.removeImage()
//        closeImagePickerLayer()
//    }

//    override fun textFieldTextChanged(formItem: FormItem, text: String) {
//        //println(row)
//        //println(text)
//        if (formItem != null) {
//            formItem.value = text
//            formItem.make()
//        }
//    }
//
//    override fun sexChanged(sex: String) {}
//
//    override fun privateChanged(checked: Boolean) {}
//
//    override fun tagChecked(checked: Boolean, name: String, key: String, value: String) {}
//
//    override fun stepperValueChanged(number: Int, name: String) {}

//    private fun getImageViewParams() {
//        val l = edit_featured.layoutParams as LinearLayout.LayoutParams
//        originW = l.width
//        originH = l.height
//        originScaleType = edit_featured.scaleType
//        //val m = edit_featured.
//        originMarginTop = l.topMargin
//        originMarginBottom = l.bottomMargin
//    }

    fun submitBtnPressed(view: View) {

        Loading.show(mask)
        hideKeyboard()

        var msg: String = ""
        for (section in oneSections) {
            for (row in section.items) {
                if (row.isRequired && row.value.isEmpty()) {
                    msg += row.msg + "\n"
                }
            }
        }

        if (msg.length > 0) {
            warning(msg)
        } else {
            Loading.show(mask)
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
                params["course_token"] = token
            }
            if (coach_token != null) {
                params["coach_token"] = coach_token!!
            }

            params["device"] = "app"
            params["do"] = "update"

//            println(params)

            CourseService.update(this, params, filePath) { success ->
                Loading.hide(mask)
                if (success) {
                    if (CourseService.success) {
                        runOnUiThread {
                            Alert.update(this, action) {
                                if (file != null) {
                                    file!!.delete()
                                }
                                val update = Intent(NOTIF_COURSE_UPDATE)
                                LocalBroadcastManager.getInstance(this).sendBroadcast(update)
                                val intent = Intent()
                                intent.putExtra("manager_token", member.token)
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            }
                        }
                    } else {
                        runOnUiThread {
                            Alert.show(context, "錯誤", CourseService.msg)
                        }
                    }
                } else {
                    runOnUiThread {
                        Alert.show(context, "錯誤", CourseService.msg)
                    }
                }
            }
        }
//        params1.clear()
//        for (formItem in form.formItems) {
//            if (formItem.name != null && formItem.value != null) {
//                var value = formItem.value!!
//                if (value.contains("\"")) {
//                    value = value.replace("\"", "\\\"")
////                    println(value)
//                }
//                params1.set(formItem.name!!, value!!)
//            }
//        }
//        var action = "UPDATE"
//        if (course_token != null && course_token!!.length == 0) {
//            action = "INSERT"
//        }
//        if (action == "INSERT") {
//            params1[CREATED_ID_KEY] = member.id.toString()
//        }
//        if (course_token != null) {
//            params1["course_token"] = course_token!!
//        }
//        if (coach_token != null) {
//            params1["coach_token"] = coach_token!!
//        }
//        println(params1)
//        println(filePath)
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}
