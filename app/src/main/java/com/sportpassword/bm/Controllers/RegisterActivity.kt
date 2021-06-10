package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import com.sportpassword.bm.Adapters.Form.*
import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Form.RegisterForm
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ImagePicker
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.editTableView
import kotlinx.android.synthetic.main.activity_register.edit_featured
import kotlinx.android.synthetic.main.activity_register.featured_text
import kotlinx.android.synthetic.main.activity_register.refresh
import kotlinx.android.synthetic.main.mask.*
import java.io.File
import com.sportpassword.bm.Form.ValueChangedDelegate
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_register.edit_featured_container
import org.jetbrains.anko.sdk27.coroutines.onClick

class RegisterActivity : MyTableVC(), ImagePicker, ValueChangedDelegate {

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

    var section_keys: ArrayList<ArrayList<String>> = arrayListOf()
    private var isFeaturedChange: Boolean = false

    private var old_selected_city: String = ""
    private var member_token: String = ""

    val SELECT_REQUEST_CODE = 1

    val testData: HashMap<String, String> = hashMapOf(
//        EMAIL_KEY to "john@housetube.tw",
//        PASSWORD_KEY to "1234",
//        REPASSWORD_KEY to "1234",
//        NAME_KEY to "孫士君",
//        NICKNAME_KEY to "孫士君",
//        DOB_KEY to "1969-01-05",
//        MOBILE_KEY to "0911299998",
//        TEL_KEY to "062295888",
//        CITY_ID_KEY to "218",
//        "city_name" to "台南市",
//        AREA_ID_KEY to "219",
//        "area_name" to "中西區",
//        ROAD_KEY to "南華街101號8樓",
//        FB_KEY to "https://www.facebook.com/ives.sun",
//        LINE_KEY to "ives9999"
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setMyTitle("註冊")
        hidekeyboard(register_layout)

        form = RegisterForm(this)
        sections = form.getSections()
        section_keys = form.getSectionKeys()

        imageView = edit_featured
        getImageViewParams()
        initImagePicker(R.layout.image_picker_layer)
        edit_featured_container.onClick {
            showImagePickerLayer()
        }

        initData()

        recyclerView = editTableView
        initAdapter(true)

        refreshLayout = refresh
        setRefreshListener()
    }

    private fun initData() {

        if (member.isLoggedIn) {

            //member.memberPrint()
            form.removeItems(arrayListOf(PASSWORD_KEY, REPASSWORD_KEY, PRIVACY_KEY))
//            for (formItem in form.formItems) {
//                println(formItem.name)
//            }
//            println("==========")
            form.formItems.removeAt(form.formItems.size - 1)
//            for (formItem in form.formItems) {
//                println(formItem.name)
//            }
            sections.removeAt(sections.size - 1)

            val keys: ArrayList<String> = arrayListOf()
            for (formItem in form.formItems) {
                if (formItem.name != null) {
                    keys.add(formItem.name!!)
                }
            }

            member_token = member.token
            for (key in keys) {
                if (MEMBER_ARRAY.containsKey(key)) {
                    val value: String = member.fetch(key)
                    val formItem = getFormItemFromKey(key)
                    if (formItem != null) {
                        if (key == AREA_ID_KEY) {
                            val cityFormItem: CityFormItem = getFormItemFromKey(CITY_ID_KEY) as CityFormItem
                            val areaFormItem: AreaFormItem = formItem as AreaFormItem
                            areaFormItem.city_id = (cityFormItem.value)?.toInt()
                        }
                        formItem.value = value
                        formItem.make()
                    }
                }
            }
            old_selected_city = member.fetch(CITY_ID_KEY)
            if (member.avatar.length > 0) {
                //println(member.avatar)
                val avatar: String = member.avatar
                //val avatar: String = BASE_URL + member.avatar
                setImage(null, avatar)
            }
        } else {
            if (testData.count() > 0) {
                for ((key, value) in testData) {
                    val formItem = getFormItemFromKey(key)
                    if (formItem != null) {
                        if (key == AREA_ID_KEY && testData.containsKey("area_name")) {
                            val _formItem = formItem as AreaFormItem
                            _formItem.selected_area_names = arrayListOf(testData["area_name"]!!)
                        } else if (key == CITY_ID_KEY && testData.containsKey("city_name")) { // test data session has, so not implement.
                            //val _formItem = formItem as CityFormItem
                            //_formItem.selected_city_names = arrayListOf(testData["city_name"]!!)
                        }
                        formItem.value = value
                        formItem.make()
                    }
                }
                old_selected_city = testData[CITY_ID_KEY]!!
            }
        }
    }

    override fun generateItems(section: Int): ArrayList<Item> {

        val rows: ArrayList<Item> = arrayListOf()

        val clearClick = { formItem: FormItem ->
        }

        val promptClick = { formItem: FormItem ->
            if (formItem.tooltip != null) {
                Alert.show(this, "提示", formItem.tooltip!!)
            }
        }

        val rowClick = { formItem: FormItem ->
            prepare(formItem)
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

            if (formItem.name == MOBILE_KEY) {
                formItemAdapter = MobileAdapter(formItem, clearClick, promptClick)
            } else if (formItem.name == EMAIL_KEY) {
                formItemAdapter = EmailAdapter(formItem, clearClick, promptClick)
            } else if (formItem.name == NAME_KEY) {
                formItemAdapter = TitleAdapter(formItem, clearClick, promptClick)
            } else if (formItem.name == NICKNAME_KEY) {
                formItemAdapter = NicknameAdapter(formItem, clearClick, promptClick)
            } else if (formItem.name == TEL_KEY) {
                formItemAdapter = TelAdapter(formItem, clearClick, promptClick)
            } else if (formItem.name == ROAD_KEY) {
                formItemAdapter = RoadAdapter(formItem, clearClick, promptClick)
            } else if (formItem.name == FB_KEY) {
                formItemAdapter = FBAdapter(formItem, clearClick, promptClick)
            } else if (formItem.name == LINE_KEY) {
                formItemAdapter = LineAdapter(formItem, clearClick, promptClick)
            } else {
                if (formItem.uiProperties.cellType == FormItemCellType.password) {
                    formItemAdapter = TextFieldAdapter(formItem, clearClick, promptClick)
                } else if (formItem.uiProperties.cellType == FormItemCellType.date) {
                    formItemAdapter = MoreAdapter(formItem, clearClick, rowClick)
                } else if (formItem.uiProperties.cellType == FormItemCellType.sex) {
                    formItemAdapter = SexAdapter(formItem)
                } else if (formItem.uiProperties.cellType == FormItemCellType.city) {
                    formItemAdapter = MoreAdapter(formItem, clearClick, rowClick)
                } else if (formItem.uiProperties.cellType == FormItemCellType.area) {
                    formItemAdapter = MoreAdapter(formItem, clearClick, rowClick)
                } else if (formItem.uiProperties.cellType == FormItemCellType.privacy) {
                    formItemAdapter = PrivacyAdapter(formItem)
                }
            }

            if (formItemAdapter != null) {
                formItemAdapter.valueChangedDelegate = this
                rows.add(formItemAdapter!!)
            }
//            idx++
        }

//        val formItem = getFormItemFromKey(MOBILE_KEY)
//        println("generate:${formItem!!.name}:${formItem!!.value}")

        return rows
    }

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
                    } else if (key == CITY_ID_KEY) {
                        item = getFormItemFromKey(key) as CityFormItem
                    } else if (key == AREA_ID_KEY) {
                        item = getFormItemFromKey(key) as AreaFormItem
                    }

                    if (item != null && selected != null) {
                        if (item.value != selected) {
                            item.reset()
                        }
                        if (key == AREA_ID_KEY) {
                            val item1: AreaFormItem = item as AreaFormItem
                            val cityItem = getFormItemFromKey(CITY_ID_KEY)
                            item1.city_id = cityItem!!.value!!.toInt()
                        } else if (key == CITY_ID_KEY) {
                            val item1 = getFormItemFromKey(AREA_ID_KEY)
                            if (old_selected_city != selected) {
                                if (item1 != null) {
                                    item1!!.reset()
                                }
                                old_selected_city = selected
                            }
                        }
                        item.value = selected
                        item.make()
                    }
                    if (item != null && selecteds != null) {
                        var value: String = "-1"
                        item.value = value
                        item.make()
                    }

//                    val formItem = getFormItemFromKey(MOBILE_KEY)
//                    println("result:${formItem!!.name}:${formItem!!.value}")

                    notifyChanged(true)
                }
            }
        }
    }

    fun prepare(formItem: FormItem) {

        val key = formItem.name

        val singleSelectIntent = Intent(this, SingleSelectVC::class.java)
        singleSelectIntent.putExtra("title", formItem.title)
        singleSelectIntent.putExtra("key", key)

        val multiSelectIntent = Intent(this, MultiSelectVC::class.java)
        multiSelectIntent.putExtra("title", formItem.title)
        multiSelectIntent.putExtra("key", key)

        if (key == PRICE_UNIT_KEY) {
            val rows = PRICE_UNIT.makeSelect()
            singleSelectIntent.putExtra("rows", rows)
            if (formItem.sender != null) {
                val selected = formItem.sender as String
                singleSelectIntent.putExtra("selected", selected)
            }
            startActivityForResult(singleSelectIntent, SELECT_REQUEST_CODE)
        } else if (key == DOB_KEY) {
            val dateSelectIntent = Intent(this, DateSelectVC::class.java)
            dateSelectIntent.putExtra("title", formItem.title)
            dateSelectIntent.putExtra("key", key)
            if (formItem.sender != null) {
                //val selected = forItem.value
                dateSelectIntent.putExtra("selected", formItem.value)
            }
            startActivityForResult(dateSelectIntent, SELECT_REQUEST_CODE)
        } else if (key == CITY_ID_KEY) {
            val citySelectIntent = Intent(this, SelectCityVC::class.java)
            citySelectIntent.putExtra("title", formItem.title)
            citySelectIntent.putExtra("key", key)
            if (formItem.sender != null) {
                val selected = formItem.sender as String
                citySelectIntent.putExtra("selected", selected)
            }
            startActivityForResult(citySelectIntent, SELECT_REQUEST_CODE)
        } else if (key == AREA_ID_KEY) {
            val cityItem = getFormItemFromKey(CITY_ID_KEY)
            val city_id = cityItem?.value
            if (city_id == null) {
                warning("請先選擇縣市")
            } else {
                val areaSelectIntent = Intent(this, SelectAreaVC::class.java)
                areaSelectIntent.putExtra("title", formItem.title)
                areaSelectIntent.putExtra("key", key)
                areaSelectIntent.putExtra("city_id", city_id)
                if (formItem.sender != null) {
                    val selected = formItem.sender as String
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

        var isSubmit: Boolean = true
        var msg: String = ""
        for (formItem in form.formItems) {
            formItem.checkValidity()
            if (!formItem.isValid) {
                if (formItem.msg != null) {
                    msg += formItem.msg!! + "\n"
                } else {
                    warning("有錯誤")
                }
                isSubmit = false
            }
        }

        if (!isSubmit) {
            warning(msg)
        } else {
            Loading.show(mask)
            val params: HashMap<String, String> = hashMapOf()
            for (formItem in form.formItems) {

                if (formItem.value != null) {
                    val value: String = formItem.value!!
                    params[formItem.name!!] = value
                }
                if (params.containsKey(CITY_KEY)) {
                    params[CITY_ID_KEY] = params[CITY_KEY]!!
                    params.remove(CITY_KEY)
                }
                if (params.containsKey(AREA_KEY)) {
                    params[AREA_ID_KEY] = params[AREA_KEY]!!
                    params.remove(AREA_KEY)
                }
                if (member_token.length > 0) {
                    params[TOKEN_KEY] = member_token
                }
            }
            params["do"] = "update"
            if (isFeaturedChange) {
                params["featured"] = "1"
            }
            //println(params)
            //println(filePath)

            MemberService.update(this, params, filePath) { success ->
                Loading.hide(mask)
                if (success) {
                    if (MemberService.success) {
                        var msg: String = ""
                        if (this.member_token.isNotEmpty()) {
                            msg = "修改成功"
                        } else {
                            msg = "註冊成功，已經寄出email與手機的認證訊息，請繼續完成認證程序"
                        }
                        Alert.show(this, "成功", msg) {
                            prev()
                        }
                    } else {
                        Alert.show(this, "警告", MemberService.msg)
                    }
                } else {
                    Alert.show(this, "警告", "伺服器錯誤，請稍後再試，或洽管理人員")
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

    override fun setImage(newFile: File?, url: String?) {
        featured_text.visibility = View.INVISIBLE
        val layoutParams = edit_featured.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.setMargins(0, 0, 0, 0)
        imageView.layoutParams = layoutParams
        isFeaturedChange = true
        super.setImage(newFile, url)
    }

    override fun removeImage() {
        featured_text.visibility = View.VISIBLE
        val layoutParams = edit_featured.layoutParams as RelativeLayout.LayoutParams
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
        toForgetPassword()
    }

    fun registerLogin(view: View) {
        toLogin()
    }

    fun cancel(view: View) {
        prev()
    }

    fun getImageViewParams() {
        val l = edit_featured.layoutParams as RelativeLayout.LayoutParams
        originW = l.width
        originH = l.height
        originScaleType = edit_featured.scaleType
        //val m = edit_featured.
        originMarginTop = l.topMargin
        originMarginBottom = l.bottomMargin
    }

    override fun textFieldTextChanged(formItem: FormItem, text: String) {
//        val item = getFormItemFromKey(key)
//        if (item != null) {
        //move to adapter
//        formItem!!.value = text
//        formItem!!.make()
//        }
        //println(text)
    }

    override fun sexChanged(sex: String) {
        //move to adapter
//        val forItem = getFormItemFromKey(SEX_KEY)
//        if (forItem != null) {
//            forItem.value = sex
//        }
        //println(sex)

    }

    override fun tagChecked(checked: Boolean, name: String, key: String, value: String) {}

    override fun stepperValueChanged(number: Int, name: String) {}

    override fun privateChanged(checked: Boolean) {
        //move to adapter
//        val formItem = getFormItemFromKey(PRIVACY_KEY)
//        if (formItem != null) {
//            if (checked) {
//                formItem.value = "1"
//            } else {
//                formItem.value = null
//            }
//        }
        if (!checked) {
            warning("必須同意隱私權條款，才能完成註冊")
        }
        //println(checked)
    }
}
