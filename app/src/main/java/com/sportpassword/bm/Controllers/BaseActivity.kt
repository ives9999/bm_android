package com.sportpassword.bm.Controllers

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.core.widget.NestedScrollView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.webkit.*
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
import com.sportpassword.bm.Adapters.SearchItemDelegate
import com.sportpassword.bm.Fragments.CoachFragment
import com.sportpassword.bm.Fragments.CourseFragment
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
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class BaseActivity : AppCompatActivity(), View.OnFocusChangeListener, SearchItemDelegate {

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
    var weekdays: ArrayList<Int> = arrayListOf()
    var times: HashMap<String, Any> = hashMapOf()
    var arenas: ArrayList<Arena> = arrayListOf()
    var degrees: ArrayList<DEGREE> = arrayListOf()
    var keyword: String = ""
    lateinit var searchAdapter: GroupAdapter<ViewHolder>
    var params: HashMap<String, Any> = hashMapOf()

    val LOGIN_REQUEST_CODE = 1
    val REGISTER_REQUEST_CODE = 2
    val VALIDATE_REQUEST_CODE = 3
    val SEARCH_REQUEST_CODE = 4
    val SEARCH_REQUEST_CODE1 = 5

    var dataService: DataService = DataService()
    
    //for layer
    var layerMask: LinearLayout? = null
    var layerBlackView: RelativeLayout? = null
    var layerScrollView: NestedScrollView? = null
    var layerContainerView: LinearLayout? = null
    var layerSubmitBtn: Button? = null
    var layerCancelBtn: Button? = null
    var layerDeleteBtn: Button? = null
    var layerRightLeftPadding: Int = 80
    var layerTopPadding: Int = 100
    lateinit var layerButtonLayout: LinearLayout
    var layerBtnCount: Int = 2
    var layerVisibility: Boolean = false

    val body_css = "<style>body{background-color:#000;padding-left:8px;padding-right:8px;margin-top:0;padding-top:0;color:#888888;font-size:18px;}a{color:#a6d903;}</style>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ConnectTask(this).execute()

        _setURLConstants()

        getScreenWidth()

        //OneSignal.setSubscription(true)
        //OneSignal.promptLocation() prompt location auth when location auth is close

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
        //gSimulate = true
        BASE_URL = if (gSimulate) LOCALHOST_BASE_URL else REMOTE_BASE_URL
        //println("os: " + BASE_URL)

        URL_HOME = BASE_URL + "/app/"

        URL_AREA_BY_CITY_IDS = URL_HOME + "area_by_citys"
        URL_ARENA_BY_CITY_ID = URL_HOME + "arena_by_city"
        URL_ARENA_BY_CITY_IDS = URL_HOME + "arena_by_citys"
        URL_CANCEL_SIGNUP = URL_HOME + "%s/cancelSignup/%d"
        URL_CHANGE_PASSWORD = "$BASE_URL/member/change_password"
        URL_CITYS = URL_HOME + "citys"
        URL_COURSE_LIST = URL_HOME + "course/list"
        URL_DELETE = URL_HOME + "%s/delete"
        URL_EMAIL_VALIDATE = URL_HOME + "member/email_validate"
        URL_FB_LOGIN = URL_HOME + "member/fb"
        URL_FORGETPASSWORD = "$BASE_URL/member/forget_password"
        URL_LIST = "${URL_HOME}%s"
        URL_LOGIN = URL_HOME + "login"
        URL_MEMBER_BLACKLIST = URL_HOME + "member/blacklist"
        URL_MEMBER_GETONE = URL_HOME + "member/getOne"
        URL_MEMBER_UPDATE = URL_HOME + "member/update"
        URL_MOBILE_VALIDATE = URL_HOME + "member/mobile_validate"
        URL_ONE = "${URL_HOME}%s/one"
        URL_REGISTER = URL_HOME + "register"
        URL_SEND_EMAIL_VALIDATE = URL_HOME + "member/sendEmailValidate"
        URL_SEND_MOBILE_VALIDATE = URL_HOME + "member/sendMobileValidate"
        URL_SHOW = "${URL_HOME}%s/show/%s?device=app"
        URL_SIGNUP = URL_HOME + "%s/signup/%s"
        URL_SIGNUP_DATE = "${URL_HOME}%s/signup_date/%s"
        URL_SIGNUP_LIST = "${URL_HOME}%s/signup_list"
        URL_TEAM = URL_HOME + "team/"
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
        prev()
        return true
    }

    fun permissionExist(permission: String): Boolean {
        val permission = ContextCompat.checkSelfPermission(this, permission)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            return false
        } else {
            return true
        }
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

    fun prev() {
        hideKeyboard()
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
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
        val intent = Intent(this, EditVC1::class.java)
        intent.putExtra("token", token)
        intent.putExtra("source", source)
        intent.putExtra("title", title)
        startActivity(intent)
    }

    fun goTeamTempPlayEdit(token: String) {
        val intent = Intent(this, TeamTempPlayEditActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    fun manager(view: View) {
        goManager(view.tag as String)
    }

    fun goManager(page: String) {
        if (!member.isLoggedIn) {
            Alert.show(this, "警告", "請先登入會員")
            return
        }
        var intent: Intent? = null
        if (page == "course") {
            intent = Intent(this, ManagerCourseVC1::class.java)
            intent.putExtra("manager_token", member.token)
        } else {
            intent = Intent(this, ManagerVC::class.java)
            intent.putExtra("source", page)
        }
        if (intent != null) {
            startActivity(intent)
        }
    }
    fun goManagerFunction(title: String, token: String, source: String) {
        val intent = Intent(this, ManagerFunctionVC1::class.java)
        intent.putExtra("title", title)
        intent.putExtra("token", token)
        intent.putExtra("source", source)
        startActivity(intent)
    }

    fun goEditCourse(title: String, course_token: String, coach_token:String) {
        val intent = Intent(this, EditCourseVC1::class.java)
        intent.putExtra("title", title)
        intent.putExtra("course_token", course_token)
        intent.putExtra("coach_token", coach_token)

        startActivityForResult(intent, GENERAL_REQUEST_CODE)
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

    fun goDelete1(page: String, token: String="") {
        Alert.delete(this, {
            Loading.show(mask)
            dataService.delete(this, page, token) { success ->
                Loading.hide(mask)
                refresh()
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

    public fun goTempPlaySignupOne(teamId: Int, teamToken: String, teamName: String, near_date: String, memberToken: String, status: String, off_at: String) {
        val i = Intent(this, TempPlaySignupOneVC::class.java)
        i.putExtra("id", teamId)
        i.putExtra("name", teamName)
        i.putExtra("token", teamToken)
        i.putExtra("near_date", near_date)
        i.putExtra("memberToken", memberToken)
        i.putExtra("status", status)
        i.putExtra("off_at", off_at)
        startActivity(i)
    }

    fun goBlackList() {
        val intent = Intent(this, BlackListVC::class.java)
        startActivity(intent)
    }

    fun goCoach() {
        val i = Intent(this, CoachVC::class.java)
        i.putExtra("type", "coach")
        i.putExtra("titleField", "name")
        startActivity(i)

    }

    fun goArena() {
        val i = Intent(this, ArenaVC::class.java)
        i.putExtra("type", "arena")
        i.putExtra("titleField", "name")
        startActivity(i)

    }

    fun goTeach() {
        val i = Intent(this, TeachVC::class.java)
        i.putExtra("type", "teach")
        i.putExtra("titleField", "title")
        startActivity(i)

    }

    fun goTimeTable(source: String, token: String) {
        val i = Intent(this, TimeTableVC::class.java)
        i.putExtra("source", source)
        i.putExtra("token", token)
        startActivity(i)
    }

    fun goCourse(name: String, token: String) {
        val i = Intent(this, ManagerCourseVC1::class.java)
        i.putExtra("token", token)
        i.putExtra("name", name)
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
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logOut()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email,public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager,
                object: FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        MemberService.FBLogin(this@BaseActivity, playerID) { success ->
                            if (success) {
                                //Session.loginReset = true
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
                        //println(error)

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

    public fun _getMemberOne(token: String, completion: CompletionHandler) {
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
                containerID = "constraintLayout"
            }
            "team" -> {
                containerID = "team_container"
                val frag = getFragment(tag) as TeamFragment
                searchRows = frag._searchRows
            }
            "arena" -> {
                containerID = "constraintLayout"
            }
            "teach" -> {
                containerID = "constraintLayout"
            }
            "course" -> {
                containerID = "course_container"
                val frag = getFragment(tag) as CourseFragment
                searchRows = frag._searchRows
            }
        }
        mask()
        addLayer(tag)
    }

    protected fun mask() {
//        val alpha = 0.8f
//        val duration: Long = 200

//        var mask = parent.findViewById<LinearLayout>(R.id.MyMask)
        if (layerMask == null) {
            layerMask = LinearLayout(this)
            layerMask!!.id = R.id.MyMask
            layerMask!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            //mask.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            layerMask!!.backgroundColor = Color.parseColor("#ffffff")
            //0是完全透明
            layerMask!!.alpha = 0.9f
            layerMask!!.setOnClickListener {
                unmask()
            }
            val parent = getMyParent()
            parent.addView(layerMask)
        } else {
            layerMask!!.visibility = View.VISIBLE
        }
//        mask.animate().setDuration(duration).alpha(alpha).setListener(object: Animator.AnimatorListener {
//            override fun onAnimationEnd(p0: Animator?) {
//                mask.visibility = View.VISIBLE
//            }
//            override fun onAnimationRepeat(p0: Animator?) {}
//            override fun onAnimationCancel(p0: Animator?) {}
//            override fun onAnimationStart(p0: Animator?) {}
//        })
    }

    public fun unmask() {
        val duration: Long = 500
//        var mask = getMask()
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

//            layerScrollView!!.animate().setDuration(duration).alpha(0f).setListener(object: Animator.AnimatorListener {
//                override fun onAnimationEnd(p0: Animator?) {
//                    mask.visibility = View.GONE
//                    mask.removeAllViews()
//                    layerScrollView = null
//                }
//                override fun onAnimationRepeat(p0: Animator?) {}
//                override fun onAnimationCancel(p0: Animator?) {}
//                override fun onAnimationStart(p0: Animator?) {}
//            })
        }
    }

    protected fun removeLayerChildViews() {
        val parent = getMyParent()
        layerContainerView!!.removeAllViews()
        layerScrollView!!.removeAllViews()
        layerBlackView!!.removeAllViews()
        layerContainerView = null
        layerMask!!.visibility = View.GONE
        layerMask!!.removeAllViews()
        layerScrollView = null
        layerBlackView = null
        parent.removeView(layerMask)
        layerMask = null
    }

    protected fun addLayer(page: String) {

        val parent = getMyParent()
        val w = parent.measuredWidth
        val h = parent.measuredHeight
        if (layerBlackView == null) {

            val lp = RelativeLayout.LayoutParams(w - (2 * layerRightLeftPadding), ViewGroup.LayoutParams.MATCH_PARENT)
            lp.setMargins(layerRightLeftPadding, layerTopPadding, layerRightLeftPadding, 0)
            layerBlackView = RelativeLayout(this)
            layerBlackView!!.layoutParams = lp
            layerBlackView!!.backgroundColor = Color.BLACK
            layerMask!!.addView(layerBlackView)

            val lp1 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.setMargins(0, 0, 0, 0)
            layerScrollView = NestedScrollView(this)
            layerScrollView!!.layoutParams = lp1
            layerScrollView!!.setOnScrollChangeListener(object: NestedScrollView.OnScrollChangeListener {
                override fun onScrollChange(p0: NestedScrollView?, p1: Int, p2: Int, p3: Int, p4: Int) {
                    if (currentFocus != null) {
                        currentFocus.clearFocus()
                    }
                }

            })
//            layerScrollView!!.layoutParams = lp
//            layerScrollView!!.backgroundColor = Color.GREEN

//            val mask = getMask()
            layerBlackView!!.addView(layerScrollView)

            val lp2 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp2.setMargins(0, 0, 0, 0)
            layerContainerView = LinearLayout(this)
            layerContainerView!!.orientation = LinearLayout.VERTICAL
            layerContainerView!!.layoutParams = lp2
//            layerContainerView!!.backgroundColor = Color.RED
            layerScrollView!!.addView(layerContainerView)

            _addLayer(page)
//            val l = LinearLayout(this)
//            l.orientation = LinearLayout.VERTICAL
//            l.addView(layerButtonLayout)
//            layerContainerView!!.addView(l)

            //append bottom space
            val bottomLayout = LinearLayout(this)
            val lpx = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200)
            bottomLayout.layoutParams = lpx
            layerContainerView!!.addView(bottomLayout)
        }


        val animate = TranslateAnimation(layerRightLeftPadding.toFloat(), layerRightLeftPadding.toFloat(), h.toFloat(), layerTopPadding.toFloat())
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
            }
            override fun onAnimationStart(p0: Animation?) {
            }
        })
        animate.duration = 500
        animate.fillAfter = true
        //layerScrollView!!.startAnimation(animate)
        layerVisibility = true
    }

    open fun _addLayer(page: String) {
        addSearchTableView(page)
        layerAddButtonLayout()
        layerBtnCount = 2
        layerAddSubmitBtn(page)
        layerAddCancelBtn()
    }

    protected fun addSearchTableView(page: String) {
        val parent = getMyParent()
        val w = parent.measuredWidth
        val searchTableView = RecyclerView(this)
        searchTableView.id = R.id.SearchRecycleItem
        val padding: Int = 80
        val lp1 = RecyclerView.LayoutParams(w-(2*padding), 1000)
        lp1.setMargins(0, 30, 0, 30)
        searchTableView.layoutParams = lp1
        searchTableView.layoutManager = LinearLayoutManager(this)
        searchTableView.backgroundColor = Color.TRANSPARENT
        searchAdapter = GroupAdapter<ViewHolder>()
        searchAdapter.setOnItemClickListener { item, view ->
            val searchItem = item as SearchItem
            val row = searchItem.row
            if (page == "course") {
                prepareSearch1(row, page)
            } else {
                if (searchItem.switch == false) {
                    prepareSearch(row, page)
                }
            }
        }
        val rows = generateSearchItems(page)
        searchAdapter.addAll(rows)

        searchTableView.adapter = searchAdapter
        layerContainerView!!.addView(searchTableView)
    }

    protected fun layerAddButtonLayout() {
        layerButtonLayout = LinearLayout(this)
        val lp = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150)
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        layerButtonLayout.layoutParams = lp
        val color = ContextCompat.getColor(this, R.color.MY_GREEN)
        layerButtonLayout.backgroundColor = color
        layerButtonLayout.gravity = Gravity.CENTER
        layerButtonLayout.orientation = LinearLayout.HORIZONTAL
        layerBlackView!!.addView(layerButtonLayout)
    }

    protected fun layerAddSubmitBtn(page: String) {
        layerSubmitBtn = layoutInflater.inflate(R.layout.submit_button, null) as Button
        //layerSubmitBtn = Button(this, null, R.style.submit_button)
//        val submitBtnWidth = 260
        val lp2 = LinearLayout.LayoutParams(260, 90)
//        lp2.setMargins(16, 0, 0, 0)
        layerSubmitBtn!!.layoutParams = lp2
        //layerSubmitBtn!!.text = "送出"
//        layerSubmitBtn!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
//        val myRed = ContextCompat.getColor(this, R.color.MY_RED)
//        val shape = GradientDrawable()
//        shape.shape = GradientDrawable.RECTANGLE
//        shape.setColor(myRed)
//        shape.cornerRadius = 62f
        layerSubmitBtn!!.setOnClickListener {
            layerSubmit(page)
        }

//        layerSubmitBtn!!.backgroundDrawable = shape
//        layerSubmitBtn!!.setTextColor(Color.WHITE)

        layerButtonLayout.addView(layerSubmitBtn)
    }

    protected fun layerAddCancelBtn() {
        layerCancelBtn = layoutInflater.inflate(R.layout.cancel_button, null) as Button
//        layerCancelBtn = Button(this)
//        val cancelBtnWidth = 260
        val lp2 = LinearLayout.LayoutParams(260, 90)
        lp2.setMargins(16, 0, 0, 0)
//        val center = (w-(2*padding)-cancelBtnWidth)/2
//        var x = center
//        when (layerBtnCount) {
//            2 -> x = center + 60
//        }
//        println(center)
//        println(x)
//        lp2.setMargins(16, 0, 16, 0)
        layerCancelBtn!!.layoutParams = lp2
//        layerCancelBtn!!.text = "取消"
//        layerCancelBtn!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
//        val myRed = ContextCompat.getColor(this, R.color.MY_RED)
//        val shape = GradientDrawable()
//        shape.shape = GradientDrawable.RECTANGLE
//        shape.setColor(myRed)
//        shape.cornerRadius = 82f
        layerCancelBtn!!.setOnClickListener {
            layerCancel()
        }

//        layerCancelBtn!!.backgroundDrawable = shape
//        layerCancelBtn!!.setTextColor(Color.WHITE)

        layerButtonLayout.addView(layerCancelBtn)
    }

    protected fun layerAddDeleteBtn() {
        layerDeleteBtn = layoutInflater.inflate(R.layout.delete_button, null) as Button
        //layerDeleteBtn = Button(this)
//        val deleteBtnWidth = 260
        val lp2 = LinearLayout.LayoutParams(260, 90)
        lp2.setMargins(16, 0, 0, 0)
        layerDeleteBtn!!.layoutParams = lp2
//        layerDeleteBtn!!.text = "刪除"
//        layerDeleteBtn!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
//        val myRed = ContextCompat.getColor(this, R.color.MY_RED)
//        val shape = GradientDrawable()
//        shape.shape = GradientDrawable.RECTANGLE
//        shape.setColor(myRed)
//        shape.cornerRadius = 82f
        layerDeleteBtn!!.setOnClickListener {
            layerDelete()
        }

//        layerDeleteBtn!!.backgroundDrawable = shape
//        layerDeleteBtn!!.setTextColor(Color.WHITE)

        layerButtonLayout.addView(layerDeleteBtn)
    }

//    fun getMask(): LinearLayout {
//        val parent = getMyParent()
//        return parent.findViewById<LinearLayout>(R.id.MyMask)
//    }

    fun getMyParent(): ConstraintLayout {
        val rootView = window.decorView.rootView
        val parentID = resources.getIdentifier(containerID, "id", packageName)
        val parent = rootView.findViewById<ConstraintLayout>(parentID)
        return parent
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
            TEAM_WEEKDAYS_KEY -> {
                intent.putExtra("key", TEAM_WEEKDAYS_KEY)
                intent.putExtra("source", "search")
                intent.putIntegerArrayListExtra("weekdays", weekdays)
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

    protected fun prepareSearch1(idx: Int, page: String) {

        var singleSelectIntent = Intent(this, SingleSelectVC1::class.java)
        var multiSelectIntent = Intent(this, MultiSelectVC1::class.java)
        val row = searchRows.get(idx)
        var key = ""
        if (row.containsKey("key")) {
            key = row["key"]!!
        }
        var value = ""
        if (row.containsKey("value")) {
            value = row["value"]!!
        }
        var value_type: String? = null
        if (row.containsKey("value_type")) {
            value_type = row["value_type"]!!
        }
        singleSelectIntent.putExtra("title", "city")
        singleSelectIntent.putExtra("key", key)
        multiSelectIntent.putExtra("title", "city")
        multiSelectIntent.putExtra("key", key)

        if (value_type != null && value_type == "Array" && value.length > 0) {
            var values: ArrayList<String> = arrayListOf()
            if (value.contains(",")) {
                values = value.split(",").toTypedArray().toCollection(java.util.ArrayList())
            } else {
                values.add(value)
            }
            multiSelectIntent.putExtra("selecteds", values)
        }
        if (value_type != null && value_type == "String" && value.length > 0) {
            singleSelectIntent.putExtra("selected", value)
        }

        when (key) {
            CITY_KEY -> {
                startActivityForResult(multiSelectIntent, SEARCH_REQUEST_CODE1)
            }
            WEEKDAY_KEY -> {
                val rows = WEEKDAY.makeSelect()
                multiSelectIntent.putExtra("rows", rows)
                startActivityForResult(multiSelectIntent, SEARCH_REQUEST_CODE1)
            }
            START_TIME_KEY, END_TIME_KEY -> {
                val times = Global.makeTimes()
                val rows: ArrayList<HashMap<String, String>> = arrayListOf()
                for (time in times) {
                    rows.add(hashMapOf("title" to time, "value" to time+":00"))
                }
                singleSelectIntent.putExtra("rows", rows)
                startActivityForResult(singleSelectIntent, SEARCH_REQUEST_CODE1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var value = "全部"
        var idx = 0
        when (requestCode) {
            SEARCH_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    var key = ""
                    if (data != null && data!!.hasExtra("key")) {
                        key = data!!.getStringExtra("key")
                    }
                    var page = ""
                    if (data != null && data!!.hasExtra("page")) {
                        page = data!!.getStringExtra("page")
                    }
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
                        TEAM_WEEKDAYS_KEY -> {
                            idx = 3
                            weekdays = data!!.getIntegerArrayListExtra("weekdays")
                            if (weekdays.size > 0) {
                                var arr: ArrayList<String> = arrayListOf()
                                val gDays = Global.weekdays
                                for (day in weekdays) {
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
                    val rows = generateSearchItems(page)
                    searchAdapter.update(rows)
                }
            }
            SEARCH_REQUEST_CODE1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    var key = ""
                    if (data != null && data!!.hasExtra("key")) {
                        key = data!!.getStringExtra("key")
                    }
                    var selecteds: ArrayList<String>? = null
                    if (data!!.hasExtra("selecteds")) {
                        selecteds = data!!.getStringArrayListExtra("selecteds")
                    }
                    var selected: String? = null
                    if (data!!.hasExtra("selected")) {
                        selected = data!!.getStringExtra("selected")
                    }

                    val getRow = fun(key: String): HashMap<String, String>? {
                        var row: HashMap<String, String>? = null
                        for ((i, searchRow) in searchRows.withIndex()) {
                            if (searchRow.containsKey("key")) {
                                if (key == searchRow.get("key")) {
                                    row = searchRow
                                    break
                                }
                            }
                        }

                        return row
                    }

                    val updateRow = fun(key: String, row:HashMap<String, String>) {
                        var idx: Int = -1
                        for ((i, searchRow) in searchRows.withIndex()) {
                            if (searchRow.containsKey("key")) {
                                if (key == searchRow.get("key")) {
                                    idx = i
                                    break
                                }
                            }
                        }
                        if (idx >= 0) {
                            searchRows[idx] = row
                        }
                    }

                    val row: HashMap<String, String>? = getRow(key)

                    var show = ""

                    when (key) {
                        CITY_KEY -> {
                            val session: SharedPreferences = this.getSharedPreferences(SESSION_FILENAME, 0)
                            val str = session.getString("citys", "")!!
                            var arr: JSONArray? = null
                            if (str.length > 0) {
                                try {
                                    arr = JSONArray(str)
                                } catch (e: java.lang.Exception) {
                                    //println(e.localizedMessage)
                                }
                            }

                            val texts: ArrayList<String> = arrayListOf()
                            for (selected in selecteds!!) {
                                if (arr != null) {
                                    for (i in 0..arr!!.length()-1) {
                                        val obj = arr!![i] as JSONObject
                                        if (selected == obj.getString("value")) {
                                            texts.add(obj.getString("title"))
                                            break
                                        }
                                    }
                                }
                            }
                            show = texts.joinToString(",")
                            if (row != null && row.containsKey("show")) {
                                row["show"] = show
                                row["value"] = selecteds.joinToString(",")
                                updateRow(key, row)
                            }
                        }
                        WEEKDAY_KEY -> {
                            val texts: ArrayList<String> = arrayListOf()
                            for (selected in selecteds!!) {
                                val text = WEEKDAY.intToString(selected.toInt())
                                texts.add(text)
                            }
                            show = texts.joinToString(",")
                            if (row != null && row.containsKey("show")) {
                                row["show"] = show
                                row["value"] = selecteds.joinToString(",")
                                updateRow(key, row)
                            }
                        }
                        START_TIME_KEY, END_TIME_KEY -> {
                            if (row != null && row.containsKey("show") && selected != null) {
                                row["show"] = selected.noSec()
                                row["value"] = selected
                                updateRow(key, row)
                            }
                        }

                    }
                    val rows = generateSearchItems("course")
                    searchAdapter.update(rows)
                }
            }
        }
    }

    fun generateSearchItems(page: String): ArrayList<SearchItem> {
        val rows: ArrayList<SearchItem> = arrayListOf()
        for (i in 0..searchRows.size-1) {
            val row = searchRows[i] as HashMap<String, String>
            val title = row.get("title")!!
            var detail: String = ""
            if (row.containsKey("detail")) {
                detail = row.get("detail")!!
            } else if (row.containsKey("show")) {
                detail = row.get("show")!!
            }
            var bSwitch = false
            if (row.containsKey("switch")) {
                bSwitch = row.get("switch")!!.toBoolean()
            }
            val searchItem = SearchItem(title, detail, keyword, bSwitch, -1, i, { k ->
                keyword = k
            }, { idx, b ->
                when (idx){
                    3 -> air_condition = b
                    4 -> bathroom = b
                    5 -> parking = b
                }
            })
            if (page == "team" || page == "course") {
                searchItem.delegate = getFragment(page)
            } else {
                searchItem.delegate = this@BaseActivity
            }
            rows.add(searchItem)
        }

        return rows
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
            if (page == "course" && frag::class == CourseFragment::class) {
                _frag = frag as CourseFragment
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
        } else {
            city_ids.clear()
        }
        if (city_ids.size > 0) {
            params["city_id"] = city_ids
            params["city_type"] = city_type
        } else {
            params.remove("city_id")
            params.remove("city_type")
        }

        val area_ids: ArrayList<Int> = arrayListOf()
        if (areas.size > 0) {
            areas.forEach  {
                area_ids.add(it.id)
            }
        } else {
            area_ids.clear()
        }
        if (area_ids.size > 0) {
            params["area_id"] = area_ids
        } else {
            params.remove("area_id")
        }

//        if (air_condition) { params["air_condition"] = 1 } else { params["air_condition"] = 0 }
//        if (bathroom) { params["bathroom"] = 1 } else { params["bathroom"] = 0 }
//        if (parking) { params["parking"] = 1 } else { params["parking"] = 0 }
        if (air_condition) { params["air_condition"] = 1 } else { params.remove("air_condition") }
        if (bathroom) { params["bathroom"] = 1 } else { params.remove("bathroom") }
        if (parking) { params["parking"] = 1 } else { params.remove("parking") }

        if (weekdays.size > 0) {
            params["play_days"] = weekdays
        } else {
            params.remove("play_days")
        }

        if (times.size > 0) {
            if (times.containsKey("time")) {
                params["use_date_range"] = 1
                val play_start = times["time"]!! as String
                val time = play_start + ":00 - 24:00:00"
                params["play_time"] = time
            }
        } else {
            params.remove("play_time")
        }

        var arena_ids: ArrayList<Int> = arrayListOf()
        if (arenas.size > 0) {
            arenas.forEach {
                arena_ids.add(it.id)
            }
        } else {
            arena_ids.clear()
        }
        if (arena_ids.size > 0) {
            params["arena_id"] = arena_ids
        } else {
            params.remove("arena_id")
        }

        var _degrees: ArrayList<String> = arrayListOf()
        if (degrees.size > 0) {
            degrees.forEach {
                _degrees.add(it.toString())
            }
        } else {
            _degrees.clear()
        }
        if (_degrees.size > 0) {
            params["degree"] = _degrees
        } else {
            params.remove("degree")
        }
        if (keyword.length > 0) {
            params["k"] = keyword
        } else {
            params.remove("k")
        }
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


    open protected fun layerSubmit(page: String) {
        unmask()
        if (page == "coach") {
            prepareParams()
            refresh()
        } else if (page == "team") {
            prepareParams()
            val frag = getFragment(page) as TeamFragment
            frag.refresh()
        } else if (page == "course") {
            val frag = getFragment(page) as CourseFragment
            frag.layerSubmit()
        } else {
            prepareParams()
            refresh()
        }
    }

    open fun cityBtnPressed(city_id: Int, page: String) {
        resetParams()
        citys.add(City(city_id, ""))
        prepareParams("all")
//        println(city_id)
        if (page == "coach") {
            refresh()
        } else if (page == "team") {
            val frag = getFragment(page) as TeamFragment
            frag.refresh()
        } else {
            refresh()
        }
    }

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

    open protected fun layerCancel() {
        unmask()
    }

    open protected fun layerDelete() {}

    fun warning(msg: String) {
        Alert.show(this, "警告", msg)
    }
    fun warning(msg: String, showCloseButton: Boolean=false, buttonTitle: String, buttonAction: ()->Unit) {
        Alert.show(this, "警告", msg, showCloseButton, buttonTitle, buttonAction)
    }
    fun warning(msg: String, closeButtonTitle: String, buttonTitle: String, buttonAction: ()->Unit) {
        Alert.show(this, "警告", msg, closeButtonTitle, buttonTitle, buttonAction)
    }
    fun info(msg: String) {
        Alert.show(this, "訊息", msg)
    }
    fun info(msg: String, closeButtonTitle: String, buttonTitle: String, buttonAction: ()->Unit) {
        Alert.show(this, "訊息", msg, closeButtonTitle, buttonTitle, buttonAction)
    }

    fun warningWithPrev(msg: String) {
        val alert = AlertDialog.Builder(this).create()
        alert.setTitle("警告")
        alert.setMessage(msg)
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "回上一頁", { Interface, j ->
            finish()
        })
        alert.show()
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

    override fun remove(indexPath: IndexPath) {

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














