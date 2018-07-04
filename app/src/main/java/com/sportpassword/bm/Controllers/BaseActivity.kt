package com.sportpassword.bm.Controllers

import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothClass
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IInterface
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBar
import android.view.*
import android.view.inputmethod.InputMethod
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
import com.sportpassword.bm.Adapters.SignupsAdapter
import com.sportpassword.bm.Models.Data
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_temp_play.*
import kotlinx.android.synthetic.main.tab.*
import org.jetbrains.anko.*
import java.util.*


open class BaseActivity : AppCompatActivity(), View.OnFocusChangeListener {

    protected lateinit var refreshLayout: SwipeRefreshLayout
    protected lateinit var refreshListener: SwipeRefreshLayout.OnRefreshListener

    var screenWidth: Int = 0
    var density: Float = 0f

    var msg: String = ""
    var dataLists: ArrayList<Data> = arrayListOf()

    protected var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        URL_TEAM_UPDATE = URL_HOME + "team/update"
        URL_UPDATE = URL_HOME + "%s/update"
        URL_DELETE = URL_HOME + "%s/delete"
        URL_ONE = "${URL_HOME}%s/one"
        URL_TEAM = URL_HOME + "team/"
        URL_TEAM_TEMP_PLAY = URL_TEAM + "tempPlay/onoff"
        URL_TEAM_TEMP_PLAY_LIST = URL_TEAM + "tempPlay/list"
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

    protected fun goLogin() {
        val loginIntent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    protected fun goRegister() {
        val registerIntent: Intent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }

    protected fun goForgetPassword() {
        val forgetPasswordIntent: Intent = Intent(this, ForgetPasswordActivity::class.java)
        startActivity(forgetPasswordIntent)
    }

    protected fun home(context: Context) {
        val intent : Intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    protected fun goEditTeam(token: String="") {
        val intent = Intent(this, EditTeamActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    protected fun goTeamTempPlayEdit(token: String) {
        val intent = Intent(this, TeamTempPlayEditActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    protected fun goTeamManager() {
        if (!member.isLoggedIn) {
            Alert.show(this, "警告", "請先登入會員")
            return
        }
        val intent = Intent(this, TeamManagerActivity::class.java)
        startActivity(intent)
    }
    protected fun goTeamManagerFunction(title: String, token: String) {
        val intent = Intent(this, TeamManagerFunctionActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("token", token)
        startActivity(intent)
    }
    protected fun goTempPlayDate(name: String, token: String) {
        val intent = Intent(this, TempPlayDateVC::class.java)
        intent.putExtra("name", name)
        intent.putExtra("token", token)
        startActivity(intent)
    }
    protected fun goTempPlayDatePlayer(date: String, name: String, token: String) {
        val intent = Intent(this, TempPlayDatePlayerVC::class.java)
        intent.putExtra("date", date)
        intent.putExtra("teamName", name)
        intent.putExtra("teamToken", token)
        startActivity(intent)
    }

    protected fun goSearch(type: String) {
        //val intent = Intent(this)
    }

    protected fun goDeleteTeam(token: String="") {
        Alert.delete(this, {
            val m = Loading.show(this)
            TeamService.delete(this, "team", token) { success ->
                m.dismiss()
                val teamUpdate = Intent(NOTIF_TEAM_UPDATE)
                LocalBroadcastManager.getInstance(this).sendBroadcast(teamUpdate)
            }
        })
    }

    protected fun goEditMember() {
        val accountIntent = Intent(this, AccountActivity::class.java)
        startActivity(accountIntent)
    }

    protected fun goUpdatePassword() {
        val updatePasswordIntent = Intent(this, UpdatePasswordActivity::class.java)
        startActivity(updatePasswordIntent)

    }

    protected fun goValidate(type: String) {
        val intent = Intent(this, ValidateActivity::class.java)
        intent.putExtra("type", type)
        startActivity(intent)
    }

    protected fun goTempPlaySignupOne(id: Int, token: String, title: String, near_date: String) {
        val i = Intent(this, TempPlaySignupOneVC::class.java)
        i.putExtra("token", token)
        i.putExtra("id", id)
        i.putExtra("title", title)
        i.putExtra("near_date", near_date)
        startActivity(i)
    }

    protected fun goBlackList() {
        val intent = Intent(this, BlackListVC::class.java)
        startActivity(intent)
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
                                val memberDidChange = Intent(NOTIF_MEMBER_DID_CHANGE)
                                LocalBroadcastManager.getInstance(this@BaseActivity).sendBroadcast(memberDidChange)
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
        MemberService.getOne(this, token, completion)
    }

    protected fun _getTeamManagerList(completion: CompletionHandler) {
        val loading = Loading.show(this)
        val filter1: Array<Any> = arrayOf("channel", "=", CHANNEL)
        val filter2: Array<Any> = arrayOf("manager_id", "=", member.id)
        val filter: Array<Array<Any>> = arrayOf(filter1, filter2)
        TeamService.getList(this, "team", "name", 1, 100, filter) { success ->
            loading.dismiss()
            if (success) {
                dataLists = TeamService.dataLists
                completion(true)
            } else {
                msg = TeamService.msg
                completion(false)
            }
        }
    }

    protected fun memberDidChange() {
        val memberDidChange = Intent(NOTIF_MEMBER_DID_CHANGE)
        LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChange)
    }

    protected fun addBlackList(memberName: String, memberToken: String, teamToken: String) {

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

    protected fun warning(msg: String) {
        Alert.show(this, "警告", msg)
    }
    protected fun warning(msg: String, showCloseButton: Boolean=false, buttonTitle: String, buttonAction: ()->Unit) {
        Alert.show(this, "警告", msg, showCloseButton, buttonTitle, buttonAction)
    }
    protected fun warning(msg: String, closeButtonTitle: String, buttonTitle: String, buttonAction: ()->Unit) {
        Alert.show(this, "警告", msg, closeButtonTitle, buttonTitle, buttonAction)
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
}
