package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.internal.Mutable
import com.sportpassword.bm.Adapters.Form.*
import com.sportpassword.bm.Form.CourseForm
import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ImagePicker
import com.sportpassword.bm.member
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_edit_course_vc.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.File
import kotlin.reflect.full.declaredMemberProperties

class EditCourseVC : MyTableVC(), ImagePicker, ViewDelegate {

    override val ACTION_CAMERA_REQUEST_CODE = 100
    override val ACTION_PHOTO_REQUEST_CODE = 200
    override val activity = this
    override val context = this
    override var currentPhotoPath = ""
    override var filePath: String = ""
    override var file: File? = null
    var params1: MutableMap<String, String> = mutableMapOf<String, String>()

    lateinit override var imagePickerLayer: AlertDialog
    lateinit override var alertView: View
    lateinit override var imageView: ImageView
    lateinit var superCourse: SuperCourse

    var title: String = ""
    var course_token: String? = null
    var coach_token: String? = null

    //Form
    var form: CourseForm = CourseForm()

    private var originW: Int = 0
    private var originH: Int = 0
    private var originMarginTop = 0
    private var originMarginBottom = 0
    private lateinit var originScaleType: ImageView.ScaleType
    private var isFeaturedChange: Boolean = false

    val SELECT_REQUEST_CODE = 1

//    val section_keys: ArrayList<ArrayList<String>> = arrayListOf(
//            arrayListOf(TITLE_KEY, YOUTUBE_KEY),
//            arrayListOf(PRICE_KEY, PRICE_UNIT_KEY)
//    )
    var section_keys: ArrayList<ArrayList<String>> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_course_vc)

        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")
        }
        if (intent.hasExtra("course_token")) {
            course_token = intent.getStringExtra("course_token")
        }
        if (intent.hasExtra("coach_token")) {
            coach_token = intent.getStringExtra("coach_token")
        }

        if (coach_token == null) {
            warningWithPrev("沒有傳送教練代碼，無法編輯課程，請洽管理員")
        }

        setMyTitle(title)

        imageView = edit_featured
        getImageViewParams()
        initImagePicker(R.layout.image_picker_layer)
        edit_featured_container.onClick {
            showImagePickerLayer()
        }

        sections = form.getSections()
        section_keys = form.getSectionKeys()
//        println(sections)
//        println(section_keys)

        recyclerView = editTableView
        initAdapter(true)

        refreshLayout = refresh
        setRefreshListener()

        if (course_token != null && course_token!!.length > 0) {
            refresh()
        }
    }

    override fun refresh() {
        Loading.show(mask)
        CourseService.getOne(this, course_token) { success ->
            if (success) {
                superCourse = CourseService.superCourse
                putValue()
                notifyChanged(true)

                //teamedit_name.setSelection(teamedit_name.length())
                closeRefresh()
            }
            Loading.hide(mask)
        }
    }

    private fun putValue() {
        if (superCourse != null) {
            val kc = superCourse::class
            for (formItem in form.formItems) {
                val name = formItem.name!!
                kc.declaredMemberProperties.forEach {
                    if (it.name == formItem.name) {
                        val type = JSONParse.getType(it)
                        when (type) {
                            "String" -> {
                                val value = JSONParse.getValue<String>(name, superCourse, it)
                                if (value != null) {
                                    formItem.value = value
                                }
                            }
                            "Int" -> {
                                val value = JSONParse.getValue<Int>(name, superCourse, it)
                                if (value != null) {
                                    formItem.value = value.toString()
                                }

                            }
                        }
                        formItem.make()
                    }
                }
            }
            if (superCourse.featured_path.count() > 0) {
                val featured: String = BASE_URL + superCourse.featured_path
//                        println(featured)
                setImage(null, featured)
            }
        }
    }

    override fun generateItems(section: Int): ArrayList<Item> {

        val rows: ArrayList<Item> = arrayListOf()

        val clearClick = { i: Int ->
        }

        val promptClick = {i: Int ->
            val forItem = form.formItems[i]
            if (forItem.tooltip != null) {
                Alert.show(this, "提示", forItem.tooltip!!)
            }
        }

        val rowClick = { i: Int ->
            prepare(i)
        }

        val arr: ArrayList<FormItem> = arrayListOf()
        for (key in section_keys[section]) {
            for (formItem in form.formItems) {
                if (key == formItem.name) {
                    arr.add(formItem)
                    break
                }
            }
        }

//        println(arr)

//        var idx: Int = 0
//        for (i in 0..(section-1)) {
//            idx += section_keys[i].size
//        }

        for ((i,formItem) in arr.withIndex()) {

            val indexPath: IndexPath = IndexPath(section, i)
            var idx: Int = 0
            for ((j, _forItem) in form.formItems.withIndex()) {
                if (formItem.name == _forItem.name) {
                    idx = j
                    break
                }
            }


            var formItemAdapter: FormItemAdapter? = null
            if (formItem.uiProperties.cellType == FormItemCellType.textField) {
                formItemAdapter = TextFieldAdapter(form, idx, indexPath, clearClick, promptClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.content) {
                formItemAdapter = ContentAdapter(form, idx, indexPath, clearClick, promptClick, rowClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.more) {
                formItemAdapter = MoreAdapter(form, idx, indexPath, clearClick, promptClick, rowClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.section) {
                break
            } else if (formItem.uiProperties.cellType == FormItemCellType.weekday) {
                formItemAdapter = MoreAdapter(form, idx, indexPath, clearClick, promptClick, rowClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.time) {
                formItemAdapter = MoreAdapter(form, idx, indexPath, clearClick, promptClick, rowClick)
            }

            if (formItemAdapter != null) {
                formItemAdapter!!.delegate = this
                rows.add(formItemAdapter!!)
            }
//            idx++
        }

        return rows
    }

    fun prepare(idx: Int) {

        val forItem = form.formItems[idx]
        val key = forItem.name

        val singleSelectIntent = Intent(this@EditCourseVC, SingleSelectVC::class.java)
        singleSelectIntent.putExtra("title", forItem.title)
        singleSelectIntent.putExtra("key", key)

        val multiSelectIntent = Intent(this@EditCourseVC, MultiSelectVC::class.java)
        multiSelectIntent.putExtra("title", forItem.title)
        multiSelectIntent.putExtra("key", key)

        val contentIntent = Intent(this@EditCourseVC, ContentEditVC::class.java)
        contentIntent.putExtra("title", forItem.title)
        contentIntent.putExtra("key", key)

        if (key == PRICE_UNIT_KEY) {
            val rows = PRICE_UNIT.makeSelect()
            singleSelectIntent.putExtra("rows", rows)
            if (forItem.sender != null) {
                val selected = forItem.sender as String
                singleSelectIntent.putExtra("selected", selected)
            }
            startActivityForResult(singleSelectIntent, SELECT_REQUEST_CODE)
        } else if (key == COURSE_KIND_KEY) {
            val rows = COURSE_KIND.makeSelect()
            singleSelectIntent.putExtra("rows", rows)
            if (forItem.sender != null) {
                val selected = forItem.sender as String
                singleSelectIntent.putExtra("selected", selected)
            }
            startActivityForResult(singleSelectIntent, SELECT_REQUEST_CODE)
        } else if (key == CYCLE_UNIT_KEY) {
            val rows = CYCLE_UNIT.makeSelect()
            singleSelectIntent.putExtra("rows", rows)
            if (forItem.sender != null) {
                val selected = forItem.sender as String
                singleSelectIntent.putExtra("selected", selected)
            }
            startActivityForResult(singleSelectIntent, SELECT_REQUEST_CODE)
        } else if (key == WEEKDAY_KEY) {
            val rows = WEEKDAY.makeSelect()
//            println(rows)
            multiSelectIntent.putExtra("rows", rows)
            if (forItem.sender != null) {
                val selecteds = forItem.sender as ArrayList<String>
//                println(selecteds)
                multiSelectIntent.putExtra("selecteds", selecteds)
            }
            startActivityForResult(multiSelectIntent, SELECT_REQUEST_CODE)
        } else if (key == START_TIME_KEY || key == END_TIME_KEY) {
            val times = Global.makeTimes()
            val rows: ArrayList<HashMap<String, String>> = arrayListOf()
            for (time in times) {
                rows.add(hashMapOf("title" to time, "value" to time+":00"))
            }
//            println(rows)
            singleSelectIntent.putExtra("rows", rows)
            if (forItem.sender != null) {
                val tmp = forItem.sender as HashMap<String, String>
                val selected = tmp.get("time")!!
                singleSelectIntent.putExtra("selected", selected)
            }
            startActivityForResult(singleSelectIntent, SELECT_REQUEST_CODE)
        } else if (key == CONTENT_KEY) {
            if (forItem.sender != null) {
                val content = forItem.sender as String
//                println(selecteds)
                contentIntent.putExtra("content", content)
            }
            startActivityForResult(contentIntent, SELECT_REQUEST_CODE)
        }

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
                if (data != null) {
                    var key: String? = null
                    if (data!!.hasExtra("key")) {
                        key = data!!.getStringExtra("key")
                    }
                    var selected: String? = null
                    if (data!!.hasExtra("selected")) {
                        selected = data!!.getStringExtra("selected")
                    }
//                println(selected)

                    var selecteds: ArrayList<String>? = null
                    if (data!!.hasExtra("selecteds")) {
                        selecteds = data!!.getStringArrayListExtra("selecteds")
                    }

                    var content: String? = null
                    if (data!!.hasExtra("content")) {
                        content = data!!.getStringExtra("content")
                    }

                    var item: FormItem? = null
                    if (key == PRICE_UNIT_KEY) {
                        item = getFormItemFromKey(key) as PriceUnitFormItem
                    } else if (key == COURSE_KIND_KEY) {
                        item = getFormItemFromKey(key) as CourseKindFormItem
                    } else if (key == CYCLE_UNIT_KEY) {
                        item = getFormItemFromKey(key) as CycleUnitFormItem
                    } else if (key == WEEKDAY_KEY) {
                        item = getFormItemFromKey(key) as WeekdayFormItem
                    } else if (key == START_TIME_KEY || key == END_TIME_KEY) {
                        item = getFormItemFromKey(key) as TimeFormItem
                    } else if (key == CONTENT_KEY) {
                        item = getFormItemFromKey(key) as ContentFormItem
                    }

                    if (item != null && selected != null) {
                        item.value = selected
                        item.make()
                    }
                    if (item != null && selecteds != null) {
                        val value = selecteds.joinToString(",")
                        item.value = value
                        item.make()
                    }
                    if (item != null && content != null) {
                        item.value = content
                        item.make()
                    }

                    notifyChanged(true)
                }
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

    override fun textFieldTextChanged(indexPath: IndexPath, text: String) {
        //println(row)
        //println(text)
        val item = form.formItems[indexPath.row]
        item.value = text
        item.make()
    }

    private fun getFormItemFromKey(key: String): FormItem? {

        var res: FormItem? = null
        for (formItem in form.formItems) {
            if (formItem.name == key) {
                res = formItem
                break
            }
        }

        return res
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

        Loading.show(mask)
        hideKeyboard()
        params1.clear()
        for (formItem in form.formItems) {
            if (formItem.name != null && formItem.value != null) {
                var value = formItem.value!!
                if (value.contains("\"")) {
                    value = value.replace("\"", "\\\"")
//                    println(value)
                }
                params1.set(formItem.name!!, value!!)
            }
        }
        var action = "UPDATE"
        if (course_token != null && course_token!!.length == 0) {
            action = "INSERT"
        }
        if (action == "INSERT") {
            params1[CREATED_ID_KEY] = member.id.toString()
        }
        if (course_token != null) {
            params1["course_token"] = course_token!!
        }
        if (coach_token != null) {
            params1["coach_token"] = coach_token!!
        }
//        println(params)
//        println(filePath)

        CourseService.update(this, params1, filePath) { success ->
            Loading.hide(mask)
            if (success) {
                if (CourseService.success) {
                    Alert.update(this, action, {
                        if (file != null) {
                            file!!.delete()
                        }
                        val update = Intent(NOTIF_COURSE_UPDATE)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(update)
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    })
                } else {
                    Alert.show(context, "錯誤", CourseService.msg)
                }
            } else {
                Alert.show(context, "錯誤", CourseService.msg)
            }
        }

    }

    fun cancel(view: View) {
        prev()
    }
}
