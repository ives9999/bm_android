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
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ImagePicker
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_edit_course_vc.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.File

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

    var title: String = ""
    var token: String = ""

    //Form
    var form: CourseForm = CourseForm()

    private var originW: Int = 0
    private var originH: Int = 0
    private var originMarginTop = 0
    private var originMarginBottom = 0
    private lateinit var originScaleType: ImageView.ScaleType
    private var isFeaturedChange: Boolean = false

    val SELECT_REQUEST_CODE = 1

    val section_keys: ArrayList<ArrayList<String>> = arrayListOf(
            arrayListOf(TITLE_KEY, YOUTUBE_KEY),
            arrayListOf(PRICE_KEY, PRICE_CYCLE_UNIT_KEY)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_course_vc)

        title = intent.getStringExtra("title")
        token = intent.getStringExtra("token")

        setMyTitle(title)

        imageView = edit_featured
        getImageViewParams()
        initImagePicker(R.layout.image_picker_layer)
        edit_featured_container.onClick {
            showImagePickerLayer()
        }

        sections = arrayListOf("一般","收費")

        recyclerView = editTableView
        initAdapter(true)

        //val rows = generateFormItems()
    }

    override fun generateItems(section: Int): ArrayList<Item> {

        val rows: ArrayList<Item> = arrayListOf()
        val section: Int = 0
//        var indexPath: HashMap<String, Int> = hashMapOf()
//        indexPath["section"] = section

        val clearClick = { i: Int ->
            val forItem = form.formItems[i]
            forItem.reset()
            val rows = generateItems()
            adapter.update(rows)
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

        for ((idx,formItem) in arr.withIndex()) {

//            indexPath["row"] = idx

            var formItemAdapter: FormItemAdapter? = null
            if (formItem.uiProperties.cellType == FormItemCellType.textField) {
                formItemAdapter = TextFieldAdapter(form, idx, section, clearClick, promptClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.content) {
                formItemAdapter = ContentAdapter(form, idx, section, clearClick, promptClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.more) {
                formItemAdapter = MoreAdapter(form, idx, section, clearClick, promptClick, rowClick)
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

    override fun textFieldTextChanged(row: Int, section: Int, text: String) {
        //println(row)
        //println(text)
        val item = form.formItems[row]
        item.value = text
        item.make()
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
                val key = data!!.getStringExtra("key")
                val selected = data!!.getStringExtra("selected")
//                println(selected)

                var item: FormItem? = null
                if (key == PRICE_CYCLE_UNIT_KEY) {
                    item = getFormItemFromKey(key) as PriceCycleUnitFormItem
                }
                if (item != null) {
                    item.value = selected
                    item.make()
                }
                val items = generateItems()
                adapter.update(items)
            }
        }
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
