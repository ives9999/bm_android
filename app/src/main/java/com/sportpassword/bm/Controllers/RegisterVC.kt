package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.editTableView
import kotlinx.android.synthetic.main.activity_register.edit_featured
import kotlinx.android.synthetic.main.activity_register.featured_text
import kotlinx.android.synthetic.main.activity_register.refresh
import kotlinx.android.synthetic.main.mask.*
import java.io.File
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_register.edit_featured_container
import kotlinx.android.synthetic.main.top_view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties

class RegisterVC : MyTableVC() {

    //image picker
//    override val ACTION_CAMERA_REQUEST_CODE = 100
//    override val ACTION_PHOTO_REQUEST_CODE = 200
//    override val activity = this
//    override val context = this
//    override var currentPhotoPath = ""
//    override var filePath: String = ""
//    override var file: File? = null
//    override lateinit var imagePickerLayer: AlertDialog
//    override lateinit var alertView: View
//    override lateinit var imageView: ImageView

    private var originW: Int = 0
    private var originH: Int = 0
    private var originMarginTop = 0
    private var originMarginBottom = 0
    private lateinit var originScaleType: ImageView.ScaleType

//    var section_keys: ArrayList<ArrayList<String>> = arrayListOf()

    private var isFeaturedChange: Boolean = false

    private var old_selected_city: String = ""
    private var member_token: String = ""

//    val SELECT_REQUEST_CODE = 1

    val testData: HashMap<String, String> = hashMapOf(
//        EMAIL_KEY to "john@housetube.tw",
//        PASSWORD_KEY to "1234",
//        REPASSWORD_KEY to "1234",
//        NAME_KEY to "孫士君",
//        NICKNAME_KEY to "孫士君",
//        DOB_KEY to "1969-01-05",
//        MOBILE_KEY to "0911299998",
//        TEL_KEY to "062295888",
//        CITY_KEY to "218",
//        "city_name" to "台南市",
//        AREA_KEY to "219",
//        "area_name" to "中西區",
//        ROAD_KEY to "南華街101號8樓",
//        FB_KEY to "https://www.facebook.com/ives.sun",
//        LINE_KEY to "ives9999"
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //setMyTitle("註冊")
        if (member.isLoggedIn) {
            topTitleLbl.text = member.name
        } else {
            topTitleLbl.text = "註冊"
        }
        hidekeyboard(register_layout)

//        form = RegisterForm(this)
//        sections = form.getSections()
//        section_keys = form.getSectionKeys()

        imageView = edit_featured
        getImageViewParams()
        initImagePicker(R.layout.image_picker_layer)
        edit_featured_container.onClick {
            showImagePickerLayer()
        }

        recyclerView = editTableView
        oneSectionAdapter = OneSectionAdapter(this, R.layout.cell_section, this, hashMapOf())
        oneSectionAdapter.setOneSection(oneSections)
        recyclerView.adapter = oneSectionAdapter

        initData()
        oneSectionAdapter.setOneSection(oneSections)
        oneSectionAdapter.notifyDataSetChanged()

        //initAdapter(true)

        refreshLayout = refresh
        setRefreshListener()

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    override fun refresh() {

//        for (formItem in form.formItems) {
//            formItem.reset()
//        }
        params.clear()
        initData()
//        generateItems()
        //adapter.notifyDataSetChanged()
        refreshLayout!!.isRefreshing = false
    }

    private fun initData() {

//        session.dump()
        oneSections.clear()
        val rows: ArrayList<OneRow> = arrayListOf()
        var row = OneRow("EMail", member.email!!, member.email!!, EMAIL_KEY, "textField", KEYBOARD.emailAddress, "service@bm.com", "", true)
        row.msg = "EMail沒有填寫"
        rows.add(row)

        if (!member.isLoggedIn) {
            row = OneRow("密碼", "", "", PASSWORD_KEY, "password", KEYBOARD.default, "", "", true)
            row.msg = "密碼沒有填寫"
            rows.add(row)
            row = OneRow("密碼確認", "", "", REPASSWORD_KEY, "password", KEYBOARD.default, "", "", true)
            row.msg = "密碼確認沒有填寫"
            rows.add(row)
        }
        var section = makeSectionRow("登入資料", "login", rows, true)
        oneSections.add(section)

        rows.clear()
        row = OneRow("姓名", member.name!!, member.name!!, NAME_KEY, "textField", KEYBOARD.default, "王大明", "", true)
        row.msg = "姓名沒有填寫"
        rows.add(row)
        row = OneRow("暱稱", member.nickname!!, member.nickname!!, NICKNAME_KEY, "textField", KEYBOARD.default, "大明哥", "", true)
        row.msg = "暱稱沒有填寫"
        rows.add(row)
        row = OneRow("生日", member.dob!!, member.dob!!, DOB_KEY, "more")
        rows.add(row)

        if (member.sex!!.isEmpty()) {
            member.sex = "M"
        }
        val sex = member.sex
        row = OneRow("性別", member.sex!!, "", SEX_KEY, "sex", KEYBOARD.default, "", "", true)
        row.msg = "沒有選擇性別"
        rows.add(row)
        row = OneRow("金鑰", member.token!!, member.token!!, TOKEN_KEY, "textField")
        rows.add(row)

        section = makeSectionRow("個人資料", "data", rows, true)
        oneSections.add(section)

        rows.clear()
        row = OneRow("行動電話", member.mobile!!, member.mobile!!, MOBILE_KEY, "textField", KEYBOARD.numberPad, "0939123456", "", true)
        row.msg = "行動電話沒有填寫"
        rows.add(row)
        row = OneRow("市內電話", member.tel!!, member.tel!!, TEL_KEY, "textField", KEYBOARD.numberPad, "021234567")
        rows.add(row)
        row = OneRow("縣市", member.city.toString(), Global.zoneIDToName(member.city), CITY_KEY, "more", KEYBOARD.default, "", "", true)
        row.msg = "沒有選擇縣市"
        rows.add(row)
        row = OneRow("區域", member.area.toString(), Global.zoneIDToName(member.area), AREA_KEY, "more", KEYBOARD.default, "", "", true)
        row.msg = "沒有選擇區域"
        rows.add(row)
        row = OneRow("住址", member.road!!, member.road!!, ROAD_KEY, "textField", KEYBOARD.default, "中山路60號", "", true)
        row.msg = "沒有填寫住址"
        rows.add(row)
        section = makeSectionRow("聯絡資料", "contact", rows, true)
        oneSections.add(section)

        rows.clear()
        row = OneRow("FB", member.fb!!, member.fb!!, FB_KEY, "textField")
        rows.add(row)
        row = OneRow("Line", member.line!!, member.line!!, LINE_KEY, "textField")
        rows.add(row)
        section = makeSectionRow("社群資料", "social", rows, true)
        oneSections.add(section)

        if (!member.isLoggedIn) {
            rows.clear()
            row = OneRow("隱私權", "true", "同意隱私權條款", PRIVACY_KEY, "privacy", KEYBOARD.default, "", "", true)
            rows.add(row)
            section = makeSectionRow("隱私權", PRIVACY_KEY, rows, true)
            oneSections.add(section)
        }

        member_token = member.token!!
        old_selected_city = member.city.toString()
        if (member.avatar!!.length > 0) {
            //println(member.avatar)
            val avatar: String = member.avatar!!
            //val avatar: String = BASE_URL + member.avatar
            setImage(null, avatar)
            isFeaturedChange = false
        }

        for ((key, value) in testData) {
            val row: OneRow = getOneRowFromKey(key)
            row.value = value
            if (key == DOB_KEY) {
                row.show = value
            } else if (key == CITY_KEY || key == AREA_KEY) {
                row.show = Global.zoneIDToName(value.toInt())
            }
        }

//        if (member.isLoggedIn) {

//            form.removeItems(arrayListOf(PASSWORD_KEY, REPASSWORD_KEY, PRIVACY_KEY))
//            form.formItems.removeAt(form.formItems.size - 1)
//            sections.removeAt(sections.size - 1)
//
//            val keys: ArrayList<String> = arrayListOf()
//            for (formItem in form.formItems) {
//                if (formItem.name != null) {
//                    keys.add(formItem.name!!)
//                }
//            }


//            for (key in keys) {
//
//                if (key == "section") { continue }
//
//                val formItem = getFormItemFromKey(key)
//                var value: String = ""
//                val items = MemberTable::class.memberProperties.iterator()
//                for (it in items) {
//
//                    val name = it.name
//                    if (key == name) {
//                        val t = it.returnType
//                        if (t == String::class.createType()) {
//                            value = session.getString(name, "")!!
//                        } else if (t == Int::class.createType()) {
//                            val tmp = session.getInt(name, 0)
//                            value = tmp.toString()
//                            if (key == AREA_KEY) {
//                                val cityFormItem: CityFormItem = getFormItemFromKey(CITY_KEY) as CityFormItem
//                                val areaFormItem: AreaFormItem = formItem as AreaFormItem
//                                if (cityFormItem.value != null && cityFormItem.value!!.isNotEmpty()) {
//                                    areaFormItem.city_id = (cityFormItem.value)?.toInt()
//                            }
//                        }
//
//                        } else if (t == Boolean::class.createType()) {
//                            val tmp = session.getBoolean(name, false)
//                            value = tmp.toString()
//                        }
//                        break
//                    }
//                }
//
//                formItem?.value = value
//                formItem?.make()
//            }
//            old_selected_city = member.city.toString()
//            if (member.avatar!!.length > 0) {
//                //println(member.avatar)
//                val avatar: String = member.avatar!!
//                //val avatar: String = BASE_URL + member.avatar
//                setImage(null, avatar)
//                isFeaturedChange = false
//            }
//        } else {
//            if (testData.count() > 0) {
//                for ((key, value) in testData) {
//                    val formItem = getFormItemFromKey(key)
//                    if (formItem != null) {
//                        if (key == AREA_KEY && testData.containsKey("area_name")) {
//                            val _formItem = formItem as AreaFormItem
//                            _formItem.selected_area_names = arrayListOf(testData["area_name"]!!)
//                        } else if (key == CITY_KEY && testData.containsKey("city_name")) { // test data session has, so not implement.
//                            //val _formItem = formItem as CityFormItem
//                            //_formItem.selected_city_names = arrayListOf(testData["city_name"]!!)
//                        }
//                        formItem.value = value
//                        formItem.make()
//                    }
//                }
//                old_selected_city = testData[CITY_KEY]!!
//            }
//        }
    }

//    override fun generateItems(section: Int): ArrayList<Item> {
//
//        val rows: ArrayList<Item> = arrayListOf()
//
//        val clearClick = { formItem: FormItem ->
//            formItem.reset()
//        }
//
//        val promptClick = { formItem: FormItem ->
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
//        for ((i,formItem) in arr.withIndex()) {
//
//            val indexPath: IndexPath = IndexPath(section, i)
//            var idx: Int = 0
//            for ((j, _forItem) in form.formItems.withIndex()) {
//                if (formItem.name == _forItem.name) {
//                    idx = j
//                    break
//                }
//            }
//
//            var formItemAdapter: FormItemAdapter? = null
//
//            if (formItem.name == MOBILE_KEY) {
//                formItemAdapter = MobileAdapter(formItem, clearClick, promptClick)
//            } else if (formItem.name == EMAIL_KEY) {
//                formItemAdapter = EmailAdapter(formItem, clearClick, promptClick)
//            } else if (formItem.name == NAME_KEY) {
//                formItemAdapter = TitleAdapter(formItem, clearClick, promptClick)
//            } else if (formItem.name == NICKNAME_KEY) {
//                formItemAdapter = NicknameAdapter(formItem, clearClick, promptClick)
//            } else if (formItem.name == TEL_KEY) {
//                formItemAdapter = TelAdapter(formItem, clearClick, promptClick)
//            } else if (formItem.name == ROAD_KEY) {
//                formItemAdapter = RoadAdapter(formItem, clearClick, promptClick)
//            } else if (formItem.name == FB_KEY) {
//                formItemAdapter = FBAdapter(formItem, clearClick, promptClick)
//            } else if (formItem.name == LINE_KEY) {
//                formItemAdapter = LineAdapter(formItem, clearClick, promptClick)
//            } else {
//                if (formItem.uiProperties.cellType == FormItemCellType.password) {
//                    formItemAdapter = TextFieldAdapter(formItem, clearClick, promptClick)
//                } else if (formItem.uiProperties.cellType == FormItemCellType.date) {
//                    formItemAdapter = MoreAdapter(formItem, clearClick, rowClick)
//                } else if (formItem.uiProperties.cellType == FormItemCellType.sex) {
//                    formItemAdapter = SexAdapter(formItem)
//                } else if (formItem.uiProperties.cellType == FormItemCellType.city) {
//                    formItemAdapter = MoreAdapter(formItem, clearClick, rowClick)
//                } else if (formItem.uiProperties.cellType == FormItemCellType.area) {
//                    formItemAdapter = MoreAdapter(formItem, clearClick, rowClick)
//                } else if (formItem.uiProperties.cellType == FormItemCellType.privacy) {
//                    formItemAdapter = PrivacyAdapter(formItem)
//                }
//            }
//
//            if (formItemAdapter != null) {
//                formItemAdapter.valueChangedDelegate = this
//                rows.add(formItemAdapter)
//            }
////            idx++
//        }
//
////        val formItem = getFormItemFromKey(MOBILE_KEY)
////        println("generate:${formItem!!.name}:${formItem!!.value}")
//
//        return rows
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            ACTION_PHOTO_REQUEST_CODE -> {
//                dealPhoto(requestCode, resultCode, data)
//            }
//            ACTION_CAMERA_REQUEST_CODE -> {
//                dealCamera(requestCode, resultCode, data)
//            }
//        }
//    }

//    fun prepare(formItem: FormItem) {
//
//        val key = formItem.name
//
//        val singleSelectIntent = Intent(this, SingleSelectVC::class.java)
//        singleSelectIntent.putExtra("title", formItem.title)
//        singleSelectIntent.putExtra("key", key)
//
//        val multiSelectIntent = Intent(this, MultiSelectVC::class.java)
//        multiSelectIntent.putExtra("title", formItem.title)
//        multiSelectIntent.putExtra("key", key)
//
//        if (key == DOB_KEY) {
//            var selected: String? = null
//            if (formItem.sender != null) {
//                selected = formItem.value
//            }
//            toSelectDate(DOB_KEY, selected, this)
//        } else if (key == CITY_KEY) {
//            var selected: String? = null
//            if (formItem.sender != null) {
//                selected = formItem.sender as String
//            }
//            toSelectCity(selected, this, able_type)
//        } else if (key == AREA_KEY) {
//            val cityItem = getFormItemFromKey(CITY_KEY)
//            val city_id = cityItem?.value
//            if (city_id == null) {
//                warning("請先選擇縣市")
//            } else {
//                var selected: String? = null
//                if (formItem.sender != null) {
//                    selected = formItem.sender as String
//                }
//                toSelectArea(selected, city_id.toInt(), this, able_type)
//            }
//        }
//
//    }

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

        var msg: String = ""
        for (section in oneSections) {
            for (row in section.items) {
                if (row.isRequired && row.value.isEmpty()) {
                    msg += row.msg + "\n"
                }
            }
        }
        if (!member.isLoggedIn) {
            val password: String = getRowValue(PASSWORD_KEY)
            val repassword: String = getRowValue(REPASSWORD_KEY)
            if (password != repassword) {
                msg += "密碼不符合" + "\n"
            }

            val privacy: Boolean = getRowValue(PRIVACY_KEY).toBoolean()
            if (!privacy) {
                msg += "必須同意隱私權政策才能完成註冊"
            }
        }
//        for (formItem in form.formItems) {
//            formItem.checkValidity()
//            if (!formItem.isValid) {
//                if (formItem.msg != null) {
//                    msg += formItem.msg!! + "\n"
//                } else {
//                    warning("有錯誤")
//                }
//                isSubmit = false
//            }
//        }

        if (msg.length > 0) {
            warning(msg)
        } else {
            Loading.show(mask)
            val params: HashMap<String, String> = hashMapOf()
            for (section in oneSections) {
                for (row in section.items) {
                    params[row.key] = row.value
                }
            }
//            for (formItem in form.formItems) {
//
//                if (formItem.value != null) {
//                    val value: String = formItem.value!!
//                    params[formItem.name!!] = value
//                }
//                //忘記這兩個程式的作用為何
//                if (params.containsKey(CITY_KEY)) {
//                    params["city_id"] = params[CITY_KEY]!!
//                    //params.remove(CITY_KEY)
//                }
//                if (params.containsKey(AREA_KEY)) {
//                    params["area_id"] = params[AREA_KEY]!!
//                    //params.remove(AREA_KEY)
//                }
//                if (member_token.length > 0) {
//                    params[TOKEN_KEY] = member_token
//                }
//            }
            params["device"] = "app"
            params["do"] = "update"
            if (isFeaturedChange) {
                params["featured"] = "1"
            }
            if (member_token.isNotEmpty()) {
                params[TOKEN_KEY] = member_token
            }
//            println(params)
            //println(filePath)

            //this is execute DataService update
            MemberService.update(this, params, filePath) { success ->
                Loading.hide(mask)
                if (success) {
                    if (MemberService.success) {
                        echoYes()
                    } else {
                        runOnUiThread {
                            warning(MemberService.msg)
                        }
                    }
                } else {
                    runOnUiThread {
                        warning("伺服器錯誤，請稍後再試，或洽管理人員")
                    }
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

    fun echoYes() {

        var msg: String = ""
        if (this.member_token.isNotEmpty()) {
            msg = "修改成功"
        } else {
            msg = "註冊成功，已經寄出email與手機的認證訊息，請繼續完成認證程序"
        }
        runOnUiThread {
            try {
                //println(MemberService.jsonString)
                val table = Gson().fromJson<RegisterResTable>(
                    MemberService.jsonString,
                    RegisterResTable::class.java
                )
                if (table != null) {
                    if (!table.success) {
                        var msg: String = ""
                        for (error in table.errors) {
                            msg += error + "\n"
                        }
                        warning(msg)
                    } else {
                        if (table.model != null) {
                            val memberTable: MemberTable = table.model!!
                            memberTable.toSession(this, true)
                            info(msg, "", "關閉") {
                                prev()
                            }
                        }
                    }
                } else {
                    warning("伺服器回傳錯誤，請稍後再試，或洽管理人員")
                }
            } catch (e: JsonParseException) {
                warning(e.localizedMessage!!)
            }
        }
    }

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

    private fun getImageViewParams() {
        val l = edit_featured.layoutParams as RelativeLayout.LayoutParams
        originW = l.width
        originH = l.height
        originScaleType = edit_featured.scaleType
        //val m = edit_featured.
        originMarginTop = l.topMargin
        originMarginBottom = l.bottomMargin
    }

    override fun cellTextChanged(sectionIdx: Int, rowIdx: Int, str: String) {
        oneSections[sectionIdx].items[rowIdx].value = str
    }

    override fun cellMoreClick(sectionIdx: Int, rowIdx: Int) {
        val row: OneRow = getOneRowFromIdx(sectionIdx, rowIdx)
        if (row.key == DOB_KEY) {
            toSelectDate(row.key, row.value, this)
        } else if (row.key == CITY_KEY) {
            toSelectCity(row.value, this)
        } else if (row.key == AREA_KEY) {
            val row: OneRow = getOneRowFromKey(CITY_KEY)
            if (row.value.isEmpty()) {
                warning("請先選擇縣市")
            } else {
                toSelectArea(row.value, row.value.toInt(), this)
            }
        }
    }

    override fun cellSexChanged(key: String, sectionIdx: Int, rowIdx: Int, sex: String) {
        val row: OneRow = getOneRowFromIdx(sectionIdx, rowIdx)
        row.value = sex
        row.show = sex
    }

    override fun cellClear(sectionIdx: Int, rowIdx: Int) {
        oneSections[sectionIdx].items[rowIdx].value = ""
        oneSections[sectionIdx].items[rowIdx].show = ""
        //searchSections = updateSectionRow()
        oneSectionAdapter.setOneSection(oneSections)
        oneSectionAdapter.notifyItemChanged(sectionIdx)
    }

    override fun cellPrivacyChanged(sectionIdx: Int, rowIdx: Int, checked: Boolean) {
        val row: OneRow = getOneRowFromIdx(sectionIdx, rowIdx)
        row.value = checked.toString()
        if (!checked) {
            warning("必須同意隱私權政策才能完成註冊")
        }
    }

    override fun singleSelected(key: String, selected: String) {

        val row: OneRow = getOneRowFromKey(key)
        row.value = selected
        if (key == DOB_KEY) {
            row.show = selected
        } else if (key == CITY_KEY) {
            row.show = Global.zoneIDToName(selected.toInt())
            if (selected != old_selected_city) {
                val row1: OneRow = getOneRowFromKey(AREA_KEY)
                row1.value = ""
                row1.show = ""
            }
        } else if (key == AREA_KEY) {
            row.show = Global.zoneIDToName(selected.toInt())
        }

        val sectionIdx: Int = getOneSectionIdxFromRowKey(key)
        oneSectionAdapter.notifyItemChanged(sectionIdx)

//        val item = getFormItemFromKey(key)
//        if (item != null && selected.isNotEmpty()) {
//            if (item.value != selected) {
//                item.reset()
//            }
//            if (key == AREA_KEY) {
//                val item1: AreaFormItem = item as AreaFormItem
//                val cityItem = getFormItemFromKey(CITY_KEY)
//                item1.city_id = cityItem!!.value!!.toInt()
//            } else if (key == CITY_KEY) {
//                val item1 = getFormItemFromKey(AREA_KEY)
//                if (old_selected_city != selected) {
//                    if (item1 != null) {
//                        item1.reset()
//                    }
//                    old_selected_city = selected
//                }
//            }
//            item.value = selected
//            item.make()
//        }
        //notifyChanged(true)
    }

//    override fun textFieldTextChanged(formItem: FormItem, text: String) {
//        val item = getFormItemFromKey(key)
//        if (item != null) {
        //move to adapter
//        formItem!!.value = text
//        formItem!!.make()
//        }
        //println(text)
//    }

//    override fun sexChanged(sex: String) {
        //move to adapter
//        val forItem = getFormItemFromKey(SEX_KEY)
//        if (forItem != null) {
//            forItem.value = sex
//        }
        //println(sex)

//    }

//    override fun tagChecked(checked: Boolean, name: String, key: String, value: String) {}
//
//    override fun stepperValueChanged(number: Int, name: String) {}
//
//    override fun privateChanged(checked: Boolean) {
        //move to adapter
//        val formItem = getFormItemFromKey(PRIVACY_KEY)
//        if (formItem != null) {
//            if (checked) {
//                formItem.value = "1"
//            } else {
//                formItem.value = null
//            }
//        }
//        if (!checked) {
//            warning("必須同意隱私權條款，才能完成註冊")
//        }
//        //println(checked)
//    }
}

class RegisterResTable {
    var success: Boolean = false
    var errors: ArrayList<String> = arrayListOf()
    var id: Int = 0
    var update: String = ""
    var model: MemberTable? = null
}
