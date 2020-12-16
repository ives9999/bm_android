package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.sportpassword.bm.Adapters.Form.*
import com.sportpassword.bm.Form.CourseForm
import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Form.RegisterForm
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ImagePicker
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_edit_course_vc.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.editTableView
import kotlinx.android.synthetic.main.activity_register.edit_featured
import kotlinx.android.synthetic.main.activity_register.featured_text
import kotlinx.android.synthetic.main.activity_register.refresh
import kotlinx.android.synthetic.main.mask.*
import java.io.File
import kotlin.reflect.full.declaredMemberProperties

class RegisterActivity : MyTableVC1(), ImagePicker {

    //image picker
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
    private var originW: Int = 0
    private var originH: Int = 0
    private var originMarginTop = 0
    private var originMarginBottom = 0
    private lateinit var originScaleType: ImageView.ScaleType


    //Form
    var form: RegisterForm = RegisterForm()

    var section_keys: ArrayList<ArrayList<String>> = arrayListOf()
    private var isFeaturedChange: Boolean = false

    val SELECT_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setMyTitle("註冊")
        hidekeyboard(register_layout)

        sections = form.getSections()
        section_keys = form.getSectionKeys()

        recyclerView = editTableView
        initAdapter(true)

        refreshLayout = refresh
        setRefreshListener()
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

        val sexClick = {selected: String ->
            val forItem = getFormItemFromKey(SEX_KEY)
            if (forItem != null) {
                forItem.value = selected
            }
            //println("main:$selected")
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
            } else if (formItem.uiProperties.cellType == FormItemCellType.password) {
                formItemAdapter = TextFieldAdapter(form, idx, indexPath, clearClick, promptClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.date) {
                formItemAdapter = MoreAdapter(form, idx, indexPath, clearClick, promptClick, rowClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.sex) {
                formItemAdapter = SexAdapter(form, idx, indexPath, sexClick, clearClick, promptClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.city) {
                formItemAdapter = MoreAdapter(form, idx, indexPath, clearClick, promptClick, rowClick)
            }

            if (formItemAdapter != null) {
                //formItemAdapter!!.delegate = this
                rows.add(formItemAdapter!!)
            }
//            idx++
        }

        return rows
    }

    fun prepare(idx: Int) {

        val forItem = form.formItems[idx]
        val key = forItem.name

        val singleSelectIntent = Intent(this, SingleSelectVC1::class.java)
        singleSelectIntent.putExtra("title", forItem.title)
        singleSelectIntent.putExtra("key", key)

        val multiSelectIntent = Intent(this, MultiSelectVC1::class.java)
        multiSelectIntent.putExtra("title", forItem.title)
        multiSelectIntent.putExtra("key", key)

        val dateSelectIntent = Intent(this, DateSelectVC::class.java)
        dateSelectIntent.putExtra("title", forItem.title)
        dateSelectIntent.putExtra("key", key)

        val contentIntent = Intent(this, ContentEditVC::class.java)
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
        } else if (key == DOB_KEY) {
            if (forItem.sender != null) {
                val tmp = forItem.sender as HashMap<String, String>
                val selected = tmp.get("date")!!
                dateSelectIntent.putExtra("selected", selected)
            }
            startActivityForResult(dateSelectIntent, SELECT_REQUEST_CODE)
        }

    }

//    private fun putValue() {
//        if (superCourse != null) {
//            val kc = superCourse::class
//            for (formItem in form.formItems) {
//                val name = formItem.name!!
//                kc.declaredMemberProperties.forEach {
//                    if (it.name == formItem.name) {
//                        val type = JSONParse.getType(it)
//                        when (type) {
//                            "String" -> {
//                                val value = JSONParse.getValue<String>(name, superCourse, it)
//                                if (value != null) {
//                                    formItem.value = value
//                                }
//                            }
//                            "Int" -> {
//                                val value = JSONParse.getValue<Int>(name, superCourse, it)
//                                if (value != null) {
//                                    formItem.value = value.toString()
//                                }
//
//                            }
//                        }
//                        formItem.make()
//                    }
//                }
//            }
//            if (superCourse.featured_path.count() > 0) {
//                val featured: String = BASE_URL + superCourse.featured_path
////                        println(featured)
//                setImage(null, featured)
//            }
//        }
//    }

    fun submit(view: View) {
//        val email: String = registerEmailTxt.text.toString()
//        if (email.isEmpty()) {
//            Alert.show(this, "警告", "EMail沒填")
//        }
//        val password: String = registerPasswordTxt.text.toString()
//        if (password.isEmpty()) {
//            Alert.show(this, "警告", "密碼沒填")
//        }
//        val repassword: String = registerRePasswordTxt.text.toString()
//        if (repassword.isEmpty()) {
//            Alert.show(this, "警告", "密碼確認欄位沒填")
//        }
//        if (password != repassword) {
//            Alert.show(this, "警告", "密碼不一致")
//        }

        //println("submit: " + URL_REGISTER)
//        Loading.show(mask)
//        MemberService.register(this, email, password, repassword) { success ->
//            Loading.hide(mask)
//            if (success) {
//                //println("register ok")
//                if (MemberService.success) {
//                    Alert.show(this, "成功", "註冊成功，請儘速通過email認證，才能使用更多功能！！") {
//                        home(this)
//                    }
//                } else {
//                    Alert.show(this, "警告", MemberService.msg)
//                }
//            } else {
//                Alert.show(this, "警告", MemberService.msg)
//            }
//        }
    }

//    fun registerFBSubmit(view: View) {
//        loginFB()
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //println(data)
        when (requestCode) {
            ACTION_PHOTO_REQUEST_CODE -> {
                //println(data.data)
                dealPhoto(requestCode, resultCode, data)
            }
            ACTION_CAMERA_REQUEST_CODE -> {
                dealCamera(requestCode, resultCode, data)
            }
            SELECT_REQUEST_CODE -> {
                if (data != null) {
                    var key: String? = null
                    if (data.hasExtra("key")) {
                        key = data.getStringExtra("key")
                    }
                    var selected: String? = null
                    if (data.hasExtra("selected")) {
                        selected = data.getStringExtra("selected")
                    }
    //                println(selected)

                    var selecteds: ArrayList<String>? = null
                    if (data.hasExtra("selecteds")) {
                        selecteds = data.getStringArrayListExtra("selecteds")
                    }

                    var content: String? = null
                    if (data.hasExtra("content")) {
                        content = data.getStringExtra("content")
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
                    } else if (key == DOB_KEY) {
                        item = getFormItemFromKey(key) as DateFormItem
                    }

                    if (item != null && selected != null) {
                        item.value = selected
                        item.make()
                    }
                    if (item != null && selecteds != null) {
                        var value: String = "-1"
                        if (key == WEEKDAY_KEY) {
                            val tmps: ArrayList<Int> = ArrayList(selecteds.map {it.toInt()})
                            value = Global.weekdaysToDBValue(tmps).toString()
                        }
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

//    override fun textFieldTextChanged(indexPath: IndexPath, text: String) {
//        //println(row)
//        //println(text)
//        val item = form.formItems[indexPath.row]
//        item.value = text
//        item.make()
//    }

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

//    fun getImageViewParams() {
//        val l = edit_featured.layoutParams as LinearLayout.LayoutParams
//        originW = l.width
//        originH = l.height
//        originScaleType = edit_featured.scaleType
//        //val m = edit_featured.
//        originMarginTop = l.topMargin
//        originMarginBottom = l.bottomMargin
//    }

    fun registerForgetPassword(view: View) {
        goForgetPassword()
    }

    fun registerLogin(view: View) {
        goLogin()
    }

    fun cancel(view: View) {
        prev()
    }
}
