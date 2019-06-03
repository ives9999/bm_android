package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.sportpassword.bm.Adapters.Form.*
import com.sportpassword.bm.Form.CourseForm
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Form.FormItem.PriceCycleUnitFormItem
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ImagePicker
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_edit_course_vc.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.File
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class EditCourseVC : MyTableVC(), ImagePicker, ViewDelegate {

    override val ACTION_CAMERA_REQUEST_CODE = 100
    override val ACTION_PHOTO_REQUEST_CODE = 200
    override val activity = this
    override val context = this
    override var currentPhotoPath = ""
    override var filePath: String = ""
    override var file: File? = null

    lateinit override var imagePickerLayer: AlertDialog
    lateinit override var alertView: View
    lateinit override var imageView: ImageView
    lateinit var superCourse: SuperCourse

    var title: String = ""
    var token: String? = null

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
//            arrayListOf(PRICE_KEY, PRICE_CYCLE_UNIT_KEY)
//    )
    var section_keys: ArrayList<ArrayList<String>> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_course_vc)

        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")
        }
        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")
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

        if (token != null) {
            refresh()
        }
    }

    override fun refresh() {
        Loading.show(mask)
        CourseService.getOne(this, token) { success ->
            if (success) {
                superCourse = CourseService.superCourse
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
                notifyChanged(true)

                //teamedit_name.setSelection(teamedit_name.length())
                closeRefresh()
            }
            Loading.hide(mask)
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
                formItemAdapter = ContentAdapter(form, idx, indexPath, clearClick, promptClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.more) {
                formItemAdapter = MoreAdapter(form, idx, indexPath, clearClick, promptClick, rowClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.section) {
                break
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

        val singleSelectintent = Intent(this@EditCourseVC, SingleSelectVC::class.java)
        singleSelectintent.putExtra("title", forItem.title)
        singleSelectintent.putExtra("key", key)

        if (key == PRICE_CYCLE_UNIT_KEY) {
            val rows = PRICE_CYCLE_UNIT.makeSelect()
            singleSelectintent.putExtra("rows", rows)
            startActivityForResult(singleSelectintent, SELECT_REQUEST_CODE)
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

                    var item: FormItem? = null
                    if (key == PRICE_CYCLE_UNIT_KEY) {
                        item = getFormItemFromKey(key) as PriceCycleUnitFormItem
                    }
                    if (item != null && selected != null) {
                        item.value = selected
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
}
