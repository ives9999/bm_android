package com.sportpassword.bm.Controllers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.internal.LinkedTreeMap
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.App
import com.sportpassword.bm.Data.*
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.*
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ImagePicker
import com.sportpassword.bm.Views.SearchPanel
import com.sportpassword.bm.member
import org.jetbrains.anko.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Method
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class BaseActivity : AppCompatActivity(), View.OnFocusChangeListener,
    SingleSelectDelegate, ToInterface, ImagePicker, List1CellDelegate {

    //override val ACTION_PHOTO_REQUEST_CODE = 200
    var able_type: String = "coach"
    override var activity = this
    var air_condition: Boolean = false
    var areas: ArrayList<Area> = arrayListOf()
    var arenas: ArrayList<ArenaTable> = arrayListOf()
    var bathroom: Boolean = false
    val body_css = "<style>body{background-color:#000;padding-left:8px;padding-right:8px;margin-top:0;padding-top:0;color:#888888;font-size:18px;}a{color:#a6d903;}img{width:380px;}</style>"
    var cartItemCount: Int = 0
    override lateinit var alertView: View
    var citys: ArrayList<City> = arrayListOf()
    var citys_coach: ArrayList<City> = arrayListOf()
    var citys_store: ArrayList<City> = arrayListOf()
    var citys_team: ArrayList<City> = arrayListOf()
    var containerID: String = "constraintLayout"
    override val context = this
    override var currentPhotoPath = ""
    var degrees: ArrayList<DEGREE> = arrayListOf()
    var delegate: BaseActivity? = null
    var density: Float = 0f
    var dataService: DataService = DataService()
    // ImagePicker Interface property
    open var editCourseResult: ActivityResultLauncher<Intent>? = null
    override var filePath: String = ""
    override var file: File? = null
    override var fileUri: Uri = Uri.EMPTY
    override lateinit var imagePickerLayer: androidx.appcompat.app.AlertDialog
    override lateinit var imageView: ImageView
    var isAddIconShow: Boolean = false
    var isPrevIconShow: Boolean = false
    var isSearchIconShow: Boolean = false
    var keyword: String = ""

    //control the cart item number and show or not show for cart icon
    //for layer
    var layerBlackView: RelativeLayout? = null
    var layerBtnCount: Int = 2
    lateinit var layerButtonLayout: LinearLayout
    var layerCancelBtn: Button? = null
    var layerDeleteBtn: Button? = null
    var layerMask: LinearLayout? = null

    var layerRightLeftPadding: Int = 80
    var layerSubmitBtn: Button? = null
    var layerTopPadding: Int = 100
    var layerVisibility: Boolean = false


    val LOGIN_REQUEST_CODE = 1
    override var mainDelegate: BaseActivity
        get() = this
        set(value) {}

//    override var launcherDelegate: BaseActivity
//        get() = this
//        set(value) {}
//
//    override var launcher: ActivityResultLauncher<Intent>
//        get() =
//        set(value) {}
    var mobile: String = ""
    var msg: String = ""
    var myColorGreen: Int = 0
    //for one
    lateinit var oneSectionAdapter: OneSectionAdapter
    var oneSections: ArrayList<OneSection> = arrayListOf()
    var params: HashMap<String, String> = hashMapOf()
    var parking: Boolean = false
    var refreshLayout: SwipeRefreshLayout? = null
    protected lateinit var refreshListener: SwipeRefreshLayout.OnRefreshListener

    val REQUEST_PHONE_CALL = 100
    val REGISTER_REQUEST_CODE = 2
    val SEARCH_REQUEST_CODE = 4
    val SEARCH_REQUEST_CODE1 = 5
    val REQUEST_CAMERA = 200

    var screenHeight: Int = 0
    var searchPanel: SearchPanel = SearchPanel()
    val session: SharedPreferences = App.ctx!!.getSharedPreferences(SESSION_FILENAME, 0)
    var screenWidth: Int = 0
    var tableLists: ArrayList<Table> = arrayListOf()
    var times: HashMap<String, Any> = hashMapOf()
    val VALIDATE_REQUEST_CODE = 3
    protected lateinit var scrollerListenr: RecyclerView.OnScrollListener

    var weekdays: ArrayList<Int> = arrayListOf()

    //for tableView
    var jsonString: String? = null

    protected var theFirstTime: Boolean = true
    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0

    val rowHeight: Int = 200
    var blackViewHeight: Int = 500
    val blackViewPaddingLeft: Int = 80
    var blackView: RelativeLayout? = null
    var layerTableView: RecyclerView? = null

    //loading
    protected var loading: Boolean = false
    lateinit var loadingAnimation: LoadingAnimation



    //for search
//    lateinit var searchSectionAdapter: SearchSectionAdapter
//    var searchSections: ArrayList<SearchSection> = arrayListOf()

//    lateinit var searchAdapter: GroupAdapter<GroupieViewHolder>



    //var layerScrollView: ScrollView? = null
    //var layerContainerView: LinearLayout? = null
//    var prevIcon: ImageButton? = null
//    var searchIcon: ImageButton? = null
//    var cartIcon: ImageButton? = null

    //var vcResult: VCResult = VCResult()

    private fun _addBlackList(reason: String, memberToken: String, teamToken: String) {
        loadingAnimation.start()
        val token = member.token
        if (token != null) {
            TeamService.addBlackList(this, teamToken, memberToken, token, reason) { success ->
                runOnUiThread {
                    loadingAnimation.stop()
                }
                if (success) {
                    info("加入黑名單成功")
                } else {
                    warning(TeamService.msg)
                }
            }
        }
    }

//    protected fun _getPlayerID(): String {
//        var playerID = ""
//        val deviceState = OneSignal.getDeviceState()
//        if (deviceState != null) {
//            playerID = deviceState.userId
//        }
//
//        return playerID
//    }

    protected fun _getTeamManagerList(completion: CompletionHandler) {
        val filter1: Array<Any> = arrayOf("channel", "=", CHANNEL)
        val filter2: Array<Any> = arrayOf("manager_id", "=", member.id)
        val filter: Array<Array<Any>> = arrayOf(filter1, filter2)
//        TeamService.getList(this, "team", "name", hashMapOf(), 1, 100, filter) { success ->
//            Loading.hide(mask)
//            if (success) {
//                //superDataLists = TeamService.superDataLists
//                completion(true)
//            } else {
//                msg = TeamService.msg
//                completion(false)
//            }
//        }
    }

    protected fun _hideKeyboard(view: View) {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

//    private fun _setURLConstants() {
//        BASE_URL = if (gSimulate) LOCALHOST_BASE_URL else REMOTE_BASE_URL
//        //println("os: " + BASE_URL)
//
//        URL_HOME = BASE_URL + "/app/"
//
//        URL_AREA_BY_CITY_IDS = URL_HOME + "area_by_citys"
//        URL_ARENA_BY_CITY_ID = URL_HOME + "arena_by_city"
//        URL_ARENA_BY_CITY_IDS = URL_HOME + "arena_by_citys"
//        URL_ARENA_LIKE = URL_HOME + "arena/like/%s"
//        URL_ARENA_LIST = URL_HOME + "arena/list"
//        URL_CANCEL_SIGNUP = URL_HOME + "%s/cancelSignup/%d"
//        URL_CART_DELETE = URL_HOME + "cart/delete"
//        URL_CART_LIST = URL_HOME + "cart/list"
//        URL_CART_UPDATE = "${URL_HOME}cart/update"
//        URL_CHANGE_PASSWORD = "${URL_HOME}member/change_password"
//        URL_CITYS = URL_HOME + "citys"
//        URL_COACH_LIKE = URL_HOME + "coach/like/%s"
//        URL_COACH_LIST = URL_HOME + "coach/list"
//        URL_COURSE_DELETE = URL_HOME + "course/delete"
//        URL_COURSE_LIKE = URL_HOME + "course/like/%s"
//        URL_COURSE_LIST = URL_HOME + "course/list"
//        URL_COURSE_UPDATE = URL_HOME + "course/update"
//        URL_DELETE = URL_HOME + "%s/delete"
//        URL_ECPAY2_C2C_MAP = URL_HOME + "order/ecpay2_c2c_map"
//        URL_EMAIL_VALIDATE = URL_HOME + "member/email_validate"
//        URL_FB_LOGIN = URL_HOME + "member/fb"
//        URL_FORGETPASSWORD = "${URL_HOME}member/forget_password"
//        URL_ISNAMEEXIST = "${URL_HOME}%s/isNameExist"
//        URL_LIST = "${URL_HOME}%s"
//        URL_LOGIN = URL_HOME + "login"
//        URL_MANAGER_SIGNUPLIST = "${URL_HOME}%s/manager_signup_list"
//        URL_MEMBER_COINLIST = "${URL_HOME}member/coinlist"
//        URL_MEMBER_COINRETURN = "${URL_HOME}member/coinReturn"
//        URL_MEMBER_DELETE = URL_HOME + "member/delete"
//        URL_MEMBER_LIKELIST = "${URL_HOME}member/likelist"
//        URL_MEMBER_BANK = URL_HOME + "member/bank"
//        URL_MEMBER_BLACKLIST = URL_HOME + "member/blacklist"
//        URL_MEMBER_GETONE = URL_HOME + "member/getOne"
//        URL_MEMBER_SIGNUPLIST = "${URL_HOME}member/signup_calendar"
//        URL_MEMBER_SUBSCRIPTION = "${URL_HOME}member/subscription"
//        URL_MEMBER_SUBSCRIPTION_KIND = "${URL_HOME}member/subscriptionKind"
//        URL_MEMBER_SUBSCRIPTION_LOG = "${URL_HOME}member/subscriptionLog"
//        URL_MEMBER_UNSUBSCRIPTION = "${URL_HOME}member/unSubscription"
//        URL_MEMBER_TEAM_DELETE = "${URL_HOME}member/deleteMemberTeam"
//        URL_MEMBER_TEAM_LIST = "${URL_HOME}member/teamlist"
//        URL_MEMBER_UPDATE = URL_HOME + "member/update"
//        URL_MOBILE_VALIDATE = URL_HOME + "member/mobile_validate"
//        URL_ONE = "${URL_HOME}%s/one"
//        URL_ORDER = "${URL_HOME}order/payment%s"
//        URL_ORDER_LIST = URL_HOME + "order/list"
//        URL_ORDER_RETURN = URL_HOME + "order/ezship_return_code"
//        URL_ORDER_UPDATE = "${URL_HOME}order/update"
//        URL_PRODUCT_LIKE = URL_HOME + "product/like/%s"
//        URL_PRODUCT_LIST = URL_HOME + "product/list"
//        URL_REGISTER = URL_HOME + "register"
//        URL_REQUEST_MANAGER = URL_HOME + "request_manager/update"
//        URL_SEND_EMAIL_VALIDATE = URL_HOME + "member/sendEmailValidate"
//        URL_SEND_MOBILE_VALIDATE = URL_HOME + "member/sendMobileValidate"
//        URL_SHOW = "${URL_HOME}%s/show/%s?device=app"
//        URL_SIGNUP = URL_HOME + "%s/signup/%s"
//        URL_SIGNUP_DATE = "${URL_HOME}%s/signup_date/%s"
//        URL_SIGNUP_LIST = "${URL_HOME}%s/signup_list"
//        URL_STORE_LIKE = URL_HOME + "store/like/%s"
//        URL_STORE_LIST = URL_HOME + "store/list"
//        URL_TEACH_LIKE = URL_HOME + "teach/like/%s"
//        URL_TEACH_LIST = URL_HOME + "teach/list"
//        URL_TEAM = URL_HOME + "team/"
//        URL_TEAM_DELETE = URL_HOME + "team/delete"
//        URL_TEAM_LIKE = URL_HOME + "team/like/%s"
//        URL_TEAM_LIST = URL_HOME + "team/list"
//        URL_TEAM_MEMBER_ADD = URL_HOME + "team/addTeamMember"
//        URL_TEAM_MEMBER_DELETE = URL_HOME + "team/deleteTeamMember"
//        URL_TEAM_MEMBER_LEAVE = URL_HOME + "team/leave"
//        URL_TEAM_MEMBER_LIST = URL_HOME + "team/teamMemberList"
//        URL_TEAM_TEMP_PLAY = URL_TEAM + "tempPlay/onoff"
//        URL_TEAM_TEMP_PLAY_ADD = URL_TEAM + "tempPlay/add"
//        URL_TEAM_TEMP_PLAY_LIST = URL_TEAM + "tempPlay/list"
//        URL_TEAM_TEMP_PLAY_BLACKLIST = URL_TEAM + "tempPlay/blacklist"
//        URL_TEAM_PLUSONE = BASE_URL + "/team/tempPlay/plusOne/"
//        URL_TEAM_CANCELPLUSONE = BASE_URL + "/team/tempPlay/cancelPlusOne/"
//        URL_TEAM_TEMP_PLAY_DATE = URL_TEAM + "tempPlay/date"
//        URL_TEAM_TEMP_PLAY_DATE_PLAYER = URL_TEAM + "tempPlay/datePlayer"
//        URL_TEAM_UPDATE = URL_HOME + "team/update"
//        URL_TT = URL_HOME + "%s/tt"
//        URL_TT_DELETE = URL_HOME + "%s/tt/delete"
//        URL_TT_UPDATE = URL_HOME + "%s/tt/update"
//        URL_UPDATE = URL_HOME + "%s/update"
//        URL_MATCH_LIST = URL_HOME + "match/list"
//
//        FEATURED_PATH = BASE_URL + FEATURED_PATH
//    }

    protected fun _showKeyboard(view: View) {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected fun addBlackList(memberName: String, memberToken: String, teamToken: String) {
        warning("是否真的要將球友" + memberName + "設為黑名單\n之後可以解除", "取消", "加入") {
            reasonBox(memberToken, teamToken)
        }

    }

    val addCartVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            val i: Intent? = res.data
            if (i != null) {
                if (i.hasExtra("refresh")) {
                    val refresh = i.getBooleanExtra("refresh", false)
                    if (delegate != null && refresh) {
                        delegate!!.refresh()
                    }
                }
            }
        }
    }

    open fun addPanelBtn() {
        layerCancelBtn = layerButtonLayout.cancelButton(this, 120) {
            findViewById<RelativeLayout>(R.id.mask) ?. let {
                it.unmask()
            }
        }
    }

    open fun addPressed(view: View) {}

    open fun arenaSelected(selected: String, show: String) {}
    ////////// for top bar cart icon pressed ///////////////////////////////////

    fun cartPressed(view: View) {
        toMemberCartList()
    }

    override fun cellClear(sectionIdx: Int, rowIdx: Int) {

        oneSections[sectionIdx].items[rowIdx].value = ""
        oneSections[sectionIdx].items[rowIdx].show = "全部"
        oneSectionAdapter.setOneSection(oneSections)
        oneSectionAdapter.notifyItemChanged(sectionIdx)
    }

    override fun cellClick(sectionIdx: Int, rowIdx: Int) {
        prepare(sectionIdx, rowIdx)
    }

    override fun cellMoreClick(sectionIdx: Int, rowIdx: Int) {

        val row: OneRow = getOneRowFromIdx(sectionIdx, rowIdx)
        val key: String = row.key
        var value: String = row.value
        if (key == DOB_KEY) {
            toSelectDate(key, value, this)
        } else if (key == CITY_KEY) {
            toSelectCity(value, this)
        } else if (key == AREA_KEY) {
            val row1: OneRow = getOneRowFromKey(CITY_KEY)
            if (row1.value.isEmpty()) {
                warning("請先選擇縣市")
            } else {
                toSelectArea(value, row1.value.toInt(), this)
            }
        } else if (key == ARENA_KEY) {
            val row1: OneRow = getOneRowFromKey(CITY_KEY)
            if (row1.value.isEmpty()) {
                warning("請先選擇縣市")
            } else {
                toSelectArena(value, row1.value.toInt(), this)
            }
        } else if (key == PRICE_UNIT_KEY) {
            toSelectSingle(SelectPriceUnitVC::class.java, key, value, this, able_type)
        } else if (key == COURSE_KIND_KEY) {
            toSelectSingle(SelectCourseKindVC::class.java, key, value, this, able_type)
        } else if (row.key == CYCLE_UNIT_KEY) {
            toSelectSingle(SelectCycleUnitVC::class.java, key, value, this, able_type)
        } else if (key == WEEKDAY_KEY) {
//                val tmp = formItem.sender as ArrayList<String>
//                val selecteds: String = tmp.joinToString(",")
            toSelectWeekday(value, this, able_type)
        } else if (key == WEEKDAYS_KEY) {
            val n: Int = ((value.isInt()) then { value.toInt() }) ?: 0
            toSelectWeekdays(n, this, able_type)
        } else if (key == START_TIME_KEY || key == END_TIME_KEY || key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
//                val tmp = formItem.sender as HashMap<String, String>
//                val selected = tmp.get("time")!!
            toSelectSingle(SelectTimeVC::class.java, key, value, this, able_type)
        } else if (key == START_DATE_KEY || key == END_DATE_KEY || key == TEAM_TEMP_DATE_KEY) {
            toSelectDate(key, value, this)
        } else if (key == TEAM_TEMP_CONTENT_KEY || key == CONTENT_KEY || key == CHARGE_KEY) {
            toEditContent(key, row.title, value, this)
        } else if (key == MANAGER_ID_KEY) {
            if (value.isEmpty()) {
                value = "0"
            }

            val res: Int = value.toIntOrNull() ?: 0

            toSelectManager(res, row.token, able_type, this)
        } else if (key == DEGREE_KEY) {
            toSelectDegree(value, null, able_type)
        }
    }

    open fun cellCourse(row: Table) {}

    override fun cellDelete(row: Table) {}

    override fun cellPrompt(sectionIdx: Int, rowIdx: Int) {

        val row: OneRow = getOneRowFromIdx(sectionIdx, rowIdx)
        if (row.prompt.isNotEmpty()) {
            info(row.prompt)
        }
    }

    override fun cellRefresh() {
        params.clear()
        tableLists.clear()
        refresh()
    }

    override fun cellSwitchChanged(sectionIdx: Int, rowIdx: Int, b: Boolean) {

        val value: String = (b then { "1" }) ?: "0"
        oneSections[sectionIdx].items[rowIdx].value = value
    }

    override fun cellTextChanged(sectionIdx: Int, rowIdx: Int, str: String) {

        oneSections[sectionIdx].items[rowIdx].value = str
    }

    open fun closeRefresh() {
        refreshLayout?.isRefreshing = false
    }

    protected open fun setTeamData(imageView: ImageView? = null) {}

    open fun contentEdit(key: String, content: String) {
        val row = getOneRowFromKey(key)
        row.value = content
        row.show = content
    }

    open fun degreeSelected(selected: String, show: String) {
        val key: String = DEGREE_KEY
        val row = getOneRowFromKey(key)
        val idx = getOneSectionIdxFromRowKey(key)

        row.value = selected
        row.show = show
        oneSectionAdapter.notifyItemChanged(idx)
    }

    val editContentVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->

        if (res.resultCode == Activity.RESULT_OK) {

            val i: Intent? = res.data
            if (i != null) {
                var key: String = ""
                if (i.hasExtra("key")) {
                    key = i.getStringExtra("key")!!
                }

                var content: String = ""
                if (i.hasExtra("content")) {
                    content = i.getStringExtra("content")!!
                }

                if (delegate != null) {
                    delegate!!.contentEdit(key, content)
                }
            }
        }
    }

    val editCourseVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->

        if (res.resultCode == Activity.RESULT_OK) {

            val i: Intent? = res.data
            if (i != null) {
                if (delegate != null) {
                    delegate!!.refresh()
                }
            }
        }
    }

    val editTeamVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->

        if (res.resultCode == Activity.RESULT_OK) {

            val i: Intent? = res.data
            if (i != null) {
                if (delegate != null) {
                    delegate!!.refresh()
                }
            }
        }
    }

    private fun getAllChildrenBFS(v: View): List<View> {
        val visited: ArrayList<View> = arrayListOf()
        val unvisited: ArrayList<View> = arrayListOf()
        unvisited.add(v)

        while (!unvisited.isEmpty()) {
            val child = unvisited.removeAt(0)
            visited.add(child)
            if (child !is ViewGroup) continue
            val group = child as ViewGroup
            val childCount = group.childCount
            for (i in 0..childCount-1) unvisited.add(group.getChildAt(i))
        }

        return visited
    }

    fun getAreasFromCity(city_id: Int, complete: (rows: ArrayList<HashMap<String, String>>) -> Unit): ArrayList<HashMap<String, String>> {
        val rows: ArrayList<HashMap<String, String>> = session.getAreasFromCity(city_id)
        if (rows.count() == 0) {
            loadingAnimation.start()
            val city_ids: ArrayList<Int> = arrayListOf(city_id)
            dataService.getAreaByCityIDs(this, city_ids, "all") { success ->
                if (success) {
                    val allAreas = session.getAllAreas()
                    val areas = dataService.citysandareas
                    //println(areas)
                    for ((key, value) in areas) {
                        if (value.containsKey("rows")) {
                            val _rows = value["rows"] as ArrayList<*>
                            val row: HashMap<String, String> = hashMapOf()
                            for (_row in _rows) {
                                val tmps = _row as LinkedTreeMap<*, *>
                                for ((key1, value1) in tmps) {
                                    val _key1 = key1.toString()
                                    val _value1 = value1.toString()
                                    row[_key1] = _value1
                                }
                                rows.add(row)
                            }
                        }
                        allAreas[city_id.toString()] = value
                    }

                    session.edit().putString("areas", allAreas.toString()).apply()

                    complete(rows)
                }
            }

            runOnUiThread {
                loadingAnimation.stop()
            }
        }

        return rows
    }

    fun getCitys(complete: (rows: ArrayList<HashMap<String, String>>)-> Unit): ArrayList<HashMap<String, String>> {
        var rows: ArrayList<HashMap<String, String>> = session.getAllCitys()
        if (rows.count() == 0) {
            loadingAnimation.start()
            dataService.getCitys(this, "all", false) { success ->
                if (success) {
                    val citys = dataService.citys
                    val arr: JSONArray = JSONArray()
                    rows = arrayListOf()
                    for (city in citys) {
                        val name = city.name
                        val id = city.id.toString()
                        rows.add(hashMapOf("title" to name, "value" to id))
                        val obj = JSONObject()
                        obj.put("name", name)
                        obj.put("id", id)
                        arr.put(obj)
                    }

                    if (arr.length() > 0) {
                        session.edit().putString("citys", arr.toString()).apply()
                    }
                    complete(rows)
                }
            }

            runOnUiThread {
                loadingAnimation.stop()
            }
        }
        return rows
    }

    //open fun getDataFromServer(page: Int) {}

    fun getMyParent(): ViewGroup {
        return window.decorView.rootView as ViewGroup
    }

    fun getOneRowFromKey(key: String): OneRow {

        for (section in oneSections) {
            for (row in section.items) {
                if (row.key == key) {
                    return row
                }
            }
        }
        return OneRow()
    }

    fun getOneRowFromIdx(sectionIdx: Int, rowIdx: Int): OneRow {
        return oneSections[sectionIdx].items[rowIdx]
    }

    fun getOneSectionFromIdx(sectionIdx: Int): OneSection {
        return oneSections[sectionIdx]
    }

    fun getOneSectionFromKey(sectionKey: String): OneSection {

        for (section in oneSections) {
            if (section.key == sectionKey) {
                return section
            }
        }

        return OneSection()
    }

    fun getOneSectionIdxFromRowKey(key: String): Int {
        for ((idx, section) in oneSections.withIndex()) {
            for (row in section.items) {
                if (row.key == key) {
                    return idx
                }
            }
        }
        return 0
    }

    protected fun getRootView(): ViewGroup {

        return window.decorView.findViewById<ViewGroup>(android.R.id.content)
    }

    private fun getScreenHeight() {
        val displayMetrics = resources.displayMetrics
        density = displayMetrics.density
        screenHeight = displayMetrics.heightPixels
    }

    private fun getScreenWidth() {
        val displayMetrics = resources.displayMetrics
        density = displayMetrics.density
        //println("density: " + density)
        //val width: Float = displayMetrics.widthPixels / density
        screenWidth = displayMetrics.widthPixels
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    protected fun hidekeyboard(parent: View) {
        val allV = getAllChildrenBFS(parent)
        for (i in 0..allV.size-1) {
            val v = allV.get(i)
            v.setOnFocusChangeListener(this)
        }
    }

    fun home(context: Context) {
        val intent : Intent = Intent(context, MemberVC::class.java)
        startActivity(intent)
    }

    fun info(msg: String) {
        Alert.show(this, "訊息", msg)
    }

    fun info(msg: String, closeButtonTitle: String, buttonTitle: String, buttonAction: () -> Unit) {
        Alert.show(this, "訊息", msg, closeButtonTitle, buttonTitle, buttonAction)
    }

    fun info(msg: String, buttonTitle: String, buttonAction: () -> Unit) {
        Alert.show(this, "訊息", msg, "", buttonTitle, buttonAction)
    }

    open fun init() {

        var prevIcon: ImageButton? = null
        findViewById<ImageButton>(R.id.prevIcon)?.let {
            prevIcon = it
        }

        var searchIcon: ImageButton? = null
        findViewById<ImageButton>(R.id.searchIcon)?.let {
            searchIcon = it
        }

        var cartIcon: ImageButton? = null
        findViewById<ImageButton>(R.id.cartIcon)?.let {
            cartIcon = it
        }

        var addIcon: ImageButton? = null
        findViewById<ImageButton>(R.id.addIcon)?.let {
            addIcon = it
        }

        prevIcon?.visibility = (isPrevIconShow then { View.VISIBLE }) ?: View.GONE

        searchIcon?.visibility = (isSearchIconShow then { View.VISIBLE }) ?: View.GONE
        addIcon?.visibility = (isAddIconShow then { View.VISIBLE }) ?: View.GONE

        //當購物車中有商品時，購物車的icon就會出現，如果沒有就不會出現
        //1.AddCartVC中，商品加入購物車時，+1
        //2.MemberCartListVC中，移除購物車中的商品時，-1
        //3.購物車轉成訂單時OrderVC，購物車中的商品數變0
        cartItemCount = session.getInt("cartItemCount", 0)
        cartIcon?.visibility = ((member.isLoggedIn && cartItemCount > 0) then { View.VISIBLE }) ?: View.GONE

//        searchSectionAdapter = SearchSectionAdapter(this, R.layout.cell_section, this)
//        searchSections = initSectionRows()
        oneSectionAdapter.setOneSection(oneSections)
    }

    open fun initSectionRows(): ArrayList<OneSection> {

        val sections: ArrayList<OneSection> = arrayListOf()

        sections.add(makeSection0Row())

        return sections
    }

    val loginVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {

                    if (delegate != null) {
                        val d: MemberVC = delegate as MemberVC
                        //d.loginout()
                    }
                }
            }
        }
    }

    private fun makeCallRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
    }

    open fun makeSection0Row(isExpanded: Boolean=true): OneSection {
        val s: OneSection = OneSection("一般", "general", isExpanded)
        return s
    }

    protected fun mask() {
        if (layerMask == null) {
            layerMask = LinearLayout(this)
            layerMask!!.id = R.id.MyMask
            layerMask!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            //mask.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            layerMask!!.backgroundColor = Color.parseColor("#888888")
            //0是完全透明
            layerMask!!.alpha = 0.9f
            layerMask!!.setOnClickListener {
                unmask()
            }
            val parent = getMyParent()
            parent.addView(layerMask)
        } else {
            val v = layerMask!!.visibility
            layerMask!!.visibility = View.VISIBLE
        }
    }

    val memberSubscriptionKindVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->

        if (res.resultCode == Activity.RESULT_OK) {

            val i: Intent? = res.data
            if (i != null) {
                if (delegate != null) {
                    delegate!!.refresh()
                }
            }
        }
    }

    val memberSubscriptionLogVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->

        if (res.resultCode == Activity.RESULT_OK) {

            val i: Intent? = res.data
            if (i != null) {
                if (delegate != null) {
                    delegate!!.refresh()
                }
            }
        }
    }

    val memberSubscriptionPayVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->

        if (res.resultCode == Activity.RESULT_OK) {

            val i: Intent? = res.data
            if (i != null) {
                if (delegate != null) {
                    delegate!!.refresh()
                }
            }
        }
    }

    protected fun myMakeCall(_mobile: String) {
        mobile = _mobile
        val p = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (p != PackageManager.PERMISSION_GRANTED) {
            makeCallRequest()
        } else {
            makeCall(mobile)
        }
    }

    //for tag delegate
//    open fun textFieldTextChanged(sectionKey: String, rowKey: String, value: String){}
//    open fun setTag(sectionKey: String, rowKey: String, attribute: String, selected: Boolean){}
//    open fun stepperValueChanged(sectionKey: String, rowKey: String, number: Int){}

//    open fun radioDidChange(sectionKey: String, rows: ArrayList<HashMap<String, String>>){}
//    open fun moreClick(sectionKey: String, rowKey: String){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ConnectTask(this).execute()
        val btn = findViewById<Button>(R.id.submit_btn)
        if (btn != null) {
            btn.setOnClickListener { searchSubmit() }
        }

        getScreenWidth()
        getScreenHeight()

        oneSectionAdapter = OneSectionAdapter(this, R.layout.cell_section, this, hashMapOf())
        oneSections = initSectionRows()

        myColorGreen = ContextCompat.getColor(context, R.color.MY_GREEN)
        delegate = this

        loadingAnimation = LoadingAnimation(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("APNs device token: ${task.result}")
            }
        }

        //MyOneSignal.clear()

        //vcResult.selectCityResult(this)

        //OneSignal.setSubscription(true)
        //OneSignal.promptLocation() prompt location auth when location auth is close

        //member.reset()
        //member.print()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v is EditText) {
            val editText = v!! as EditText
            if (!hasFocus) {
                _hideKeyboard(v)
            } else {
                editText.setSelection(editText.length())
                _showKeyboard(v)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_PHONE_CALL -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    warning("沒有同意app撥打電話的權限，因此無法使用此功能")
                } else {
                    makeCall(this.mobile)
                }
                return
            }
//            REQUEST_CAMERA -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                }
//            }
//            ACTION_PHOTO_REQUEST_CODE -> {
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    warning("沒有同意權限，因此無法使用此功能，請至設定功能中開啟權限，方能使用此功能")
//                } else {
//                    info("同意權已經取得")
//                    finish()
//                }
//            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        prev()
        return true
    }

    private fun openPermissionSettings() {
        val i: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        i.data = uri
        startActivity(i)
    }

    val paymentVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            val i: Intent? = res.data
//            if (i != null) {
//                if (delegate != null) {
//                    delegate!!.refresh()
//                }
//            }
        }
    }

//    val scanQRCodeVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
//        if (res.resultCode == Activity.RESULT_OK) {
//            val i: Intent? = res.data
//            i.apply {
//                if (i!!.hasExtra("token")) {
//
//                }
//            }
//        }
//    }

    fun permissionExist(permission: String): Boolean {
        val permission = ContextCompat.checkSelfPermission(this, permission)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    fun permissionsExist(permissions: ArrayList<String>): Boolean {
        var b: Boolean = false
        for (permission in permissions) {
            val permission = ContextCompat.checkSelfPermission(this, permission)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                return false
            } else {
                b = true
            }
        }
        return b
    }

    open fun prepare(idx: Int) {}
    open fun prepare(sectionIdx: Int, rowIdx: Int) {}

    open fun prepareParams(city_type: String = "simple") {

        params.clear()

        for (oneSection in oneSections) {
            for (oneRow in oneSection.items) {
                val key = oneRow.key
                val value = oneRow.value
                params[key] = value
            }
        }
//        val city_ids: ArrayList<Int> = arrayListOf()
//        if (citys.size > 0) {
//            citys.forEach {
//                city_ids.add(it.id)
//            }
//        } else {
//            city_ids.clear()
//        }
//        if (city_ids.size > 0) {
//            params["city_id"] = city_ids
//            params["city_type"] = city_type
//        } else {
//            params.remove("city_id")
//            params.remove("city_type")
//        }
//
//        val area_ids: ArrayList<Int> = arrayListOf()
//        if (areas.size > 0) {
//            areas.forEach  {
//                area_ids.add(it.id)
//            }
//        } else {
//            area_ids.clear()
//        }
//        if (area_ids.size > 0) {
//            params["area_id"] = area_ids
//        } else {
//            params.remove("area_id")
//        }
//
////        if (air_condition) { params["air_condition"] = 1 } else { params["air_condition"] = 0 }
////        if (bathroom) { params["bathroom"] = 1 } else { params["bathroom"] = 0 }
////        if (parking) { params["parking"] = 1 } else { params["parking"] = 0 }
//        if (air_condition) { params["air_condition"] = 1 } else { params.remove("air_condition") }
//        if (bathroom) { params["bathroom"] = 1 } else { params.remove("bathroom") }
//        if (parking) { params["parking"] = 1 } else { params.remove("parking") }
//
//        if (weekdays.size > 0) {
//            params["play_days"] = weekdays
//        } else {
//            params.remove("play_days")
//        }
//
//        if (times.size > 0) {
//            if (times.containsKey("time")) {
//                params["use_date_range"] = 1
//                val play_start = times["time"]!! as String
//                val time = play_start + ":00 - 24:00:00"
//                params["play_time"] = time
//            }
//        } else {
//            params.remove("play_time")
//        }
//
//        var arena_ids: ArrayList<Int> = arrayListOf()
//        if (arenas.size > 0) {
//            arenas.forEach {
//                arena_ids.add(it.id)
//            }
//        } else {
//            arena_ids.clear()
//        }
//        if (arena_ids.size > 0) {
//            params["arena_id"] = arena_ids
//        } else {
//            params.remove("arena_id")
//        }
//
//        var _degrees: ArrayList<String> = arrayListOf()
//        if (degrees.size > 0) {
//            degrees.forEach {
//                _degrees.add(it.toString())
//            }
//        } else {
//            _degrees.clear()
//        }
//        if (_degrees.size > 0) {
//            params["degree"] = _degrees
//        } else {
//            params.remove("degree")
//        }
//        if (keyword.length > 0) {
//            params["k"] = keyword
//        } else {
//            params.remove("k")
//        }
    }

    //for prev image button use
    fun prev(view: View) {
        prev()
    }

    open fun prev() {
        hideKeyboard()
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    private fun reasonBox(memberToken: String, teamToken: String) {
        var reasonTxt: EditText? = null
        alert("請輸入理由") {
            title = "訊息"
            positiveButton("加入"){
                val reason: String = reasonTxt!!.text.toString()
                _addBlackList(reason, memberToken, teamToken)
            }
            negativeButton("取消"){}
            customView {
                reasonTxt = editText()
            }
        }.show()
    }

    open fun refresh() {}

    val registerVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            val i: Intent? = res.data
            if (i != null) {
                if (delegate != null) {
                    delegate!!.refresh()
                }
            }
        }
    }

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher. You can use either a val, as shown in this snippet,
    // or a lateinit var in your onAttach() or onCreate() method.
    val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            //permission is granted. Continue the action or workflow in your app
            if (delegate != null) {
                delegate!!.toSelectDevicePhoto()
            }
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
            openPermissionSettings()
        }
    }

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher. You can use either a val, as shown in this snippet,
    // or a lateinit var in your onAttach() or onCreate() method.
    val requestPhotoPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            //permission is granted. Continue the action or workflow in your app
            if (delegate != null) {
                delegate!!.toSelectDevicePhoto()
            }
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
            openPermissionSettings()
        }
    }

    protected fun removeLayerChildViews() {
        val parent = getMyParent()
//        if (layerContainerView != null) {
//            layerContainerView!!.removeAllViews()
//        }
//
//        if (layerScrollView != null) {
//            layerScrollView!!.removeAllViews()
//        }

        if (layerBlackView != null) {
            layerBlackView!!.removeAllViews()
        }

//        layerContainerView = null

        if (layerMask != null) {
            layerMask!!.visibility = View.GONE
            layerMask!!.removeAllViews()
        }

//        layerScrollView = null
        layerBlackView = null
        parent.removeView(layerMask)
        layerMask = null
    }

    fun requestPermission(permissions: Array<out String>, requestCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    fun resetParams() {
        citys.clear()
        areas.clear()
        arenas.clear()
        weekdays.clear()
        degrees.clear()
        times.clear()
        keyword = ""
    }

    fun searchPressed(view: View) {
        showSearchPanel()
    }

    open fun searchSubmit() {}

//    fun showPrevIcon(visibility: Boolean = true) {
//        if (visibility) { prevIcon?.visibility = View.VISIBLE } else { prevIcon?.visibility = View.INVISIBLE }
//    }
//
//    fun showSearchIcon() {
//        if (isSearchIconShow) { searchIcon?.visibility = View.VISIBLE } else { searchIcon?.visibility = View.INVISIBLE }
//    }
//
//    fun showCartIcon(visibility: Boolean = true) {
//        if (visibility) { cartIcon?.visibility = View.VISIBLE } else { cartIcon?.visibility = View.INVISIBLE }
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.all, menu)
//
//        val menuView = menu!!.findItem(R.id.menu_all).actionView
//        val shoppingCartBtn = menuView.findViewById<ImageButton>(R.id.cart)
//        val searchBtn = menuView.findViewById<ImageButton>(R.id.search)
//
//        //當購物車中有商品時，購物車的icon就會出現，如果沒有就不會出現
//        //1.AddCartVC中，商品加入購物車時，+1
//        //2.MemberCartListVC中，移除購物車中的商品時，-1
//        //3.購物車轉成訂單時OrderVC，購物車中的商品數變0
//        cartItemCount = session.getInt("cartItemCount", 0)
//        if (member.isLoggedIn && cartItemCount > 0) {
//            shoppingCartBtn.visibility = View.VISIBLE
//        } else {
//            shoppingCartBtn.visibility = View.GONE
//        }
//
//        searchBtn.visibility = if (isSearchIconShow) { View.VISIBLE } else { View.GONE }
//
//        return true
//    }

    val selectAreaVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    val key: String = AREA_KEY
                    var selected: String = ""
                    if (i.hasExtra("selected")) {
                        selected = i.getStringExtra("selected")!!
                    }

                    var show: String = ""
                    if (i.hasExtra("show")) {
                        show = i.getStringExtra("show")!!
                    }

                    //activity
                    if (delegate != null) {
                        delegate!!.singleSelected(key, selected)
                    }
//                    else {
//                        //fragment
//                        able_type = "course"
//                        if (i.hasExtra("able_type")) {
//                            able_type = i.getStringExtra("able_type")!!
//                        }
//                        val f = getFragment()
//                        f?.singleSelected(key, selected)
//                    }
                }
            }
        }
    }

    val selectArenaVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    var key: String = ARENA_KEY
                    var selected: String = ""
                    if (i.hasExtra("selected")) {
                        selected = i.getStringExtra("selected")!!
                    }

                    var show: String = ""
                    if (i.hasExtra("show")) {
                        show = i.getStringExtra("show")!!
                    }

                    //activity
                    if (delegate != null) {
                        delegate!!.arenaSelected(selected, show)
                    }
//                    else {
//                        //fragment
//                        able_type = "course"
//                        if (i.hasExtra("able_type")) {
//                            able_type = i.getStringExtra("able_type")!!
//                        }
//                        val f = getFragment()
//                        f?.arenaSelected(selected, show)
//                    }
                }
            }
        }
    }

    val selectCityVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    val key: String = CITY_KEY
                    var selected: String = ""
                    if (i.hasExtra("selected")) {
                        selected = i.getStringExtra("selected")!!
                    }

                    //activity
                    if (delegate != null) {
                        delegate!!.singleSelected(key, selected)
                    }
//                    else {
//                        //fragment
//                        able_type = "course"
//                        if (i.hasExtra("able_type")) {
//                            able_type = i.getStringExtra("able_type")!!
//                        }
//                        val f = getFragment()
//                        f?.singleSelected(key, selected)
//                    }
                }
            }
        }
    }

    val selectDateVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    var key: String = ""
                    if (i.hasExtra("key")) {
                        key = i.getStringExtra("key")!!
                    }

                    var selected: String = ""
                    if (i.hasExtra("selected")) {
                        selected = i.getStringExtra("selected")!!
                        if (key != DOB_KEY && key != START_DATE_KEY && key != END_DATE_KEY && key != TEAM_TEMP_DATE_KEY) {
                            selected += ":00"
                        }
                    }

                    //activity
                    if (delegate != null) {
                        delegate!!.singleSelected(key, selected)
                    }
//                    else {
//                        //fragment
//                        able_type = "course"
//                        if (i.hasExtra("able_type")) {
//                            able_type = i.getStringExtra("able_type")!!
//                        }
//                        val f = getFragment()
//                        f?.singleSelected(key, selected)
//                    }
                }
            }
        }
    }

    val selectDegreeVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    var key: String = DEGREE_KEY
                    var selected: String = ""
                    if (i.hasExtra("selecteds")) {
                        val selecteds = i.getStringArrayListExtra("selecteds")!!
                        selected = selecteds.joinToString(",")
                    }

                    var show: String = ""
                    if (i.hasExtra("show")) {
                        show = i.getStringExtra("show")!!
                    }

                    //activity
                    if (delegate != null) {
                        delegate!!.degreeSelected(selected, show)
                    }
//                    else {
//                        //fragment
//                        able_type = "course"
//                        if (i.hasExtra("able_type")) {
//                            able_type = i.getStringExtra("able_type")!!
//                        }
//                        val f = getFragment()
//                        f?.degreeSelected(selected, show)
//                    }
                }
            }
        }
    }

    val selectDeviceCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->

        if (res.resultCode == Activity.RESULT_OK) {

            dealCamera()
        }
    }

    val selectDevicePhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->

        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data
                if (i != null) {
                    dealPhoto(res.data)
                }
            }
        }
    }

    fun selectedManager(selected: Int, show: String, token: String) {
        val key: String = MANAGER_ID_KEY
        val row = getOneRowFromKey(key)
        row.value = selected.toString()
        row.show = show
        row.token = token

        val idx: Int = getOneSectionIdxFromRowKey(key)
        oneSectionAdapter.notifyItemChanged(idx)
    }

    val selectManager = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->

        if (res.resultCode == Activity.RESULT_OK) {
            if (res.data != null) {
                val i: Intent? = res.data
                if (i != null) {

                    var manager_id: Int = 0
                    if (i.hasExtra("manager_id")) {
                        manager_id = i.getIntExtra("manager_id", 0)
                    }

                    var manager_token: String = ""
                    if (i.hasExtra("manager_token")) {
                        manager_token = i.getStringExtra("manager_token")!!
                    }

                    var manager_nickname: String = ""
                    if (i.hasExtra("manager_nickname")) {
                        manager_nickname = i.getStringExtra("manager_nickname")!!
                    }

                    var able_type: String = ""
                    if (i.hasExtra("able_type")) {
                        able_type = i.getStringExtra("able_type")!!
                    }

                    if (delegate != null) {
                        delegate!!.selectedManager(manager_id, manager_nickname, manager_token)
                    }
                }
            }
        }
    }

    val selectSingleVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    var key: String = ""
                    if (i.hasExtra("key")) {
                        key = i.getStringExtra("key")!!
                    }

                    var selected: String = ""
                    if (i.hasExtra("selected")) {
                        selected = i.getStringExtra("selected")!!
                    }

                    //activity
                    if (delegate != null) {
                        delegate!!.singleSelected(key, selected)
                    }
//                    else {
//                        //fragment
//                        able_type = "course"
//                        if (i.hasExtra("able_type")) {
//                            able_type = i.getStringExtra("able_type")!!
//                        }
//                        val f = getFragment()
//                        f?.singleSelected(key, selected)
//                    }
                }
            }
        }
    }

    val selectTimeVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    var key: String = ""
                    if (i.hasExtra("key")) {
                        key = i.getStringExtra("key")!!
                    }

                    var selected: String = ""
                    if (i.hasExtra("selected")) {
                        selected = i.getStringExtra("selected")!! + ":00"
                    }

                    //activity
                    if (delegate != null) {
                        delegate!!.singleSelected(key, selected)
                    }
//                    else {
//                        //fragment
//                        able_type = "course"
//                        if (i.hasExtra("able_type")) {
//                            able_type = i.getStringExtra("able_type")!!
//                        }
//                        val f = getFragment()
//                        f?.singleSelected(key, selected)
//                    }
                }
            }
        }
    }

    val selectWeekdayVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    val key: String = WEEKDAY_KEY
                    var selected: String = ""
                    if (i.hasExtra("selected")) {
                        selected = i.getStringExtra("selected")!!
                    }

                    //activity
                    if (delegate != null) {
                        delegate!!.singleSelected(key, selected)
                    }
//                    else {
//                        //fragment
//                        able_type = "course"
//                        if (i.hasExtra("able_type")) {
//                            able_type = i.getStringExtra("able_type")!!
//                        }
//                        val f = getFragment()
//                        f?.singleSelected(key, selected)
//                    }
                }
            }
        }
    }

    val selectWeekdaysVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
//                    var key: String = WEEKDAYS_KEY
                    var selected: Int = 0
                    if (i.hasExtra("selecteds")) {
                        selected = i.getIntExtra("selecteds", 0)
                    }

                    var show: String = ""
                    if (i.hasExtra("show")) {
                        show = i.getStringExtra("show")!!
                    }

                    //activity
                    if (delegate != null) {
                        delegate!!.weekendsSelected(selected, show)
                    }
//                    else {
//                        //fragment
//                        able_type = "course"
//                        if (i.hasExtra("able_type")) {
//                            able_type = i.getStringExtra("able_type")!!
//                        }
//                        val f = getFragment()
//                        f?.weekendSelected(selected, show)
//                    }
                }
            }
        }
    }

    open fun setButtonLayoutHeight(): Int {
        val buttonViewHeight: Int = 180

        return buttonViewHeight
    }

    protected fun setBottomTabFocus() {

        val tabTitleID: Int = resources.getIdentifier(able_type + "TabTitle", "id", packageName)
        val tabLineID: Int = resources.getIdentifier(able_type + "TabLine", "id", packageName)
        val tabIconID: Int = resources.getIdentifier(able_type + "TabIcon", "id", packageName)

        findViewById<TextView>(tabTitleID) ?. let {
            it.textColor = ContextCompat.getColor(context, R.color.MY_GREEN)
        }

        findViewById<LinearLayout>(tabLineID) ?. let {
            it.backgroundColor = myColorGreen
        }

        findViewById<ImageView>(tabIconID) ?. let {
            it.setImage("ic_${able_type}_on_svg")
        }
    }

    fun TextView.setMyText(value: String, default: String = "") {
        if (value.isEmpty()) text = default else text = value
    }

    protected fun setMyTitle(title: String) {
        findViewById<TextView>(R.id.topTitleLbl) ?. let {
            it.text = title
        }
//        val actionBar: ActionBar = supportActionBar!!
//        actionBar.setDisplayShowTitleEnabled(false)
//        actionBar.setDisplayHomeAsUpEnabled(true)
//        actionBar.setHomeAsUpIndicator(R.drawable.prev)
//
//        val l: LinearLayout = LayoutInflater.from(this).inflate(R.layout.title_bar, null) as LinearLayout
//        val titleView: TextView = l.findViewById<TextView>(R.id.myTitle)
//        titleView.setText(title)
//        //println(titleView)
//        val display: Display = windowManager.defaultDisplay
//        val size: Point = Point()
//        display.getSize(size)
//        val actionBarWidth: Int = size.x
//        //println(actionBarWidth)
//
//        titleView.measure(0, 0)
//        val titleViewWidth = titleView.measuredWidth
//        //println(titleViewWidth)
//
//
////        val dimensions: BitmapFactory.Options = BitmapFactory.Options()
////        dimensions.inJustDecodeBounds = true
////        val prev: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.prev, dimensions)
////        val prevWidth: Int = dimensions.outWidth
////        println(prevWidth)
//
//
//        val params: ActionBar.LayoutParams = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
//        params.leftMargin = (actionBarWidth/2) - (titleViewWidth/2) - 170
//
//        actionBar.setCustomView(l, params)
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM)
    }

    protected open fun setRefreshListener() {
        if (refreshLayout != null) {
            refreshListener = SwipeRefreshLayout.OnRefreshListener {
                refresh()
            }
            refreshLayout!!.setOnRefreshListener(refreshListener)
        }
    }

    fun showTableLayer(tableViewHeight: Int) {

        findViewById<RelativeLayout>(R.id.top) ?. let {
            layerMask = it.mask(this)
            layerMask!!.setOnClickListener { it1 ->
                it.unmask()
            }
        }

        val layerButtonLayoutHeight: Int = setButtonLayoutHeight()
        blackViewHeight = tableViewHeight + layerButtonLayoutHeight + 200

        val statusBarHeight: Int = getStatusBarHeight()
//        val appBarHeight: Int = 64
        val frame_width = Resources.getSystem().displayMetrics.widthPixels
        val frame_height = Resources.getSystem().displayMetrics.heightPixels - statusBarHeight - 200
        val width: Int = frame_width - 2*blackViewPaddingLeft
        val topX: Int = (frame_height-blackViewHeight)/2;

        blackView = layerMask!!.blackView(
            this,
            blackViewPaddingLeft,
            topX,
            width,
            blackViewHeight)

        layerTableView = blackView!!.tableView(this, 0, layerButtonLayoutHeight)
        layerButtonLayout = blackView!!.buttonPanel(this, layerButtonLayoutHeight)

        addPanelBtn()
    }

    ////// search panel start //////////////////////////////////////
    open fun showSearchPanel() {

//        if (view.tag != null) {
//            able_type = view.tag as String
//        }
//        val frag = getFragment()
//
//        if (frag != null) {
//            when (frag::class) {
//            "team" -> {
//                containerID = "course_container"
//                val frag = getFragment() as TempPlayFragment
//                searchSections = frag.searchSections
//            }
//                CourseFragment::class -> {
//                    containerID = "course_container"
//                val frag = getFragment() as CourseFragment
//                    frag.showSearchPanel()
        //searchSections = frag.searchSections
//                }
//                ArenaFragment::class -> {
//                    containerID = "course_container"
//                val frag = getFragment() as ArenaFragment
//                    frag.showSearchPanel()
//                searchSections = frag.searchSections
//                }
//            "coach", "teach", "store" -> {
//                containerID = "constraintLayout"
//            }
//            }
//            return
//        }

        //first add a mask
        //val p: ConstraintLayout = getMyParent()
        //val b = findViewById<ViewGroup>(R.id.content)
        //val a = findViewById<RelativeLayout>(R.id.topView).getRootView() as ViewGroup
        val p = window.decorView.rootView as ViewGroup
        oneSectionAdapter.setOneSection(oneSections)
        //searchPanel.mask(this, p)
        searchPanel.addSearchLayer(this, p, able_type, oneSectionAdapter)
        //mask()

        //second add search view in mask
        // addSearchLayer(tag)
    }

    open fun showSignupInfo(position: Int) {}

//    open fun <T: MyViewHolder2<U>, U: Table> showTableView(tableView: MyTable2VC<T, U>, jsonString: String?) {
//
//        if (jsonString != null) {
//            val b: Boolean = tableView.parseJSON(jsonString)
//            if (!b && tableView.msg.isEmpty()) {
//                val rootView: ViewGroup = getRootView()
//                runOnUiThread {
//                    rootView.setInfo(this, "目前暫無資料")
//                }
//            } else {
//                runOnUiThread {
//                    tableView.notifyDataSetChanged()
//                }
//            }
//        }
//    }

    override fun singleSelected(key: String, selected: String) {

        var show: String = ""

        if (key == START_TIME_KEY || key == END_TIME_KEY || key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
            show = selected.noSec()
        } else if (key == CITY_KEY || key == AREA_KEY) {
            show = Global.zoneIDToName(selected.toInt())
        } else if (key == WEEKDAY_KEY || key == WEEKDAYS_KEY) {
            show = WEEKDAY.intToString(selected.toInt())
        } else if (key == PRICE_UNIT_KEY) {
            show = PRICE_UNIT.from(selected).value
        } else if (key == COURSE_KIND_KEY) {
            show = COURSE_KIND.from(selected).value
        } else if (key == CYCLE_UNIT_KEY) {
            show = CYCLE_UNIT.from(selected).value
        } else if (key == START_DATE_KEY || key == END_DATE_KEY || key == TEAM_TEMP_DATE_KEY) {
            show = selected
        }

        var idx: Int = 0
//        val row1 = getSearchRowFromKey(key)
        val row = getOneRowFromKey(key)

        idx = getOneSectionIdxFromRowKey(key)
        row.value = selected
        row.show = show
        oneSectionAdapter.notifyItemChanged(idx)
    }

    fun tabToArena(view: View) {
        if (able_type != "arena") {
            toArena(false)
        }
    }

    fun tabToCourse(view: View) {
        if (able_type != "course") {
            toCourse(false)
        }
    }

    fun tabToMember(view: View) {
        if (able_type != "member") {
            toMember()
        }
    }

    fun tabToMore(view: View) {
        if (able_type != "more") {
            toMore()
        }
    }

    fun tabToTeam(view: View) {
        if (able_type != "team") {
            toSearch()
        }
    }

    open fun teamMemberInfo(idx: Int) {}

    fun unmask() {
        val duration: Long = 500
        if (layerBlackView != null) {
            val parent = getMyParent()
            val h = parent.measuredHeight

            val animate = TranslateAnimation(0f, 0f, layerTopPadding.toFloat(), h.toFloat())
            animate.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                    removeLayerChildViews()
                }

                override fun onAnimationStart(p0: Animation?) {}
            })
            animate.duration = 500
            animate.fillAfter = true
            layerBlackView!!.startAnimation(animate)
            layerVisibility = false
        }
    }

    val updatePasswordVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    if (delegate != null) {
                        val m: MemberVC = delegate as MemberVC
                        member.isLoggedIn = false
                        member.reset()
                        m.logout()
                    }
                    //refresh()
                }
            }
        }
    }

    val validateVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    refresh()

//                    val frags = supportFragmentManager.fragments
//                    for (frag in frags) {
//                        val memberFragment = frag as? MemberFragment
//                        memberFragment?.refresh()
//                    }
                }
            }
        }
    }

    fun warning(msg: String) {
        Alert.show(this, "警告", msg)
    }

    fun warning(msg: String, showCloseButton: Boolean = false, buttonTitle: String, buttonAction: () -> Unit) {
        Alert.show(this, "警告", msg, showCloseButton, buttonTitle, buttonAction)
    }

    fun warning(msg: String, closeButtonTitle: String, buttonTitle: String, buttonAction: () -> Unit) {
        Alert.show(this, "警告", msg, closeButtonTitle, buttonTitle, buttonAction)
    }

    fun warningWithPrev(msg: String) {
        val alert = AlertDialog.Builder(this).create()
        alert.setTitle("警告")
        alert.setMessage(msg)
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "回上一頁") { Interface, j ->
            finish()
        }
        alert.show()
    }


    fun webViewSettings(context: Context, webView: WebView) {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        //settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        //settings.setAppCachePath(cacheDir.path)
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = true
        settings.textZoom = 125
        settings.blockNetworkImage = false
        settings.loadsImagesAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true
        }
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false
        settings.domStorageEnabled = true
//        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true

        webView.fitsSystemWindows = true
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                //toast("Page loading.")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                //toast("Page loaded complete")
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request!!.url.toString()
                //println(url)
                url.website(context)
                return true
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                //println(url)
                url!!.website(context)
                return true
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {

            }
        }
        webView.setBackgroundColor(Color.TRANSPARENT)
    }

    val webViewVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {

                    if (delegate != null) {
                        delegate!!.info("超商付款取貨，已經完成門市設定，我們將盡快出貨！！", "", "關閉") {
                            finishAffinity()
                            toProduct()
                        }
                    }
                }
            }
        }
    }

    open fun weekendsSelected(selected: Int, show: String) {
        val key: String = WEEKDAYS_KEY
        val row = getOneRowFromKey(key)
        val idx = getOneSectionIdxFromRowKey(key)

        row.value = selected.toString()
        row.show = show
        oneSectionAdapter.notifyItemChanged(idx)
    }
}

object SystemProperties {
    private var failedUsingReflection = false
    private var getPropMethod: Method? = null

    @SuppressLint("PrivateApi")
    fun getProp(propName: String, defaultResult: String = ""): String {
        if (!failedUsingReflection) try {
            if (getPropMethod == null) {
                val clazz = Class.forName("android.os.SystemProperties")
                getPropMethod = clazz.getMethod("get", String::class.java, String::class.java)
            }
            return getPropMethod!!.invoke(null, propName, defaultResult) as String? ?: defaultResult
        } catch (e: Exception) {
            getPropMethod = null
            failedUsingReflection = true
        }
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec("getprop \"$propName\" \"$defaultResult\"")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            return reader.readLine()
        } catch (e: IOException) {
        } finally {
            process?.destroy()
        }
        return defaultResult
    }
}














