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
import org.jetbrains.anko.alert
import java.io.File
import kotlin.reflect.full.declaredMemberProperties

class RegisterActivity : MyTableVC1(), ImagePicker, TextFieldChangeDelegate, SexChangeDelegate, PrivacyChangeDelegate {

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
    lateinit var form: RegisterForm

    var section_keys: ArrayList<ArrayList<String>> = arrayListOf()
    private var isFeaturedChange: Boolean = false

    val SELECT_REQUEST_CODE = 1

    val testData: HashMap<String, String> = hashMapOf(
        EMAIL_KEY to "ives@housetube.tw",
        PASSWORD_KEY to "1234",
        REPASSWORD_KEY to "1234",
        NAME_KEY to "孫志煌",
        NICKNAME_KEY to "列車長",
        DOB_KEY to "1969-01-05",
        MOBILE_KEY to "0911299994",
        TEL_KEY to "062295888",
        CITY_KEY to "218",
        "city_name" to "台南市",
        AREA_KEY to "219",
        "area_name" to "中西區",
        ROAD_KEY to "南華街101號8樓",
        FB_KEY to "https://www.facebook.com/ives.sun",
        LINE_KEY to "ives9999"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setMyTitle("註冊")
        hidekeyboard(register_layout)

        form = RegisterForm(this)
        sections = form.getSections()
        section_keys = form.getSectionKeys()

        initData()

        recyclerView = editTableView
        initAdapter(true)

        refreshLayout = refresh
        setRefreshListener()
    }

    private fun initData() {
        if (testData.count() > 0) {
            for ((key, value) in testData) {
                val formItem = getFormItemFromKey(key)
                if (formItem != null) {
                    if (key == AREA_KEY && testData.containsKey("area_name")) {
                        val _formItem = formItem as AreaFormItem
                        _formItem.selected_area_names = arrayListOf(testData["area_name"]!!)
                    } else if (key == CITY_KEY && testData.containsKey("city_name")) { // test data session has, so not implement.
                        //val _formItem = formItem as CityFormItem
                        //_formItem.selected_city_names = arrayListOf(testData["city_name"]!!)
                    }
                    formItem.value = value
                    formItem.make()
                }
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
                formItemAdapter!!.textFieldDelegate = this
            } else if (formItem.uiProperties.cellType == FormItemCellType.password) {
                formItemAdapter = TextFieldAdapter(form, idx, indexPath, clearClick, promptClick)
                formItemAdapter!!.textFieldDelegate = this
            } else if (formItem.uiProperties.cellType == FormItemCellType.date) {
                formItemAdapter = MoreAdapter(form, idx, indexPath, clearClick, promptClick, rowClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.sex) {
                formItemAdapter = SexAdapter(form, idx, indexPath, clearClick, promptClick)
                formItemAdapter!!.sexDelegate = this
            } else if (formItem.uiProperties.cellType == FormItemCellType.city) {
                formItemAdapter = MoreAdapter(form, idx, indexPath, clearClick, promptClick, rowClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.area) {
                formItemAdapter = MoreAdapter(form, idx, indexPath, clearClick, promptClick, rowClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.privacy) {
                formItemAdapter = PrivacyAdapter(form, idx, indexPath, clearClick, promptClick)
                formItemAdapter!!.privacyDelegate = this
            }

            if (formItemAdapter != null) {
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

        if (key == PRICE_UNIT_KEY) {
            val rows = PRICE_UNIT.makeSelect()
            singleSelectIntent.putExtra("rows", rows)
            if (forItem.sender != null) {
                val selected = forItem.sender as String
                singleSelectIntent.putExtra("selected", selected)
            }
            startActivityForResult(singleSelectIntent, SELECT_REQUEST_CODE)
        } else if (key == DOB_KEY) {
            val dateSelectIntent = Intent(this, DateSelectVC::class.java)
            dateSelectIntent.putExtra("title", forItem.title)
            dateSelectIntent.putExtra("key", key)
            if (forItem.sender != null) {
                val selected = forItem.sender as String
                dateSelectIntent.putExtra("selected", selected)
            }
            startActivityForResult(dateSelectIntent, SELECT_REQUEST_CODE)
        } else if (key == CITY_KEY) {
            val citySelectIntent = Intent(this, SelectCityVC::class.java)
            citySelectIntent.putExtra("title", forItem.title)
            citySelectIntent.putExtra("key", key)
            if (forItem.sender != null) {
                val selected = forItem.sender as String
                citySelectIntent.putExtra("selected", selected)
            }
            startActivityForResult(citySelectIntent, SELECT_REQUEST_CODE)
        } else if (key == AREA_KEY) {
            val cityItem = getFormItemFromKey(CITY_KEY)
            val city_id = cityItem?.value
            if (city_id == null) {
                warning("請先選擇縣市")
            } else {
                val areaSelectIntent = Intent(this, SelectAreaVC::class.java)
                areaSelectIntent.putExtra("title", forItem.title)
                areaSelectIntent.putExtra("key", key)
                areaSelectIntent.putExtra("city_id", city_id)
                if (forItem.sender != null) {
                    val selected = forItem.sender as String
                    areaSelectIntent.putExtra("selected", selected)
                }
                startActivityForResult(areaSelectIntent, SELECT_REQUEST_CODE)
            }
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

        for (formItem in form.formItems) {
            formItem.checkValidity()
            if (!formItem.isValid) {
                if (formItem.msg != null) {
                    warning(formItem.msg!!)
                } else {
                    warning("有錯誤")
                }
            }
        }


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

                    var item: FormItem? = null
                    if (key == PRICE_UNIT_KEY) {
                        item = getFormItemFromKey(key) as PriceUnitFormItem
                    } else if (key == DOB_KEY) {
                        item = getFormItemFromKey(key) as DateFormItem
                    } else if (key == CITY_KEY) {
                        item = getFormItemFromKey(key) as CityFormItem
                    } else if (key == AREA_KEY) {
                        item = getFormItemFromKey(key) as AreaFormItem
                    }

                    if (item != null && selected != null) {
                        if (item.value != selected) {
                            item.reset()
                        }
                        if (key == AREA_KEY) {
                            val item1: AreaFormItem = item as AreaFormItem
                            val cityItem = getFormItemFromKey(CITY_KEY)
                            item1.city_id = cityItem!!.value!!.toInt()
                        }
                        item.value = selected
                        item.make()
                    }
                    if (item != null && selecteds != null) {
                        var value: String = "-1"
                        item.value = value
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

    override fun textFieldTextChanged(indexPath: IndexPath, text: String) {
        val item = form.formItems[indexPath.row]
        item.value = text
        item.make()
    }

    override fun sexChanged(sex: String) {
        val forItem = getFormItemFromKey(SEX_KEY)
        if (forItem != null) {
            forItem.value = sex
        }
        println(sex)

    }

    override fun privateChanged(checked: Boolean) {
        val formItem = getFormItemFromKey(PRIVACY_KEY)
        if (formItem != null) {
            if (checked) {
                formItem.value = "1"
            } else {
                formItem.value = null
            }
        }
        if (!checked) {
            warning("必須同意隱私權條款，才能完成註冊")
        }
    }
}
