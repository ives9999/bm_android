package com.sportpassword.bm.Controllers

import android.animation.Animator
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.GradientDrawable
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.onesignal.OneSignal
import com.sportpassword.bm.Adapters.SearchItem
import com.sportpassword.bm.Fragments.CoachFragment
import com.sportpassword.bm.Fragments.TabFragment
import com.sportpassword.bm.Fragments.TeamFragment
import com.sportpassword.bm.Models.Area
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Models.SuperData
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import org.jetbrains.anko.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.system.exitProcess
import kotlinx.android.synthetic.main.mask.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class BaseActivity : AppCompatActivity(), View.OnFocusChangeListener {

    protected lateinit var refreshLayout: SwipeRefreshLayout
    protected lateinit var refreshListener: SwipeRefreshLayout.OnRefreshListener

    var screenWidth: Int = 0
    var density: Float = 0f

    var msg: String = ""
    var superDataLists: ArrayList<SuperData> = arrayListOf()

    protected var callbackManager: CallbackManager? = null

    val REQUEST_PHONE_CALL = 100
    var mobile: String = ""

    //for search
    var searchRows: ArrayList<HashMap<String, String>> = arrayListOf()
    var containerID: String = "constraintLayout"
    var citys: ArrayList<City> = arrayListOf()
    var citys_coach: ArrayList<City> = arrayListOf()
    var citys_team: ArrayList<City> = arrayListOf()
    var areas: ArrayList<Area> = arrayListOf()
    var air_condition: Boolean = false
    var bathroom: Boolean = false
    var parking: Boolean = false
    var days: ArrayList<Int> = arrayListOf()
    var times: HashMap<String, Any> = hashMapOf()
    var arenas: ArrayList<Arena> = arrayListOf()
    var degrees: ArrayList<DEGREE> = arrayListOf()
    var keyword: String = ""
    protected lateinit var searchAdapter: GroupAdapter<ViewHolder>
    var params: HashMap<String, Any> = hashMapOf()

    val LOGIN_REQUEST_CODE = 1
    val REGISTER_REQUEST_CODE = 2
    val VALIDATE_REQUEST_CODE = 3
    val SEARCH_REQUEST_CODE = 4

    var dataService: DataService = DataService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ConnectTask(this).execute()

        _setURLConstants()

        getScreenWidth()

        //member.reset()
        //member.print()
    }

//    protected fun printMember() {
//        for (a in Member::class.memberProperties) {
//            println("${a.name} = ${a.get(member)}")
//        }
//    }

    private fun _setURLConstants() {
        gSimulate = isEmulator()
        BASE_URL = if (gSimulate) LOCALHOST_BASE_URL else REMOTE_BASE_URL
        //println("os: " + BASE_URL)
        URL_HOME = BASE_URL + "/app/"
        URL_LIST = "${URL_HOME}%s"
        URL_SHOW = "${URL_HOME}%s/show/%s?device=app"
        URL_LOGIN = URL_HOME + "login"
        URL_FB_LOGIN = URL_HOME + "member/fb"
        URL_REGISTER = URL_HOME + "register"
        URL_FORGETPASSWORD = "$BASE_URL/member/forget_password"
        URL_CHANGE_PASSWORD = "$BASE_URL/member/change_password"
        URL_MEMBER_UPDATE = URL_HOME + "member/update"
        URL_EMAIL_VALIDATE = URL_HOME + "member/email_validate"
        URL_MOBILE_VALIDATE = URL_HOME + "member/mobile_validate"
        URL_SEND_EMAIL_VALIDATE = URL_HOME + "member/sendEmailValidate"
        URL_SEND_MOBILE_VALIDATE = URL_HOME + "member/sendMobileValidate"
        URL_MEMBER_GETONE = URL_HOME + "member/getOne"
        URL_MEMBER_BLACKLIST = URL_HOME + "member/blacklist"
        URL_CITYS = URL_HOME + "citys"
        URL_ARENA_BY_CITY_ID = URL_HOME + "arena_by_city"
        URL_ARENA_BY_CITY_IDS = URL_HOME + "arena_by_citys"
        URL_AREA_BY_CITY_IDS = URL_HOME + "area_by_citys"
        URL_TEAM_UPDATE = URL_HOME + "team/update"
        URL_UPDATE = URL_HOME + "%s/update"
        URL_DELETE = URL_HOME + "%s/delete"
        URL_TT = URL_HOME + "%s/tt"
        URL_ONE = "${URL_HOME}%s/one"
        URL_TEAM = URL_HOME + "team/"
        URL_TEAM_TEMP_PLAY = URL_TEAM + "tempPlay/onoff"
        URL_TEAM_TEMP_PLAY_LIST = URL_TEAM + "tempPlay/list"
        URL_TEAM_TEMP_PLAY_BLACKLIST = URL_TEAM + "tempPlay/blacklist"
        URL_TEAM_PLUSONE = BASE_URL + "/team/tempPlay/plusOne/"
        URL_TEAM_CANCELPLUSONE = BASE_URL + "/team/tempPlay/cancelPlusOne/"
        URL_TEAM_TEMP_PLAY_DATE = URL_TEAM + "tempPlay/date"
        URL_TEAM_TEMP_PLAY_DATE_PLAYER = URL_TEAM + "tempPlay/datePlayer"
    }

    protected fun setMyTitle(title: String) {
        val actionBar: ActionBar = supportActionBar!!
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.prev)

        val l: LinearLayout = LayoutInflater.from(this).inflate(R.layout.title_bar, null) as LinearLayout
        val titleView: TextView = l.findViewById<TextView>(R.id.myTitle)
        titleView.setText(title)
        //println(titleView)
        val display: Display = windowManager.defaultDisplay
        val size: Point = Point()
        display.getSize(size)
        val actionBarWidth: Int = size.x
        //println(actionBarWidth)

        titleView.measure(0, 0)
        val titleViewWidth = titleView.measuredWidth
        //println(titleViewWidth)


//        val dimensions: BitmapFactory.Options = BitmapFactory.Options()
//        dimensions.inJustDecodeBounds = true
//        val prev: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.prev, dimensions)
//        val prevWidth: Int = dimensions.outWidth
//        println(prevWidth)


        val params: ActionBar.LayoutParams = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
        params.leftMargin = (actionBarWidth/2) - (titleViewWidth/2) - 170

        actionBar.setCustomView(l, params)
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM)
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

    protected fun getScreenWidth() {
        val displayMetrics = resources.displayMetrics
        density = displayMetrics.density
        //println("density: " + density)
        //val width: Float = displayMetrics.widthPixels / density
        screenWidth = displayMetrics.widthPixels
    }

    override fun onSupportNavigateUp(): Boolean {
        hideKeyboard()
        finish()
        return true
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_PHONE_CALL -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    warning("沒有同意app撥打電話的權限，因此無法使用此功能")
                } else {
                    makeCall(mobile)
                }
                return
            }
        }
    }

    public fun goLogin() {
        val loginIntent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    public fun goRegister() {
        val registerIntent: Intent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }

    public fun goForgetPassword() {
        val forgetPasswordIntent: Intent = Intent(this, ForgetPasswordActivity::class.java)
        startActivity(forgetPasswordIntent)
    }

    public fun home(context: Context) {
        val intent : Intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    public fun goEdit(source: String, title: String="", token: String="") {
        val intent = Intent(this, EditVC::class.java)
        intent.putExtra("token", token)
        intent.putExtra("source", source)
        intent.putExtra("title", title)
        startActivity(intent)
    }

    public fun goTeamTempPlayEdit(token: String) {
        val intent = Intent(this, TeamTempPlayEditActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    public fun goManager(page: String) {
        if (!member.isLoggedIn) {
            Alert.show(this, "警告", "請先登入會員")
            return
        }
        val intent = Intent(this, ManagerVC::class.java)
        intent.putExtra("source", page)
        startActivity(intent)
    }
    public fun goManagerFunction(title: String, token: String, source: String) {
        val intent = Intent(this, ManagerFunctionVC::class.java)
        intent.putExtra("title", title)
        intent.putExtra("token", token)
        intent.putExtra("source", source)
        startActivity(intent)
    }
    public fun goTempPlayDate(name: String, token: String) {
        val intent = Intent(this, TempPlayDateVC::class.java)
        intent.putExtra("name", name)
        intent.putExtra("token", token)
        startActivity(intent)
    }
    public fun goTempPlayDatePlayer(date: String, name: String, token: String) {
        val intent = Intent(this, TempPlayDatePlayerVC::class.java)
        intent.putExtra("date", date)
        intent.putExtra("teamName", name)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    public fun goSearch(type: String) {
        //val intent = Intent(this)
    }

    public fun goDelete(page: String, token: String="") {
        Alert.delete(this, {
            Loading.show(mask)
            dataService.delete(this, page, token) { success ->
                Loading.hide(mask)
//                val teamUpdate = Intent(NOTIF_TEAM_UPDATE)
//                LocalBroadcastManager.getInstance(this).sendBroadcast(teamUpdate)
                goManager(page)
            }
        })
    }

    public fun goEditMember() {
        val accountIntent = Intent(this, AccountActivity::class.java)
        startActivity(accountIntent)
    }

    public fun goUpdatePassword() {
        val updatePasswordIntent = Intent(this, UpdatePasswordActivity::class.java)
        startActivity(updatePasswordIntent)

    }

    public fun goValidate(type: String) {
        val intent = Intent(this, ValidateActivity::class.java)
        intent.putExtra("type", type)
        startActivityForResult(intent, VALIDATE_REQUEST_CODE)
    }

    public fun goTempPlaySignupOne(teamId: Int, teamToken: String, teamName: String, near_date: String, memberToken: String) {
        val i = Intent(this, TempPlaySignupOneVC::class.java)
        i.putExtra("id", teamId)
        i.putExtra("name", teamName)
        i.putExtra("token", teamToken)
        i.putExtra("near_date", near_date)
        i.putExtra("memberToken", memberToken)
        startActivity(i)
    }

    public fun goBlackList() {
        val intent = Intent(this, BlackListVC::class.java)
        startActivity(intent)
    }

    public fun goArena() {
        val i = Intent(this, ArenaVC::class.java)
        i.putExtra("type", "arena")
        i.putExtra("titleField", "name")
        startActivity(i)

    }

    public fun goCourse() {
        val i = Intent(this, CourseVC::class.java)
        i.putExtra("type", "course")
        i.putExtra("titleField", "title")
        startActivity(i)

    }

    public fun goTimeTable(source: String) {
        val i = Intent(this, TimeTableVC::class.java)
        i.putExtra("source", source)
        startActivity(i)
    }


    protected fun getAllChildrenBFS(v: View): List<View> {
        var visited: ArrayList<View> = arrayListOf()
        var unvisited: ArrayList<View> = arrayListOf()
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
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected fun loginFB() {
        val playerID = _getPlayerID()
        //val context = this
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logOut()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email,public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager,
                object: FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        MemberService.FBLogin(this@BaseActivity, playerID) { success ->
                            if (success) {
                                LocalBroadcastManager.getInstance(this@BaseActivity).sendBroadcast(memberDidChangeIntent)
                                finish()
                            } else {
                                val msg = MemberService.msg
                                Alert.show(this@BaseActivity,"錯誤", msg)
                            }
                        }
                    }

                    override fun onCancel() {
                        println("cancel")
                    }

                    override fun onError(error: FacebookException?) {
                        println(error)

                    }
                }
        )
    }

    protected fun _getPlayerID(): String {
        var playerID = ""
        OneSignal.idsAvailable { userId, registrationId ->
            playerID = userId
        }
        return playerID
    }

    protected fun _getMemberOne(token: String, completion: CompletionHandler) {
        if (member.isLoggedIn) {
            Loading.show(mask)
            MemberService.getOne(this, token) { success ->
                Loading.hide(mask)
                if (success) {
                    completion(true)
                } else {
                    Alert.show(this, "錯誤", MemberService.msg)
                    completion(false)
                }
            }
        } else {
            warning("沒有登入")
        }
    }

//    protected fun memberDidChange() {
//        val memberDidChange = Intent(NOTIF_MEMBER_DID_CHANGE)
//        LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChange)
//    }

    protected val memberDidChange = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            refreshMember() { success ->

            }
        }
    }

    protected fun refreshMember(completion: CompletionHandler) {
        _getMemberOne(member.token, completion)
    }

    protected fun _updatePlayerIDWhenIsNull() {
        _getMemberOne(member.token) { success ->
            if (success) {
                member.justGetMemberOne = true
                if (member.player_id.length == 0) {
                    _updatePlayerID()
                }
            }
        }
    }

    protected fun _updatePlayerID() {
        val player_id = _getPlayerID()
        MemberService.update(this, member.id, PLAYERID_KEY, player_id) { success ->
            if (success) {
                member.player_id = player_id
            }
        }
    }

    protected fun _getTeamManagerList(completion: CompletionHandler) {
        Loading.show(mask)
        val filter1: Array<Any> = arrayOf("channel", "=", CHANNEL)
        val filter2: Array<Any> = arrayOf("manager_id", "=", member.id)
        val filter: Array<Array<Any>> = arrayOf(filter1, filter2)
        TeamService.getList(this, "team", "name", hashMapOf(), 1, 100, filter) { success ->
            Loading.hide(mask)
            if (success) {
                superDataLists = TeamService.superDataLists
                completion(true)
            } else {
                msg = TeamService.msg
                completion(false)
            }
        }
    }

    protected fun addBlackList(memberName: String, memberToken: String, teamToken: String) {
        warning("是否真的要將球友"+memberName+"設為黑名單\n之後可以解除", "取消", "加入", {
            reasonBox(memberToken, teamToken)
        })

    }
    protected fun reasonBox(memberToken: String, teamToken: String) {
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
    protected fun _addBlackList(reason: String, memberToken: String, teamToken: String) {
        Loading.show(mask)
        TeamService.addBlackList(this, teamToken, memberToken, member.token, reason) { success ->
            Loading.hide(mask)
            if (success) {
                info("加入黑名單成功")
            } else {
                warning(TeamService.msg)
            }
        }
    }

    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }

    fun TextView.setMyText(value: String, default: String="") {
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

    fun showSearchPanel(view: View) {
        val tag = view.tag as String
        when (tag) {
            "coach" -> {
                containerID = "coach_container"
                val frag = getFragment(tag) as CoachFragment
                searchRows = frag._searchRows
            }
            "team" -> {
                containerID = "team_container"
                val frag = getFragment(tag) as TeamFragment
                searchRows = frag._searchRows
            }
            "arena" -> {
                containerID = "constraintLayout"
            }
            "course" -> {
                containerID = "constraintLayout"
            }
        }
        mask()
        addLayer(tag)
    }

    protected fun mask() {
        val alpha = 0.8f
        val duration: Long = 200

        val rootView = window.decorView.rootView
        val parentID = resources.getIdentifier(containerID, "id", packageName)
        val parent = rootView.findViewById<ConstraintLayout>(parentID)
        var mask = parent.findViewById<LinearLayout>(R.id.MyMask)
        if (mask == null) {
            mask = LinearLayout(this)
            mask.id = R.id.MyMask
            mask.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            mask.backgroundColor = Color.parseColor("#ffffff")
            mask.alpha = 0f
            mask.setOnClickListener {
                unmask()
            }
            parent.addView(mask)
        }
        mask.animate().setDuration(duration).alpha(alpha).setListener(object: Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator?) {
                mask.visibility = View.VISIBLE
            }
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
    }

    protected fun unmask() {
        val duration: Long = 200
        var mask = getMask()
        if (mask != null) {
            mask.visibility = View.GONE
            mask.visibility = View.VISIBLE
            mask.animate().setDuration(duration).alpha(0f).setListener(object: Animator.AnimatorListener {
                override fun onAnimationEnd(p0: Animator?) {
                    mask.visibility = View.INVISIBLE
                }
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationStart(p0: Animator?) {}
            })
        }
    }

    protected fun addLayer(page: String) {

        val duration: Long = 500
        val rootView = window.decorView.rootView
        val parentID = resources.getIdentifier(containerID, "id", packageName)
        val parent = rootView.findViewById<ConstraintLayout>(parentID)
        //val p = parent.layoutParams as ViewPager.LayoutParams
        val w = parent.measuredWidth
        val h = parent.measuredHeight

        val containerView = LinearLayout(this)
        containerView.orientation = LinearLayout.VERTICAL
        val padding = 80
        val headerHeight = 100
        val lp = LinearLayout.LayoutParams(w-(2*padding), h-headerHeight)
        //lp.setMargins(padding, headerHeight, padding, 0)
        lp.setMargins(padding, h, padding, 0)
        containerView.layoutParams = lp
        containerView.backgroundColor = Color.BLACK

        addSearchTableView(page, containerView, w, padding)
        addSubmitBtn(page, containerView, w, padding)

        val mask = getMask()
        mask.addView(containerView)

        val h1 = containerView.layoutParams.height
        containerView.animate().setDuration(duration).translationY((0-h1).toFloat()).setListener(object: Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator?) {
                //containerView.layoutParams.height = containerView.height
                //containerView.visibility = View.VISIBLE
            }
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
    }

    protected fun addSearchTableView(page: String, containerView: LinearLayout, w: Int, padding: Int) {
        val searchTableView = RecyclerView(this)
        searchTableView.id = R.id.SearchRecycleItem
        val lp1 = RecyclerView.LayoutParams(w-(2*padding), 1000)
        lp1.setMargins(0, 30, 0, 0)
        searchTableView.layoutParams = lp1
        searchTableView.layoutManager = LinearLayoutManager(this)
        searchAdapter = GroupAdapter<ViewHolder>()
        searchAdapter.setOnItemClickListener { item, view ->
            val searchItem = item as SearchItem
            val row = searchItem.row
            if (searchItem.switch == false) {
                prepareSearch(row, page)
            }
        }
        val rows = generateSearchItems()
        searchAdapter.addAll(rows)

        searchTableView.adapter = searchAdapter
        containerView.addView(searchTableView)
    }

    protected fun addSubmitBtn(page: String, containerView: LinearLayout, w:Int, padding: Int) {
        val submitBtn = Button(this)
        val submitBtnWidth = 360
        val lp2 = LinearLayout.LayoutParams(submitBtnWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp2.setMargins((w-(2*padding)-submitBtnWidth)/2, 100, 0, 0)
        submitBtn.layoutParams = lp2
        submitBtn.text = "提交"
        submitBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
        submitBtn.setOnClickListener {
            searchSubmit(page)
        }
        val myRed = ContextCompat.getColor(this, R.color.MY_RED)
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(myRed)
        shape.cornerRadius = 82f

        submitBtn.backgroundDrawable = shape
        submitBtn.setTextColor(Color.WHITE)

        containerView.addView(submitBtn)
    }

    protected fun prepareSearch(idx: Int, page: String) {
        var intent = Intent(this, EditItemActivity::class.java)
        val row = searchRows.get(idx)
        var key = ""
        if (row.containsKey("key")) {
            key = row["key"]!!
        }
        when (key) {
            CITY_KEY -> {
                intent.putExtra("key", CITY_KEY)
                intent.putExtra("source", "search")
                intent.putExtra("page", page)
                intent.putExtra("type", "simple")
                intent.putExtra("select", "multi")
                if (page == "coach") {
                    citys = citys_coach
                } else if (page == "team") {
                    citys = citys_team
                }
                intent.putParcelableArrayListExtra("citys", citys)
            }
            ARENA_KEY -> {
                if (citys.size == 0) {
                    Alert.warning(this, "請先選擇縣市")
                    return
                }
                intent.putExtra("key", ARENA_KEY)
                intent.putExtra("source", "search")
                intent.putExtra("type", "simple")
                intent.putExtra("select", "multi")

                var citysForArena: ArrayList<Int> = arrayListOf()
                for (city in citys) {
                    citysForArena.add(city.id)
                }
                intent.putIntegerArrayListExtra("citys_for_arena", citysForArena)
                intent.putParcelableArrayListExtra("arenas", arenas)
            }
            TEAM_DAYS_KEY -> {
                intent.putExtra("key", TEAM_DAYS_KEY)
                intent.putExtra("source", "search")
                intent.putIntegerArrayListExtra("days", days)
            }
            TEAM_PLAY_START_KEY -> {
                intent.putExtra("key", TEAM_PLAY_START_KEY)
                intent.putExtra("source", "search")
//                        times["time"] = "09:00"
                times["type"] = SELECT_TIME_TYPE.play_start
                intent.putExtra("times", times)
            }
            TEAM_DEGREE_KEY -> {
                intent.putExtra("key", TEAM_DEGREE_KEY)
                intent.putExtra("source", "search")
                intent.putExtra("degrees", degrees)
            }
            AREA_KEY -> {
                if (citys.size == 0) {
                    Alert.warning(this, "請先選擇縣市")
                    return
                }
                intent.putExtra("key", AREA_KEY)
                intent.putExtra("source", "search")
                intent.putExtra("type", "simple")
                intent.putExtra("select", "multi")

                var citysForArea: ArrayList<Int> = arrayListOf()
                for (city in citys) {
                    citysForArea.add(city.id)
                }
                intent.putIntegerArrayListExtra("citys_for_area", citysForArea)
                intent.putParcelableArrayListExtra("areas", areas)
            }
        }
        startActivityForResult(intent!!, SEARCH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var value = "全部"
        var idx = 0
        when (requestCode) {
            SEARCH_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val key = data!!.getStringExtra("key")
                    val page = data!!.getStringExtra("page")
                    when (key) {
                        CITY_KEY -> {
                            idx = 1
                            citys = data!!.getParcelableArrayListExtra("citys")
                            if (citys.size > 0) {
                                var arr: ArrayList<String> = arrayListOf()
                                for (city in citys) {
                                    arr.add(city.name)
                                }
                                value = arr.joinToString()
                            } else {
                                value = "全部"
                            }
                            if (page == "coach") {
                                citys_coach = citys
                            } else if (page == "team") {
                                citys_team = citys
                            }
                        }
                        ARENA_KEY -> {
                            idx = 2
                            arenas = data!!.getParcelableArrayListExtra("arenas")
                            if (arenas.size > 0) {
                                var arr: ArrayList<String> = arrayListOf()
                                for (arena in arenas) {
                                    arr.add(arena.name)
                                }
                                value = arr.joinToString()
                            } else {
                                value = "全部"
                            }
                        }
                        TEAM_DAYS_KEY -> {
                            idx = 3
                            days = data!!.getIntegerArrayListExtra("days")
                            if (days.size > 0) {
                                var arr: ArrayList<String> = arrayListOf()
                                val gDays = Global.days
                                for (day in days) {
                                    for (gDay in gDays) {
                                        if (day == gDay.get("value")!! as Int) {
                                            arr.add(gDay.get("simple_text")!! as String)
                                            break
                                        }
                                    }
                                }
                                value = arr.joinToString()
                            } else {
                                value = "全部"
                            }
                        }
                        TEAM_PLAY_START_KEY -> {
                            idx = 4
                            times = data!!.getSerializableExtra("times") as HashMap<String, Any>
                            if (times.containsKey("time")) {
                                value = times["time"]!! as String
                            } else {
                                value = "全部"
                            }
                        }
                        TEAM_DEGREE_KEY -> {
                            idx = 5
                            degrees = data!!.getSerializableExtra("degrees") as ArrayList<DEGREE>
                            if (degrees.size > 0) {
                                var arr: ArrayList<String> = arrayListOf()
                                degrees.forEach {
                                    arr.add(it.value)
                                }
                                value = arr.joinToString()
                            } else {
                                value = "全部"
                            }
                        }
                        AREA_KEY -> {
                            idx = 2
                            areas = data!!.getParcelableArrayListExtra("areas")
                            if (areas.size > 0) {
                                var arr: ArrayList<String> = arrayListOf()
                                for (area in areas) {
                                    arr.add(area.name)
                                }
                                value = arr.joinToString()
                            } else {
                                value = "全部"
                            }
                        }
                    }
                    searchRows[idx]["detail"] = value
                    val rows = generateSearchItems()
                    searchAdapter.update(rows)
                }
            }
        }
    }

    fun generateSearchItems(): ArrayList<SearchItem> {
        val rows: ArrayList<SearchItem> = arrayListOf()
        for (i in 0..searchRows.size-1) {
            val row = searchRows[i] as HashMap<String, String>
            val title = row.get("title")!!
            val detail = row.get("detail")!!
            var bSwitch = false
            if (row.containsKey("switch")) {
                bSwitch = row.get("switch")!!.toBoolean()
            }
            rows.add(SearchItem(title, detail, bSwitch, -1, i, { k ->
                keyword = k
            }, { idx, b ->
                when (idx){
                    3 -> air_condition = b
                    4 -> bathroom = b
                    5 -> parking = b
                }
            })
            )
        }

        return rows
    }

    fun getMask(): LinearLayout {
        val rootView = window.decorView.rootView
        val parentID = resources.getIdentifier(containerID, "id", packageName)
        val parent = rootView.findViewById<ConstraintLayout>(parentID)
        return parent.findViewById<LinearLayout>(R.id.MyMask)
    }

    protected fun getFragment(page: String): TabFragment? {
        val frags = supportFragmentManager.fragments
        var _frag: TabFragment? = null
        for (frag in frags) {
            if (page == "coach" && frag::class == CoachFragment::class) {
                _frag = frag as CoachFragment
                break
            }
            if (page == "team" && frag::class == TeamFragment::class) {
                _frag = frag as TeamFragment
                break
            }
        }

        return _frag
    }

    fun prepareParams(city_type: String="simple") {
        val city_ids: ArrayList<Int> = arrayListOf()
        if (citys.size > 0) {
            citys.forEach {
                city_ids.add(it.id)
            }
        }
        if (city_ids.size > 0) {
            params["city_id"] = city_ids
            params["city_type"] = city_type
        }

        val area_ids: ArrayList<Int> = arrayListOf()
        if (areas.size > 0) {
            areas.forEach  {
                area_ids.add(it.id)
            }
        }
        if (area_ids.size > 0) {
            params["area_id"] = area_ids
        }

        if (air_condition) { params["air_condition"] = 1 } else { params["air_condition"] = 0 }
        if (bathroom) { params["bathroom"] = 1 } else { params["bathroom"] = 0 }
        if (parking) { params["parking"] = 1 } else { params["parking"] = 0 }

        if (days.size > 0) {
            params["play_days"] = days
        }

        if (times.size > 0) {
            if (times.containsKey("time")) {
                params["use_date_range"] = 1
                val play_start = times["time"]!! as String
                val time = play_start + ":00 - 24:00:00"
                params["play_time"] = time
            }
        }

        var arena_ids: ArrayList<Int> = arrayListOf()
        if (arenas.size > 0) {
            arenas.forEach {
                arena_ids.add(it.id)
            }
        }
        if (arena_ids.size > 0) {
            params["arena_id"] = arena_ids
        }

        var _degrees: ArrayList<String> = arrayListOf()
        if (degrees.size > 0) {
            degrees.forEach {
                _degrees.add(it.toString())
            }
        }
        if (_degrees.size > 0) {
            params["degree"] = _degrees
        }

        params["k"] = keyword
    }

    fun resetParams() {
        citys.clear()
        areas.clear()
        arenas.clear()
        days.clear()
        degrees.clear()
        times.clear()
        keyword = ""
    }


    protected fun searchSubmit(page: String) {
        unmask()
        prepareParams()
        if (page == "coach") {
            val frag = getFragment(page) as CoachFragment
            frag.refresh()
        } else if (page == "team") {
            val frag = getFragment(page) as TeamFragment
            frag.refresh()
        } else {
            refresh()
        }
    }

    protected fun warning(msg: String) {
        Alert.show(this, "警告", msg)
    }
    protected fun warning(msg: String, showCloseButton: Boolean=false, buttonTitle: String, buttonAction: ()->Unit) {
        Alert.show(this, "警告", msg, showCloseButton, buttonTitle, buttonAction)
    }
    protected fun warning(msg: String, closeButtonTitle: String, buttonTitle: String, buttonAction: ()->Unit) {
        Alert.show(this, "警告", msg, closeButtonTitle, buttonTitle, buttonAction)
    }
    protected fun info(msg: String) {
        Alert.show(this, "訊息", msg)
    }
    protected fun info(msg: String, closeButtonTitle: String, buttonTitle: String, buttonAction: ()->Unit) {
        Alert.show(this, "訊息", msg, closeButtonTitle, buttonTitle, buttonAction)
    }

    open protected fun setRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            refresh()
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }
    open protected fun closeRefresh() {
        refreshLayout.isRefreshing = false
    }
    open protected fun refresh() {}

    open protected fun setTeamData(imageView: ImageView?=null) {
    }

    class ConnectTask(val context: Context) : AsyncTask<Unit, Unit, String>() {

        override fun doInBackground(vararg params: Unit?): String? {
            val url = URL("https://google.com")
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.connectTimeout = 1000
            httpClient.readTimeout = 1000
            try {
                if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                    //println("connection")
                } else {
                }
            } catch (e: Exception) {
                val handler = Handler(context.mainLooper)
                handler.post {
                    Alert.show(context, "警告", "沒有連接網路，所以無法使用此app") {
                        exitProcess(-1)
                    }
                }
            } finally {
                httpClient.disconnect()
            }
            return null
        }

    }

}














