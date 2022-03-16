package com.sportpassword.bm.Controllers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import com.google.gson.internal.LinkedTreeMap
import com.onesignal.OneSignal
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Adapters.SearchSectionAdapter
import com.sportpassword.bm.App
import com.sportpassword.bm.Data.*
import com.sportpassword.bm.Fragments.*
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.*
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ImagePicker
import com.sportpassword.bm.Views.SearchPanel
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.bottom_view.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.top_view.*
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

    var refreshLayout: SwipeRefreshLayout? = null
    protected lateinit var refreshListener: SwipeRefreshLayout.OnRefreshListener
    protected lateinit var scrollerListenr: RecyclerView.OnScrollListener

    // ImagePicker Interface property
    override val ACTION_PHOTO_REQUEST_CODE = 200
    override val activity = this
    override val context = this
    override lateinit var imagePickerLayer: androidx.appcompat.app.AlertDialog
    override lateinit var alertView: View
    override lateinit var imageView: ImageView
    override var currentPhotoPath = ""
    override var filePath: String = ""
    override var file: File? = null
    override var fileUri: Uri = Uri.EMPTY

    //control the cart item number and show or not show for cart icon
    var cartItemCount: Int = 0
    var isSearchIconShow: Boolean = false
    var isPrevIconShow: Boolean = false
    var isAddIconShow: Boolean = false

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

    var screenWidth: Int = 0
    var screenHeight: Int = 0
    var density: Float = 0f

    var msg: String = ""
//    var superDataLists: ArrayList<SuperData> = arrayListOf()
    //var searchRows: ArrayList<HashMap<String, String>> = arrayListOf()

    val REQUEST_PHONE_CALL = 100
    var mobile: String = ""

    var tableLists: ArrayList<Table> = arrayListOf()

    //for search
//    lateinit var searchSectionAdapter: SearchSectionAdapter
//    var searchSections: ArrayList<SearchSection> = arrayListOf()

    //for one
    lateinit var oneSectionAdapter: OneSectionAdapter
    var oneSections: ArrayList<OneSection> = arrayListOf()


    var containerID: String = "constraintLayout"
    var citys: ArrayList<City> = arrayListOf()
    var citys_coach: ArrayList<City> = arrayListOf()
    var citys_team: ArrayList<City> = arrayListOf()
    var citys_store: ArrayList<City> = arrayListOf()
    var areas: ArrayList<Area> = arrayListOf()
    var air_condition: Boolean = false
    var bathroom: Boolean = false
    var parking: Boolean = false
    var weekdays: ArrayList<Int> = arrayListOf()
    var times: HashMap<String, Any> = hashMapOf()
    var arenas: ArrayList<ArenaTable> = arrayListOf()
    var degrees: ArrayList<DEGREE> = arrayListOf()
    var keyword: String = ""
//    lateinit var searchAdapter: GroupAdapter<GroupieViewHolder>
    var params: HashMap<String, String> = hashMapOf()

    var searchPanel: SearchPanel = SearchPanel()

    val LOGIN_REQUEST_CODE = 1
    val REGISTER_REQUEST_CODE = 2
    val VALIDATE_REQUEST_CODE = 3
    val SEARCH_REQUEST_CODE = 4
    val SEARCH_REQUEST_CODE1 = 5

    var dataService: DataService = DataService()

    //for layer
    var layerMask: LinearLayout? = null
    var layerBlackView: RelativeLayout? = null
    //var layerScrollView: ScrollView? = null
    //var layerContainerView: LinearLayout? = null
    var layerSubmitBtn: Button? = null
    var layerCancelBtn: Button? = null
    var layerDeleteBtn: Button? = null
    var layerRightLeftPadding: Int = 80
    var layerTopPadding: Int = 100
    lateinit var layerButtonLayout: LinearLayout
    var layerBtnCount: Int = 2
    var layerVisibility: Boolean = false

    val body_css = "<style>body{background-color:#000;padding-left:8px;padding-right:8px;margin-top:0;padding-top:0;color:#888888;font-size:18px;}a{color:#a6d903;}</style>"

    val session: SharedPreferences = App.ctx!!.getSharedPreferences(SESSION_FILENAME, 0)

    var delegate: BaseActivity? = null

    var able_type: String = "coach"

    var myColorGreen: Int = 0
//    var prevIcon: ImageButton? = null
//    var searchIcon: ImageButton? = null
//    var cartIcon: ImageButton? = null

    //var vcResult: VCResult = VCResult()

    open fun arenaSelected(selected: String, show: String) {}
    open fun contentEdit(key: String, content: String) {
        val row = getOneRowFromKey(key)
        row.value = content
        row.show = content
    }

    //for tag delegate
//    open fun textFieldTextChanged(sectionKey: String, rowKey: String, value: String){}
//    open fun setTag(sectionKey: String, rowKey: String, attribute: String, selected: Boolean){}
//    open fun stepperValueChanged(sectionKey: String, rowKey: String, number: Int){}

//    open fun radioDidChange(sectionKey: String, rows: ArrayList<HashMap<String, String>>){}
//    open fun moreClick(sectionKey: String, rowKey: String){}

    open var editCourseResult: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gSimulate = isEmulator()
        //gSimulate = false

        //ConnectTask(this).execute()
        val btn = findViewById<Button>(R.id.submit_btn)
        if (btn != null) {
            btn.setOnClickListener { searchSubmit() }
        }

        _setURLConstants()

        getScreenWidth()
        getScreenHeight()

        oneSectionAdapter = OneSectionAdapter(this, R.layout.cell_section, this, hashMapOf())
        oneSections = initSectionRows()

        myColorGreen = ContextCompat.getColor(context, R.color.MY_GREEN)
        delegate = this

        //MyOneSignal.clear()

        //vcResult.selectCityResult(this)

        //OneSignal.setSubscription(true)
        //OneSignal.promptLocation() prompt location auth when location auth is close

        //member.reset()
        //member.print()
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

        prevIcon?.visibility = isPrevIconShow then { View.VISIBLE } ?: View.GONE

        searchIcon?.visibility = isSearchIconShow then { View.VISIBLE } ?: View.GONE
        addIcon?.visibility = isAddIconShow then { View.VISIBLE } ?: View.GONE

        //當購物車中有商品時，購物車的icon就會出現，如果沒有就不會出現
        //1.AddCartVC中，商品加入購物車時，+1
        //2.MemberCartListVC中，移除購物車中的商品時，-1
        //3.購物車轉成訂單時OrderVC，購物車中的商品數變0
        cartItemCount = session.getInt("cartItemCount", 0)
        cartIcon?.visibility = (member.isLoggedIn && cartItemCount > 0) then { View.VISIBLE } ?: View.GONE

//        searchSectionAdapter = SearchSectionAdapter(this, R.layout.cell_section, this)
//        searchSections = initSectionRows()
        oneSectionAdapter.setOneSection(oneSections)
    }

    override fun onResume() {
        super.onResume()

//        MyOneSignal.dump()
//        val unShowPushs: JSONArray = MyOneSignal.getUnShowRows()
//        var rows = MyOneSignal.getAllRows()
//        val rows1: JSONArray = JSONArray()
//        for (i in 0 until rows.length()) {
//            val row: JSONObject = rows.getJSONObject(i)
//            row.put("isShow", false)
//            rows1.put(row)
//        }
//        MyOneSignal.updateAll(rows1)



        //if has new push show the push
        val rows = MyOneSignal.getAllRows()
        val pnArr1 = JSONArray()

        for (i in 0 until rows.length()) {
//        for (i in 0 until unShowPushs.length()) {
            val row: JSONObject = rows.getJSONObject(i)
            //val row: JSONObject = unShowPushs.getJSONObject(i)
            if (row.has("isShow") && !row.getBoolean("isShow")) {
                val content: String = row.getString("content")
                info(content)
                row.put("isShow", true)
            }
            pnArr1.put(row)
        }
        MyOneSignal.updateAll(pnArr1)
    }

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

    fun tabToTeam(view: View) {
        if (able_type != "team") {
            toSearch()
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

    fun tabToArena(view: View) {
        if (able_type != "arena") {
            toArena(false)
        }
    }

    fun tabToMore(view: View) {
        if (able_type != "more") {
            toMore()
        }
    }

    override fun cellRefresh() {
        params.clear()
        tableLists.clear()
        refresh()
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
            val n: Int = (value.isInt()) then { value.toInt() } ?: 0
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

    override fun cellClear(sectionIdx: Int, rowIdx: Int) {

        oneSections[sectionIdx].items[rowIdx].value = ""
        oneSections[sectionIdx].items[rowIdx].show = "全部"
        oneSectionAdapter.setOneSection(oneSections)
        oneSectionAdapter.notifyItemChanged(sectionIdx)
    }

    override fun cellPrompt(sectionIdx: Int, rowIdx: Int) {

        val row: OneRow = getOneRowFromIdx(sectionIdx, rowIdx)
        if (row.prompt.isNotEmpty()) {
            info(row.prompt)
        }
    }

    override fun cellTextChanged(sectionIdx: Int, rowIdx: Int, str: String) {

        oneSections[sectionIdx].items[rowIdx].value = str
    }

    override fun cellSwitchChanged(sectionIdx: Int, rowIdx: Int, b: Boolean) {

        val value = if (b) {
            "1"
        } else {
            "0"
        }
        oneSections[sectionIdx].items[rowIdx].value = value
    }

    open fun cellCourse(row: Table) {

    }

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

    open fun weekendsSelected(selected: Int, show: String) {
        val key: String = WEEKDAYS_KEY
        val row = getOneRowFromKey(key)
        val idx = getOneSectionIdxFromRowKey(key)

        row.value = selected.toString()
        row.show = show
        oneSectionAdapter.notifyItemChanged(idx)
    }

    open fun degreeSelected(selected: String, show: String) {
        val key: String = DEGREE_KEY
        val row = getOneRowFromKey(key)
        val idx = getOneSectionIdxFromRowKey(key)

        row.value = selected
        row.show = show
        oneSectionAdapter.notifyItemChanged(idx)
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

//    fun getSearchSectionIdxFromRowKey(key: String): Int {
//        for ((idx, section) in searchSections.withIndex()) {
//            for (row in section.items) {
//                if (row.key == key) {
//                    return idx
//                }
//            }
//        }
//        return 0
//    }
//
//    fun getSearchRowFromKey(key: String): SearchRow {
//
//        for (section in searchSections) {
//            for (row in section.items) {
//                if (row.key == key) {
//                    return row
//                }
//            }
//        }
//        return SearchRow()
//    }
//
//    fun getSearchRowFromIdx(sectionIdx: Int, rowIdx: Int): SearchRow {
//        return searchSections[sectionIdx].items[rowIdx]
//    }

    fun getOneRowFromIdx(sectionIdx: Int, rowIdx: Int): OneRow {
        return oneSections[sectionIdx].items[rowIdx]
    }

    fun getOneSectionFromIdx(sectionIdx: Int): OneSection {
        return oneSections[sectionIdx]
    }

    protected fun setMyTitle(title: String) {
        if (topTitleLbl != null) {
            topTitleLbl.text = title
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

    protected fun myMakeCall(_mobile: String) {
        mobile = _mobile
        val p = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (p != PackageManager.PERMISSION_GRANTED) {
            makeCallRequest()
        } else {
            makeCall(mobile)
        }
    }

    private fun makeCallRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
    }

    private fun getScreenWidth() {
        val displayMetrics = resources.displayMetrics
        density = displayMetrics.density
        //println("density: " + density)
        //val width: Float = displayMetrics.widthPixels / density
        screenWidth = displayMetrics.widthPixels
    }

    private fun getScreenHeight() {
        val displayMetrics = resources.displayMetrics
        density = displayMetrics.density
        screenHeight = displayMetrics.heightPixels
    }

    override fun onSupportNavigateUp(): Boolean {
        prev()
        return true
    }

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

    fun requestPermission(permissions: Array<out String>, requestCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
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
        }
    }

    //for prev image button use
    fun prev(view: View) {
        prev()
    }

    fun prev() {
        hideKeyboard()
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    fun searchPressed(view: View) {
        showSearchPanel()
    }

    open fun searchSubmit() {}

    fun home(context: Context) {
        val intent : Intent = Intent(context, MemberVC::class.java)
        startActivity(intent)
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

    protected fun hidekeyboard(parent: View) {
        val allV = getAllChildrenBFS(parent)
        for (i in 0..allV.size-1) {
            val v = allV.get(i)
            v.setOnFocusChangeListener(this)
        }
    }

    protected fun _hideKeyboard(view: View) {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    protected fun _showKeyboard(view: View) {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /*
    protected fun loginFB() {
        val playerID = _getPlayerID()
        //val context = this
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logOut()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email,public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        MemberService.FBLogin(this@BaseActivity, playerID) { success ->
                            if (success) {
                                //Session.loginReset = true
                                LocalBroadcastManager.getInstance(this@BaseActivity).sendBroadcast(memberDidChangeIntent)
                                finish()
                            } else {
                                val msg = MemberService.msg
                                Alert.show(this@BaseActivity, "錯誤", msg)
                            }
                        }
                    }

                    override fun onCancel() {
                        println("cancel")
                    }

                    override fun onError(error: FacebookException?) {
                        //println(error)

                    }
                }
        )
    }
     */

    protected fun _getPlayerID(): String {
        var playerID = ""
        val deviceState = OneSignal.getDeviceState()
        if (deviceState != null) {
            playerID = deviceState.userId
        }

//        OneSignal.idsAvailable { userId, registrationId ->
//            playerID = userId
//        }
        return playerID
    }

//    public fun _getMemberOne(token: String, completion: CompletionHandler) {
//        if (member.isLoggedIn) {
//            Loading.show(mask)
//            MemberService.getOne(this, token) { success ->
//                Loading.hide(mask)
//                if (success) {
//                    completion(true)
//                } else {
//                    Alert.show(this, "錯誤", MemberService.msg)
//                    completion(false)
//                }
//            }
//        } else {
//            warning("沒有登入")
//        }
//    }

//    protected fun memberDidChange() {
//        val memberDidChange = Intent(NOTIF_MEMBER_DID_CHANGE)
//        LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChange)
//    }

//    protected val memberDidChange = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            refreshMember() { success ->
//
//            }
//        }
//    }

//    fun refreshMember(completion: CompletionHandler) {
//        member.token?.let { _getMemberOne(it, completion) }
//    }

//    protected fun _updatePlayerIDWhenIsNull() {
//        val token = member.token
//        if (token != null) {
//            _getMemberOne(token) { success ->
//                if (success) {
//                    member.justGetMemberOne = true
//                    if (member.player_id?.length == 0) {
//                        _updatePlayerID()
//                    }
//                }
//            }
//        }
//    }
//
//    protected fun _updatePlayerID() {
//        val player_id = _getPlayerID()
//        MemberService.update(this, member.id, PLAYERID_KEY, player_id) { success ->
//            if (success) {
//                member.player_id = player_id
//            }
//        }
//    }

    protected fun _getTeamManagerList(completion: CompletionHandler) {
        Loading.show(mask)
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

    protected fun addBlackList(memberName: String, memberToken: String, teamToken: String) {
        warning("是否真的要將球友" + memberName + "設為黑名單\n之後可以解除", "取消", "加入", {
            reasonBox(memberToken, teamToken)
        })

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
    private fun _addBlackList(reason: String, memberToken: String, teamToken: String) {
        Loading.show(mask)
        val token = member.token
        if (token != null) {
            TeamService.addBlackList(this, teamToken, memberToken, token, reason) { success ->
                Loading.hide(mask)
                if (success) {
                    info("加入黑名單成功")
                } else {
                    warning(TeamService.msg)
                }
            }
        }
    }

    private fun isEmulator(): Boolean {

//        println(Build.FINGERPRINT.startsWith("google/sdk_gphone_")
//                && Build.FINGERPRINT.endsWith(":user/release-keys")
//                && Build.MANUFACTURER == "Google" && Build.PRODUCT.startsWith("sdk_gphone_") && Build.BRAND == "google"
//                && Build.MODEL.startsWith("sdk_gphone_"))
//
//        println("FINGERPRINT:${Build.FINGERPRINT}\n" +
//                "MODEL:${Build.MODEL}\n" +
//                "MANUFACTURER:${Build.MANUFACTURER}\n" +
//                "BRAND:${Build.BRAND}\n" +
//                "DEVICE:${Build.DEVICE}\n" +
//                "BOARD:${Build.BOARD}\n" +
//                "HOST:${Build.HOST}\n" +
//                "PRODUCT:${Build.PRODUCT}\n")

        val result = (Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                && Build.FINGERPRINT.endsWith(":user/release-keys")
                && Build.MANUFACTURER == "Google" && Build.PRODUCT.startsWith("sdk_gphone_") && Build.BRAND == "google"
                && Build.MODEL.startsWith("sdk_gphone_"))
                //
                || Build.FINGERPRINT.contains("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                //bluestacks
                || "QC_Reference_Phone" == Build.BOARD && !"Xiaomi".equals(Build.MANUFACTURER, ignoreCase = true) //bluestacks
                || Build.MANUFACTURER.contains("Genymotion")

                //Sony is true, so mark it. HOST:BuildHost
//                || Build.HOST.startsWith("Build") //MSI App Player
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.PRODUCT == "google_sdk"
                // another Android SDK emulator check
                || SystemProperties.getProp("ro.kernel.qemu") == "1"
        return result

//        return (Build.FINGERPRINT.startsWith("generic")
//                || Build.FINGERPRINT.startsWith("unknown")
//                || Build.MODEL.contains("google_sdk")
//                || Build.MODEL.contains("Emulator")
//                || Build.MODEL.contains("Android SDK built for x86")
//                || Build.MANUFACTURER.contains("Genymotion")
//                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
//                || "google_sdk" == Build.PRODUCT)
    }

    fun TextView.setMyText(value: String, default: String = "") {
        if (value.isEmpty()) text = default else text = value
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

    open fun initSectionRows(): ArrayList<OneSection> {

        val sections: ArrayList<OneSection> = arrayListOf()

        sections.add(makeSection0Row())

        return sections
    }

    open fun makeSection0Row(isExpanded: Boolean=true): OneSection {
        val s: OneSection = OneSection("一般", "general", isExpanded)
        return s
    }

//    override fun handleSectionExpanded(idx: Int) {
//        //println(idx)
//        val searchSection = searchSections[idx]
//        var isExpanded: Boolean = searchSection.isExpanded
//        isExpanded = !isExpanded
//        searchSections[idx].isExpanded = isExpanded
//        searchSectionAdapter.setSearchSection(searchSections)
//        searchSectionAdapter.notifyDataSetChanged()
//    }

    ////////// for top bar cart icon pressed ///////////////////////////////////
    fun cartPressed(view: View) {
        toMemberCartList()
    }

    open fun addPressed(view: View) {}

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

    protected fun setBottomTabFocus() {

        val tabTitleID: Int = resources.getIdentifier(able_type + "TabTitle", "id", packageName)
        val tabLineID: Int = resources.getIdentifier(able_type + "TabLine", "id", packageName)

        findViewById<TextView>(tabTitleID) ?. let {
            it.textColor = ContextCompat.getColor(context, R.color.MY_GREEN)
        }

        findViewById<LinearLayout>(tabLineID) ?. let {
            it.backgroundColor = myColorGreen
        }
    }

//    protected fun addSearchLayer(page: String) {
//
//        val parent = getMyParent()
//        val w = parent.measuredWidth
//        val h = parent.measuredHeight
//        if (layerBlackView == null) {
//
//            //container layer for search
//            val lp = RelativeLayout.LayoutParams(w - (2 * layerRightLeftPadding), h - layerTopPadding)
//            //val lp = RelativeLayout.LayoutParams(w - (2 * layerRightLeftPadding), 1000)
//            //lp.setMargins(layerRightLeftPadding, layerTopPadding, layerRightLeftPadding, 0)
//            layerBlackView = RelativeLayout(this)
//            layerBlackView!!.layoutParams = lp
//            layerBlackView!!.translationX = layerRightLeftPadding.toFloat()
//            layerBlackView!!.translationY = layerTopPadding.toFloat()
//            layerBlackView!!.backgroundColor = Color.BLACK
//            layerMask!!.addView(layerBlackView)
//
////            val lp1 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2000)
////            lp.setMargins(0, 0, 0, 60)
////            layerScrollView = ScrollView(this)
////            layerScrollView!!.layoutParams = lp1
////            layerScrollView!!.backgroundColor = Color.RED
////            layerScrollView!!.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
////                override fun onScrollChange(p0: NestedScrollView?, p1: Int, p2: Int, p3: Int, p4: Int) {
////                    currentFocus?.clearFocus()
////                }
////
////            })
////            layerScrollView!!.layoutParams = lp
////            layerScrollView!!.backgroundColor = Color.GREEN
//
////            val mask = getMask()
//            //layerBlackView!!.addView(layerScrollView)
//
////            val lp2 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
////            lp2.setMargins(0, 0, 0, 0)
////            layerContainerView = LinearLayout(this)
////            layerContainerView!!.orientation = LinearLayout.VERTICAL
////            layerContainerView!!.layoutParams = lp2
////            layerContainerView!!.backgroundColor = Color.BLUE
//            //layerScrollView!!.addView(layerContainerView)
//
//            _addLayer(page)
////            val l = LinearLayout(this)
////            l.orientation = LinearLayout.VERTICAL
////            l.addView(layerButtonLayout)
////            layerContainerView!!.addView(l)
//
//            //append bottom space
////            val bottomLayout = LinearLayout(this)
////            val lpx = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200)
////            bottomLayout.layoutParams = lpx
////            layerContainerView!!.addView(bottomLayout)
//        }
//
//
//        val animate = TranslateAnimation(layerRightLeftPadding.toFloat(), layerRightLeftPadding.toFloat(), h.toFloat(), layerTopPadding.toFloat())
//        animate.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationRepeat(p0: Animation?) {}
//            override fun onAnimationEnd(p0: Animation?) {
//            }
//
//            override fun onAnimationStart(p0: Animation?) {
//            }
//        })
//        animate.duration = 500
//        animate.fillAfter = true
//        //layerScrollView!!.startAnimation(animate)
//        layerVisibility = true
//    }

//    open fun _addLayer(page: String) {
//        addSearchTableView(page)
//        layerAddButtonLayout()
//        layerBtnCount = 2
//        layerAddSubmitBtn(page)
//        layerAddCancelBtn()
//    }
//
//    protected fun addSearchTableView(page: String) {
//        val parent = getMyParent()
//        val w = parent.measuredWidth
//        val searchTableView = RecyclerView(this)
//        searchTableView.id = R.id.SearchRecycleItem
////        val padding: Int = 80
//        //val lp1 = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800)
//        val lp1 = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
////        val lp1 = RecyclerView.LayoutParams(w - (2 * padding), 1000)
//        lp1.setMargins(0, 0, 0, 150)
//        searchTableView.layoutParams = lp1
//        searchTableView.layoutManager = LinearLayoutManager(this)
//        //searchTableView.backgroundColor = Color.RED
//        searchTableView.backgroundColor = Color.TRANSPARENT
//        searchAdapter = GroupAdapter<GroupieViewHolder>()
//        searchAdapter.setOnItemClickListener { item, view ->
//            val searchItem = item as SearchItem
//            val row = searchItem.row
//            if (page == "course" || page == "team") {
//
//                //val tag = parent.tag as String
//                var frag: TabFragment? = null
//                if (page == "course") {
//                    frag = getFragment() as CourseFragment
//                } else if (page == "team") {
//                    frag = getFragment() as TeamFragment
//                }
//                if (frag != null) {
//                    frag.prepare(row)
//                }
//                //prepareSearch1(row, page)
//            } else {
//                prepare(row)
////                if (searchItem.switch == false) {
////                    prepareSearch(row, page)
////                }
//            }
//        }
//        val rows = generateSearchItems(page)
//        searchAdapter.addAll(rows)
//
//        searchTableView.adapter = searchAdapter
//        //layerContainerView!!.addView(searchTableView)
//        layerBlackView!!.addView(searchTableView)
//    }
//
//    protected fun layerAddButtonLayout() {
//        layerButtonLayout = LinearLayout(this)
//        val lp = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150)
//        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//        layerButtonLayout.layoutParams = lp
//        val color = ContextCompat.getColor(this, R.color.MY_GREEN)
//        layerButtonLayout.backgroundColor = color
//        layerButtonLayout.gravity = Gravity.CENTER
//        layerButtonLayout.orientation = LinearLayout.HORIZONTAL
//        layerBlackView!!.addView(layerButtonLayout)
//    }
//
//    protected fun layerAddSubmitBtn(page: String) {
//        layerSubmitBtn = layoutInflater.inflate(R.layout.submit_button, null) as Button
//        //layerSubmitBtn = Button(this, null, R.style.submit_button)
////        val submitBtnWidth = 260
//        val lp2 = LinearLayout.LayoutParams(300, 90)
////        lp2.setMargins(16, 0, 0, 0)
//        layerSubmitBtn!!.layoutParams = lp2
//        //layerSubmitBtn!!.text = "送出"
////        layerSubmitBtn!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
////        val myRed = ContextCompat.getColor(this, R.color.MY_RED)
////        val shape = GradientDrawable()
////        shape.shape = GradientDrawable.RECTANGLE
////        shape.setColor(myRed)
////        shape.cornerRadius = 62f
//        layerSubmitBtn!!.setOnClickListener {
//            layerSubmit(page)
//        }
//
////        layerSubmitBtn!!.backgroundDrawable = shape
////        layerSubmitBtn!!.setTextColor(Color.WHITE)
//
//        layerButtonLayout.addView(layerSubmitBtn)
//    }
//
//    protected fun layerAddCancelBtn() {
//        layerCancelBtn = layoutInflater.inflate(R.layout.cancel_button, null) as Button
////        layerCancelBtn = Button(this)
////        val cancelBtnWidth = 260
//        val lp2 = LinearLayout.LayoutParams(300, 90)
//        lp2.setMargins(16, 0, 0, 0)
////        val center = (w-(2*padding)-cancelBtnWidth)/2
////        var x = center
////        when (layerBtnCount) {
////            2 -> x = center + 60
////        }
////        println(center)
////        println(x)
////        lp2.setMargins(16, 0, 16, 0)
//        layerCancelBtn!!.layoutParams = lp2
////        layerCancelBtn!!.text = "取消"
////        layerCancelBtn!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
////        val myRed = ContextCompat.getColor(this, R.color.MY_RED)
////        val shape = GradientDrawable()
////        shape.shape = GradientDrawable.RECTANGLE
////        shape.setColor(myRed)
////        shape.cornerRadius = 82f
//        layerCancelBtn!!.setOnClickListener {
//            layerCancel()
//        }
//
////        layerCancelBtn!!.backgroundDrawable = shape
////        layerCancelBtn!!.setTextColor(Color.WHITE)
//
//        layerButtonLayout.addView(layerCancelBtn)
//    }
//
//    protected fun layerAddDeleteBtn() {
//        layerDeleteBtn = layoutInflater.inflate(R.layout.delete_button, null) as Button
//        //layerDeleteBtn = Button(this)
////        val deleteBtnWidth = 260
//        val lp2 = LinearLayout.LayoutParams(300, 90)
//        lp2.setMargins(16, 0, 0, 0)
//        layerDeleteBtn!!.layoutParams = lp2
////        layerDeleteBtn!!.text = "刪除"
////        layerDeleteBtn!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
////        val myRed = ContextCompat.getColor(this, R.color.MY_RED)
////        val shape = GradientDrawable()
////        shape.shape = GradientDrawable.RECTANGLE
////        shape.setColor(myRed)
////        shape.cornerRadius = 82f
//        layerDeleteBtn!!.setOnClickListener {
//            layerDelete()
//        }
//
////        layerDeleteBtn!!.backgroundDrawable = shape
////        layerDeleteBtn!!.setTextColor(Color.WHITE)
//
//        layerButtonLayout.addView(layerDeleteBtn)
//    }

//    fun getMask(): LinearLayout {
//        val parent = getMyParent()
//        return parent.findViewById<LinearLayout>(R.id.MyMask)
//    }

    fun getMyParent(): ViewGroup {
//        val rootView = window.decorView.rootView
//        val parentID = resources.getIdentifier(containerID, "id", packageName)
//        val parent = rootView.findViewById<ConstraintLayout>(parentID)
//        return parent
        return window.decorView.rootView as ViewGroup
    }

    ////// search panel end //////////////////////////////////////


    open fun prepare(idx: Int) {}
    open fun prepare(sectionIdx: Int, rowIdx: Int) {}

//    protected fun prepareSearch(idx: Int, page: String) {
//        val intent = Intent(this, EditItemActivity::class.java)
//        val row = searchRows.get(idx)
//        var key = ""
//        if (row.containsKey("key")) {
//            key = row["key"]!!
//        }
//        when (key) {
//            CITY_KEY -> {
//                intent.putExtra("key", CITY_KEY)
//                intent.putExtra("source", "search")
//                intent.putExtra("page", page)
//                intent.putExtra("type", "simple")
//                intent.putExtra("select", "multi")
//                if (page == "coach") {
//                    citys = citys_coach
//                } else if (page == "team") {
//                    citys = citys_team
//                }
//                intent.putParcelableArrayListExtra("citys", citys)
//            }
//            ARENA_KEY -> {
//                if (citys.size == 0) {
//                    Alert.warning(this, "請先選擇縣市")
//                    return
//                }
//                intent.putExtra("key", ARENA_KEY)
//                intent.putExtra("source", "search")
//                intent.putExtra("type", "simple")
//                intent.putExtra("select", "multi")
//
//                var citysForArena: ArrayList<Int> = arrayListOf()
//                for (city in citys) {
//                    citysForArena.add(city.id)
//                }
//                intent.putIntegerArrayListExtra("citys_for_arena", citysForArena)
//                intent.putParcelableArrayListExtra("arenas", arenas)
//            }
//            TEAM_WEEKDAYS_KEY -> {
//                intent.putExtra("key", TEAM_WEEKDAYS_KEY)
//                intent.putExtra("source", "search")
//                intent.putIntegerArrayListExtra("weekdays", weekdays)
//            }
//            TEAM_PLAY_START_KEY -> {
//                intent.putExtra("key", TEAM_PLAY_START_KEY)
//                intent.putExtra("source", "search")
////                        times["time"] = "09:00"
//                times["type"] = SELECT_TIME_TYPE.play_start
//                intent.putExtra("times", times)
//            }
//            TEAM_DEGREE_KEY -> {
//                intent.putExtra("key", TEAM_DEGREE_KEY)
//                intent.putExtra("source", "search")
//                intent.putExtra("degrees", degrees)
//            }
//            AREA_KEY -> {
//                if (citys.size == 0) {
//                    Alert.warning(this, "請先選擇縣市")
//                    return
//                }
//                intent.putExtra("key", AREA_KEY)
//                intent.putExtra("source", "search")
//                intent.putExtra("type", "simple")
//                intent.putExtra("select", "multi")
//
//                var citysForArea: ArrayList<Int> = arrayListOf()
//                for (city in citys) {
//                    citysForArea.add(city.id)
//                }
//                intent.putIntegerArrayListExtra("citys_for_area", citysForArea)
//                intent.putParcelableArrayListExtra("areas", areas)
//            }
//        }
//        startActivityForResult(intent!!, SEARCH_REQUEST_CODE)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        var value = "全部"
//        var idx = 0
//        when (requestCode) {
//            SEARCH_REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    var key = ""
//                    if (data != null && data.hasExtra("key")) {
//                        key = data.getStringExtra("key")!!
//                    }
//                    var page = ""
//                    if (data != null && data.hasExtra("page")) {
//                        page = data.getStringExtra("page")!!
//                    }
//                    when (key) {
//                        CITY_KEY -> {
//                            idx = 1
//                            citys = data!!.getParcelableArrayListExtra("citys")!!
//                            if (citys.size > 0) {
//                                var arr: ArrayList<String> = arrayListOf()
//                                for (city in citys) {
//                                    arr.add(city.name)
//                                }
//                                value = arr.joinToString()
//                            } else {
//                                value = "全部"
//                            }
//                            if (page == "coach") {
//                                citys_coach = citys
//                            } else if (page == "team") {
//                                citys_team = citys
//                            }
//                        }
//                        ARENA_KEY -> {
//                            idx = 2
//                        }
//                        TEAM_WEEKDAYS_KEY -> {
//                            idx = 3
//                            weekdays = data!!.getIntegerArrayListExtra("weekdays")!!
//                            if (weekdays.size > 0) {
//                                var arr: ArrayList<String> = arrayListOf()
//                                val gDays = Global.weekdays
//                                for (day in weekdays) {
//                                    for (gDay in gDays) {
//                                        if (day == gDay.get("value")!! as Int) {
//                                            arr.add(gDay.get("simple_text")!! as String)
//                                            break
//                                        }
//                                    }
//                                }
//                                value = arr.joinToString()
//                            } else {
//                                value = "全部"
//                            }
//                        }
//                        TEAM_PLAY_START_KEY -> {
//                            idx = 4
//                            times = data!!.getSerializableExtra("times") as HashMap<String, Any>
//                            if (times.containsKey("time")) {
//                                value = times["time"]!! as String
//                            } else {
//                                value = "全部"
//                            }
//                        }
//                        TEAM_DEGREE_KEY -> {
//                            idx = 5
//                            degrees = data!!.getSerializableExtra("degrees") as ArrayList<DEGREE>
//                            if (degrees.size > 0) {
//                                var arr: ArrayList<String> = arrayListOf()
//                                degrees.forEach {
//                                    arr.add(it.value)
//                                }
//                                value = arr.joinToString()
//                            } else {
//                                value = "全部"
//                            }
//                        }
//                        AREA_KEY -> {
//                            idx = 2
//                            areas = data!!.getParcelableArrayListExtra("areas")!!
//                            if (areas.size > 0) {
//                                var arr: ArrayList<String> = arrayListOf()
//                                for (area in areas) {
//                                    arr.add(area.name)
//                                }
//                                value = arr.joinToString()
//                            } else {
//                                value = "全部"
//                            }
//                        }
//                    }
//                    searchRows[idx]["detail"] = value
//                    //val rows = generateSearchItems(page)
//                    //searchAdapter.update(rows)
//                }
//            }
//            SEARCH_REQUEST_CODE1 -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    var key = ""
//                    if (data != null && data.hasExtra("key")) {
//                        key = data.getStringExtra("key")!!
//                    }
//                    var selecteds: ArrayList<String>? = null
//                    if (data!!.hasExtra("selecteds")) {
//                        selecteds = data.getStringArrayListExtra("selecteds")
//                    }
//                    var selected: String? = null
//                    if (data.hasExtra("selected")) {
//                        selected = data.getStringExtra("selected")
//                    }
//
//                    val getRow = fun(key: String): HashMap<String, String>? {
//                        var row: HashMap<String, String>? = null
//                        for ((i, searchRow) in searchRows.withIndex()) {
//                            if (searchRow.containsKey("key")) {
//                                if (key == searchRow.get("key")) {
//                                    row = searchRow
//                                    break
//                                }
//                            }
//                        }
//
//                        return row
//                    }
//
//                    val updateRow = fun(key: String, row: HashMap<String, String>) {
//                        var idx: Int = -1
//                        for ((i, searchRow) in searchRows.withIndex()) {
//                            if (searchRow.containsKey("key")) {
//                                if (key == searchRow.get("key")) {
//                                    idx = i
//                                    break
//                                }
//                            }
//                        }
//                        if (idx >= 0) {
//                            searchRows[idx] = row
//                        }
//                    }
//
//                    val row: HashMap<String, String>? = getRow(key)
//
//                    var show = ""
//
//                    when (key) {
//                        CITY_KEY -> {
//                            val session: SharedPreferences = this.getSharedPreferences(SESSION_FILENAME, 0)
//                            val str = session.getString("citys", "")!!
//                            var arr: JSONArray? = null
//                            if (str.length > 0) {
//                                try {
//                                    arr = JSONArray(str)
//                                } catch (e: java.lang.Exception) {
//                                    //println(e.localizedMessage)
//                                }
//                            }
//
//                            val texts: ArrayList<String> = arrayListOf()
//                            for (selected in selecteds!!) {
//                                if (arr != null) {
//                                    for (i in 0..arr!!.length() - 1) {
//                                        val obj = arr!![i] as JSONObject
//                                        if (selected == obj.getString("value")) {
//                                            texts.add(obj.getString("title"))
//                                            break
//                                        }
//                                    }
//                                }
//                            }
//                            show = texts.joinToString(",")
//                            if (row != null && row.containsKey("show")) {
//                                row["show"] = show
//                                row["value"] = selecteds.joinToString(",")
//                                updateRow(key, row)
//                            }
//                        }
//                        WEEKDAY_KEY -> {
//                            val texts: ArrayList<String> = arrayListOf()
//                            for (selected in selecteds!!) {
//                                val text = WEEKDAY.intToString(selected.toInt())
//                                texts.add(text)
//                            }
//                            show = texts.joinToString(",")
//                            if (row != null && row.containsKey("show")) {
//                                row["show"] = show
//                                row["value"] = selecteds.joinToString(",")
//                                updateRow(key, row)
//                            }
//                        }
//                        START_TIME_KEY, END_TIME_KEY -> {
//                            if (row != null && row.containsKey("show") && selected != null) {
//                                row["show"] = selected.noSec()
//                                row["value"] = selected
//                                updateRow(key, row)
//                            }
//                        }
//
//                    }
//                }
//            }
//        }
//    }

//    fun generateSearchItems(page: String): ArrayList<SearchItem> {
//
//        val rows: ArrayList<SearchItem> = arrayListOf()
//        for (i in 0..searchRows.size-1) {
//            val row = searchRows[i] as HashMap<String, String>
//            val title = row.get("title")!!
//            var detail: String = ""
//            if (row.containsKey("detail")) {
//                detail = row.get("detail")!!
//            } else if (row.containsKey("show")) {
//                detail = row.get("show")!!
//            }
//            var bSwitch = false
//            if (row.containsKey("switch")) {
//                bSwitch = row.get("switch")!!.toBoolean()
//            }
//            val searchItem = SearchItem(title, detail, keyword, bSwitch, -1, i)
//            if (page == "team" || page == "course") {
//                //searchItem.delegate = getFragment()
//            } else {
//                searchItem.delegate = this@BaseActivity
//            }
//            rows.add(searchItem)
//        }
//
//        return rows
//    }


    //已經移到Global成為 global function 了，以後停止使用
//    fun getFragment(): TabFragment? {
//        val frags = supportFragmentManager.fragments
//        for (frag in frags) {
//            if (frag != null && frag.isVisible) {
//                when (frag::class) {
//                    TempPlayFragment::class ->
//                        return frag as TempPlayFragment
//                    CourseFragment::class ->
//                        return frag as CourseFragment
//                    ArenaFragment::class ->
//                        return frag as ArenaFragment
//                }
//            }
//        }
//        return null

//        var _frag: TabFragment? = null
//        for (frag in frags) {
//            if (able_type == "team" && frag::class == TempPlayFragment::class) {
//                _frag = frag as TempPlayFragment
//                break
//            }
//            if (able_type == "course" && frag::class == CourseFragment::class) {
//                _frag = frag as CourseFragment
//                break
//            }
//            if (able_type == "team" && frag::class == TempPlayFragment::class) {
//                _frag = frag as TempPlayFragment
//                break
//            }
//            if (able_type == "arena" && frag::class == ArenaFragment::class) {
//                _frag = frag as ArenaFragment
//                break
//            }
//        }
//
//        return _frag
//    }

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

    fun resetParams() {
        citys.clear()
        areas.clear()
        arenas.clear()
        weekdays.clear()
        degrees.clear()
        times.clear()
        keyword = ""
    }


//    open protected fun layerSubmit(page: String) {
//        unmask()
//        if (page == "coach") {
//            prepareParams()
//            refresh()
//        } else if (page == "team") {
//            val frag = getFragment() as TeamFragment
//            frag.prepareParams()
//            frag.refresh()
//        } else if (page == "course") {
//            val frag = getFragment() as CourseFragment
//            frag.prepareParams()
//            frag.refresh()
//            //frag.layerSubmit()
//
//        } else {
//            prepareParams()
//            refresh()
//        }
//    }

//    open fun cityBtnPressed(city_id: Int, page: String) {
//        resetParams()
//        citys.add(City(city_id, ""))
//        prepareParams("all")
////        println(city_id)
//        if (page == "coach") {
//            refresh()
//        } else if (page == "team") {
//            val frag = getFragment() as TempPlayFragment
//            frag.refresh()
//        } else {
//            refresh()
//        }
//    }

    fun webViewSettings(context: Context, webView: WebView) {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)
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

//    open protected fun layerCancel() {
//        unmask()
//    }
//
//    open protected fun layerDelete() {}

    fun getCitys(complete: (rows: ArrayList<HashMap<String, String>>)-> Unit): ArrayList<HashMap<String, String>> {
        var rows: ArrayList<HashMap<String, String>> = session.getAllCitys()
        if (rows.count() == 0) {
            Loading.show(mask)
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
            Loading.hide(mask)
        }
        return rows
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

    fun getAreasFromCity(city_id: Int, complete: (rows: ArrayList<HashMap<String, String>>) -> Unit): ArrayList<HashMap<String, String>> {
        var rows: ArrayList<HashMap<String, String>> = session.getAreasFromCity(city_id)
        if (rows.count() == 0) {
            Loading.show(mask)
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

            Loading.hide(mask)
        }

        return rows
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
    fun info(msg: String) {
        Alert.show(this, "訊息", msg)
    }
    fun info(msg: String, closeButtonTitle: String, buttonTitle: String, buttonAction: () -> Unit) {
        Alert.show(this, "訊息", msg, closeButtonTitle, buttonTitle, buttonAction)
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

    protected open fun setRefreshListener() {
        if (refreshLayout != null) {
            refreshListener = SwipeRefreshLayout.OnRefreshListener {
                refresh()
            }
            refreshLayout!!.setOnRefreshListener(refreshListener)
        }
    }
    open fun closeRefresh() {
        refreshLayout?.isRefreshing = false
    }
    open fun refresh() {}

    protected open fun setTeamData(imageView: ImageView? = null) {}

    private fun _setURLConstants() {
        BASE_URL = if (gSimulate) LOCALHOST_BASE_URL else REMOTE_BASE_URL
        //println("os: " + BASE_URL)

        URL_HOME = BASE_URL + "/app/"

        URL_AREA_BY_CITY_IDS = URL_HOME + "area_by_citys"
        URL_ARENA_BY_CITY_ID = URL_HOME + "arena_by_city"
        URL_ARENA_BY_CITY_IDS = URL_HOME + "arena_by_citys"
        URL_ARENA_LIKE = URL_HOME + "arena/like/%s"
        URL_ARENA_LIST = URL_HOME + "arena/list"
        URL_CANCEL_SIGNUP = URL_HOME + "%s/cancelSignup/%d"
        URL_CART_DELETE = URL_HOME + "cart/delete"
        URL_CART_LIST = URL_HOME + "cart/list"
        URL_CART_UPDATE = "${URL_HOME}cart/update"
        URL_CHANGE_PASSWORD = "$BASE_URL/member/change_password"
        URL_CITYS = URL_HOME + "citys"
        URL_COACH_LIKE = URL_HOME + "coach/like/%s"
        URL_COACH_LIST = URL_HOME + "coach/list"
        URL_COURSE_DELETE = URL_HOME + "course/delete"
        URL_COURSE_LIKE = URL_HOME + "course/like/%s"
        URL_COURSE_LIST = URL_HOME + "course/list"
        URL_COURSE_UPDATE = URL_HOME + "course/update"
        URL_DELETE = URL_HOME + "%s/delete"
        URL_EMAIL_VALIDATE = URL_HOME + "member/email_validate"
        URL_FB_LOGIN = URL_HOME + "member/fb"
        URL_FORGETPASSWORD = "$BASE_URL/member/forget_password"
        URL_ISNAMEEXIST = "${URL_HOME}%s/isNameExist"
        URL_MANAGER_SIGNUPLIST = "${URL_HOME}%s/manager_signup_list"
        URL_MEMBER_DELETE = URL_HOME + "member/delete"
        URL_MEMBER_LIKELIST = "${URL_HOME}member/likelist"
        URL_LIST = "${URL_HOME}%s"
        URL_LOGIN = URL_HOME + "login"
        URL_MEMBER_BLACKLIST = URL_HOME + "member/blacklist"
        URL_MEMBER_GETONE = URL_HOME + "member/getOne"
        URL_MEMBER_SIGNUPLIST = "${URL_HOME}member/signup_calendar"
        URL_MEMBER_UPDATE = URL_HOME + "member/update"
        URL_MOBILE_VALIDATE = URL_HOME + "member/mobile_validate"
        URL_ONE = "${URL_HOME}%s/one"
        URL_ORDER = "${URL_HOME}order/payment%s"
        URL_ORDER_LIST = URL_HOME + "order/list"
        URL_ORDER_UPDATE = "${URL_HOME}order/update"
        URL_PRODUCT_LIKE = URL_HOME + "product/like/%s"
        URL_PRODUCT_LIST = URL_HOME + "product/list"
        URL_REGISTER = URL_HOME + "register"
        URL_REQUEST_MANAGER = URL_HOME + "request_manager/update"
        URL_SEND_EMAIL_VALIDATE = URL_HOME + "member/sendEmailValidate"
        URL_SEND_MOBILE_VALIDATE = URL_HOME + "member/sendMobileValidate"
        URL_SHOW = "${URL_HOME}%s/show/%s?device=app"
        URL_SIGNUP = URL_HOME + "%s/signup/%s"
        URL_SIGNUP_DATE = "${URL_HOME}%s/signup_date/%s"
        URL_SIGNUP_LIST = "${URL_HOME}%s/signup_list"
        URL_STORE_LIKE = URL_HOME + "store/like/%s"
        URL_STORE_LIST = URL_HOME + "store/list"
        URL_TEACH_LIKE = URL_HOME + "teach/like/%s"
        URL_TEACH_LIST = URL_HOME + "teach/list"
        URL_TEAM = URL_HOME + "team/"
        URL_TEAM_DELETE = URL_HOME + "team/delete"
        URL_TEAM_LIKE = URL_HOME + "team/like/%s"
        URL_TEAM_LIST = URL_HOME + "team/list"
        URL_TEAM_TEMP_PLAY = URL_TEAM + "tempPlay/onoff"
        URL_TEAM_TEMP_PLAY_LIST = URL_TEAM + "tempPlay/list"
        URL_TEAM_TEMP_PLAY_BLACKLIST = URL_TEAM + "tempPlay/blacklist"
        URL_TEAM_PLUSONE = BASE_URL + "/team/tempPlay/plusOne/"
        URL_TEAM_CANCELPLUSONE = BASE_URL + "/team/tempPlay/cancelPlusOne/"
        URL_TEAM_TEMP_PLAY_DATE = URL_TEAM + "tempPlay/date"
        URL_TEAM_TEMP_PLAY_DATE_PLAYER = URL_TEAM + "tempPlay/datePlayer"
        URL_TEAM_UPDATE = URL_HOME + "team/update"
        URL_TT = URL_HOME + "%s/tt"
        URL_TT_DELETE = URL_HOME + "%s/tt/delete"
        URL_TT_UPDATE = URL_HOME + "%s/tt/update"
        URL_UPDATE = URL_HOME + "%s/update"

        FEATURED_PATH = BASE_URL + FEATURED_PATH
    }

    open fun showSignupInfo(position: Int) {}

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

    val loginVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {

                    if (delegate != null) {
                        val d: MemberVC = delegate as MemberVC
                        d.loginout()
                    }

//                    val frags = supportFragmentManager.fragments
//                    for (frag in frags) {
//                        val memberFragment = frag as? MemberFragment
//                        memberFragment?.loginout()
//                    }
                }
            }
        }
    }

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

    val updatePasswordVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    if (delegate != null) {
                        val m: MemberVC = delegate as MemberVC
                        member.isLoggedIn = false
                        member.reset()
                        m.loginout()
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

//    val selectPriceUnitVC = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
//        if (res.resultCode == Activity.RESULT_OK) {
//
//            if (res.data != null) {
//                val i: Intent? = res.data
//
//                if (i != null) {
//                    val key: String = PRICE_UNIT_KEY
//                    var selected: String = ""
//                    if (i.hasExtra("selected")) {
//                        selected = i.getStringExtra("selected")!!
//                    }
//
//                    //activity
//                    if (delegate != null) {
//                        delegate!!.singleSelected(key, selected)
//                    } else {
//                        //fragment
//                        able_type = "course"
//                        if (i.hasExtra("able_type")) {
//                            able_type = i.getStringExtra("able_type")!!
//                        }
//                        val f = getFragment()
//                        if (f != null) {
//                            f.singleSelected(key, selected)
//                        }
//                    }
//                }
//            }
//        }
//    }

//    override fun remove(indexPath: IndexPath) {}
//    override fun textChanged(str: String) {}
//    override fun switchChanged(pos: Int, b: Boolean) {}

//    class ConnectTask(val context: Context) : AsyncTask<Unit, Unit, String>() {
//
//        override fun doInBackground(vararg params: Unit?): String? {
//            val url = URL("https://google.com")
//            val httpClient = url.openConnection() as HttpURLConnection
//            httpClient.connectTimeout = 1000
//            httpClient.readTimeout = 1000
//            try {
//                if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
//                    //println("connection")
//                } else {
//                }
//            } catch (e: Exception) {
//                val handler = Handler(context.mainLooper)
//                handler.post {
//                    Alert.show(context, "警告", "沒有連接網路，所以無法使用此app") {
//                        exitProcess(-1)
//                    }
//                }
//            } finally {
//                httpClient.disconnect()
//            }
//            return null
//        }
//    }
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














