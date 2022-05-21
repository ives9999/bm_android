package com.sportpassword.bm.Controllers

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Adapters.SingleSelectAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.Models.Area
import com.sportpassword.bm.Models.City
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
import com.sportpassword.bm.Views.MoreDialog
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_register.edit_featured_container
import kotlinx.android.synthetic.main.activity_single_select_vc.*
import kotlinx.android.synthetic.main.title_bar.*
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

    var bottom_button_count: Int = 2
    val button_width: Int = 400

//    val SELECT_REQUEST_CODE = 1

    private lateinit var moreDialog: MoreDialog

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
        activity = this
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
        setBottomButtonPadding()

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    //in BaseActivicy
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }

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

        if (member.isLoggedIn) {
            row = OneRow("金鑰", member.token!!, member.token!!, TOKEN_KEY, "textField")
            rows.add(row)
        }

        section = makeSectionRow("個人資料", "data", rows, true)
        oneSections.add(section)

        rows.clear()
        row = OneRow("行動電話", member.mobile!!, member.mobile!!, MOBILE_KEY, "textField", KEYBOARD.numberPad, "0939123456", "", true)
        row.msg = "行動電話沒有填寫"
        rows.add(row)
        row = OneRow("市內電話", member.tel!!, member.tel!!, TEL_KEY, "textField", KEYBOARD.numberPad, "021234567")
        rows.add(row)

        val city: String = (member.city == 0) then { "" } ?: member.city.toString()
        row = OneRow("縣市", city, Global.zoneIDToName(member.city), CITY_KEY, "more", KEYBOARD.default, "", "", true)
        row.msg = "沒有選擇縣市"
        rows.add(row)

        val area: String = (member.area == 0) then { "" } ?: member.area.toString()
        row = OneRow("區域", area, Global.zoneIDToName(member.area), AREA_KEY, "more", KEYBOARD.default, "", "", true)
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

    fun signupButtonPressed(view: View) {

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
                        msg = ""
                        for (error in table.errors) {
                            msg += error + "\n"
                        }
                        warning(msg)
                    } else {
                        if (table.model != null) {
                            val memberTable: MemberTable = table.model!!
                            memberTable.toSession(this, true)
                            info(msg, "", "關閉") {
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                                //prev()
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
            //toSelectCity(row.value, this)
            toMoreDialog(row.key, row.value)
        } else if (row.key == AREA_KEY) {
            val row1: OneRow = getOneRowFromKey(CITY_KEY)
            if (row1.value.isEmpty()) {
                warning("請先選擇縣市")
            } else {
                //toSelectArea(row.value, row.value.toInt(), this)

                toMoreDialog(row.key, row.value)
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

    override fun cellClick(idx: Int) {
        //super.cellClick(idx)
        val moreKey: String? = moreDialog.key
        if (moreKey != null) {
            val row: OneRow = getOneRowFromKey(moreKey)
            if (moreKey == CITY_KEY) {
                if (citys == null || citys.size == 0) {
                    citys = Global.getCitys()
                }
                val city = citys[idx]
                row.value = city.id.toString()
                row.show = city.name
                //row.show = Global.zoneIDToName(selected.toInt())
                if (city.id.toString() != old_selected_city) {
                    val row1: OneRow = getOneRowFromKey(AREA_KEY)
                    row1.value = ""
                    row1.show = ""
                }
            } else if (moreKey == AREA_KEY) {
                val row1: OneRow = getOneRowFromKey(CITY_KEY)
                val city_id: Int = (row1.value.isInt()) then { row1.value.toInt() } ?: 0
                val areas: ArrayList<Area> = Global.getAreasByCityID(city_id)
                row.value = areas[idx].id.toString()
                row.show = areas[idx].name
            }
            val sectionIdx: Int = getOneSectionIdxFromRowKey(moreKey)
            oneSectionAdapter.notifyItemChanged(sectionIdx)
        }
        moreDialog.dismiss()
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
    }

    private fun toMoreDialog(key: String, selected: String) {

        moreDialog = MoreDialog(this, screenWidth, key)
        moreDialog.setContentView(R.layout.activity_single_select_vc)

        moreDialog.setBottomButtonPadding(1, button_width)

        if (key == AREA_KEY) {
            val row1: OneRow = getOneRowFromKey(CITY_KEY)
            val city_id_str: String = row1.value
            val city_id: Int = ((city_id_str.isInt()) then { city_id_str.toInt() }) ?: 0
            moreDialog.city_id = city_id
        }
        moreDialog.setSingleSelect(selected, this)

        moreDialog.show(30)
    }
}

class RegisterResTable {
    var success: Boolean = false
    var errors: ArrayList<String> = arrayListOf()
    var id: Int = 0
    var update: String = ""
    var model: MemberTable? = null
}
